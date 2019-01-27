package game;

import heroes.Heisenberg;
import heroes.Hero;
import heroes.Jesse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game tetris;
    private Timer timer;
    private boolean overtime;


    @Before
    public void setUp() {
        tetris = new Game();
        timer = new Timer(5);
        assertEquals(5, timer.getSeconds());
    }

    @Test
    public void inOvertime() {
        while (timer.getSeconds() > 0) {
            timer.removeSecond();
        }
        assertEquals("0:00", timer.toString());
        assertTrue(checkOvertime());
        assertFalse(checkOvertime());
    }

    private boolean checkOvertime() {
        if (!overtime && "0:00".equals(timer.toString())) {
            overtime = true;
            return true;
        }
        return false;
    }

    @Test
    public void playerCheck() {
        tetris.addPlayerToGame(1, "Heisenberg");
        Hero heisenberg = new Heisenberg();

        tetris.addPlayerToGame(2, "Jesse");
        Hero jesse = new Jesse();

        assertEquals(heisenberg.getName(), tetris.getPlayer(1).getName());
        assertEquals(jesse.getName(), tetris.getPlayer(2).getName());

        useAnAbility(1,1);
        assertTrue(tetris.getPlayer(1).isCooldown1());

        assertFalse(tetris.getPlayer(1).isCooldown2());

        useAnAbility(1,2);
        assertTrue(tetris.getPlayer(1).isCooldown2());
    }

    private boolean useAnAbility(int playerId, int abilityId) {
        if(abilityId == 1){
            tetris.getPlayer(playerId).useFirstAbility();
            tetris.getPlayer(playerId).setOnCooldown1(true);
            return tetris.getPlayer(playerId).isCooldown1();
        }
        tetris.getPlayer(playerId).useSecondAbility();
        tetris.getPlayer(playerId).setOnCooldown2(true);
        return tetris.getPlayer(playerId).isCooldown1();
    }
}