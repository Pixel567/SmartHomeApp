const lock = document.getElementById("lockSwitch");

function oneFetch() {
fetch('/user/main/state', {
    credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {

        console.log(JSON.stringify(data));
        if (data["lockStatus"] == false){
            lock.checked = false;
        }
        else {
            lock.checked = true;
        }
        const lockState = lock.checked ? true : false;
        document.getElementById("lockPicture").setAttribute("src", lockState ? "/img/lock_closed.png" : "/img/lock_opened.png");
});
}
oneFetch();

lock.addEventListener("change", () => {
    lock.disabled = true;
    const lockState = lock.checked ? true : false;
    document.getElementById("lockPicture").setAttribute("src", lockState ? "/img/lock_closed.png" : "/img/lock_opened.png");

    fetch("/user/main/lock", {
        credentials: "include",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ "lockStatus": lockState })
    });

    setTimeout(() => {
        lock.disabled = false;
    }, 1000);
});
