package window;

import util.observer.AbstractPushObserver;

import java.awt.image.BufferedImage;

public interface AbstractGraphicalDisplay {
    AbstractPushObserver<BufferedImage> getImageReceiver();
}