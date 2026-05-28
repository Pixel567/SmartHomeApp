import {updateLockPic, lock} from './main.js'

lock?.addEventListener("change", () => {
    lock.disabled = true;

    const lockStatus = lock.checked;
    updateLockPic(lockStatus);

    fetch("/user/main/lock", {
        credentials: "include",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({lockStatus})
    })
    .catch((err) => {
        console.error(err)
    })
    .finally(() => {
        lock.disabled = false;
    });
});

