package window.frame;

import util.observer.ISubject;
import util.observer.Subject;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractFrame extends JFrame {

    protected final Subject<Void> windowCloseBroadcaster;

    public AbstractFrame(){
        this.windowCloseBroadcaster = makeWindowCloseBroadcaster();
    }

    public Subject<Void> makeWindowCloseBroadcaster(){
        Subject<Void> windowCloseBroadcaster = new Subject<>();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                windowCloseBroadcaster.broadcast(null);
            }
        });
        return windowCloseBroadcaster;
    }

    public ISubject<Void> getWindowCloseBroadcaster(){
        return windowCloseBroadcaster;
    }
}
