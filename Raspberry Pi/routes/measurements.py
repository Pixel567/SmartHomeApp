from flask import Blueprint, jsonify
from hardware import dht
from datetime import datetime
from helpers import jsonWithSessionCookie
import config

measurements_bp = Blueprint("measurements_bp", __name__)

@measurements_bp.route("/device/measurements")
def all_measurements():
    try:
        try:
            temperature = dht.temperature
            humidity = dht.humidity
        except Exception:
            temperature = None
            humidity = None

        if config.MOTION_DETECTED and config.MOTION_DISTANCE is not None:
            distance = round(config.MOTION_DISTANCE, 2)
        else:
            distance = None

        return jsonWithSessionCookie({
            "measurementTime": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "temperature": temperature,
            "humidity": humidity,
            "doorOpened": config.DOOR_OPENED,
            "motion": config.MOTION_DETECTED,
            "distance": distance
        })
    except Exception as e:
        return jsonWithSessionCookie({
            "status": "error",
            "msg": str(e)
        })