package sudoku;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class SudokuSolverTest {

    public static boolean isBoardLegal(int[][] board) {
        final int SIZE = board.length;
        for (int[] arr : board) {
            int[][] row = new int[1][SIZE];
            row[0] = arr;
            if (!isSubarrayLegal(row, SIZE)) {
                return false;
            }
        }
        for (int y = 0; y < SIZE; y++) {
            int[][] col = new int[1][SIZE];
            for (int x = 0; x < SIZE; x++) {
                col[0][x] = board[x][y];
            }
            if (!isSubarrayLegal(col, SIZE)) {
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
                if (!isSubarrayLegal(arr, SIZE)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSubarrayLegal(int[][] board, int SIZE) {
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

    private void checkSolve(int[][] board) {
        Assert.assertNotNull(board);
        Assert.assertTrue(SudokuSolver.isFull(board));
        Assert.assertTrue(isBoardLegal(board));
    }

    @Test
    public void testSolve1() throws InterruptedException {
        int[][] board = {{9, 1, 2, 8, 4, 6, 5, 7, 3},
        {6, 8, 3, 5, 7, 1, 2, 9, 4},
        {4, 5, 7, 3, 2, 9, 1, 6, 8},
        {8, 2, 9, 6, 1, 3, 4, 5, 7},
        {1, 6, 4, 9, 5, 7, 8, 3, 2},
        {3, 7, 5, 2, 8, 4, 6, 1, 9},
        {7, 4, 6, 1, 9, 2, 3, 8, 5},
        {5, 9, 1, 4, 3, 8, 7, 2, 6},
        {2, 3, 8, 7, 6, 0, 0, 0, 0}};
        boolean result = SudokuSolver.solve(board, 0, 0);
        Assert.assertTrue(result);
        checkSolve(board);
    }

    @Test
    public void testSolve2() throws InterruptedException {
        int[][] board = {{9, 1, 2, 8, 4, 6, 5, 7, 3},
        {6, 8, 3, 5, 7, 1, 2, 9, 4},
        {4, 5, 7, 3, 2, 9, 1, 6, 8},
        {8, 2, 9, 6, 1, 3, 4, 5, 7},
        {1, 6, 4, 9, 5, 7, 8, 3, 2},
        {3, 7, 5, 2, 8, 4, 6, 1, 9},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0}};
        boolean result = SudokuSolver.solve(board, 0, 0);
        Assert.assertTrue(result);
        checkSolve(board);
    }

    @Test
    public void testSolve3() throws InterruptedException {
        int[][] board = new int[9][9];
        boolean result = SudokuSolver.solve(board, 0, 0);
        Assert.assertTrue(result);
        checkSolve(board);
    }

}
