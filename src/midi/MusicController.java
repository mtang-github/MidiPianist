package midi;

import util.Tuple2;
import util.observer.IObserver;
import util.observer.ISubject;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.io.InputStream;

public class MusicController {

    private final InterceptingMidiDeviceCoordinator midiDeviceCoordinator;
    private final IObserver<InputStream> trackStartReceiver;
    private final IObserver<Void> sequencerResetReceiver;

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

    private IObserver<InputStream> makeTrackStartReceiver(){
        return this::startTrack;
    }
    private IObserver<Void> makeSequencerResetReceiver(){
        return (Void) -> resetSequencer(midiDeviceCoordinator.getSequencer());
    }

    public IObserver<InputStream> getTrackStartReceiver(){
        return trackStartReceiver;
    }
    public IObserver<Void> getSequencerResetReceiver(){
        return sequencerResetReceiver;
    }
    public ISubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        return midiDeviceCoordinator.getMidiMessageBroadcaster();
    }

    public void cleanUp(){
        midiDeviceCoordinator.cleanUp();
    }
}