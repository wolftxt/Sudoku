package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class SudokuGame {

    private int size = 9;
    private boolean won;
    // 0 is empty, 1-9 are numbers
    private int[][] board;
    private boolean[][] editable;

    public SudokuGame(int size, int originalPieceCount) throws InterruptedException {
        this.size = size * size;
        if (originalPieceCount < 0 || originalPieceCount > this.size * this.size) {
            throw new RuntimeException("Trying to start a game with an illegal piece count");
        }
        this.board = new int[this.size][this.size];
        this.editable = new boolean[this.size][this.size];
        for (boolean[] arr : editable) {
            Arrays.fill(arr, true);
        }
        Random r = new Random();
        while (originalPieceCount > 0) {
            int num = r.nextInt(1, this.size + 1);
            int x = r.nextInt(this.size);
            int y = r.nextInt(this.size);
            if (!placeNumber(x, y, num)) {
                continue;
            }
            if (SudokuSolver.solve(makeBoardCopy(), 0, 0)) {
                originalPieceCount--;
                editable[x][y] = false;
            } else {
                board[x][y] = 0;
            }
        }
        won = SudokuSolver.isFull(board);
    }

    public boolean hint(int num) {
        int[][] copy = makeBoardCopy();
        try {
            if (SudokuSolver.solve(copy, 0, 0)) {
                List<int[]> list = new ArrayList();
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        if (num == 0) {
                            if (board[x][y] == 0) {
                                list.add(new int[]{x, y});
                            }
                        } else {
                            if (board[x][y] == 0 && copy[x][y] == num) {
                                list.add(new int[]{x, y});
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    Random r = new Random();
                    int[] randomPoint = list.get(r.nextInt(list.size()));
                    board[randomPoint[0]][randomPoint[1]] = copy[randomPoint[0]][randomPoint[1]];
                }
                return true;
            }
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException was unexpectedly thrown upon a user called solve methods");
        }
        return false;
    }

    public boolean solve() {
        int[][] copy = makeBoardCopy();
        try {
            if (SudokuSolver.solve(copy, 0, 0)) {
                board = copy;
                return true;
            }
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException was unexpectedly thrown upon a user called solve methods");
        }
        return false;
    }

    public boolean isPlacementLegal(int x, int y, int num) {
        if (!editable[x][y]) {
            return false;
        }
        return SudokuSolver.isValid(board, x, y, num);
    }

    public boolean placeNumber(int x, int y, int num) {
        if (!editable[x][y]) {
            return false;
        }
        if (num != 0 && !SudokuSolver.isValid(board, x, y, num)) {
            return false;
        }
        board[x][y] = num;
        won = SudokuSolver.isFull(board);
        return true;
    }

    private int[][] makeBoardCopy() {
        int[][] copy = new int[size][];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(board[i], size);
        }
        return copy;
    }

}
