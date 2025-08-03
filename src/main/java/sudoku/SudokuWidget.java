package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;

public class SudokuWidget extends JComponent {

    private static final Color GRID = Color.BLACK;
    private static final int MARGIN = 5;
    private static final Color[] COLORS = new Color[]{new Color(0, 0, 0, 0), Color.RED, new Color(255, 127, 0), Color.YELLOW, Color.GREEN, new Color(0, 127, 0), Color.CYAN, Color.BLUE, Color.PINK, new Color(170, 0, 170)};
    private static final Color SELECTEDCOLOR = new Color(255, 255, 0, 100);
    private static final Color WONCOLOR = new Color(0, 255, 0, 100);

    private SudokuGame game;
    private int selected;

    public SudokuWidget() {
        newGame();
    }

    public void newGame() {
        game = new SudokuGame(10);
        selected = 0;
        this.repaint();
    }

    public void setSelected(int val) {
        if (val < 0 || val > SudokuGame.SIZE) {
            return;
        }
        selected = val;
        this.repaint();
    }

    public void click(int x, int y) {
        if (x < MARGIN || x > this.getWidth() - MARGIN || y < MARGIN || y > this.getWidth() - MARGIN) {
            return; // Clicked inside of MARGIN
        }
        int SIZE = SudokuGame.SIZE;
        int s = getScaling(SIZE, SIZE);
        int xOffset = getXOffset(SIZE, SIZE);
        int xStart = xOffset + 5 * MARGIN + SIZE * s; // xStart for selectable numbers
        if (x > xStart) {
            s = s * SIZE / (SIZE + 1);
            y -= MARGIN;
            if (y / s >= 0 && y / s <= SIZE) {
                selected = y / s;
                this.repaint();
            }
            return;
        }
        x -= MARGIN + xOffset;
        y -= MARGIN;
        x /= s;
        y /= s;
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            game.placeNumber(x, y, selected);
            this.repaint();
        }
    }

    private int getScaling(int width, int height) {
        int w = (this.getWidth() - 6 * MARGIN) / (width + 1); // 4 MARGINS for space between board and selectable numbers
        int h = (this.getHeight() - 2 * MARGIN) / height;
        return Math.min(w, h);
    }

    private int getXOffset(int width, int height) {
        int w = (this.getWidth() - 6 * MARGIN) / (width + 1); // 4 MARGINS for space between board and selectable numbers
        int h = (this.getHeight() - 2 * MARGIN) / height;
        int s = Math.min(w, h);
        if (s == w) {
            return 0;
        }
        int xSpace = 6 * MARGIN + (width + 1) * s;
        return (this.getWidth() - xSpace) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int SIZE = SudokuGame.SIZE;
        int[][] board = game.getBoard();

        int s = getScaling(SIZE, SIZE);
        int xOffset = getXOffset(SIZE, SIZE);
        // Draw grid
        g.setColor(GRID);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.drawRect(xOffset + x * s + MARGIN, y * s + MARGIN, s, s);
            }
        }
        // Draw thicker grid around 3x3 parts
        int thickness = 5;
        int offset = thickness / 2;
        int count = (int) Math.sqrt(SIZE);
        for (int x = 0; x <= count; x++) {
            g.fillRect(xOffset + count * x * s - offset + MARGIN, -offset + MARGIN, thickness, s * SIZE + thickness);
        }
        for (int y = 0; y <= count; y++) {
            g.fillRect(xOffset - offset + MARGIN, count * y * s - offset + MARGIN, s * SIZE + thickness, thickness);
        }
        // Draw winning color
        if (game.isWon()) {
            g.setColor(WONCOLOR);
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    g.fillRect(xOffset + x * s + MARGIN, y * s + MARGIN, s, s);
                }
            }
        }
        // Draw numbers
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int num = board[x][y];
                g.setColor(COLORS[num]);
                int xStart = xOffset + x * s + s / 4 + MARGIN; // adding s / 4 centralises the numbers
                int yStart = (y + 1) * s - s / 4 + MARGIN; // + 1 because strings are drawn in the top right direction
                g.drawString(Integer.toString(num), xStart, yStart);
            }
        }
        // Draw selectable numbers
        int xStart = xOffset + 5 * MARGIN + SIZE * s;
        s = s * SIZE / (SIZE + 1);
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int i = 0; i <= SIZE; i++) {
            if (selected == i) {
                g.setColor(SELECTEDCOLOR);
                g.fillRect(xStart, i * s + MARGIN, s, s);
            }
            g.setColor(GRID);
            g.drawRect(xStart, i * s + MARGIN, s, s);
            g.setColor(COLORS[i]);
            int x = xStart + s / 4; // adding s / 4 centralises the numbers
            int y = (i + 1) * s - s / 4 + MARGIN; // + 1 because strings are drawn in the top right direction
            g.drawString(Integer.toString(i), x, y);
        }
    }

}
