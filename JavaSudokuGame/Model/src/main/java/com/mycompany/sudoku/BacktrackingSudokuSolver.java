package com.mycompany.sudoku;

import java.util.logging.Logger;

public class BacktrackingSudokuSolver implements SudokuSolver {

    static Logger log = Logger.getLogger(BacktrackingSudokuSolver.class.getName());

    public int tries = 0;

    @Override
    public void solve(final SudokuBoard board) {
        //boolean isSolved = true;
        while (!backtrack(0, 0, board)) {
            log.info("Unsolvable");
            board.clear();
            board.randomBoardInitialization(15);
            //isSolved = false;
        }
        log.info("Tries: " + tries);
        board.toString();
        //return isSolved;
    }

    public boolean backtrack(int column, int row, final SudokuBoard board) {
        if (board.isFilled()) {
            return true;
        }
        if (!board.at(column, row).isInitialized() | board.get(column, row) == 0) {
            for (int i = 1; i < 10; i++) {
                board.set(column, row, i);
                tries++;
                if (board.checkIfValid(column, row, i)) {
                    if (column == 8 && row == 8) {
                        board.at(column, row).setInitialized(true);
                        return true;
                    }
                    if (column == 8) {
                        if (backtrack(0, row + 1, board)) {
                            board.at(column, row).setInitialized(true);
                            return true;
                        }
                    } else {
                        if (backtrack(column + 1, row, board)) {
                            board.at(column, row).setInitialized(true);
                            return true;
                        }
                    }
                }
            }
        } else {
            if (column == 8) {
                if (backtrack(0, row + 1, board)) {

                    return true;
                } else {
                    return false;
                }
            } else {
                if (backtrack(column + 1, row, board)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        board.set(column, row, 0);
        return false;
    }
}
