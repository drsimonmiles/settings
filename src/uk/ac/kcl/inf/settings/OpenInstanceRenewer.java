package uk.ac.kcl.inf.settings;

public class OpenInstanceRenewer implements Runnable {
    private static OpenInstanceRenewer _renewer = null;
    private boolean _open;

    private OpenInstanceRenewer () {
        _open = false;
    }
    
    public void close () {
        _open = false;
        Settings.get ().recordCloseInstance ();
    }
    
    @Override
    public void run () {
        try {
            _open = true;
            while (_open) {
                Settings.get ().recordOpenInstance ();
                Thread.sleep (Settings.OPEN_INSTANCE_DELAY);
            }
        } catch (InterruptedException ex) {
        }
    }

    public static OpenInstanceRenewer get () {
        if (_renewer == null) {
            _renewer = new OpenInstanceRenewer ();
        }
        return _renewer;
    }
    
    public void start () {
        new Thread (this).start ();
    }
}
