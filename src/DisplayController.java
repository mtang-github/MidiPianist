import resource.AbstractResourceManager;
import util.Tuple2;
import util.observer.IObserver;
import util.observer.ISubject;
import util.observer.Subject;

import javax.sound.midi.MidiMessage;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayController {

    private final IObserver<Void> updateReceiver;
    private final Subject<BufferedImage> imageBroadcaster;
    private final IObserver<Tuple2<MidiMessage, Long>> midiMessageReceiver;

    private final BufferedImage toDraw;
    private final NoteData noteData;
    private final PianoDisplay pianoDisplay;

    public DisplayController(int width, int height, AbstractResourceManager<BufferedImage> imageManager){
        toDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        noteData = new NoteData();
        pianoDisplay = new PianoDisplay(imageManager);
        updateReceiver = makeUpdateReceiver();
        imageBroadcaster = new Subject<>();
        midiMessageReceiver = new NoteDataUpdater(noteData);
    }

    private IObserver<Void> makeUpdateReceiver(){
        return (Void) -> {
            pianoDisplay.readAndUpdateNoteData(noteData);

            Graphics2D g2d = toDraw.createGraphics();
            pianoDisplay.drawOn(g2d);
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
