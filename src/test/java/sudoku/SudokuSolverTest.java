package sudoku;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class SudokuSolverTest {

    public SudokuSolverTest() {
    }

    @Test
    public void testIsBoardLegal() {
    }

    private void checkSolve(int[][] board) {
        Assert.assertNotNull(board);
        Assert.assertTrue(SudokuSolver.isFull(board));
        Assert.assertTrue(SudokuSolver.isBoardLegal(board));
    }

    @Test
    public void testSolve1() {
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
    public void testSolve2() {
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
    public void testSolve3() {
        int[][] board = new int[9][9];
        boolean result = SudokuSolver.solve(board, 0, 0);
        Assert.assertTrue(result);
        checkSolve(board);
    }

}
