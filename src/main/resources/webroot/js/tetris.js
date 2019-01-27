/*jshint globalstrict: true*/
/* jshint esnext: true */
/* jshint browser:true */
"use strict";

document.addEventListener("DOMContentLoaded", init);

//canvas globals
let canv1;
let ctx1;

let canv2;
let ctx2;
//dimensions of canvas
/* jshint ignore:start */
let canvasHeight = document.querySelector("#canvas-player1").getAttribute("height");
let canvasWidth = document.querySelector("#canvas-player1").getAttribute("width");


//Associative array that links color strings (received from the backend to fill up the canvas) to their respective rgb values needed by canvas
let colors = {
    "O": "rgb(255, 255, 0)", //yellow
    "I": "rgb(255, 0, 0)", //red
    "S": "rgb(102, 255, 255)", //dark purple
    "Z": "rgb(0, 0, 255)", //blue
    "L": "rgb(153, 51, 153)", //orange
    "T": "rgb(255, 128, 179)", //pink
    "X": "rgb(255, 153, 0)", //medium purple
    "J": "rgb(153, 51, 153)", //green
    "F": "rgb(0, 102, 0)" //dark green
};
/* jshint ignore:end */

//setup canvas initialisation, configure 2d context and create event listener for keypresses
function init() {
    if (debug) {
        console.log("init");
    }
    canv1 = document.querySelector("#canvas-player1");
    ctx1 = canv1.getContext("2d");
    canv2 = document.querySelector("#canvas-player2");
    ctx2 = canv2.getContext("2d");
    document.addEventListener("keydown", keyPress);
    //TODO: do cleanly
    setTimeout(addNamesAndId, 2000);
}

function switchBack() {
    switched = false;
    if (playerId === 1){
        playerId = 2;
    } else {
        playerId = 1;
    }
}

function addNamesAndId() {
    eb.send("brickingbad.events.getNamesAndId", {"message": "requestNames"}, function (error, message) {
        if (debug) {
            console.log("response from eb getNamesAndId: ", message.body);
        }
        if(playerId === null) {
            playerId = JSON.parse(message.body).playerId;
        }
        addNames(JSON.parse(message.body));
    });
}

/* jshint ignore:start */
function addNames(message) {
    let h2 = document.querySelectorAll('h2');
    let name1 = message.name1;
    let name2 = message.name2;
    h2[0].innerHTML = name1;
    if (name2) {
        h2[1].innerHTML = name2;
    }
    addNamesAndId();
    displayPortraits([name1, name2]);
}
/* jshint ignore:end */

/* jshint ignore:start */
function displayPortraits(names) {
    let portraitContainers = document.querySelectorAll(".js-portrait");
    if (debug) {
        console.log("names for displayPortraits: ", names);
    }
    for (let i = 0; i < names.length; i++) {
        let imgNode = document.createElement("img");
        if (typeof names[i] !== 'undefined') {
            imgNode.src = "images/" + names[i].toLowerCase() + ".png";
        }
        while (portraitContainers[i].firstChild) {
            portraitContainers[i].removeChild(portraitContainers[i].firstChild);
        }
        portraitContainers[i].appendChild(imgNode);
    }
}

function displayOvertime() {
    document.querySelector(".timer-container").style.color = "red";
}

function abilityCooldown(ability) {
    if(abilityDebug){
        console.log("Disabling ability " + ability);
    }
    let spanNode = document.querySelector("#ability" + ability + " span");
    spanNode.classList.add("disabled-animation");
    setTimeout(function () {
        spanNode.classList.remove("disabled-animation");
    }, 20000);
}
/* jshint ignore:end */

/* jshint ignore:start */
function gameTick() {
    if (verboseDebug) {
        console.log("gametick received from backend");
        console.log("Loaded JSON: ", bucketJson);
    }

    //decides if overtime is active
    if (bucketJson.bucket.overtime) {
        displayOvertime();
    }

    //decides if game has ended
    if (!bucketJson.bucket.running) {
        let winner = bucketJson.bucket.winner;
        gameEnded(winner);
    }

    //decides if a player has already used pause
    if(bucketJson.bucket.player1.bucket.hasPaused){
        if(pauseDebug){
            console.log("Disabling pause button for player 1");
        }
        document.querySelector("#js-pause-p1").style.backgroundColor = "grey";
    }
    if(bucketJson.bucket.player2.bucket.hasPaused){
        document.querySelector("#js-pause-p2").style.backgroundColor = "grey";
    }

    //decides if abilities are on cooldown
    if (bucketJson.bucket.player1.cooldown2) {
        abilityCooldown(1);
    }
    if (bucketJson.bucket.player1.cooldown1) {
        abilityCooldown(2);
    }
    if (bucketJson.bucket.player2.cooldown2) {
        abilityCooldown(3);
    }
    if (bucketJson.bucket.player2.cooldown1) {
        abilityCooldown(4);
    }

    let p1Name = bucketJson.bucket.player1.name;
    let p2Name = bucketJson.bucket.player2.name;
    //displayNames(p1Name, p2Name);

    //displays player scores
    let p1Score = bucketJson.bucket.player1.bucket.completedLines * 100;
    let p2Score = bucketJson.bucket.player2.bucket.completedLines * 100;
    displayScores(p1Score, p2Score);

    //decides if game is paused
    if (bucketJson.bucket.gamePaused) {
        pauseGame();
    }

    //checks the next tetromino and displays a preview to the player
    let p1NextTetromino = bucketJson.bucket.player1.bucket.nextTetromino.type;
    let p2NextTetromino = bucketJson.bucket.player2.bucket.nextTetromino.type;
    let tetrominos = [p1NextTetromino, p2NextTetromino];
    displayTetrominoPreviews(tetrominos);


    ctx1.clearRect(0, 0, canvasWidth, canvasHeight);
    ctx2.clearRect(0, 0, canvasWidth, canvasHeight);
    let canvas1 = bucketJson.bucket.player1.bucket.updatedBucket;
    let canvas2 = bucketJson.bucket.player2.bucket.updatedBucket;
    if (bucketDebug) {
        console.log("bucketJson: ", bucketJson);
    }
    //fill canvas
    for (let y = canvas1.length - 1; y >= 0; y--) {
        if (verboseDebug) {
            console.log("Bucket row ", y, " :", canvas1[y]);
        }
        for (let x = 0; x < canvas1[y].length; x++) {
            ctx1.globalAlpha = 1;
            ctx1.fillStyle = "black";
            ctx1.fillRect(canvasWidth - y * 10 - 10 - 1, canvasHeight - x * 10 - 10 - 1, 10 + 2, 10 + 2);


            if (verboseDebug) {
                console.log("Bucket item: ", canvas1[y][x].tetrominoType);
            }
            let block = canvas1[y][x].tetrominoType;
            if (block !== null) {

                ctx1.fillStyle = "white";
                ctx1.fillRect(canvasWidth - y * 10 - 10 - 1, canvasHeight - x * 10 - 10 - 1, 10 + 2, 10 + 2);
                ctx1.globalAlpha = 0.5;
                if (verboseDebug) {
                    console.log("Found block! Filling a ", colors[block], " block at position (X: ", y * 10 + 10, " , Y: ", x * 10, " )");
                }
                ctx1.fillStyle = colors[block];
                //canvasHeight - position because grid coordinates are inverted (horizontal mirroring)
                ctx1.fillRect(canvasWidth - y * 10 - 10, canvasHeight - x * 10 - 10, 10, 10);
            }
        }
    }

    for (let y = canvas2.length - 1; y >= 0; y--) {
        if (verboseDebug) {
            console.log("Bucket row ", y, " :", canvas2[y]);
        }
        for (let x = 0; x < canvas2[y].length; x++) {
            ctx2.globalAlpha = 1;
            ctx2.fillStyle = "black";
            ctx2.fillRect(canvasWidth - y * 10 - 10 - 1, canvasHeight - x * 10 - 10 - 1, 10 + 2, 10 + 2);

            if (verboseDebug) {
                console.log("Bucket item: ", canvas2[y][x].tetrominoType);
            }
            let block = canvas2[y][x].tetrominoType;
            if (block !== null) {

                ctx2.fillStyle = "white";
                ctx2.fillRect(canvasWidth - y * 10 - 10 - 1, canvasHeight - x * 10 - 10 - 1, 10 + 2, 10 + 2);
                ctx2.globalAlpha = 0.5;
                if (verboseDebug) {
                    console.log("Found block! Filling a ", colors[block], " block at position (X: ", y * 10 + 10, " , Y: ", x * 10, " )");
                }
                ctx2.fillStyle = colors[block];
                //canvasHeight - position because grid coordinates are inverted (horizontal mirroring)
                //vreeeeee magic lijntje
                ctx2.fillRect(canvasWidth - y * 10 - 10, canvasHeight - x * 10 - 10, 10, 10);
            }
        }
    }

}

function displayTetrominoPreviews(tetrominos) {
    if (verboseDebug) {
        console.log("Displaying tetromino previews! ", tetrominos);
    }
    let previews = document.querySelectorAll(".js-preview");
    for (let i = 0; i < tetrominos.length; i++) {
        let player = i + 1;
        let imageNode = document.createElement("img");
        imageNode.src = "images/tetrominos/" + tetrominos[i] + ".png";
        let parentNode = document.querySelector("#preview-player" + player);
        while (parentNode.firstChild) {
            parentNode.removeChild(parentNode.firstChild);
        }
        parentNode.appendChild(imageNode);
    }
}

function pauseGame() {
    swal({
        title: 'Paused',
        text: 'Game will resume after 30 seconds',
        timer: 30000,
        buttons: false,
        closeOnClickOutside: false,
    }).then(
        function () {
        },
        function (dismiss) {
            if (dismiss === 'timer' && debug) {
                console.log('swal closed by timer');
            }
        }
    );

    let counter = 30;
    let timer = setInterval(countDown, 1000);

    function countDown() {
        counter--;
        if (counter > 0) {
            document.querySelector(".swal-text").innerHTML = "Game will resume after " + counter + " seconds"
        } else {
            clearInterval(timer);
        }
    }
}

function displayNames(p1Name, p2Name) {
    document.querySelector("#js-name-p1").innerHTML = p1Name;
    document.querySelector("#js-name-p2").innerHTML = p2Name;
}

function displayScores(p1Score, p2Score) {
    document.querySelector("#js-score-p1").innerHTML = "Score: " + p1Score;
    document.querySelector("#js-score-p2").innerHTML = "Score: " + p2Score;
}

function gameEnded(winner) {
    document.removeEventListener("keydown", keyPress);
    // let airhornSound = new Audio('sounds/airhorn.mp3');
    // airhornSound.play();
    swal({
        text: "Player " + winner + " won!",
        icon: "success",
        buttons: ["Go back", "Play another"],
    })
        .then((value) => {
            if (value) {
                window.location.href = "hero-select.html";
            } else {
                window.location.href = "index.html";
            }
        });
}

/* jshint ignore:end */

//handler for keypresses
function keyPress(e) {
    if (debug) {
        console.log("Detected keypress, switch case decides if it's a relevant one");
    }
    //switch case for every relevant key
    switch (e.keyCode) {
        case 37:
            console.log("Handling keycode 37 (left arrow)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "left",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 38:
            console.log("Handling keycode 38 (up arrow)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "up",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 39:
            console.log("Handling keycode 39 (right arrow)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "right",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 40:
            console.log("Handling keycode 40 (down arrow)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "down",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 32:
            console.log("Handling keycode 32 (spacebar)");
            if (switched === false){
                eb.send("brickingbad.events.keypress", {
                    "keypress": "spacebar",
                    "playerId": playerId
                }, function (error, message) {
                    console.log(message);
                    console.log(error);
                });
            }

            break;
        case 83:
            console.log("Handling keycode 83 (s)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "s",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 80:
            console.log("Handling keycode 80 (p)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "p",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        case 82:
            console.log("Handling keycode 82 (r)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "r",
                "playerId": playerId
            }, function (error, message) {

                console.log(message);
                console.log(error);
            });
            break;
        case 81:
            console.log("Handling keycode 81 (q)");
            eb.send("brickingbad.events.keypress", {
                "keypress": "q",
                "playerId": playerId
            }, function (error, message) {
                console.log(message);
                console.log(error);
            });
            break;
        default:
            console.log("Keypress not relevant, keycode: ", e.keyCode);
            break;
    }
}



