package data;

import org.pmw.tinylog.Logger;

import java.sql.*;

public final class TetrisRepository {

    private static String errormessage = "Error populating db {}";

    private static final String SQL_POPULATE_TETROMINO_DB = "CREATE TABLE IF NOT EXISTS Tetromino "
            + "(TetrominoId int NOT NULL AUTO_INCREMENT, name VARCHAR(255), "
            + "maxOrentationsT INT,"
            + "pointT1 ARRAY,"
            + "pointT2 ARRAY,"
            + "pointT3 ARRAY,"
            + "pointT4 ARRAY,"
            + "PRIMARY KEY ( TetrominoId ))";

    private static final String SQL_POPULATE_PENTOMINO_DB = "CREATE TABLE IF NOT EXISTS Pentomino "
            + "(PentominoId int NOT NULL AUTO_INCREMENT, name VARCHAR(255), "
            + "maxOrentationsP INT,"
            + "pointP1 ARRAY,"
            + "pointP2 ARRAY,"
            + "pointP3 ARRAY,"
            + "pointP4 ARRAY,"
            + "pointP5 ARRAY,"
            + "PRIMARY KEY ( PentominoId ))";

    private static final String SQL_ADD_TETROMINO = "INSERT INTO Tetromino (name, maxOrentationsT,"
            + " pointT1, pointT2, pointT3, pointT4)"
            + " VALUES(?,?,?,?,?,?)";

    private static final String SQL_ADD_PENTOMINO = "INSERT INTO Pentomino (name, maxOrentationsP,"
            + " pointP1, pointP2, pointP3, pointP4, pointP5)"
            + " VALUES(?,?,?,?,?,?,?)";

    private static final String SQL_HAS_TETROMINOS = "SELECT * FROM Tetromino";

    private static final String SQL_HAS_PENTOMINOS = "SELECT * FROM Pentomino";

    private TetrisRepository() {
    }

    public static void populateDB() {
        int one = 1;
        int two = 2;
        try (Statement stmt = H2Connection.getConnection().createStatement()) {
            stmt.executeUpdate(SQL_POPULATE_TETROMINO_DB);
            stmt.executeUpdate(SQL_POPULATE_PENTOMINO_DB);
            if (datebaseHasBlocks() == one) {
                addAllTetrominos();
            }
            if (datebaseHasBlocks() == two) {
                addAllPentominos();
            }
        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

    private static int datebaseHasBlocks() throws SQLException {
        ResultSet resultTetromino = null;
        ResultSet resultPentomino = null;
        int res = 0;
        try (Connection con = H2Connection.getConnection();
             PreparedStatement prepTetromino = con.prepareStatement(SQL_HAS_TETROMINOS);
             PreparedStatement prepPentomino = con.prepareStatement(SQL_HAS_PENTOMINOS)) {
            resultTetromino = prepTetromino.executeQuery();
            resultPentomino = prepPentomino.executeQuery();
            Logger.info(resultTetromino);
            if (!resultTetromino.first()) {
                resultTetromino.close();
                resultPentomino.close();
                return 1;
            }
            if (!resultPentomino.first()) {
                resultTetromino.close();
                resultPentomino.close();
                res = 2;
            }
            resultTetromino.close();
            resultPentomino.close();
        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        } finally {
            if (resultTetromino != null) {
                resultTetromino.close();
            }
            if (resultPentomino != null) {
                resultPentomino.close();
            }
        }
        return res;
    }

    private static void addAllTetrominos() {
        try (Connection con = H2Connection.getConnection()) {
            addTetromino(con, "O", 0,
                    new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{-1, -1}, new Integer[]{0, -1});

            addTetromino(con, "I", 2,
                    new Integer[]{-2, 0}, new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{1, 0});

            addTetromino(con, "S", 2,
                    new Integer[]{0, 0}, new Integer[]{1, 0}, new Integer[]{-1, -1}, new Integer[]{0, -1});

            addTetromino(con, "Z", 2,
                    new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{0, -1}, new Integer[]{1, -1});

            addTetromino(con, "L", 4,
                    new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{1, 0}, new Integer[]{-1, -1});

            addTetromino(con, "J", 4,
                    new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{1, 0}, new Integer[]{1, -1});

            addTetromino(con, "T", 4,
                    new Integer[]{-1, 0}, new Integer[]{0, 0}, new Integer[]{1, 0}, new Integer[]{0, -1});

        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

    private static void addAllPentominos() {
        try (Connection con = H2Connection.getConnection()) {
            addPentomino(con, "X", 0,
                    new Integer[]{-1, -1}, new Integer[]{0, -1}, new Integer[]{0, 0},
                    new Integer[]{1, 1}, new Integer[]{0, -2});

            addPentomino(con, "F", 4,
                    new Integer[]{0, -2}, new Integer[]{-1, 0}, new Integer[]{0, 0},
                    new Integer[]{1, 0}, new Integer[]{0, -1});

        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

    private static void addTetromino(Connection con, String name, int rotations, Integer[]... coordinates) {
        try (PreparedStatement prep = con.prepareStatement(SQL_ADD_TETROMINO)) {
            prep.setString(1, name);
            prep.setInt(2, rotations);
            prep.setObject(3, coordinates[0]);
            prep.setObject(4, coordinates[1]);
            prep.setObject(5, coordinates[2]);
            prep.setObject(6, coordinates[3]);
            prep.executeUpdate();
        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

    private static void addPentomino(Connection con, String name, int rotations, Integer[]... coordinates) {
        try (PreparedStatement prep = con.prepareStatement(SQL_ADD_PENTOMINO)) {
            prep.setString(1, name);
            prep.setInt(2, rotations);
            prep.setObject(3, coordinates[0]);
            prep.setObject(4, coordinates[1]);
            prep.setObject(5, coordinates[2]);
            prep.setObject(6, coordinates[3]);
            prep.setObject(7, coordinates[4]);
            prep.executeUpdate();
        } catch (SQLException ex) {
            Logger.warn(errormessage, ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }
}
