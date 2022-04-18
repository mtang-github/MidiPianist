package window.frame;

import javax.swing.*;
import java.awt.*;

/**
 * A {@code NonResizableFrame} is a {@code JFrame} which cannot be resized.
 * It also provides a window close subject.
 */
public class NonResizableFrame extends BroadcastOnCloseFrame {

    private final int windowedWidth;
    private final int windowedHeight;
    private final String title;
    private final JComponent content;

    /**
     * Constructs a {@code NonResizableFrame} with the given dimensions, title, and content.
     *
     * @param width the preferred width of the frame.
     * @param height the preferred height of the frame.
     * @param title the title of the frame to display to the user.
     * @param content the {@code JComponent} content to display within the frame.
     */
    public NonResizableFrame(int width, int height, String title, JComponent content){
        super();

        windowedWidth = width;
        windowedHeight = height;
        this.title = title;
        this.content = content;
        init();
    }

    /**
     * Initiates the frame by setting the dimensions, title, and content of the frame. Makes the frame visible.
     */
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