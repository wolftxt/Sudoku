package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * JComponent class showing the sudoku board and all the selectable numbers.
 * Allows the user to select the numbers by clicking and change the numbers on
 * the board by clicking on them. Also allows the user to select from the
 * selectable numbers by typing the keys that represent those numbers on their
 * keyboard.
 *
 * @author davidwolf
 */
public class SudokuWidget extends JComponent {

    private static final Color BG = new Color(60, 60, 60);
    private static final Color GRID = Color.BLACK;
    private static final int MARGIN = 5;

    private Color[] colors;
    private SudokuGame game;
    private int selected;

    public SudokuWidget() {
        newGame();
    }

    public SudokuGame getGame() {
        return game;
    }

    public void newGame() {
        Settings settings = Settings.getInstance();
        final int size = settings.SUDOKU_BOARD_SIZE_DO_NOT_INPUT_MORE_THAN_4;
        Thread current = Thread.currentThread();
        Thread interruptThread = Thread.ofVirtual().start(() -> {
            try {
                long time = 100;
                while (!Thread.interrupted()) {
                    /* Uses exponential backoff to restart new game creation.
                    This is necessary because we need to filter out randomly created 
                    almost impossible to solve boards for small and big boards 
                    and for hardware of any speed*/
                    Thread.sleep(time);
                    current.interrupt();
                    time *= 2;
                }
            } catch (InterruptedException ex) {
            }
        });
        boolean success = false;
        while (!success) {
            try {
                game = new SudokuGame(size, settings.STARTING_PIECES);
                success = true;
            } catch (InterruptedException e) {
            }
        }
        selected = 0;
        interruptThread.interrupt();

        generateColors(size * size);
        this.repaint();
    }

    private void generateColors(int count) {
        colors = new Color[count + 1];
        colors[0] = new Color(0, 0, 0, 0);
        float startHue = 0.0f;
        float endHue = 0.83f;
        float increment = (endHue - startHue) / (count - 1);
        for (int i = 0; i < colors.length - 1; i++) {
            float hue = startHue + (i * increment);
            int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            colors[i + 1] = new Color(rgb);
        }
    }

    public void setSelected(int val) {
        if (val < 0 || val > game.getSize()) {
            return;
        }
        selected = val;
        this.repaint();
    }

    public boolean hint() {
        return game.hint(selected);
    }

    public void click(int x, int y) {
        if (x < MARGIN || x > this.getWidth() - MARGIN || y < MARGIN || y > this.getWidth() - MARGIN) {
            return; // Clicked inside of MARGIN
        }
        int SIZE = game.getSize();
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
        final int SIZE = game.getSize();
        int[][] board = game.getBoard();
        
        int s = getScaling(SIZE, SIZE);
        int xOffset = getXOffset(SIZE, SIZE);

        Settings settings = Settings.getInstance();
        
        // Draw background
        g.setColor(BG);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Draw opaque colors
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.setColor(getHighlightColor(x, y));
                g.fillRect(xOffset + x * s + MARGIN, y * s + MARGIN, s, s);
            }
        }

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
        // Draw numbers
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int num = board[x][y];
                g.setColor(colors[num]);
                int xStart = xOffset + x * s + s / 4 + MARGIN; // adding s / 4 centralises the numbers
                int yStart = (y + 1) * s - s / 4 + MARGIN; // + 1 because strings are drawn in the top right direction
                g.drawString(Integer.toString(num, Character.MAX_RADIX), xStart, yStart);
            }
        }
        // Draw selectable numbers
        int xStart = xOffset + 5 * MARGIN + SIZE * s;
        s = s * SIZE / (SIZE + 1);
        g.setFont(new Font("SudokuFont", Font.BOLD, s * 2 / 3));
        for (int i = 0; i <= SIZE; i++) {
            if (selected == i) {
                g.setColor(settings.SELECTED_COLOR);
                g.fillRect(xStart, i * s + MARGIN, s, s);
            }
            g.setColor(GRID);
            g.drawRect(xStart, i * s + MARGIN, s, s);
            g.setColor(colors[i]);
            int x = xStart + s / 4; // adding s / 4 centralises the numbers
            int y = (i + 1) * s - s / 4 + MARGIN; // + 1 because strings are drawn in the top right direction
            g.drawString(Integer.toString(i, Character.MAX_RADIX), x, y);
        }
    }

    private Color getHighlightColor(int x, int y) {
        Settings settings = Settings.getInstance();
        if (!game.getEditable()[x][y]) {
            return settings.NOT_EDITABLE;
        }
        if (game.isWon()) {
            return settings.WON_COLOR;
        }
        if (selected != 0 && game.isPlacementLegal(x, y, selected)) {
            return settings.LEGAL_MOVE_COLOR;
        }
        return colors[0];
    }

}
