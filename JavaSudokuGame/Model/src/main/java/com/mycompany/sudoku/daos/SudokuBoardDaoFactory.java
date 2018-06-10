package com.mycompany.sudoku.daos;

import com.mycompany.sudoku.exceptions.ApplicationException;

public class SudokuBoardDaoFactory {

    public Dao getFileDao(final String fileName) throws ApplicationException {
        return new FileSudokuBoardDao(fileName);
    }

    public Dao getDatabaseDao(final String fileName) throws ApplicationException {
        return new JdbcSudokuBoardDao(fileName);
    }

    public Dao getDatabaseDao(final String fileName, final int id) throws ApplicationException {
        return new JdbcSudokuBoardDao(fileName, id);
    }

}
