package game;

import org.junit.Test;

import static org.junit.Assert.*;

public class TetrominoTypeTest {
    private TetrominoType tetrominoType;

    @Test
    public void setTetromino() {
        tetrominoType = TetrominoType.S;
        assertEquals("S", tetrominoType.name());

        tetrominoType = TetrominoType.L;
        assertEquals("L", tetrominoType.name());

        tetrominoType = TetrominoType.J;
        assertEquals("J", tetrominoType.name());

        tetrominoType = TetrominoType.J;
        assertEquals("I", TetrominoType.getIBlock().name());

        assertEquals("O", TetrominoType.getSquare().name());
        assertEquals(0, TetrominoType.getSquare().getMaxOrientations());
    }
}