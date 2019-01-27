package game;

import java.util.Arrays;

public final class BucketCell {

    private TetrominoType tetrominoType;

    private BucketCell() {
        this.tetrominoType = null;
    }

    private BucketCell(TetrominoType type) {
        tetrominoType = type;
    }

    public boolean isEmpty() {
        return tetrominoType == null;
    }

    public TetrominoType getTetrominoType() {
        return tetrominoType;
    }

    public static BucketCell getCell(TetrominoType type) {
        return new BucketCell(type);
    }

    public static BucketCell getEmptyCell() {
        return new BucketCell();
    }

    public static BucketCell[] getEmptyArray(int height) {
        BucketCell[] bucketcells = new BucketCell[height];
        Arrays.fill(bucketcells, new BucketCell());
        return bucketcells;
    }

}
