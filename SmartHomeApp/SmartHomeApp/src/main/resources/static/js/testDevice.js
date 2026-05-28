import {testDevice} from './main.js'

testDevice?.addEventListener("click", () => {
    fetch("/user/main/test", {
        method: "POST",
        credentials: "include"
    })
    .then(res => res.json())
    .then(({deviceTest}) => {
        const {
            ledTest,
            temperatureSensor,
            motionSensor,
            doorSensor,
            details
        } = deviceTest ?? {};

        const {
            temperature,
            humidity,
            distance,
            motion,
            doorOpened
        } = details ?? {};

        alert(
            "Device test results:\n" +
            `LEDs: ${ledTest}\n` +
            `Temperature sensor: ${temperatureSensor}\n` +
            `Motion sensor: ${motionSensor}\n` +
            `Door servo: ${doorSensor}\n\n` +
            `Temp: ${temperature}\n` +
            `Humidity: ${humidity}\n` +
            `Distance: ${distance}\n` +
            `Motion: ${motion}\n` +
            `Door opened: ${doorOpened}`
        );
    })
    .catch((err) => {
        console.error(err);
    })
});
