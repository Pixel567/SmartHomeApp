from flask import Blueprint, request, jsonify
from hardware import light
from helpers import jsonWithSessionCookie
import config

light_bp = Blueprint("light_bp", __name__)

@light_bp.route("/user/light", methods=["POST"])
def set_light():
    try:
        data = request.get_json()
        brightness = data.get("light", 0)

        value = max(0, min(brightness, 100)) / 100
        light.value = value
        config.CURRENT_BRIGHTNESS = value

        return jsonWithSessionCookie({
            "status": "ok",
            "brightness": brightness
        })

    except Exception as e:
        return jsonWithSessionCookie({
            "status": "error",
            "msg": str(e)
        })
