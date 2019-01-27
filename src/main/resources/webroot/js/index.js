/*jshint globalstrict: true*/
/* jshint esnext: true */

"use strict";

document.addEventListener("DOMContentLoaded", init);


function init() {
    document.querySelector("#js-play-btn").addEventListener("click", playNav);
    /* jshint ignore:start */
    eventListeners();
    /* jshint ignore:end */
}

function playNav() {
    window.location.href = "hero-select.html";
}
/* jshint ignore:start */
function eventListeners() {
    document.querySelectorAll(".js-btn-not-implemented")
        .forEach(btn => btn.addEventListener("click", featureComingSoon));
}

function featureComingSoon(){
    swal("Feature coming soon!");
}
/* jshint ignore:end */

