package sudoku;

import java.util.Arrays;

public class SudokuSolver {

    public static boolean isFull(int[][] board) {
        int SIZE = SudokuGame.SIZE;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isBoardLegal(int[][] board) {
        final int SIZE = SudokuGame.SIZE;
        for (int[] arr : board) {
            int[][] row = new int[1][SIZE];
            row[0] = arr;
            if (!isSubarrayLegal(row)) {
                return false;
            }
        }
        for (int y = 0; y < SIZE; y++) {
            int[][] col = new int[1][SIZE];
            for (int x = 0; x < SIZE; x++) {
                col[0][x] = board[x][y];
            }
            if (!isSubarrayLegal(col)) {
                return false;
            }
        }
        int limit = (int) Math.sqrt(SIZE);
        for (int x = 0; x < limit; x++) {
            for (int y = 0; y < limit; y++) {
                int[][] arr = new int[limit][limit];
                arr[0] = Arrays.copyOfRange(board[x * limit], y * limit, y * limit + limit);
                arr[1] = Arrays.copyOfRange(board[x * limit + 1], y * limit, y * limit + limit);
                arr[2] = Arrays.copyOfRange(board[x * limit + 2], y * limit, y * limit + limit);
                if (!isSubarrayLegal(arr)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSubarrayLegal(int[][] board) {
        final int SIZE = SudokuGame.SIZE;
        if (board.length * board[0].length != SIZE) {
            throw new IllegalStateException("Illegal subarray");
        }
        boolean[] seen = new boolean[SIZE + 1];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                int index = board[x][y];
                if (index != 0 && seen[index]) {
                    return false;
                }
                seen[index] = true;
            }
        }
        return true;
    }

    public static boolean solve(int[][] board, int x, int y) {
        if (y >= board[0].length) {
            return true;
        }
        if (x >= board.length) {
            return solve(board, 0, y + 1);
        }
        if (board[x][y] != 0) {
            return solve(board, x + 1, y);
        }
        for (int i = 1; i <= board.length; i++) {
            board[x][y] = i;
            if (!isBoardLegal(board)) {
                continue;
            }
            if (solve(board, x + 1, y)) {
                return true;
            }
        }
        board[x][y] = 0;
        return false;
    }

}
