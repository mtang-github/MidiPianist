package midi;

import util.observer.AbstractObserver;
import util.observer.AbstractPushObserver;
import util.observer.ConfigurablePushSubject;
import util.Tuple2;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.io.InputStream;

public class MusicController {

    private final InterceptingMidiDeviceCoordinator midiDeviceCoordinator;
    private final AbstractPushObserver<InputStream> trackStartReceiver;
    private final AbstractObserver sequencerResetReceiver;

    public MusicController(){
        this.midiDeviceCoordinator = new InterceptingMidiDeviceCoordinator();
        trackStartReceiver = makeTrackStartReceiver();
        sequencerResetReceiver = makeSequencerResetReceiver();
    }

    private void startTrack(InputStream sequence){
        Sequencer sequencer = midiDeviceCoordinator.getSequencer();
        resetSequencer(sequencer);
        try {
            sequencer.setSequence(sequence);
            sequencer.start();
        } catch (InvalidMidiDataException | IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetSequencer(Sequencer sequencer){
        sequencer.stop();
    }

    private AbstractPushObserver<InputStream> makeTrackStartReceiver(){
        return this::startTrack;
    }
    private AbstractObserver makeSequencerResetReceiver(){
        return () -> resetSequencer(midiDeviceCoordinator.getSequencer());
    }

    public AbstractPushObserver<InputStream> getTrackStartReceiver(){
        return trackStartReceiver;
    }
    public AbstractObserver getSequencerResetReceiver(){
        return sequencerResetReceiver;
    }
    public ConfigurablePushSubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        return midiDeviceCoordinator.getMidiMessageBroadcaster();
    }

    public void cleanUp(){
        midiDeviceCoordinator.cleanUp();
    }
}