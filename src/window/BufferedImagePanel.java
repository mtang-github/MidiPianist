package window;

import util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A {@code BufferedImagePanel} is a {@code JPanel} which implements {@link IGraphicalDisplay}. Whenever the set image
 * observer receives a new image, a {@code BufferedImagePanel} will try to repaint itself with the new image displayed.
 */
public class BufferedImagePanel extends JPanel implements IGraphicalDisplay {
    private final IObserver<BufferedImage> imageReceiver;
    private BufferedImage image;

    /**
     * Constructs a {@code BufferedImagePanel} with the given dimensions.
     *
     * @param width width of the created {@code BufferedImagePanel}
     * @param height height of the created {@code BufferedImagePanel}
     */
    public BufferedImagePanel(int width, int height){
        setBackground(Color.BLACK);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageReceiver = makeImageReceiver();
    }

    /**
     * Creates the image receiver, which is a {@code BufferedImage} observer which causes the panel to repaint
     * itself with any new image received.
     *
     * @return the image receiver
     */
    private IObserver<BufferedImage> makeImageReceiver() {
        return data -> {
            setImage(data);
            repaint();
        };
    }

    /**
     * Sets the stored image. Does not cause a repaint attempt.
     *
     * @param image the new image to store
     */
    private void setImage(BufferedImage image){
        this.image = image;
    }

    /**
     * Returns the image receiver, which is a {@code BufferedImage} observer which causes the panel to repaint
     * itself with any new image received.
     *
     * @return the image receiver
     */
    @Override
    public IObserver<BufferedImage> getImageReceiver() {
        return imageReceiver;
    }

    /**
     * {@inheritDoc}
     *
     * @param g the Graphics object to protect
     * @see #paint
     * @see javax.swing.plaf.ComponentUI
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
