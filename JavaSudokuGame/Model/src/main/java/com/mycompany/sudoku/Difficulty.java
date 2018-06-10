package com.mycompany.sudoku;

public class Difficulty {

    public enum Levels {
        Easy(0),
        Medium(1),
        Hard(2);

        private int lv;

        private Levels(int lv) {
            this.lv = lv;
        }
    }

    Levels level;

    public Difficulty(final Levels level) {
        this.level = level;
    }

    public void applyTo(final SudokuBoard board) {
        int i = 0;
        while (i < level.lv * 10 + 10) {
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);
            if (board.get(x, y) != 0) {
                board.set(x, y, 0);
                board.at(x, y).setInitialized(false);
                i++;
            }
        }
    }
}
