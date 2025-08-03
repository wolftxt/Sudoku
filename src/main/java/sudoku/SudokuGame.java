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

    public SudokuGame(int originalPieceCount) {
        if (originalPieceCount < 0 || originalPieceCount > SIZE * SIZE) {
            throw new RuntimeException("Trying to start a game with an illegal piece count");
        }
        this.board = new int[SIZE][SIZE];
        this.editable = new boolean[SIZE][SIZE];
        for (boolean[] arr : editable) {
            Arrays.fill(arr, true);
        }
        Random r = new Random();
        while (originalPieceCount > 0) {
            int num = r.nextInt(1, SIZE + 1);
            int x = r.nextInt(SIZE);
            int y = r.nextInt(SIZE);
            if (placeNumber(x, y, num)) {
                originalPieceCount--;
                editable[x][y] = false;
            }
        }
        won = isFull();
    }

    public boolean isPlacementLegal(int x, int y, int num) {
        if (!editable[x][y]) {
            return false;
        }
        if (board[x][y] == num) {
            return false;
        }
        int previous = board[x][y];
        board[x][y] = num;
        if (!SudokuSolver.isBoardLegal(board)) {
            board[x][y] = previous;
            return false;
        }
        board[x][y] = previous;
        return true;
    }

    public boolean placeNumber(int x, int y, int num) {
        if (!editable[x][y]) {
            return false;
        }
        int previous = board[x][y];
        board[x][y] = num;
        if (!SudokuSolver.isBoardLegal(board)) {
            board[x][y] = previous;
            return false;
        }
        won = isFull();
        return true;
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
