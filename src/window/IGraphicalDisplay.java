package window;

import util.observer.IObserver;

import java.awt.image.BufferedImage;

/**
 * An {@code IGraphicalDisplay} offers an observer which can receive {@code BufferedImage} messages.
 */
public interface IGraphicalDisplay {

    /**
     * Returns the observer used for receiving {@code BufferedImage} messages
     * @return the observer used for receiving {@code BufferedImage} messages
     */
    IObserver<BufferedImage> getImageReceiver();
}