from time import sleep
from datetime import datetime
from hardware import dht, OK
from helpers import getMotionDistance, getSensorValues
from handlers import press_ok
import threading
import config
import requests

def sender():
    while True:
        temperature = getSensorValues(lambda: dht.temperature)
        humidity = getSensorValues(lambda: dht.humidity)

        data = {
            "measurementTime": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "temperature": None if temperature is None else round(temperature, 2),
            "humidity": None if humidity is None else round(humidity, 2),
            "doorOpened": config.DOOR_OPENED,
            "motion": config.MOTION_DETECTED,
            "distance": config.MOTION_DISTANCE if config.MOTION_DETECTED else None
        }

        try:
            resp = config.SESSION.post(
                config.SPRING_URL_DATA,
                json=data,
                timeout=3
            )
        except Exception as e:
            print(e)

        config.MOTION_DETECTED = False
        config.MOTION_DISTANCE = None

        sleep(10)

def motionWatcher():
    while True:
        try:
            current = getMotionDistance()
            prev = config.LAST_DISTANCE

            if current is None:
                sleep(0.1)
                continue

            if prev is not None:
                change = abs(current - prev)
            else:
                change = 0

            if change >= 1.0:
                config.MOTION_DETECTED = True
                config.MOTION_DISTANCE = round(current, 2)

            config.LAST_DISTANCE = current

        except:
            pass

        sleep(0.1)


def ok_listener():
    while True:
        OK.wait_for_press()
        press_ok()

def startBackground():
    threading.Thread(target=ok_listener, daemon=True).start()
    threading.Thread(target=sender, daemon=True).start()
    threading.Thread(target=motionWatcher, daemon=True).start()
