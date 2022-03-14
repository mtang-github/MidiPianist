package window.frame;

import javax.swing.*;
import java.awt.*;

public class Frame extends AbstractFrame {

    private final int windowedWidth;
    private final int windowedHeight;
    private final String title;
    private final JComponent content;

    public Frame(int width, int height, String title, JComponent component){
        super();

        windowedWidth = width;
        windowedHeight = height;
        this.title = title;
        content = component;
        init();
    }

    private void init(){
        setTitle(title);
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(content, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(windowedWidth, windowedHeight));
        setUndecorated(false);
        pack();

        setVisible(true);
    }
}