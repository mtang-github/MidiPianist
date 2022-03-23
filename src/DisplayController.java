import resource.AbstractResourceManager;
import util.observer.AbstractObserver;
import util.observer.AbstractPushObserver;
import util.observer.ConfigurablePushSubject;
import util.Tuple2;

import javax.sound.midi.MidiMessage;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayController {

    private final AbstractObserver updateReceiver;
    private final ConfigurablePushSubject<BufferedImage> imageBroadcaster;
    private final AbstractPushObserver<Tuple2<MidiMessage, Long>> midiMessageReceiver;

    private final BufferedImage toDraw;
    private final NoteData noteData;
    private final PianoDisplay pianoDisplay;

    public DisplayController(int width, int height, AbstractResourceManager<BufferedImage> imageManager){
        toDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        noteData = new NoteData();
        pianoDisplay = new PianoDisplay(imageManager);
        updateReceiver = makeUpdateReceiver();
        imageBroadcaster = new ConfigurablePushSubject<>();
        midiMessageReceiver = new NoteDataUpdater(noteData);
    }

    private AbstractObserver makeUpdateReceiver(){
        return () -> {
            pianoDisplay.readAndUpdateNoteData(noteData);

            Graphics2D g2d = toDraw.createGraphics();
            pianoDisplay.drawOn(g2d);
            g2d.dispose();

            imageBroadcaster.setPushData(toDraw);
            imageBroadcaster.broadcast();
        };
    }

    public AbstractObserver getUpdateReceiver() {
        return updateReceiver;
    }

    public ConfigurablePushSubject<BufferedImage> getImageBroadcaster() {
        return imageBroadcaster;
    }

    public AbstractPushObserver<Tuple2<MidiMessage, Long>> getMidiMessageReceiver() {
        return midiMessageReceiver;
    }
}
