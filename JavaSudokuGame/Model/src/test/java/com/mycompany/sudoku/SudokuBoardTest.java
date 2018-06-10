package com.mycompany.sudoku;

import com.mycompany.sudoku.daos.SudokuBoardDaoFactory;
import com.mycompany.sudoku.daos.FileSudokuBoardDao;
import com.mycompany.sudoku.daos.JdbcSudokuBoardDao;
import com.mycompany.sudoku.exceptions.ApplicationException;
import com.mycompany.sudoku.exceptions.DaoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class SudokuBoardTest {

    static Logger log = Logger.getLogger(SudokuBoardTest.class.getName());

    public SudokuBoardTest() {
    }

    @Test
    public void testCorrectLayout() {
        System.out.println("testCorrectLayout: \n");

        SudokuBoard sBoard = new SudokuBoard();
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sBoard);
        assertEquals(sBoard.checkFill(), true);
    }

    @Test
    public void testDifferentResults() {
        System.out.println("testDifferentResults: \n");

        SudokuBoard sBoard = new SudokuBoard();
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(sBoard);
        SudokuBoard sBoard1 = new SudokuBoard();
        solver.solve(sBoard1);
        boolean eq = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sBoard.get(j, i) != sBoard1.get(j, i)) {
                    eq = false;
                }
            }
        }
        assertEquals(sBoard.equals(sBoard1), false);
        assertEquals(eq, false);
    }

    @Test
    public void ToStringTest() {
        System.out.println("ToStringTest: \n");

        SudokuBoard sBoard = new SudokuBoard();
        SudokuBox sBox = new SudokuBox();
        SudokuField sField = new SudokuField();
        SudokuLine sLine = new SudokuLine();

        System.out.print(sBoard.toString());
        System.out.print(sBox.toString());
        System.out.print(sField.toString());
        System.out.print(sLine.toString());
    }

    @Test
    public void equalsTest() {
        System.out.println("equalsTest: \n");

        SudokuBoard sBoard = new SudokuBoard();
        SudokuBoard sBoard1 = new SudokuBoard();

        SudokuBox sBox = new SudokuBox();
        SudokuBox sBox1 = new SudokuBox();

        SudokuField sField = new SudokuField();
        SudokuField sField1 = new SudokuField();

        SudokuLine sLine = new SudokuLine();
        SudokuLine sLine1 = new SudokuLine();

        assertEquals(sBoard.equals(sBoard1), false);
        assertEquals(sBox.equals(sBox1), true);
        assertEquals(sField.equals(sField1), true);
        assertEquals(sLine.equals(sLine1), true);
    }

    @Test
    public void hashCodeTest() {
        System.out.println("hashCodeTest: \n");

        SudokuBoard sBoard = new SudokuBoard();
        SudokuBox sBox = new SudokuBox();
        SudokuField sField = new SudokuField();
        SudokuLine sLine = new SudokuLine();

        System.out.println(sBoard.hashCode());
        System.out.println(sBox.hashCode());
        System.out.println(sField.hashCode());
        System.out.println(sLine.hashCode());
    }

    @Test
    public void testSaveRead() {
        System.out.println("testSaveRead: \n");

        SudokuBoard sBoard = new SudokuBoard();
        SudokuBoard sBoard1 = new SudokuBoard();
        SudokuBoardDaoFactory fileFactory = new SudokuBoardDaoFactory();
        try (FileSudokuBoardDao file = (FileSudokuBoardDao) fileFactory.getFileDao("Save.data")) {
            file.write(sBoard);
            sBoard1 = file.read();
            sBoard1.toString();
        } catch (DaoException de) {
            Logger.getLogger(SudokuBoardTest.class.getName()).log(Level.SEVERE, null, de);
        } catch (ApplicationException ex) {
            Logger.getLogger(SudokuBoardTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(sBoard.equals(sBoard1), true);
    }

    @Test
    public void testSaveReadInDatabase() {
        System.out.println("testSaveReadInDatabase: \n");

        SudokuBoard sBoard = new SudokuBoard();
        SudokuBoardDaoFactory fileFactory = new SudokuBoardDaoFactory();
        SudokuBoard sBoard1 = new SudokuBoard();
        try (JdbcSudokuBoardDao file = (JdbcSudokuBoardDao) fileFactory.getDatabaseDao("SudokuBoard1")) {
            file.write(sBoard);
            int id = file.getId();
//            System.out.println(id);
            sBoard1 = file.readWithId(id);
//            throw new DaoException(DaoException.NULL_NAME);
        } catch (DaoException de) {
            Logger.getLogger(SudokuBoardTest.class.getName()).log(Level.SEVERE, null, de);
        } catch (ApplicationException ex) {
            Logger.getLogger(SudokuBoardTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(sBoard.equals(sBoard1), true);
    }

    @Test
    public void testCompareTo() {
        System.out.println("testCompareTo: \n");

        SudokuField field = new SudokuField(5);
        SudokuField field1 = new SudokuField(6);
        assertTrue(field1.compareTo(field) > 0);
    }

    @Test
    public void testClone() {
        System.out.println("testClone: \n");

        SudokuField field = new SudokuField(5);
        SudokuField field1 = field.clone();

        SudokuLine line = new SudokuLine();
        SudokuLine line1 = line.clone();

        SudokuBoard board = new SudokuBoard();
        SudokuBoard board1 = board.clone();

        SudokuBox box = new SudokuBox();
        SudokuBox box1 = box.clone();

        assertEquals(field, field1);
        assertEquals(line, line1);
        assertEquals(board, board1);
        assertEquals(box, box1);

    }
}
