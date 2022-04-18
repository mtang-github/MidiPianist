package window.frame;

import util.observer.ISubject;
import util.observer.Subject;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A {@code BroadcastOnCloseFrame} extends {@code JFrame} by providing a window close subject.
 */
public abstract class BroadcastOnCloseFrame extends JFrame {

    private final Subject<Void> windowCloseBroadcaster;

    /**
     * Constructs a {@code BroadcastOnCloseFrame} with a new window close subject.
     */
    public BroadcastOnCloseFrame(){
        this.windowCloseBroadcaster = makeWindowCloseBroadcaster();
    }

    /**
     * Constructs a new window close subject and attaches it to this frame.
     * @return the new window close subject
     */
    private Subject<Void> makeWindowCloseBroadcaster(){
        Subject<Void> windowCloseBroadcaster = new Subject<>();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                windowCloseBroadcaster.broadcast(null);
            }
        });
        return windowCloseBroadcaster;
    }

    /**
     * Returns the window close subject attached to this frame
     * @return the window close subject attached to this frame
     */
    public ISubject<Void> getWindowCloseBroadcaster(){
        return windowCloseBroadcaster;
    }
}
