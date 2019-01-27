package game;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;
import webapi.WebServer;

import java.util.Random;

@SuppressWarnings("PMD.UnusedLocalVariable")
public class StartTest extends AbstractVerticle {
    private static final int MAX_PLAYERS = 2;

    private Game tetris = new Game();

    private EventBus eb;

    private long gameTick;
    private long globalTimer;
    private long localTimer;
    private long overtimeTimer;

    private Timer timer = new Timer();
    private Timer otTimer = new Timer(60);

    private int remainingSeconds = (timer.getSeconds() + timer.getMinutes() * 60) * 1000;
    private int remainingSecondsOvertime = (otTimer.getSeconds() + otTimer.getMinutes() * 60) * 1000;
    private int secondsPerTick = 1000;

    private int playerId;
    private String playerIdString = "playerId";

    private String bucket = "bucket";

    private boolean overtime;

    public boolean isOvertime() {
        return overtime;
    }

    public String getBucket() {
        return bucket;
    }

    @Override
    public void start() {

        vertx.deployVerticle(new WebServer(), new DeploymentOptions(), complete -> {
            Logger.info("Deployed webserver");
        });

        eb = vertx.eventBus();

        //game channel
        eb.consumer("brickingbad.events.addPlayer", message -> {
            Logger.info("got message from events.startGame {}", message.body());
            //we krijgen request, dus we zenden reply

            if (playerId >= MAX_PLAYERS) {
                resetGame();

            }
            String heroNameString = String.valueOf(message.body());
            JsonObject heroNameObject = new JsonObject(heroNameString);
            String heroName = heroNameObject.getString("hero");
            playerId++;
            addPlayer(playerId, heroName);
        });

        eb.consumer("brickingbad.events.keypress", message -> {
            Logger.info("got message from events.keypress {}", message.body());
            String string = String.valueOf(message.body());
            JsonObject keypressObject = new JsonObject(string);
            //TODO: check if playerIdString is something sensible to avoid crashes
            keyButtons(keypressObject.getString("keypress"), keypressObject.getInteger(playerIdString));
        });

        eb.consumer("brickingbad.events.getNamesAndId", message -> {
            Logger.info("got message from events.getNamesAndId {}", message.body());
            message.reply(getNames());
        });

    }

    private void resetGame() {
        playerId = 0;
        tetris = null;
        tetris = new Game();
        vertx.cancelTimer(localTimer);
        timer = null;
        timer = new Timer();
        otTimer = null;
        otTimer = new Timer(60);
    }

    private String getNames() {
        int player2 = 2;
        String name1 = tetris.getPlayer1().getName();
        JSONObject res = new JSONObject()
                .put("name1", name1);

        if (playerId == player2) {
            String name2 = tetris.getPlayer2().getName();
            res.put("name2", name2);
        }

        return res.put(playerIdString, playerId).toString();
    }

    private void addPlayer(int playerId, String heroName) {
        Logger.info("Assigned playerId: " + playerId);
        if (!tetris.addPlayerToGame(playerId, heroName)) {
            eb.publish("brickingbad.events.name", getNames());
            startGame();
        }
    }


    private void keyButtons(String button, int playerId) {
        hanldeKey(button, playerId);
    }

    private void hanldeKey(String key, int idPlayer) {
        switch (key) {
            case "down":
                tetris.moveDown(idPlayer);
                break;
            case "left":
                tetris.moveLeft(idPlayer);
                break;
            case "right":
                tetris.moveRight(idPlayer);
                break;
            case "up":
                tetris.rotate(idPlayer);
                break;
            case "spacebar":
                while (tetris.moveEnd(idPlayer)) {
                    tetris.moveDown(idPlayer);
                }
                tetris.moveDown(idPlayer);
                break;
            default:
                hanldeSpecialKey(key, idPlayer);
                break;
        }
        updateJson();
    }

    private void hanldeSpecialKey(String key, int id) {
        switch (key) {
            case "s":
                abilityOne(id);
                break;
            case "q":
                abilityTwo(id);
                break;
            case "p":
                keyP(id);
                break;
            default:
                Logger.info("Error in switch statement");
                break;
        }
    }


    private void abilityOne(int playerId) {
        if (!tetris.getPlayer(playerId).isCooldown1()) {
            tetris.abilityOneTest(playerId);
            tetris.getPlayer(playerId).setOnCooldown1(true);
            vertx.setTimer(20000, id -> {
                tetris.getPlayer(playerId).setOnCooldown1(false);
                updateJson();
            });
        }
        updateJson();
    }


    private void abilityTwo(int playerId) {
        if (!tetris.getPlayer(playerId).isCooldown2()) {
            tetris.abilityTwoTest(playerId);
            tetris.getPlayer(playerId).setOnCooldown2(true);
            vertx.setTimer(20000, id -> {
                tetris.getPlayer(playerId).setOnCooldown2(false);
                updateJson();
            });
        } else {
            Logger.info("Wait for the cooldown");
        }
        updateJson();
    }

    private void keyP(int playerId) {
        if (!tetris.isPaused(playerId) && remainingSeconds > 0) {
            tetris.setPaused(playerId);
            tetris.switchGameState();
            stopTimers();
            vertx.setTimer(30000, id -> {
                startGameTimer();
                tetris.switchGameState();
                updateJson();
            });
        }
    }

    private void startGameTimer() {
        globalTimer = vertx.setTimer(remainingSeconds + 1, id -> {
            stopTimers();
        });

        startGameTickTimer();

        localTimer = vertx.setPeriodic(secondsPerTick, id -> {
            timer.removeSecond();
            remainingSeconds = (timer.getSeconds() + timer.getMinutes() * 60) * 1000;
            if ((120000 - remainingSeconds) % 25000 == 0) {
                activateEvent();
            }
        });
    }

    private void startOvertimeTimer() {
        overtimeTimer = vertx.setTimer(remainingSecondsOvertime + 1, id -> {
            stopTimers();
        });

        secondsPerTick = 500;
        startGameTickTimer();

        localTimer = vertx.setPeriodic(1000, id -> {
            otTimer.removeSecond();
            remainingSecondsOvertime = (otTimer.getSeconds() + otTimer.getMinutes() * 60) * 1000;
            if ((180000 - remainingSeconds - remainingSecondsOvertime) % 25000 == 0) {
                activateEvent();
            }
        });
    }

    private void activateEvent() {
        Logger.info("Event started");

        Random rand = new Random();
        int one = 1;

        int n = rand.nextInt(2) + 1;
        if (n == one) {

            tetris.activateEvent();
            updateJson();
        } else {
            eb.publish("brickingbad.events.switch", "switch");
        }
    }

    private void startGameTickTimer() {
        gameTick = vertx.setPeriodic(secondsPerTick, id -> {
            if (!tetris.isRunning()) {
                vertx.cancelTimer(gameTick);
            }
            tetris.moveDown(0);
            updateJson();
        });
    }

    private void updateJson() {
        JsonObject bucketArray = new JsonObject(Json.encode(tetris));
        eb.publish("brickingbad.events.gameState", new JsonObject()
                .put(getBucket(), bucketArray));
        eb.publish("brickingbad.events.timer", checkTimer().toString());

    }

    private Timer checkTimer() {
        if (remainingSeconds == 0) {
            return otTimer;
        } else {
            return timer;
        }
    }

    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new StartTest());
    }


    private void startGame() {
        tetris.startGame();
        startGameTimer();
    }

    private void stopTimers() {
        remainingSeconds = (timer.getSeconds() + timer.getMinutes() * 60) * 1000;
        vertx.cancelTimer(gameTick);
        vertx.cancelTimer(globalTimer);
        vertx.cancelTimer(localTimer);
        vertx.cancelTimer(overtimeTimer);

        final int three = 3;

        if (remainingSeconds == 0) {
            if (tetris.getWinner() == three && !overtime) {
                Logger.info("overtime");
                overtime = true;
                startOvertimeTimer();
            } else {
                tetris.setRunning(false);
            }
        }
        updateJson();
    }
}
