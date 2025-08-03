package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;

public class SudokuWidget extends JComponent {

    private static final Color GRID = Color.BLACK;
    private static final Color[] COLORS = new Color[]{new Color(0, 0, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, new Color(255, 128, 0)};

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
        // Draw grid
        g.setColor(GRID);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.drawRect(x * s, y * s, s, s);
            }
        }
        // Draw numbers
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int num = board[x][y];
                g.setColor(COLORS[num]);
                int xStart = x * s + s / 4; // adding s / 4 centralises the numbers
                int yStart = (y + 1) * s - s / 4; // + 1 because strings are drawn in the top right direction
                g.drawString(Integer.toString(num), xStart, yStart);
            }
        }
    }

}
