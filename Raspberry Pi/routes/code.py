from flask import Blueprint, request
import helpers
from helpers import jsonWithSessionCookie
import config

code_bp = Blueprint("code_bp", __name__)

@code_bp.route("/user/code", methods=["POST"])
def update_code():
    try:
        data = request.get_json()
        raw = data.get("code")
        newCode = [str(x) for x in raw]
        valid_buttons = {"1", "2", "3", "4"}

        if not isinstance(newCode, list):
            return jsonWithSessionCookie({
                "status": "error",
                "msg": "Code must be a list"
            })

        if any(item not in valid_buttons for item in newCode):
            return jsonWithSessionCookie({
                "status": "error",
                "msg": "Allowed buttons: 1,2,3,4"
            })

        config.CORRECT_CODE = newCode

        return jsonWithSessionCookie({
            "status": "ok",
            "newCode": config.CORRECT_CODE
        })

    except Exception as e:
        return jsonWithSessionCookie({
            "status": "error",
            "msg": str(e)
        })
