package window.frame;

import util.observer.AbstractBlockableSubject;
import util.observer.AbstractSubject;
import util.observer.BlockableSubject;

import javax.swing.JFrame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractFrame extends JFrame {

    protected final AbstractBlockableSubject windowCloseBroadcaster;

    public AbstractFrame(){
        this.windowCloseBroadcaster = makeWindowCloseBroadcaster();
    }

    //todo what's the point of blockable?
    public AbstractBlockableSubject makeWindowCloseBroadcaster(){
        AbstractBlockableSubject windowCloseBroadcaster = new BlockableSubject();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                windowCloseBroadcaster.broadcast();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                windowCloseBroadcaster.unblock();
            }
        });
        return windowCloseBroadcaster;
    }

    public AbstractSubject getWindowCloseBroadcaster(){
        return windowCloseBroadcaster;
    }
}
