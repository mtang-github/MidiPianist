package window;

import util.observer.IObserver;
import util.observer.ISubject;
import window.frame.AbstractFrame;
import window.frame.Frame;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class WindowController {
    private final AbstractGraphicalDisplay graphicalDisplay;
    private final AbstractFrame frame;

    public WindowController(
            int width,
            int height,
            String title
    ){
        graphicalDisplay = new BufferedImagePanel(width, height);
        frame = new Frame(width, height, title, (JComponent)graphicalDisplay);
    }

    public IObserver<BufferedImage> getImageReceiver(){
        return graphicalDisplay.getImageReceiver();
    }
    public ISubject<Void> getWindowCloseBroadcaster(){
        return frame.getWindowCloseBroadcaster();
    }
}