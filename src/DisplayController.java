import resource.IResourceManager;
import util.Tuple2;
import util.observer.IObserver;
import util.observer.ISubject;
import util.observer.Subject;

import javax.sound.midi.MidiMessage;
import java.awt.*;
import java.awt.image.BufferedImage;

//todo: put in display package?
public class DisplayController {

    private final IResourceManager<BufferedImage> imageManager;

    private final IObserver<Void> updateReceiver;
    private final Subject<BufferedImage> imageBroadcaster;
    private final IObserver<Tuple2<MidiMessage, Long>> midiMessageReceiver;

    private final BufferedImage toDraw;
    private final TotalNoteData totalNoteData;
    private final TotalDisplay totalDisplay;

    public DisplayController(int width, int height, IResourceManager<BufferedImage> imageManager){
        this.imageManager = imageManager;
        toDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        totalNoteData = new TotalNoteData();
        totalDisplay = new TotalDisplay();
        updateReceiver = makeUpdateReceiver();
        imageBroadcaster = new Subject<>();
        midiMessageReceiver = new NoteDataUpdater(totalNoteData);
    }

    private IObserver<Void> makeUpdateReceiver(){
        return (Void) -> {
            totalDisplay.readAndUpdateNoteData(totalNoteData);

            Graphics2D g2d = toDraw.createGraphics();
            totalDisplay.drawOn(g2d, imageManager);
            g2d.dispose();

            imageBroadcaster.broadcast(toDraw);
        };
    }

    public IObserver<Void> getUpdateReceiver() {
        return updateReceiver;
    }

    public ISubject<BufferedImage> getImageBroadcaster() {
        return imageBroadcaster;
    }

    public IObserver<Tuple2<MidiMessage, Long>> getMidiMessageReceiver() {
        return midiMessageReceiver;
    }
}
