from flask import Blueprint, jsonify
from helpers import blink, jsonWithSessionCookie
from hardware import green, red, light, dht, motion_sensor, servo
from datetime import datetime
from time import sleep
import config

test_bp = Blueprint("test_bp", __name__)

@test_bp.route("/user/test", methods=["POST"])
def device_test():
    try:
        try:
            blink(green, 2)
            blink(red, 2)

            for i in range(101):
                light.value = i / 100
                sleep(0.01)

            for i in range(100, -1, -1):
                light.value = i / 100
                sleep(0.01)

            light.value = config.CURRENT_BRIGHTNESS
            led_test = "ok"
        except Exception as e:
            led_test = "error"

        try:
            temperature = dht.temperature
            humidity = dht.humidity
            th_test = "ok" if temperature is not None else "error"
        except:
            temperature = humidity = None
            th_test = "error"

        try:
            current = motion_sensor.distance * 100
            change = abs(current - config.LAST_DISTANCE) if config.LAST_DISTANCE else 0
            motion = change >= 1.0
            test_distance = round(current, 2)
            motion_test = "ok"
        except:
            test_distance = None
            motion = False
            motion_test = "error"

        try:
            servo.min()
            sleep(1)
            servo.max()
            sleep(1)
            if config.DOOR_OPENED:
                servo.max()
            else:
                servo.min()
            door_test = "ok"
        except:
            door_test = "error"

        return jsonWithSessionCookie({
            "ledTest": led_test,
            "temperatureSensor": th_test,
            "motionSensor": motion_test,
            "doorSensor": door_test,
            "details": {
                "temperature": temperature,
                "humidity": humidity,
                "distance": test_distance,
                "measurementTime": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                "doorOpened": config.DOOR_OPENED,
                "motion": motion,
            }
        })
    except Exception as e:
        return jsonWithSessionCookie({
            "status": "error",
            "msg": str(e)
        })