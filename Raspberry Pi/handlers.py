from time import sleep
from hardware import A, B, C, D, OK, red
from helpers import blink, openDoor, doorSendInfo, closeDoor
import config

def press_1():
    config.ENTERED_CODE.append("1")

def press_2():
    config.ENTERED_CODE.append("2")

def press_3():
    config.ENTERED_CODE.append("3")

def press_4():
    config.ENTERED_CODE.append("4")


def press_ok():
    if not config.DOOR_LOCK.acquire(blocking=False):
        return

    try:
        if not config.DOOR_OPENED:
            if len(config.CORRECT_CODE) == 0 or config.ENTERED_CODE == config.CORRECT_CODE:
                openDoor(True)
                sleep(3)
                closeDoor(True)
            else:
                blink(red)
        else:
            closeDoor(True)

    finally:
        config.ENTERED_CODE.clear()
        config.DOOR_LOCK.release()



A.when_pressed = press_1
B.when_pressed = press_2
C.when_pressed = press_3
D.when_pressed = press_4
