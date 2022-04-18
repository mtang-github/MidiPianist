package midi;

import util.Tuple2;
import util.observer.ISubject;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

/**
 * An {@code InterceptingMidiDeviceCoordinator} coordinates a sequencer and a synthesizer while also offering a MIDI
 * message broadcaster which rebroadcasts all messages output by the sequencer.
 */
class InterceptingMidiDeviceCoordinator {
    //todo: microsoft specific
    private static final String DEFAULT_SYNTH_NAME = "Microsoft MIDI Mapper";

    private static Sequencer sequencer;
    private static MidiMessageInterceptor interceptor;
    private static MidiDevice synth;

    /**
     * Constructs an {@code InterceptingMidiDeviceCoordinator}, initiating the sequencer-interceptor-synthesizer
     * system.
     */
    public InterceptingMidiDeviceCoordinator(){
        init();
    }

    /**
     * Returns the sequencer this object is using.
     * @return the sequencer this object is using.
     */
    public Sequencer getSequencer() {
        if(sequencer == null){
            init();
        }
        return sequencer;
    }

    /**
     * Returns the synthesizer this object is using.
     * @return the synthesizer this object is using.
     */
    public MidiDevice getSynth() {
        if(synth == null){
            init();
        }
        return synth;
    }

    /**
     * Returns the subject used for broadcasting {@code Tuple2<MidiMessage, Long>} messages intercepted from the
     * sequencer.
     *
     * @return the subject used for broadcasting {@code Tuple2<MidiMessage, Long>} messages intercepted from the
     * sequencer.
     */
    public ISubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        if(interceptor == null){
            init();
        }
        return interceptor.getMidiMessageBroadcaster();
    }

    /**
     * Initiates the sequencer-interceptor-synthesizer system.
     */
    private static void init(){
        sequencer = getMidiSystemSequencer();
        clearSequencer(sequencer);

        synth = getMidiSystemSynth(DEFAULT_SYNTH_NAME);

        Receiver synthReceiver = getReceiverOfSynth(synth);

        Transmitter sequencerTransmitter = getTransmitterOfSequencer(sequencer);
        clearTransmitter(sequencerTransmitter);

        interceptor = new MidiMessageInterceptor();

        sequencerTransmitter.setReceiver(interceptor);
        interceptor.setReceiver(synthReceiver);

        openSynth(synth);
        openSequencer(sequencer);
    }

    /**
     * Returns the default sequencer as given by {@code MidiSystem}.
     *
     * @return the default sequencer as given by {@code MidiSystem}.
     *
     * @throws RuntimeException if the default synthesizer is unavailable.
     */
    private static Sequencer getMidiSystemSequencer(){
        try {
            return MidiSystem.getSequencer();
        }catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to get sequencer", mue);
        }
    }

    /**
     * Closes all receivers and transmitters attached to the given {@code Sequencer}.
     *
     * @param sequencer the {@code Sequencer} to clear.
     */
    private static void clearSequencer(Sequencer sequencer){
        for(Receiver r : sequencer.getReceivers()){
            r.close();
        }
        for(Transmitter t : sequencer.getTransmitters()){
            t.close();
        }
    }

    /**
     * Returns the synthesizer associated with the given synthesizer name as given by {@code MidiSystem}.
     *
     * @param synthName the name of the synthesizer to retrieve.
     *
     * @return the synthesizer associated with the given synthesizer name as given by {@code MidiSystem}.
     *
     * @throws RuntimeException if the specified synthesizer cannot be found or is otherwise unavailable.
     */
    private static MidiDevice getMidiSystemSynth(String synthName){
        try {
            for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
                String name = info.getName();
                if (name.equals(synthName)) {
                    return MidiSystem.getMidiDevice(info);
                }
            }
        }
        catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to open MIDI device \"" + synthName + '"', mue);
        }
        throw new RuntimeException("Unable to retrieve any synthesizer");
    }

    /**
     * Returns the receiver of the given synthesizer.
     *
     * @param synth the synthesizer to get the receiver of.
     *
     * @return the receiver of the given synthesizer.
     *
     * @throws RuntimeException if the receiver is unavailable.
     */
    private static Receiver getReceiverOfSynth(MidiDevice synth){
        try{
            return synth.getReceiver();
        }catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to get receiver of synth", mue);
        }
    }

    /**
     * Returns the transmitter of the given sequencer.
     *
     * @param sequencer the sequencer to get the transmitter of.
     *
     * @return the transmitter of the given sequencer.
     *
     * @throws RuntimeException if the transmitter is unavailable.
     */
    private static Transmitter getTransmitterOfSequencer(Sequencer sequencer){
        try{
            return sequencer.getTransmitter();
        }catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to get transmitter of sequencer", mue);
        }
    }

    /**
     * Closes all receivers attached to the given {@code Transmitter}.
     *
     * @param transmitter the {@code Transmitter} to clear.
     */
    private static void clearTransmitter(Transmitter transmitter){
        if(transmitter.getReceiver() != null) {
            transmitter.getReceiver().close();
        }
    }

    /**
     * Calls open() on the given synthesizer.
     *
     * @param synth the synthesizer to open.
     *
     * @throws RuntimeException if the call to open() fails.
     */
    private static void openSynth(MidiDevice synth){
        try{
            synth.open();
        }catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to open synth", mue);
        }
    }

    /**
     * Calls open() on the given sequencer.
     *
     * @param sequencer the sequencer to open.
     *
     * @throws RuntimeException if the call to open() fails.
     */
    private static void openSequencer(Sequencer sequencer){
        try{
            sequencer.open();
        }catch(MidiUnavailableException mue){
            throw new RuntimeException("Unable to open sequencer", mue);
        }
    }

    /**
     * Stops the sequencer and closes the sequencer-interceptor-sequencer system.
     */
    public void cleanUp(){
        sequencer.stop();
        synth.close();
        interceptor.close();
        sequencer.close();
    }
}
