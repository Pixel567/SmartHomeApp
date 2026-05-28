from time import sleep
from statistics import median
from hardware import motion_sensor, servo
from flask import make_response, jsonify
from hardware import green, red
import config
from datetime import datetime

def blink(led, times=3, speed=0.18):
    for _ in range(times):
        led.on()
        sleep(speed)
        led.off()
        sleep(speed)

def servoOpen(led):
    led.on()
    sleep(1.5)
    servo.max()
    sleep(1.5)
    led.off()

def servoClose(led):
    blink(led)
    servo.min()
    blink(led)

def openDoor(buttons = False):
    try:
        if config.DOOR_OPENED:
            return {
                "status": "error",
                "msg": "door is already opened"
            }

        config.DOOR_OPENED = True
        if buttons:
            doorSendInfo()

        servoOpen(green)
        
        return {"status": "ok"}
    except Exception as e:
        return {
            "status": "error",
            "msg": str(e)
        }
        

def closeDoor(buttons = False):
    try:
        if config.DOOR_OPENED:
            config.DOOR_OPENED = False
            servoClose(green)
            if buttons:
                doorSendInfo()

            return {"status": "ok"}
        else:
            return {
                "status": "error",
                "msg": "door is already closed"
            }
    except Exception as e:
        return {
            "status": "error",
            "msg": str(e)
        }

def getMotionDistance(measureCount=3, sleepTime=0.02):
    values = []
    for _ in range(measureCount):
        try:
            values.append(motion_sensor.distance * 100)
        except Exception:
            pass
        sleep(sleepTime)

    if not values:
        return None

    dist = median(values)
    return dist

def jsonWithSessionCookie(data):
    resp = make_response(jsonify(data))
    if config.JSESSIONID:
        resp.set_cookie("JSESSIONID", config.JSESSIONID, path="/", httponly=True)
    return resp

def doorSendInfo():
    try:
        data = {
            "measurementTime": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "doorOpened": not(config.DOOR_OPENED)
        }

        config.SESSION.post(config.SPRING_URL_DOOR_STATUS, json=data, timeout=3)

    except Exception as e:
        print("Send error:", e)


def getSensorValues(callback):
    values = []

    for _ in range(8):
        try:
            val = callback()
            if val is not None:
                values.append(val)
        except:
            pass

        sleep(0.2)

    if len(values) < 3:
        return None

    values.sort()
    trimmed = values[1:-1]

    return sum(trimmed) / len(trimmed)