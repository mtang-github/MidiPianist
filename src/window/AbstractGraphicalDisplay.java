package window;

import util.observer.IObserver;

import java.awt.image.BufferedImage;

public interface AbstractGraphicalDisplay {
    IObserver<BufferedImage> getImageReceiver();
}