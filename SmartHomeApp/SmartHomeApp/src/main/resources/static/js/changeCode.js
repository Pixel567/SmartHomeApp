import {codeInput, updateSliderBackground} from './main.js'

const handleData = (code) => {
    fetch('/user/main/changeCode', {
        method: "POST",
        credentials: "include",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({code})
    })
    .then((res) => res.json())
    .then(({status, msg, newCode}) => {
        codeInput.value = newCode ?? '';
        alert(status !== 'ok'
            ? `Error setting new door code: ${msg}`
            : `New code set successfully: ${newCode}`
        );
    })
    .catch((err) => {
        console.error(err);
    });
};

codeInput?.addEventListener("change", (e) => {
    const code = e.target.value.split('');
    handleData(code);
});
