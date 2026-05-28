import {lightSlider} from './main.js'

lightSlider.addEventListener("input", e => {
    lightSlider.style.setProperty("--value", lightSlider.value + "%");
});