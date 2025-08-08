package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

public class Settings implements Serializable {

    private static final String FILENAME = "Settings";

    private static Settings instance;

    private Settings() {
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = load();
        }
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public static void resetToDefaults() {
        instance = new Settings();
        File folder = getFolder();
        File file = new File(folder, FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void save() throws IOException {
        File folder = getFolder();
        File file = new File(folder, FILENAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(instance);
        oos.close();
    }

    private static Settings load() {
        File folder = getFolder();
        File file = new File(folder, FILENAME);

        Settings result = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            result = (Settings) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
        }
        return result;
    }

    private static File getFolder() {
        URL url = Settings.class.getProtectionDomain().getCodeSource().getLocation();
        return new File(url.getPath()).getParentFile();
    }
    public int SUDOKU_BOARD_SIZE = 3;
    public int STARTING_PIECES = 10;
}
