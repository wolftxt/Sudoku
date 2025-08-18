package sudoku;

public class SudokuSolver {

    public static boolean isFull(int[][] board) {
        int SIZE = board.length;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValid(int[][] board, int x, int y, int num) {
        for (int i = 0; i < board.length; i++) {
            if (board[x][i] == num) {
                return false;
            }
            if (board[i][y] == num) {
                return false;
            }
        }
        int count = (int) Math.sqrt(board.length);
        int row = x - x % count;
        int col = y - y % count;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (board[row + i][col + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Recursive backtracking algorithm that solves the sudoku board, uses
     * exponential time complexity
     *
     * @param board
     * @param x
     * @param y
     * @return
     * @throws InterruptedException
     */
    public static boolean solve(int[][] board, int x, int y) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException("Was interrupted during the solve method");
        }
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
            if (!isValid(board, x, y, i)) {
                continue;
            }
            board[x][y] = i;
            if (solve(board, x + 1, y)) {
                return true;
            }
            board[x][y] = 0;
        }
        return false;
    }

}
