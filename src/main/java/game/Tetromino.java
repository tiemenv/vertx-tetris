package game;

import java.awt.*;
import java.util.Arrays;

public final class Tetromino {
    private final Point[] coordinates;
    private final TetrominoType type;
    private final boolean initalOrientation;

    private Tetromino(TetrominoType type, Point[] coordinates, boolean inital) {
        initalOrientation = inital;
        this.coordinates = coordinates;
        this.type = type;
    }

    public Tetromino rotate() {
        int typeB = 2;
        int typeC = 4;
        Tetromino tetromino = this;
        if (type.getMaxOrientations() == typeB) {
            if (initalOrientation) {
                tetromino = new Tetromino(type, rotateRight(coordinates), false);
            } else {
                tetromino = new Tetromino(type, rotateLeft(coordinates), true);
            }
        } else if (type.getMaxOrientations() == typeC) {
            tetromino = new Tetromino(type, rotateRight(coordinates), true);
        }
        return tetromino;
    }

    private Point[] rotate(Point[] coordinatesToRotate, int x, int y) {
        Point[] rotated = new Point[coordinatesToRotate.length];
        //TODO: mss een eigen punt-klasse maken?
        // -> fact methode create: om nieuw punt te genereren:
        // cool want totale controle:
        // bv. cache, alle mogelijk coordinaten kunnen er al van voorhand in.
        // -> super performant

        for (int i = 0; i < coordinatesToRotate.length; i++) {
            int temp = coordinatesToRotate[i].x;
            rotated[i] = createPoint(x * coordinatesToRotate[i].y, y * temp);
        }
        return rotated;
    }

    public static Tetromino getRandomTetromino() {
        TetrominoType tetrominoType = TetrominoType.getRandomType();
        return new Tetromino(tetrominoType, tetrominoType.getCoordinates(), true);
    }

    public Point[] getCoordinates() {
        return Arrays.copyOf(coordinates, coordinates.length);
    }

    public TetrominoType getType() {
        return type;
    }

    public boolean isInitalOrientation() {
        return initalOrientation;
    }

    private Point[] rotateRight(Point... coordinatesToRotate) {
        return rotate(coordinatesToRotate, 1, -1);
    }

    private Point[] rotateLeft(Point... coordinatesToRotate) {
        return rotate(coordinatesToRotate, -1, 1);
    }

    private Point createPoint(int x, int y) {
        return new Point(x, y);
    }

    public static Tetromino geti() {
        TetrominoType tetrominoType = TetrominoType.getIBlock();
        return new Tetromino(tetrominoType, tetrominoType.getCoordinates(), true);
    }

    public static Tetromino getSquare() {
        TetrominoType tetrominoType = TetrominoType.getSquare();
        return new Tetromino(tetrominoType, tetrominoType.getCoordinates(), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tetromino tetromino = (Tetromino) o;

        return type == tetromino.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
