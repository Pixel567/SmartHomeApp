from dotenv import load_dotenv
from threading import Lock
import os
import requests
from threading import Lock

load_dotenv()

VALID_USERNAME = os.getenv("USERNAME")
VALID_PASSWORD = os.getenv("PASSWORD")
SPRING_URL_LOGIN = os.getenv("SPRING_URL_LOGIN")
SPRING_URL_DATA = os.getenv("SPRING_URL_DATA")
SPRING_URL_DOOR_STATUS = os.getenv("SPRING_URL_DOOR_STATUS")

SESSION = requests.Session()
JSESSIONID = None
DEVICE_ACTIVE = False

CURRENT_BRIGHTNESS = 0.0

LAST_DISTANCE = None
MOTION_DETECTED = False
MOTION_DISTANCE = None

DOOR_LOCK = Lock()
DOOR_OPENED = False
CORRECT_CODE = []
ENTERED_CODE = []