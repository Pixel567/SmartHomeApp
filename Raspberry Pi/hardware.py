from gpiozero import LED, PWMLED, Button, DistanceSensor, Servo
import adafruit_dht
import board

servo = Servo(12)
green = LED(20)
red = LED(21)
light = PWMLED(18)

A = Button(5, pull_up=True, bounce_time=0.05)
B = Button(6, pull_up=True, bounce_time=0.05)
C = Button(13, pull_up=True, bounce_time=0.05)
D = Button(19, pull_up=True, bounce_time=0.05)
OK = Button(26, pull_up=True, bounce_time=0.05)

dht = adafruit_dht.DHT11(board.D4)
motion_sensor = DistanceSensor(trigger=23, echo=24, max_distance=2.0)
