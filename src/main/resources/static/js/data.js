
function oneFetch() {
fetch('/user/main/measurements/latest', {
    credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
        for (let key in data){
            const el = document.getElementById(key);
            if (key == 'motion'){
                if (data[key] == 'false'){
                    el.innerText = 'No motion'
                }
                else {
                    el.innerText = 'Motion detected'
                }
                continue;
            }
            if (el) el.innerText = data[key];
    }
});
}
oneFetch();
setInterval(oneFetch, 10000);