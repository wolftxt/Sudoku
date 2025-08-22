package sudoku;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * Main class holding the window, eventqueue, JMenuBar, widget. Also configures
 * keybinds
 *
 * @author davidwolf
 */
public class SudokuWindow extends javax.swing.JFrame {

    JMenuBar bar;

    private LinkedBlockingQueue<Runnable> eventQueue;

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
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int menuSize = SudokuWindow.this.getHeight() / 35;
                int itemSize = SudokuWindow.this.getHeight() / 50;
                Font menuFont = new Font("BarFont", Font.PLAIN, menuSize);
                Font itemFont = new Font("BarFont", Font.PLAIN, itemSize);
                setJMenuBarFont(bar, menuFont, itemFont);
                bar.revalidate();
                bar.repaint();
            }
        });

        eventQueue = new LinkedBlockingQueue();
        new Thread(() -> {
            while (true) {
                try {
                    eventQueue.take().run();
                } catch (InterruptedException ex) {
                }
            }
        }).start();
    }

    private void help() {
        JOptionPane.showMessageDialog(this, "", "Welcome to Sudoku", JOptionPane.PLAIN_MESSAGE);
    }

    private void initComponents() {
        widget = new SudokuWidget();
        widget.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                eventQueue.add(() -> {
                    widget.click(e.getX(), e.getY());
                });
            }
        });
        widget.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                eventQueue.add(() -> {
                    widget.click(e.getX(), e.getY());
                });
            }
        });
        widget.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                char released = e.getKeyChar();
                if (Character.isDigit(released)) {
                    eventQueue.add(() -> {
                        widget.setSelected(Character.getNumericValue(released));
                    });
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_N -> {
                        eventQueue.add(() -> {
                            widget.newGame();
                        });
                    }
                    case KeyEvent.VK_H -> {
                        eventQueue.add(() -> {
                            if (widget.hint()) {
                                widget.repaint();
                            } else {
                                JOptionPane.showMessageDialog(SudokuWindow.this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                    case KeyEvent.VK_S -> {
                        eventQueue.add(() -> {
                            if (widget.getGame().solve()) {
                                widget.repaint();
                            } else {
                                JOptionPane.showMessageDialog(SudokuWindow.this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                }
            }
        });
        this.setLayout(new BorderLayout());

        this.add(widget, BorderLayout.CENTER);

        addMenuBar();
    }

    private void addMenuBar() {
        bar = new JMenuBar();
        JMenu game = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> {
            eventQueue.add(() -> {
                widget.newGame();
            });
        });
        game.add(newGame);

        JMenuItem hint = new JMenuItem("Hint");
        hint.addActionListener(e -> {
            eventQueue.add(() -> {
                if (widget.hint()) {
                    widget.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
        game.add(hint);

        JMenuItem solve = new JMenuItem("Solve");
        solve.addActionListener(e -> {
            eventQueue.add(() -> {
                if (widget.getGame().solve()) {
                    widget.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Game is not solvable", "Solution not found", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
        game.add(solve);

        game.addSeparator();

        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> {
            new SettingsDialog(this);
            widget.repaint();
        });
        game.add(settings);

        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(e -> {
            help();
        });
        game.add(help);

        bar.add(game);
        this.setJMenuBar(bar);
    }

    public static void setJMenuBarFont(JMenuBar menuBar, Font menuFont, Font itemFont) {
        for (Component component : menuBar.getComponents()) {
            if (component instanceof JMenu menu) {
                menu.setFont(menuFont);
                setMenuFont(menu, menuFont, itemFont);
            } else {
                component.setFont(itemFont);
            }
        }
    }

    private static void setMenuFont(JMenu menu, Font menuFont, Font itemFont) {
        for (Component component : menu.getMenuComponents()) {
            if (component instanceof JMenu jMenu) {
                jMenu.setFont(menuFont);
                setMenuFont(menu, menuFont, itemFont);
            } else {
                component.setFont(itemFont);
            }
        }
    }

    public static void main(String args[]) {
        FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new SudokuWindow().setVisible(true));
    }

    private SudokuWidget widget;
}
