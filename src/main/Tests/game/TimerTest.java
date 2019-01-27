package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer();
        assertEquals(timer.toString(), "2:00");
        timer.removeSecond();
        assertEquals(timer.toString(), "1:59");
    }

    @Test
    public void isCorrect(){
        assertEquals(59, timer.getSeconds());
    }

    @Test
    public void viewingResults(){
        assertEquals(timer.getMinutes() + ":" + timer.getSeconds(), "1:59");

        timer = new Timer(5);
        assertEquals("0:05", timer.toString());

        timer = new Timer(25);
        assertEquals("0:25", timer.toString());
    }

}