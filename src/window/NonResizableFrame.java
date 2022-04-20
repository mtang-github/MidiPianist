package window;

import util.observer.ISubject;
import util.observer.Subject;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * A {@code NonResizableFrame} is a {@code JFrame} which cannot be resized. It also provides a window close subject and
 * a file drop subject.
 */
public class NonResizableFrame extends JFrame {

    private final Subject<Void> windowCloseBroadcaster;
    private final Subject<List<File>> fileDropBroadcaster;

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
        windowCloseBroadcaster = makeWindowCloseBroadcaster();
        fileDropBroadcaster = makeFileDropBroadcaster();

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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(windowedWidth, windowedHeight));
        setUndecorated(false);
        pack();

        setVisible(true);
    }

    /**
     * Constructs a new window close subject and attaches it to this frame.
     * @return the new window close subject.
     */
    private Subject<Void> makeWindowCloseBroadcaster(){
        Subject<Void> windowCloseBroadcaster = new Subject<>();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                windowCloseBroadcaster.broadcast(null);
            }
        });
        return windowCloseBroadcaster;
    }

    /**
     * Constructs a new file drop subject and attaches it to this frame.
     * @return the new file drop subject.
     */
    private Subject<List<File>> makeFileDropBroadcaster(){
        Subject<List<File>> fileDropBroadcaster = new Subject<>();
        new DropTarget(this, new DropTargetListener() {

            @Override
            @SuppressWarnings("unchecked")
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);

                Transferable transferable = event.getTransferable();

                DataFlavor[] flavors = transferable.getTransferDataFlavors();

                for (DataFlavor flavor : flavors) {
                    if (flavor.isFlavorJavaFileListType()) {
                        try {
                            List<File> files = (List<File>) transferable.getTransferData(flavor);
                            fileDropBroadcaster.broadcast(files);
                        }
                        catch (UnsupportedFlavorException ufe) {
                            throw new RuntimeException(ufe);
                        }
                        catch (IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                    }
                }

                event.dropComplete(true);
            }

            @Override
            public void dragEnter(DropTargetDragEvent event) {}
            @Override
            public void dragOver(DropTargetDragEvent event) {}
            @Override
            public void dropActionChanged(DropTargetDragEvent event) {}
            @Override
            public void dragExit(DropTargetEvent event) {}
        });
        return fileDropBroadcaster;
    }

    /**
     * Returns the window close subject attached to this frame.
     * @return the window close subject attached to this frame.
     */
    public ISubject<Void> getWindowCloseBroadcaster(){
        return windowCloseBroadcaster;
    }

    /**
     * Returns the file drop subject attached to this frame.
     * @return the file drop subject attached to this frame.
     */
    public ISubject<List<File>> getFileDropBroadcaster(){
        return fileDropBroadcaster;
    }
}