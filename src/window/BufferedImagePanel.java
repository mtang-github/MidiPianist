package window;

import util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImagePanel extends JPanel implements IGraphicalDisplay {
    private final IObserver<BufferedImage> imageReceiver;
    private BufferedImage image;

    public BufferedImagePanel(int width, int height){
        setBackground(Color.BLACK);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageReceiver = makeImageReceiver();
    }

    private IObserver<BufferedImage> makeImageReceiver() {
        return data -> {
            setImage(data);
            repaint();
        };
    }

    private void setImage(BufferedImage image){
        this.image = image;
    }

    @Override
    public IObserver<BufferedImage> getImageReceiver() {
        return imageReceiver;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
