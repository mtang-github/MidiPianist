package window;

import util.observer.IObserver;
import util.observer.ISubject;
import window.frame.BroadcastOnCloseFrame;
import window.frame.NonResizableFrame;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * A {@code WindowController} creates and provides the interface for accessing an application window.
 */
public class WindowController {
    private final IGraphicalDisplay graphicalDisplay;
    private final BroadcastOnCloseFrame frame;

    /**
     * Constructs a {@code WindowController} with the given dimensions and title.
     *
     * @param width the preferred width of the frame.
     * @param height the preferred height of the frame.
     * @param title the title of the frame to display to the user.
     */
    public WindowController(
            int width,
            int height,
            String title
    ){
        graphicalDisplay = new BufferedImagePanel(width, height);
        frame = new NonResizableFrame(width, height, title, (JComponent)graphicalDisplay);
    }

    /**
     * Returns the image receiver, which is a {@code BufferedImage} observer which causes the panel to repaint
     * itself with any new image received.
     *
     * @return the image receiver.
     */
    public IObserver<BufferedImage> getImageReceiver(){
        return graphicalDisplay.getImageReceiver();
    }

    /**
     * Returns the window close subject attached to this window.
     *
     * @return the window close subject attached to this window.
     */
    public ISubject<Void> getWindowCloseBroadcaster(){
        return frame.getWindowCloseBroadcaster();
    }
}