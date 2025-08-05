package sudoku;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class SudokuWindow extends javax.swing.JFrame {

    public SudokuWindow() {
        this.setTitle("Sudoku");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        initComponents();
        this.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                widget.requestFocusInWindow();
            }
        });
    }

    private void initComponents() {
        widget = new SudokuWidget();
        widget.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                widget.click(e.getX(), e.getY());
            }
        });
        widget.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                widget.click(e.getX(), e.getY());
            }
        });
        widget.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                char released = e.getKeyChar();
                if (Character.isDigit(released)) {
                    widget.setSelected(Character.getNumericValue(released));
                }
            }
        });
        this.setLayout(new BorderLayout());

        this.add(widget, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> widget.newGame());
        game.add(newGame);

        game.addSeparator();

        JMenuItem hint = new JMenuItem("Hint");
        hint.addActionListener(e -> {
            if (widget.hint()) {
                widget.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
            }
        });
        game.add(hint);

        JMenuItem solve = new JMenuItem("Solve");
        solve.addActionListener(e -> {
            if (widget.getGame().solve()) {
                widget.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
            }
        });
        game.add(solve);

        menuBar.add(game);
        this.setJMenuBar(menuBar);
    }

    public static void main(String args[]) {
        FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new SudokuWindow().setVisible(true));
    }

    private SudokuWidget widget;
}
