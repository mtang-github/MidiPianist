package midi;

import util.Tuple2;
import util.observer.IObserver;
import util.observer.ISubject;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.io.InputStream;

/**
 * A {@code MidiController} provides the interface for accessing MIDI playback capabilities.
 */
public class MidiController {

    private final InterceptingMidiDeviceCoordinator midiDeviceCoordinator;
    private final IObserver<InputStream> trackStartReceiver;
    private final IObserver<Void> sequencerResetReceiver;

    /**
     * Constructs a {@code MidiController} and initiates the underlying MIDI system
     */
    public MidiController(){
        this.midiDeviceCoordinator = new InterceptingMidiDeviceCoordinator();
        trackStartReceiver = makeTrackStartReceiver();
        sequencerResetReceiver = makeSequencerResetReceiver();
    }

    /**
     * Begins playback of the specified MIDI sequence.
     *
     * @param sequence the MIDI sequence to play
     */
    private void startTrack(InputStream sequence){
        Sequencer sequencer = midiDeviceCoordinator.getSequencer();
        resetSequencer(sequencer);
        try {
            sequencer.setSequence(sequence);
            sequencer.start();
        }
        catch (InvalidMidiDataException | IOException ignored) {}
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops playback on the specified sequencer
     *
     * @param sequencer the sequencer to stop
     */
    private void resetSequencer(Sequencer sequencer){
        sequencer.stop();
    }

    /**
     * Creates an observer used for receiving track start messages.
     * @return an observer used for receiving track start messages.
     */
    private IObserver<InputStream> makeTrackStartReceiver(){
        return this::startTrack;
    }

    /**
     * Creates an observer used for receiving sequencer reset messages.
     * @return an observer used for receiving sequencer reset messages.
     */
    private IObserver<Void> makeSequencerResetReceiver(){
        return (Void) -> resetSequencer(midiDeviceCoordinator.getSequencer());
    }

    /**
     * Returns the observer used for receiving track start messages.
     * @return the observer used for receiving track start messages.
     */
    public IObserver<InputStream> getTrackStartReceiver(){
        return trackStartReceiver;
    }
    /**
     * Returns the observer used for receiving sequencer reset messages.
     * @return the observer used for receiving sequencer reset messages.
     */
    public IObserver<Void> getSequencerResetReceiver(){
        return sequencerResetReceiver;
    }
    /**
     * Returns the subject used for broadcasting intercepted MIDI messages.
     * @return the subject used for broadcasting intercepted MIDI messages.
     */
    public ISubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        return midiDeviceCoordinator.getMidiMessageBroadcaster();
    }

    /**
     * Cleans up the MIDI playback environment.
     */
    public void cleanUp(){
        midiDeviceCoordinator.cleanUp();
    }
}