package game;

import heroes.Heisenberg;
import heroes.Hero;
import heroes.Jesse;

public class Game {
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;
    private static final int OVERTIME = 3;
    private static final int ZERO = 0;

    private Hero player1;
    private Hero player2;
    private int winner;
    private boolean running;
    private boolean paused;
    private boolean overtime;

    public boolean isOvertime() {
        return overtime;
    }

    public Hero getPlayer1() {
        return player1;
    }

    public Hero getPlayer2() {
        return player2;
    }

    public Hero getPlayer(int playerId) {
        if (playerId == PLAYER1) {
            return player1;
        }
        return player2;
    }

    public void switchGameState() {
        paused = !paused;
    }

    public boolean isGamePaused() {
        return paused;
    }

    public int getWinner() {
        if (player1.getBucket().isDead()) {
            winner = PLAYER2;
        } else if (player2.getBucket().isDead()) {
            winner = PLAYER1;
        } else if (player1.getBucket().getCompletedLines() < player2.getBucket().getCompletedLines()) {
            winner = PLAYER2;
        } else if (player2.getBucket().getCompletedLines() < player1.getBucket().getCompletedLines()) {
            winner = PLAYER1;
        } else if (player1.getBucket().getCompletedLines() == player2.getBucket().getCompletedLines()) {
            winner = OVERTIME;
        }
        return winner;
    }


    public boolean addPlayerToGame(int playerId, String heroName) {
        boolean res = false;
        if ("Heisenberg".equals(heroName)) {
            if (playerId == PLAYER1) {
                player1 = new Heisenberg();
                res = true;
            } else {
                player2 = new Heisenberg();
            }
        } else {
            if (playerId == PLAYER1) {
                player1 = new Jesse();
                res = true;
            } else {
                player2 = new Jesse();
            }
        }
        return res;
    }

    public int getHeight(int i) {
        if (i == PLAYER1) {
            return player1.getBucket().getHeight();
        } else {
            return player2.getBucket().getHeight();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPaused(int playerId) {
        if (playerId == PLAYER1) {
            return player1.getBucket().isHasPaused();
        } else {
            return player2.getBucket().isHasPaused();
        }
    }

    public void setPaused(int playerId) {
        if (playerId == PLAYER1) {
            player1.getBucket().setHasPaused(true);
        } else {
            player2.getBucket().setHasPaused(true);
        }
    }

    public void startGame() {
        running = true;
        player1.getBucket().setCurrentBlock(Tetromino.getRandomTetromino());
        player2.getBucket().setCurrentBlock(Tetromino.getRandomTetromino());
    }


    public void moveLeft(int i) {
        if (i == PLAYER1) {
            player1.getBucket().moveLeft();
        } else {
            player2.getBucket().moveLeft();
        }
    }

    public void moveRight(int i) {
        if (i == PLAYER1) {
            player1.getBucket().moveRight();
        } else {
            player2.getBucket().moveRight();
        }
    }

    public void moveDown(int i) {
        if (i == ZERO && player1.getBucket().hasGameRunning() && player2.getBucket().hasGameRunning()) {
            player1.getBucket().moveDown();
            player2.getBucket().moveDown();
        } else if (i == PLAYER1 && player1.getBucket().hasGameRunning() && player2.getBucket().hasGameRunning()) {
            player1.getBucket().moveDown();
        } else if (i == PLAYER2 && player1.getBucket().hasGameRunning() && player2.getBucket().hasGameRunning()) {
            player2.getBucket().moveDown();
        } else {
            running = false;
            winner = getWinner();
        }
    }

    public boolean moveEnd(int i) {
        if (i == PLAYER1) {
            return player1.getBucket().isHasHitEnd();
        } else {
            return player2.getBucket().isHasHitEnd();
        }
    }

    public void rotate(int i) {
        if (i == PLAYER1) {
            player1.getBucket().rotate();
        } else {
            player2.getBucket().rotate();
        }
    }

    public void abilityOneTest(int i) {
        if (i == PLAYER1) {
            if (player1.offensiveAbilityOne()) {
                player2.setBucket(player1.useFirstAbility(player2.getBucket()));
            } else {
                player1.useFirstAbility();
            }
        } else {
            if (player2.offensiveAbilityOne()) {
                player1.setBucket(player2.useFirstAbility(player1.getBucket()));
            } else {
                player2.useFirstAbility();
            }
        }
    }

    public void abilityTwoTest(int i) {
        if (i == PLAYER1) {
            if (player1.offensiveAbilityTwo()) {
                player2.setBucket(player1.useSecondAbility(player2.getBucket()));
            } else {
                player1.useSecondAbility();
            }
        } else {
            if (player2.offensiveAbilityTwo()) {
                player1.setBucket(player2.useSecondAbility(player1.getBucket()));
            } else {
                player2.useSecondAbility();
            }
        }
    }

    public void activateEvent() {
        player1.getBucket().randomize();
        player2.getBucket().randomize();
    }
}
