import {light} from './main.js'

light.addEventListener("change", () => {
    fetch("/user/main/light", {
        credentials: "include",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({"brightness": light.value})
    })
    .catch((err) => {
        console.error(err);
    });
});
