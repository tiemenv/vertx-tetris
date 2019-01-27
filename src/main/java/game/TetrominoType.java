package game;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public enum TetrominoType {

    O(0, p(-1, 0), p(0, 0), p(-1, -1), p(0, -1)),

    I(2, p(-2, 0), p(-1, 0), p(0, 0), p(1, 0)),

    S(2, p(-1, 0), p(0, 0), p(0, -1), p(1, -1)),

    Z(2, p(0, 0), p(1, 0), p(-1, -1), p(0, -1)),

    L(4, p(-1, 0), p(0, 0), p(1, 0), p(1, -1)),

    J(4, p(-1, 0), p(0, 0), p(1, 0), p(-1, -1)),

    T(4, p(-1, 0), p(0, 0), p(1, 0), p(0, -1)),

    X(0, p(-1, -1), p(0, -1), p(0, 0), p(1, -1), p(0, -2)),
    F(4, p(0, -2), p(-1, 0), p(0, 0), p(1, 0), p(0, -1));


    private static final Random RANDOM = new Random();
    private final int maxOrientations;
    private final Point[] coordinates;


    TetrominoType(int maxOrientations, Point... coordinates) {
        this.maxOrientations = maxOrientations;
        this.coordinates = coordinates;
    }

    public static TetrominoType getRandomType() {
        return TetrominoType.values()[RANDOM.nextInt(TetrominoType.values().length)];
    }

    public static TetrominoType getIBlock() {
        return I;
    }

    public static TetrominoType getSquare() {
        return O;
    }


    public int getMaxOrientations() {
        return maxOrientations;
    }

    public Point[] getCoordinates() {
        return Arrays.copyOf(coordinates, coordinates.length);
    }

    private static Point p(int x, int y) {
        return new Point(x, y);
    }
}
