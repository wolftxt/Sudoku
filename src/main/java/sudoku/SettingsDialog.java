package sudoku;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 * Popup used as GUI for the game settings
 *
 * @author davidwolf
 */
public class SettingsDialog extends JDialog {

    public SettingsDialog(JFrame parent) {
        super(parent, "Configure settings:", true);
        Settings settings = Settings.getInstance();

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Field field : settings.getClass().getDeclaredFields()) {
            Object object;
            try {
                object = field.get(settings);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                continue;
            }
            addRow(panel, field, object);
        }

        JButton reset = new JButton("Reset all settings to defaults");
        reset.addActionListener(e -> {
            Settings.resetToDefaults();
            this.dispose();
        });

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.add(reset);
        panel.add(wrapper);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void addRow(JPanel panel, Field field, Object object) {
        String value = getStringValue(object);
        if (value == null) {
            return;
        }
        JPanel row = new JPanel(new BorderLayout(100, 10));

        String name = field.getName().replace('_', ' ').toLowerCase().replaceAll("seven", "7") + ": ";
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        if (capitalized.length() < 6) {
            capitalized = capitalized.toUpperCase();
        }
        JLabel label = new JLabel(capitalized);
        row.add(label, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel currValue = new JLabel(value);
        currValue.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(currValue);
        if (object instanceof Color color) {
            currValue.setForeground(color);
        }

        JButton button = new JButton("Change");
        button.addActionListener(e -> {
            setSetting(field, object, currValue);
        });
        right.add(button);

        row.add(right, BorderLayout.EAST);

        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panel.add(row);
        panel.add(Box.createVerticalStrut(10));
    }

    private static String getStringValue(Object object) {
        switch (object) {
            case Color c -> {
                StringBuilder sb = new StringBuilder();
                sb.append(c.getRed());
                sb.append(", ");
                sb.append(c.getGreen());
                sb.append(", ");
                sb.append(c.getBlue());
                return sb.toString();
            }
            case Integer i -> {
                return String.valueOf(i);
            }
            default -> {
                return null;
            }
        }
    }

    private void setSetting(Field field, Object object, JLabel label) {
        Settings gs = Settings.getInstance();
        try {
            switch (object) {
                case Color c -> {
                    c = JColorChooser.showDialog(this.getParent(), "Choose a new color", c, true);
                    if (c == null) {
                        return;
                    }
                    label.setForeground(c);
                    field.set(gs, c);
                    label.setText(getStringValue(c));
                }
                case Integer i -> {
                    String input = JOptionPane.showInputDialog(label, "Set a numeric value");
                    try {
                        i = Integer.parseInt(input);
                        field.set(gs, i);
                    } catch (NumberFormatException ex) {
                        System.err.println("Invalid number input");
                    }
                    label.setText(getStringValue(i));
                }
                default ->
                    JOptionPane.showMessageDialog(this.getParent(), "Unsupported datatype", "Cannot modify the datatype of this setting", JOptionPane.ERROR_MESSAGE);
            }
            Settings.save();
        } catch (IllegalArgumentException | IllegalAccessException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
