package window;

import util.observer.AbstractObserver;
import util.observer.AbstractPushObserver;
import util.observer.AbstractSubject;
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

    //todo: combine these two, when we send the image immediately render it
    public AbstractPushObserver<BufferedImage> getImageReceiver(){
        return graphicalDisplay.getImageReceiver();
    }
    public AbstractObserver getImageUpdateReceiver(){
        return graphicalDisplay.getImageUpdateReceiver();
    }
    public AbstractSubject getWindowCloseBroadcaster(){
        return frame.getWindowCloseBroadcaster();
    }
}