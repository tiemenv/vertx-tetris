package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("PMD.TooManyMethods")
public class Bucket {
    private static final int Y_SPAWN = 19;
    private static final int X_SPAWN = 5;

    private final int width;
    private final int height;

    private Point tetrominoCenter = new Point(X_SPAWN, Y_SPAWN);

    private Tetromino currentBlock;

    private Tetromino nextTetromino;

    private BucketCell[][] gameBucket;

    private int completedLines;
    private boolean isRunning = true;

    private boolean dead;

    private boolean hasPaused;

    public Bucket(int width, int height) {
        this.width = width;
        this.height = height;
        this.gameBucket = createEmptyBucket();
        this.nextTetromino = Tetromino.getRandomTetromino();
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    public boolean isHasPaused() {
        return hasPaused;
    }

    public void setHasPaused(boolean hasPaused) {
        this.hasPaused = hasPaused;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHasHitEnd() {
        return canMove(currentBlock.getCoordinates(), 0, -1);
    }

    public boolean hasGameRunning() {
        return isRunning;
    }

    public boolean isDead() {
        return dead;
    }

    private BucketCell[][] createEmptyBucket() {
        BucketCell[][] board = new BucketCell[width][height];

        for (int x = 0; x < width; x++) {
            board[x] = BucketCell.getEmptyArray(height);
        }
        return board;
    }

    public int getCompletedLines() {
        return completedLines;
    }

    private BucketCell getLocation(int x, int y) {
        return gameBucket[x][y];
    }

    private boolean isRowFull(int y) {
        for (int x = 0; x < width; x++) {
            if (getLocation(x, y).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void removeFullRows() {
        BucketCell[][] newBucket = createEmptyBucket();

        int fullRows = 0;
        for (int y = 0; y < height; y++) {
            if (isRowFull(y)) {
                fullRows++;
            } else {
                copyRow(newBucket, y, y - fullRows);
            }
        }
        completedLines += fullRows;
        gameBucket = newBucket;
    }

    private void copyRow(BucketCell[][] newBucket, int y, int i) {
        for (int x = 0; x < width; x++) {
            newBucket[x][i] = gameBucket[x][y];
        }
    }

    private boolean canMove(Point[] coordinates, int moveXas, int moveYas) {
        for (Point coordinate : coordinates) {
            int x = tetrominoCenter.x + coordinate.x + moveXas;
            int y = tetrominoCenter.y + coordinate.y + moveYas;

            removeLastTetromino();
            if (checkBounds(x, y)) {
                addTetrominoToBucket(gameBucket);
                return false;
            }
        }
        return true;
    }

    private boolean checkBounds(int x, int y) {
        boolean flag = false;
        if (x < 0 || x >= width || y < 0 || y >= 20) {
            flag = true;
        } else if (!gameBucket[x][y].isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public void rotate() {
        removeLastTetromino();
        Tetromino rotate = currentBlock.rotate();

        if (canMove(rotate.getCoordinates(), 0, 0)) {
            currentBlock = rotate;
            addTetrominoToBucket(gameBucket);
        }
    }

    private void move(int moveXas, int moveYas) {
        removeLastTetromino();
        if (tetrominoCenter.x + moveXas >= 0 && tetrominoCenter.x + moveXas <= 9) {
            tetrominoCenter = new Point(tetrominoCenter.x + moveXas, tetrominoCenter.y + moveYas);
        }
        addTetrominoToBucket(gameBucket);
    }

    public void moveLeft() {
        if (canMove(currentBlock.getCoordinates(), 1, 0)) {
            move(1, 0);
        }
    }

    public void moveRight() {
        if (canMove(currentBlock.getCoordinates(), -1, 0)) {
            move(-1, 0);
        }
    }


    public void moveDown() {
        if (canMove(currentBlock.getCoordinates(), 0, -1)) {
            move(0, -1);
        } else {
            removeLastTetromino();
            addTetrominoToBucket(gameBucket);
            currentBlock = nextTetromino;
            nextTetromino = Tetromino.getRandomTetromino();
            resetTetrominoCenter();
            removeFullRows();
        }
    }

    public BucketCell[][] getUpdatedBucket() {
        BucketCell[][] updatedBucket = new BucketCell[width][height];

        for (int y = 0; y < width; y++) {
            System.arraycopy(gameBucket[y], 0, updatedBucket[y], 0, gameBucket[0].length);
        }
        addTetrominoToBucket(updatedBucket);

        return updatedBucket;
    }

    private void addTetrominoToBucket(BucketCell[]... bucket) {
        for (Point coordinate : currentBlock.getCoordinates()) {
            int x = coordinate.x + tetrominoCenter.x;
            int y = coordinate.y + tetrominoCenter.y;
            if (bucket[x][y].isEmpty()) {
                bucket[x][y] = BucketCell.getCell(currentBlock.getType());
            } else if (y == Y_SPAWN && !canMove(currentBlock.getCoordinates(), 0, -1)) {
                isRunning = false;
                dead = true;
            }

        }
    }

    private void removeLastTetromino() {
        for (Point coordinate : currentBlock.getCoordinates()) {
            int x = coordinate.x + tetrominoCenter.x;
            int y = coordinate.y + tetrominoCenter.y;
            gameBucket[x][y] = BucketCell.getEmptyCell();
        }
    }

    public void setCurrentBlock(Tetromino tetromino) {
        if (currentBlock != null) {
            addTetrominoToBucket(getUpdatedBucket());
            resetTetrominoCenter();
        }
        currentBlock = tetromino;
        addTetrominoToBucket(gameBucket);
        resetTetrominoCenter();
    }

    private void resetTetrominoCenter() {
        tetrominoCenter.x = X_SPAWN;
        tetrominoCenter.y = Y_SPAWN;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (gameBucket[x][y].isEmpty()) {
                    res.append('*');
                } else {
                    res.append('R');
                }
            }
            res.append('\n');
        }
        return res.toString();
    }

    public void removeLastRow() {
        BucketCell[][] newBucket = createEmptyBucket();

        int fullRows = 0;
        for (int y = 0; y < height; y++) {
            if (y == 0) {
                fullRows++;
            } else {
                copyRow(newBucket, y, y - fullRows);
            }
        }
        completedLines += fullRows;
        gameBucket = newBucket;
    }

    public void removeRandomPieces() {
        int bottomRowAmount = countRow(0);
        int removedBlocks = 0;
        int minimumAmountOfBlocks = 3;
        if (bottomRowAmount >= minimumAmountOfBlocks) {
            for (int x = 0; x < width; x++) {
                if (!gameBucket[x][0].isEmpty() && (removedBlocks < 3)) {
                    gameBucket[x][0] = BucketCell.getEmptyCell();
                    removedBlocks++;
                }
            }
        }
    }

    private int countRow(int row) {
        int res = 0;
        for (int x = 0; x < width; x++) {
            if (!gameBucket[x][row].isEmpty()) {
                res++;
            }
        }
        return res;
    }

    public void removeColumn() {
        List<Integer> columns = getNonEmptyColumns();
        int minimum = 0;
        if (columns.size() > minimum) {
            Random r = new Random();
            int random = r.nextInt((columns.size()) + 1);
            removeColumn(columns.get(random));
        }
    }

    private void removeColumn(int column) {
        for (int y = 0; y < height; y++) {
            gameBucket[column][y] = BucketCell.getEmptyCell();
        }
    }

    private List<Integer> getNonEmptyColumns() {
        ArrayList<Integer> listOfColumns = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            if (!isColumnEmpty(x)) {
                listOfColumns.add(x);
            }
        }
        return listOfColumns;
    }

    private boolean isColumnEmpty(int x) {
        boolean res = true;
        for (int y = 0; y < height; y++) {
            if (!gameBucket[x][y].isEmpty()) {
                res = false;
            }
        }
        return res;
    }

    public void randomize() {
        Tetromino temp = Tetromino.getRandomTetromino();
        while (temp.equals(currentBlock)) {
            temp = Tetromino.getRandomTetromino();
        }
        if (canMove(temp.getCoordinates(), 0, -1)) {
            currentBlock = Tetromino.getRandomTetromino();
        }
    }
}
