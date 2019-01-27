package game;

import heroes.Hero;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TetrominoTest {
    Tetromino tetromino;

    @Test
    public void rotate2MaxOrientation() {
        tetromino = Tetromino.geti();
        assertEquals(2, tetromino.getType().getMaxOrientations());
        assertEquals(tetromino.getCoordinates()[0].x, -2);
        assertEquals(tetromino.getCoordinates()[0].y, 0);

        tetromino = tetromino.rotate();
        assertEquals(tetromino.getCoordinates()[0].x, 0);
        assertEquals(tetromino.getCoordinates()[0].y, 2);

        tetromino = tetromino.rotate();
        assertEquals(tetromino.getCoordinates()[0].x, 2);
        assertEquals(tetromino.getCoordinates()[0].y, 0);
    }

    @Test
    public void rotateOMaxOrientation() {
        tetromino = Tetromino.getSquare();
        assertEquals(0, tetromino.getType().getMaxOrientations());
        assertEquals(tetromino.getCoordinates()[0].x, -1);
        assertEquals(tetromino.getCoordinates()[0].y, 0);

        tetromino.rotate();
        assertEquals(tetromino.getCoordinates()[0].x, -1);
        assertEquals(tetromino.getCoordinates()[0].y, 0);
    }

}