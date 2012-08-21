package uk.ac.kcl.inf.settings;

import java.awt.Color;
import java.util.Stack;
import javax.swing.JProgressBar;

public class ProgressLog {
    private static int BAR_LENGTH = 1000;
    private static JProgressBar _bar;
    private static Stack<Double> _steps;
    private static Stack<Double> _offsets;

    private ProgressLog () {
    }

    public static JProgressBar getBar () {
        if (_bar == null) {
            _bar = new JProgressBar (0, BAR_LENGTH);
            _bar.setBackground (Color.yellow);
            _bar.setForeground (Color.red);
        }
        return _bar;
    }

    public static void end () {
        if (_bar != null) {
            _steps.pop ();
            _offsets.pop ();
        }
    }

    public static void event (String message) {
        if (_bar != null) {
            double step = _steps.peek ();
            double current = _offsets.peek () + step;
            int oldValue = _bar.getValue ();
            int newValue = (int) Math.round (current);

            _offsets.set (_offsets.size () - 1, current);
            if (newValue != oldValue) {
                _bar.setValue (newValue);
            }
        }
    }

    public static void reset () {
        if (_bar != null) {
            _steps = new Stack<> ();
            _steps.push ((double) BAR_LENGTH);
            _offsets = new Stack<> ();
            _offsets.push (0.0);
            _bar.setValue (0);
        }
    }

    public static void split (int parts) {
        if (_bar != null) {
            double current = _steps.peek ();
            _steps.push (current / parts);
            _offsets.push (_offsets.peek ());
        }
    }
}
