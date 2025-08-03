package sudoku;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class SudokuWidget extends JComponent {

    private static final Color GRID = Color.BLACK;

    private SudokuGame game;

    public SudokuWidget() {
        newGame();
    }

    public void newGame() {
        game = new SudokuGame();
    }

    private int getScaling(int width, int height) {
        int w = this.getWidth() / width;
        int h = this.getHeight() / height;
        return Math.min(w, h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int SIZE = SudokuGame.SIZE;
        int[][] board = game.getBoard();

        int s = getScaling(SIZE, SIZE);
        // Draw background
        g.setColor(GRID);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.drawRect(x * s, y * s, s, s);
            }
        }
    }

}
