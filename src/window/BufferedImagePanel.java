package window;

import util.observer.AbstractPushObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImagePanel extends JPanel implements AbstractGraphicalDisplay {
    private BufferedImage image;
    private AbstractPushObserver<BufferedImage> imageReceiver;

    public BufferedImagePanel(int width, int height){
        setBackground(Color.BLACK);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        makeGraphicalObservers();
    }

    private void makeGraphicalObservers(){
        imageReceiver = makeImageReceiver();
    }

    private AbstractPushObserver<BufferedImage> makeImageReceiver() {
        return data -> {
            setImage(data);
            repaint();
        };
    }

    private void setImage(BufferedImage image){
        this.image = image;
    }

    @Override
    public AbstractPushObserver<BufferedImage> getImageReceiver() {
        return imageReceiver;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
