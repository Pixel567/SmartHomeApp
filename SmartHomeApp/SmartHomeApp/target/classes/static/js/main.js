export const codeInput = document.getElementById("code-input");
export const lock = document.getElementById("lockSwitch");
export const lockPic = document.getElementById("lockPicture");
export const light = document.getElementById("lightSlider");
export const lightSlider = document.getElementById("lightSlider");
export const testDevice = document.getElementById("test-device");

export function updateSliderBackground(value) {
    light.style.setProperty("--value", value + "%");
}

export function updateLockPic(lockState) {
    lockPic?.setAttribute("src", lockState ? "/img/lock_closed.png" : "/img/lock_opened.png");
}

function updateState() {
    return fetch('/user/main/state', { credentials: 'include' })
        .then(res => res.json())
        .then(({ brightness, lockStatus }) => {
            light.value = brightness;
            updateSliderBackground(brightness);

            lock.checked = lockStatus;
            updateLockPic(lockStatus);
        })
        .catch((err) => {
            console.error(err)
        });
}

function loadCode() {
    return fetch("/user/main/doorCode", { credentials: "include" })
        .then(res => res.json())
        .then(({ status, doorCode }) => {
            if (status === "ok") {
                codeInput.value = doorCode ?? '';
            }
        })
        .catch((err) => {
            console.error(err)
        });
}

function loadData() {
    fetch('/user/main/measurements/latest', {
        credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
        for (const [key, value] of Object.entries(data)) {
            const el = document.getElementById(key);

            if (!el) continue;

            el.innerText = key !== 'motion'
                ? value
                : (!value ? 'No motion' : 'Motion detected');

        }
    })
    .catch((err) => {
        console.error(err);
    });
}

function updateAll() {
    updateState();
    loadData();
    if (document.activeElement !== codeInput) {
        loadCode();
    }
}

updateAll();
setInterval(updateAll, 3000);
