/*jshint globalstrict: true*/
/* jshint esnext: true */
/* jshint browser:true */
"use strict";
/* exported sendMessage */
/* global EventBus, document, console */
if (debug) {
    console.log("loading eb");
}


//wordt clientside steeds een nieuwe eb gemaakt of dezelde herbruikt?
let eb = new EventBus("/tetris-15/socket/");

eb.onopen = function () {
    if (debug) {
        console.log("Openend eventbus");
    }
    eb.registerHandler("brickingbad.events.gameState", function (error, message) {
        if (error && debug) {
            console.log("error from events.gameState: " + JSON.stringify(error));
        }
        if (verboseDebug) {
            console.log("message from events.gameState: " + JSON.stringify(message.body));
        }
        bucketJson = message.body;
        document.querySelector(".js-waiting-overlay").classList.add("hidden");
        gameTick();
    });

    eb.registerHandler("brickingbad.events.timer", function (error, message) {
        if (error && debug) {
            console.log("error from events.timer: " + JSON.stringify(error));
        }
        if (debug) {
            console.log("message from events.timer: " + JSON.stringify(message.body));
        }
        document.querySelector(".timer-container").innerHTML = message.body;
    });

    eb.registerHandler("brickingbad.events.getNamesAndId", function (error, message) {
        if(error && debug){
            console.log("error from eb getNamesAndId: ", JSON.stringify(error));
        }
        if (debug) {
            console.log("message from eb getNamesAndId: ", JSON.stringify(message.body));
        }

    });

    eb.registerHandler("brickingbad.events.name", function (error, message) {
        if (error && debug) {
            console.log("error from events.name: " + JSON.stringify(error));
        }
        if (verboseDebug) {
            console.log("message from events.name: " + JSON.stringify(message.body));
        }
        addNames(JSON.parse(message.body));
    });

    eb.registerHandler("brickingbad.events.switch", function (error, message) {
        if (error && debug) {
            console.log("error from events.name: " + JSON.stringify(error));
        }
        if (verboseDebug) {
            console.log("message from events.name: " + JSON.stringify(message.body));
        }

        switched = true;

        if (playerId === 1){
            playerId = 2;
            setTimeout(switchBack, 5000);
        } else {
            playerId = 1;
            setTimeout(switchBack, 5000);
        }

    });



    //wordt uitgevoerd op moment dat socket open is
    //vanaf nu luistert JS socket naar alles op dit adres
    //send = naar 1 iemand sturen (unicast)
    //publish = broadcasten
    //zowel in java als js (misschien, eens checken)

};

eb.onclose = function () {
    if (debug) {
        console.log("Verticle closed");
    }
};


