package sudoku;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

public class SudokuWindow extends javax.swing.JFrame {

    public SudokuWindow() {
        this.setTitle("Sudoku");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        initComponents();
    }

    private void initComponents() {

        widget = new SudokuWidget();

        this.setLayout(new BorderLayout());

        this.add(widget, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu newGame = new JMenu("New Game");
        newGame.addActionListener(e -> widget.newGame());
        menuBar.add(newGame);

        JMenu hint = new JMenu("Hint");
        hint.addActionListener(e -> {
            throw new RuntimeException("NOT IMPLEMENTED");
        });
        menuBar.add(hint);

        JMenu solve = new JMenu("Solve");
        solve.addActionListener(e -> {
            throw new RuntimeException("NOT IMPLEMENTED");
        });
        menuBar.add(solve);

        this.setJMenuBar(menuBar);
    }

    public static void main(String args[]) {
        FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new SudokuWindow().setVisible(true));
    }

    private SudokuWidget widget;
}
