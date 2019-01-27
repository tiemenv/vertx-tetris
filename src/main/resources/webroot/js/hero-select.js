/*jshint globalstrict: true*/
/* jshint esnext: true */
/* jshint browser:true */
"use strict";

document.addEventListener("DOMContentLoaded", init);

let selectedHero = "";

function init() {
    /* jshint ignore:start */
    document.querySelectorAll(".js-hero-btn").forEach(btn => btn.addEventListener("click", clickedBtn));
    /* jshint ignore:end */
    document.querySelector(".js-confirm-btn").addEventListener("click", confirm);
}

eb.onopen = function () {
    eb.send("brickingbad.events.requestNewGame", {
        "requestNewGame": "requestNewGame"
    }, function (error, message) {
        console.log(message);
    });
};

/* jshint ignore:start */
function clickedBtn(e) {
    let selectedHeroId = e.currentTarget.getAttribute("id");

    document.querySelectorAll(".js-hero-btn").forEach(btn => btn.classList.remove("selected-hero"));

    e.currentTarget.classList.add("selected-hero");
    switch (selectedHeroId) {
        case "js-hero-hector":
            selectedHero = "Hector Salamanca";
            break;
        case "js-hero-walter":
            selectedHero = "Walter White";
            break;
        case "js-hero-jesse":
            selectedHero = "Jesse Pinkman";
            break;
        case "js-hero-gustavo":
            selectedHero = "Gustavo Fring";
            break;
        case "js-hero-hank":
            selectedHero = "Hank Schrader";
            break;
        case "js-hero-heisenberg":
            selectedHero = "Heisenberg";
            break;
        case "js-hero-junior":
            selectedHero = "Walter White Jr.";
            break;
        case "js-hero-marie":
            selectedHero = "Maria Schrader";
            break;
        case "js-hero-mike":
            selectedHero = "Mike Ehrmantraut";
            break;
        case "js-hero-saul":
            selectedHero = "Saul Goodman";
            break;
        case "js-hero-skyler":
            selectedHero = "Skyler White";
            break;
        case "js-hero-todd":
            selectedHero = "Todd Alquist";
            break;
        case "js-hero-cousins":
            selectedHero = "The Cousins";
            break;
        case "js-hero-jane":
            selectedHero = "Jane Margolis";
            break;
        case "js-hero-alexey":
            selectedHero = "Alexey Pajitnov";
            break;
        default:
            if (debug) {
                console.log("Error in switch case");
            }
    }
    displayHeroInformation();
}


function displayHeroInformation() {
    let infoContainer = document.querySelector(".hero-info");
    infoContainer.innerHTML = "<p>" + selectedHero + "</p>";
    let infoText = "";
    switch (selectedHero) {
        case "Heisenberg":
            infoText = "<hr><ul><li>S: Spins an opponents block for 20 seconds</li><li>Q: Delete 3 random blocks from your opponent</li></ul>";
            infoText += "<br>" +
                "<div class='skin-container'>" +
                "<div class='hero-skin-enabled mui-panel selected-hero'>" +
                "<img src='images/heisenberg.png' alt='Heisenberg default skin'>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/heisenberg1.png' alt='Heisenberg skin 1'/>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/heisenberg2.png' alt='Heisenberg skin 2'/>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/heisenberg3.png' alt='Heisenberg skin 3'/>" +
                "</div>" +
                "</div>";
            break;
        case "Jesse Pinkman":
            infoText = "<hr><ul><li>S: Delete a random column from your opponent</li><li>Q: Clear a line from your own field</li></ul>";
            infoText += "<br>" +
                "<div class='skin-container'>" +
                "<div class='hero-skin-enabled mui-panel selected-hero'>" +
                "<img src='images/jesse.png' alt='Jesse default skin'>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/jesse1.png' alt='Jesse skin 1'/>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/jesse2.png' alt='Jesse skin 2'/>" +
                "</div>" +
                "<div class='hero-skin-disabled mui-panel'>" +
                "<img src='images/skins/jesse3.png' alt='Jesse skin 3'/>" +
                "</div>" +
                "</div>";
            break;
        default:
            infoText = "<hr><button class='mui-btn' onclick='goToShop()'>Buy this hero</button>";
    }
    infoContainer.innerHTML += infoText;

    //adding event listeners
    document.querySelectorAll(".hero-skin-disabled")
        .forEach(div => div.addEventListener("click", function () {
        swal("Buy this skin?", {
            buttons: ["Back", "Go Shopping"]
        })
            .then((value) => {
                if(value){
                    swal("Shop coming soon");
                }
            });
    }));
}

function goToShop() {
    swal("Shop coming soon");
}

/* jshint ignore:end */

function confirm() {
    if (selectedHero !== "") {
        switch (selectedHero) {
            case "Heisenberg":
                sendHero();
                window.location.href = "sandbox.html";
                break;
            case "Jesse Pinkman":
                sendHero();
                window.location.href = "sandbox.html";
                break;
            case "":
                swal("Select a hero first!");
                break;
            default:
                swal("Locked hero, play some more to unlock new heroes!");
        }
    }
}

function sendHero() {
    eb.send("brickingbad.events.addPlayer", {"hero": selectedHero}, function (error, message) {
        console.log(message);
    });
}