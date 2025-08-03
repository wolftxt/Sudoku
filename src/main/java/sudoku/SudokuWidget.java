package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;

public class SudokuWidget extends JComponent {

    private static final Color GRID = Color.BLACK;
    private static final int MARGIN = 5;
    private static final Color[] COLORS = new Color[]{new Color(0, 0, 0, 0), Color.RED, new Color(255, 127, 0), Color.YELLOW, Color.GREEN, new Color(0, 127, 0), Color.CYAN, Color.BLUE, Color.PINK, new Color(170, 0, 170)};

    private SudokuGame game;

    public SudokuWidget() {
        newGame();
    }

    public void newGame() {
        game = new SudokuGame();
    }

    private int getScaling(int width, int height) {
        int w = (this.getWidth() - 2 * MARGIN) / width;
        int h = (this.getHeight() - 2 * MARGIN) / height;
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
                g.drawRect(x * s + MARGIN, y * s + MARGIN, s, s);
            }
        }
        // Draw thicker grid around 3x3 parts
        int thickness = 5;
        int offset = thickness / 2;
        int count = (int) Math.sqrt(SIZE);
        for (int x = 0; x <= count; x++) {
            g.fillRect(count * x * s - offset + MARGIN, -offset + MARGIN, thickness, s * SIZE + thickness);
        }
        for (int y = 0; y <= count; y++) {
            g.fillRect(-offset + MARGIN, count * y * s - offset + MARGIN, s * SIZE + thickness, thickness);
        }
        // Draw numbers
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int num = board[x][y];
                g.setColor(COLORS[num]);
                int xStart = x * s + s / 4 + MARGIN; // adding s / 4 centralises the numbers
                int yStart = (y + 1) * s - s / 4 + MARGIN; // + 1 because strings are drawn in the top right direction
                g.drawString(Integer.toString(num), xStart, yStart);
            }
        }
    }

}
