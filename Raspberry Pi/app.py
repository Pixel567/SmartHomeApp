from flask import Flask, request, jsonify
from background import startBackground
from routes import measurements_bp, code_bp, light_bp, test_bp, door_open_bp
import config
import handlers
import threading
import config
import requests
from hardware import servo, light
import socket

app = Flask(__name__)

app.register_blueprint(measurements_bp)
app.register_blueprint(code_bp)
app.register_blueprint(light_bp)
app.register_blueprint(test_bp)
app.register_blueprint(door_open_bp)

def getRaspberryIP():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
    except:
        ip = "0.0.0.0"
    finally:
        s.close()
    return ip

def login():
    resp = config.SESSION.post(
        config.SPRING_URL_LOGIN,
        data={"username": config.VALID_USERNAME, "password": config.VALID_PASSWORD},
        headers={"Content-Type": "application/x-www-form-urlencoded"}
    )

    config.JSESSIONID = config.SESSION.cookies.get("JSESSIONID")

    init = config.SESSION.post("http://10.136.202.171:8080/device/init").json()

    if init.get("status") != "ok":
        return

    brightness = init.get("brightness", 0)
    lockStatus = init.get("lockStatus", False)
    code = init.get("doorCode", "")

    try:
        config.CURRENT_BRIGHTNESS = brightness
        light.value = brightness / 100
    except Exception as e:
        print(e)

    try:
        if lockStatus:
            servo.min()
            config.DOOR_OPENED = False
        else:
            servo.max()
            config.DOOR_OPENED = True
    except Exception as e:
        print(e)

    config.CORRECT_CODE = list(code)

def sendIP():
    ip_address = getRaspberryIP()
    url = "http://10.136.202.171:8080/device/ip"

    resp = config.SESSION.post(url, data=ip_address, timeout=3)


if __name__ == "__main__":
    login()
    sendIP()
    startBackground()
    app.run(host="0.0.0.0", port=5000)
