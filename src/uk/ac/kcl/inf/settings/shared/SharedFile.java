package uk.ac.kcl.inf.settings.shared;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public abstract class SharedFile {
    private Path _path;
    private List<SharedFileChangeListener> _listeners;
    
    public SharedFile (Path path) {
        _path = path;
        _listeners = new LinkedList<> ();
    }
    
    public void addChangeListener (SharedFileChangeListener listener) {
        _listeners.add (listener);
    }
    
    public PrintWriter edit () throws IOException {
        FileChannel channel = FileChannel.open (_path);
        Writer writer = Channels.newWriter (channel, StandardCharsets.UTF_16.newEncoder (), -1);
        PrintWriter out = new PrintWriter (writer);
        
        channel.lock ();
       
        return out;
    }
    
    public Path getPath () {
        return _path;
    }
    
    public void load () {
        List<SharedFileChange> changes = update ();
        
        for (SharedFileChange change : changes) {
            for (SharedFileChangeListener listener : _listeners) {
                listener.sharedFileChange (change);
            }
        }
    }
    
    public void removeChangeListener (SharedFileChangeListener listener) {
        _listeners.remove (listener);
    }
    
    protected abstract List<SharedFileChange> update ();
}
