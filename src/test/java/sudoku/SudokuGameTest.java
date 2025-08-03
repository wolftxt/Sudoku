package sudoku;

import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class SudokuGameTest {

    public SudokuGameTest() {
    }

    @Test
    public void testPlaceNumber() {
        SudokuGame game = new SudokuGame(0);
        int[][] winningBoard = new int[][]{{9, 1, 2, 8, 4, 6, 5, 7, 3},
        {6, 8, 3, 5, 7, 1, 2, 9, 4},
        {4, 5, 7, 3, 2, 9, 1, 6, 8},
        {8, 2, 9, 6, 1, 3, 4, 5, 7},
        {1, 6, 4, 9, 5, 7, 8, 3, 2},
        {3, 7, 5, 2, 8, 4, 6, 1, 9},
        {7, 4, 6, 1, 9, 2, 3, 8, 5},
        {5, 9, 1, 4, 3, 8, 7, 2, 6},
        {2, 3, 8, 7, 6, 5, 9, 4, 1}};
        for (int x = 0; x < winningBoard.length; x++) {
            for (int y = 0; y < winningBoard[x].length; y++) {
                Assert.assertEquals(false, game.isWon());
                game.placeNumber(x, y, winningBoard[x][y]);
            }
        }
        Assert.assertEquals(true, game.isWon());
    }

}
