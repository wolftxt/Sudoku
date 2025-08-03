package sudoku;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        widget.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                widget.click(e.getX(), e.getY());
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
            System.out.println("NOT IMPLEMENTED");
        });
        game.add(hint);

        JMenuItem solve = new JMenuItem("Solve");
        solve.addActionListener(e -> {
            System.out.println("NOT IMPLEMENTED");
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
