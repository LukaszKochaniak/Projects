package com.mycompany.sudoku;

import com.mycompany.sudoku.daos.SudokuBoardDaoFactory;
import com.mycompany.sudoku.daos.FileSudokuBoardDao;
import com.mycompany.sudoku.daos.JdbcSudokuBoardDao;
import com.mycompany.sudoku.exceptions.ApplicationException;
import com.mycompany.sudoku.exceptions.DaoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.logging.Logger;

public class SudokuBoard implements Serializable, Cloneable {

    private static final long serialVersionUID = 50L;
    static Logger log = Logger.getLogger(SudokuBoard.class.getName());

    private OurHashMap<Integer, Integer, SudokuField> board = new OurHashMap<>();
    private List<SudokuLine> rows = new ArrayList<>(9);
    private List<SudokuLine> columns = new ArrayList<>(9);
    private OurHashMap<Integer, Integer, SudokuBox> boxes = new OurHashMap<>();

    public SudokuBoard() {
        for (int i = 0; i < 9; i++) {
            rows.add(new SudokuLine());
            columns.add(new SudokuLine());
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes.put(j, i, new SudokuBox());
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.put(j, i, new SudokuField());
                rows.get(i).set(j, board.get(j, i));
                columns.get(j).set(i, board.get(j, i));
                boxes.get(j / 3, i / 3).set(j % 3, i % 3, board.get(j, i));
            }
        }
        randomBoardInitialization(15);
        log.info("\n" + toString());
    }

    public void setLevel(int level) {
        if (level == 0) {
            randomBoardInitialization(10);
        } else if (level == 1) {
            randomBoardInitialization(20);
        } else if (level == 2) {
            randomBoardInitialization(30);
        }
    }

    public void save(final String filename) {
        SudokuBoardDaoFactory fileFactory = new SudokuBoardDaoFactory();
        try (FileSudokuBoardDao file = (FileSudokuBoardDao) fileFactory.getFileDao(filename)) {
            file.write(this);
        } catch (DaoException de) {
            Logger.getLogger(SudokuBoard.class.getName()).log(Level.SEVERE, null, de);
        } catch (ApplicationException ex) {
            Logger.getLogger(SudokuBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SudokuBoard load(final String filename) {
        SudokuBoardDaoFactory fileFactory = new SudokuBoardDaoFactory();
        try (FileSudokuBoardDao file = (FileSudokuBoardDao) fileFactory.getFileDao(filename)) {
            return file.read();
        } catch (DaoException de) {
            Logger.getLogger(JdbcSudokuBoardDao.class.getName()).log(Level.SEVERE, null, de);
        } catch (ApplicationException ex) {
            Logger.getLogger(SudokuBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int get(int x, int y) {
        return board.get(x, y).getFieldValue();
    }

    public SudokuField at(int x, int y) {
        return board.get(x, y);
    }

    public OurHashMap<Integer, Integer, SudokuField> getBoard() {
        return (OurHashMap<Integer, Integer, SudokuField>) board.clone();
    }

    public void set(int x, int y, int v) {
        board.get(x, y).setFieldValue(v);
    }

    public void clear() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                set(j, i, 0);
                at(j, i).setInitialized(false);
            }
        }
    }

    public boolean isFilled() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.get(j, i).getFieldValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkFill() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (get(j, i) == 0) {
                    continue;
                }
                if (!checkRow(j, i, get(j, i)) || !checkColumn(j, i, get(j, i)) || !checkSquare(j, i, get(j, i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkRow(int column, int row, int value) {
        return rows.get(row).verify(column, value);
    }

    public boolean checkColumn(int column, int row, int value) {
        return columns.get(column).verify(row, value);
    }

    public boolean checkSquare(int column, int row, int value) {
        int SqX = column / 3;
        int SqY = row / 3;
        return boxes.get(SqX, SqY).verify(column % 3, row % 3, value);
    }

    public boolean checkIfValid(int column, int row, int value) {
        return checkRow(column, row, value) && checkColumn(column, row, value) && checkSquare(column, row, value);
    }

    public void randomBoardInitialization(int n) {
        int i = 0;
        while (i < n) {
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);
            int v = (int) (Math.random() * 9) + 1;
            if (!at(x, y).isInitialized() & checkIfValid(x, y, v)) {
                i++;
                at(x, y).setInitialized(true);
                set(x, y, v);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Board:\n");

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    str.append(" ");
                }
                str.append(board.get(j, i).getFieldValue());
            }
            str.append("\n");
        }
        str.append("\n");

        return str.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SudokuBoard) {
            final SudokuBoard other = (SudokuBoard) obj;
            return new EqualsBuilder()
                    .append(board, other.board)
                    .append(rows, other.rows)
                    .append(columns, other.columns)
                    .append(boxes, other.boxes)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(board).
                append(rows).
                append(columns).
                append(boxes).
                toHashCode();
    }

    @Override
    public SudokuBoard clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (SudokuBoard) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException error) {
            error.getMessage();
        }
        return null;
    }

//        @Override
//    public Iterator iterator() {
//        return new BoardIterator();
//    }
//    class BoardIterator implements Iterator {
//
//        private int row;
//        private int column;
//
//        @Override
//        public boolean hasNext() {
//            if (column > 8 || row > 8) {
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        public SudokuField next() {
//            if (column == 8 && row == 8) {
//                column++;
//                return board.get(column - 1, row);
//            } else if (column != 8) {
//                column++;
//                return board.get(column - 1, row);
//            } else {
//                column = 0;
//                row++;
//                return board.get(column, row);
//            }
//        }
//
//    }
}
