package sudoku;

import java.util.Arrays;

public class SudokuSolver {

    public static boolean isBoardLegal(int[][] board) {
        final int SIZE = SudokuGame.SIZE;
        for (int[] arr : board) {
            int[][] row = new int[1][SIZE];
            row[0] = arr;
            if (!isSubarrayLegal(row)) {
                return false;
            }
        }
        for (int x = 0; x < SIZE; x++) {
            int[][] col = new int[1][SIZE];
            for (int y = 0; y < SIZE; y++) {
                col[0][y] = board[x][y];
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

    public static int[][] solve(int[][] board) {
        throw new IllegalStateException("Not solvable");
    }

}
