from flask import Blueprint, jsonify, request
import config
from helpers import openDoor, closeDoor, jsonWithSessionCookie

door_open_bp = Blueprint("door_open_bp", __name__)

@door_open_bp.route("/user/lock", methods=["POST"])
def openDoorRemote():
    try:
        data = request.get_json()
        doorClosed = data.get("doorClosed")

        if doorClosed:
            status = closeDoor()
        else:
            status = openDoor()

        return jsonWithSessionCookie(status)
    except Exception as e:
        return jsonWithSessionCookie({
            "status": "error",
            "msg": str(e)
        })