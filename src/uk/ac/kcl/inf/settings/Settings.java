package uk.ac.kcl.inf.settings;

import java.awt.Dimension;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JSplitPane;

public class Settings {
    private static final String LAST_RECORD_OPEN = "LAST_RECORD_OPEN";
    public static final long OPEN_INSTANCE_DELAY = 60000;
    private Preferences _preferences;
    private static Settings _singleton;

    private Settings (Class application) {
        _preferences = Preferences.userNodeForPackage (application);
    }

    public File getFile (Object type) {
        Path path = getFilePath (type);
        
        if (path == null) {
            return null;
        } else {
            return path.toFile ();
        }
    }
    
    public Path getFilePath (Object type) {
        String path = _preferences.get (type.toString (), null);
        
        if (path == null) {
            return null;
        } else {
            return Paths.get (path);
        }
    }

    public static Settings get () {
        if (_singleton == null) {
            throw new RuntimeException ("Must initialise settings with application class before use");
        }
        return _singleton;
    }

    public static Settings initialise (Class application) {
        _singleton = new Settings (application);
        return _singleton;
    }

    public boolean isOpenInstance () {
        long last = _preferences.getLong (LAST_RECORD_OPEN, 0);
        
         return last >= System.currentTimeMillis () - OPEN_INSTANCE_DELAY;
    }
    
    public void loadPosition (Object type, JFrame window) {
        int x = _preferences.getInt (type.toString () + "_WINDOW_X", 0);
        int y = _preferences.getInt (type.toString () + "_WINDOW_Y", 0);
        int width = _preferences.getInt (type.toString () + "_WINDOW_WIDTH", 800);
        int height = _preferences.getInt (type.toString () + "_WINDOW_HEIGHT", 800);
        
        window.setLocation (x, y);
        window.setPreferredSize (new Dimension (width, height));
    }
    
    public void loadPosition (Object type, JSlider slider) {
        slider.setValue (_preferences.getInt (type.toString (), 0));
    }

    public void loadPosition (Object type, JSplitPane split) {
        split.setDividerLocation (_preferences.getInt (type.toString (), 100));
    }

    public void recordCloseInstance () {
        _preferences.putLong (LAST_RECORD_OPEN, 0);
    }
    
    public void recordOpenInstance () {
        _preferences.putLong (LAST_RECORD_OPEN, System.currentTimeMillis ());
    }
    
    public void saveFile (Object type, File file) {
        saveFilePath (type, file.toPath ());
    }
    
    public void saveFilePath (Object type, Path path) {
        _preferences.put (type.toString (), path.toString ());
    }

    public void savePosition (Object type, JFrame window) {
        _preferences.putInt (type.toString () + "_WINDOW_X", window.getX ());
        _preferences.putInt (type.toString () + "_WINDOW_Y", window.getY ());
        _preferences.putInt (type.toString () + "_WINDOW_WIDTH", window.getWidth ());
        _preferences.putInt (type.toString () + "_WINDOW_HEIGHT", window.getHeight ());
    }

    public void savePosition (Object type, JSlider slider) {
        _preferences.putInt (type.toString (), slider.getValue ());
    }

    public void savePosition (Object type, JSplitPane split) {
        _preferences.putInt (type.toString (), split.getDividerLocation ());
    }
}
