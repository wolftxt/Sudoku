package sudoku;

import java.util.Arrays;
import java.util.Random;
import lombok.Data;

@Data
public class SudokuGame {

    public static final int SIZE = 9;

    // 0 is empty, 1-9 are numbers
    private boolean won;
    private int[][] board;
    private boolean[][] editable;

    public SudokuGame() {
        this.won = false;
        this.board = new int[SIZE][SIZE];
        this.editable = new boolean[SIZE][SIZE];
        for (boolean[] arr : editable) {
            Arrays.fill(arr, true);
        }
    }

    public void placeNumber(int x, int y, int num) {
        if (!editable[x][y]) {
            return;
        }
        board[x][y] = num;
        won = isFull();
    }

    private boolean isFull() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
