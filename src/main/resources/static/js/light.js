const light = document.getElementById("lightSlider");
function oneFetch() {
fetch('/user/main/state', {
    credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {

        console.log(JSON.stringify(data));
        light.value = data["brightness"];
        const lockState = lock.checked ? true : false;
        document.getElementById("lockPicture").setAttribute("src", lockState ? "/img/lock_closed.png" : "/img/lock_opened.png");
});
}
oneFetch();

light.addEventListener("change", () => {
    fetch("/user/main/light", {
        credentials: "include",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({"brightness": light.value})
    });
});
