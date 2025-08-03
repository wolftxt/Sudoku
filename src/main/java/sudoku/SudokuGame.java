package sudoku;

import lombok.Data;

@Data
public class SudokuGame {

    public static final int SIZE = 9;

    // 0 is empty, 1-9 are numbers
    private int[][] board;
    private boolean[][] editable;

    public SudokuGame() {
        this.board = new int[SIZE][SIZE];
        this.editable = new boolean[SIZE][SIZE];
    }

    public boolean placeNumber(int x, int y, int num) {
        if (!editable[x][y]) {
            return false;
        }
        board[x][y] = num;
        return checkWin();
    }

    private boolean checkWin() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return SudokuSolver.isBoardLegal(board);
    }

}
