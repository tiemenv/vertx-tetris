package game;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BucketCellTest {
    private BucketCell cell;
    BucketCell[] bucketcells;

    @Before
    public void setUp() {
        cell = BucketCell.getEmptyCell();
        bucketcells = new BucketCell[5];
    }

    @Test
    public void checkCell() {

        assertNull(cell.getTetrominoType());
        assertTrue(cell.isEmpty());

        cell = BucketCell.getCell(TetrominoType.T);
        assertEquals("T", cell.getTetrominoType().name());
        assertFalse(cell.isEmpty());

        cell = BucketCell.getCell(TetrominoType.L);
        assertEquals("L", cell.getTetrominoType().name());
        assertFalse(cell.isEmpty());

        cell = BucketCell.getEmptyCell();
        assertNull(cell.getTetrominoType());
        assertTrue(cell.isEmpty());
    }

    @Test
    public void checkCells() {

        cell = BucketCell.getEmptyCell();
        Arrays.fill(bucketcells, cell);
        for (BucketCell cell : bucketcells) {
            assertTrue(cell.isEmpty());
        }

        assertEquals(5, bucketcells.length);
        bucketcells = new BucketCell[8];
        assertEquals(8, bucketcells.length);

        cell = BucketCell.getCell(TetrominoType.L);

        Arrays.fill(bucketcells, cell);
        for (BucketCell cell : bucketcells) {
            assertFalse(cell.isEmpty());
            assertEquals("L", cell.getTetrominoType().name());
        }
    }

}