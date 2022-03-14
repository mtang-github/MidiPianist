package midi;

import util.observer.AbstractObserver;
import util.observer.AbstractPushObserver;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;
import java.io.IOException;

public class MusicController {

    private final MidiDeviceCoordinator midiDeviceCoordinator;
    private final AbstractPushObserver<MidiSequence> trackStartReceiver;
    private final AbstractObserver sequencerResetReceiver;

    public MusicController(){
        this.midiDeviceCoordinator = new DefaultMidiDeviceCoordinator();
        trackStartReceiver = makeTrackStartReceiver();
        sequencerResetReceiver = makeSequencerResetReceiver();
    }

    private void startTrack(MidiSequence sequence){
        Sequencer sequencer = midiDeviceCoordinator.getSequencer();
        resetSequencer(sequencer);
        sequence.reset();
        try {
            sequencer.setSequence(sequence.getMidiInputStream());
            sequencer.start();
        } catch (InvalidMidiDataException | IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetSequencer(Sequencer sequencer){
        sequencer.stop();
    }

    private AbstractPushObserver<MidiSequence> makeTrackStartReceiver(){
        return this::startTrack;
    }
    private AbstractObserver makeSequencerResetReceiver(){
        return () -> resetSequencer(midiDeviceCoordinator.getSequencer());
    }

    public AbstractPushObserver<MidiSequence> getTrackStartReceiver(){
        return trackStartReceiver;
    }
    public AbstractObserver getSequencerResetReceiver(){
        return sequencerResetReceiver;
    }

    public void cleanUp(){
        midiDeviceCoordinator.cleanUp();
    }
}