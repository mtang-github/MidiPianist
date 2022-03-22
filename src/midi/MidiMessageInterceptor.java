package midi;

import util.observer.ConfigurablePushSubject;
import util.tuple.Tuple2;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class MidiMessageInterceptor implements Transmitter, Receiver {

    private boolean closed;
    private final ConfigurablePushSubject<Tuple2<MidiMessage, Long>> midiMessageBroadcaster;
    private Receiver receiver;

    /**
     * Constructs a new {@code MidiMessageInterceptor} with no receiver.
     */
    public MidiMessageInterceptor(){
        closed = false;
        midiMessageBroadcaster = new ConfigurablePushSubject<>();
        receiver = null;
    }

    //todo this allows callers to call broadcast()... probably need to rethink this
    public ConfigurablePushSubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        return midiMessageBroadcaster;
    }

    /**
     * Sends a MIDI message and time-stamp to this receiver. If time-stamping is
     * not supported by this receiver, the time-stamp value should be -1.
     *
     * @param message   the MIDI message to send
     * @param timeStamp the time-stamp for the message, in microseconds
     * @throws IllegalStateException if the receiver is closed
     */
    @Override
    public void send(MidiMessage message, long timeStamp){
        if(closed){
            throw new IllegalStateException("This MidiEventInterceptor is closed");
        }
        //broadcast to listeners
        midiMessageBroadcaster.setPushData(new Tuple2<>(message, timeStamp));
        midiMessageBroadcaster.broadcast();
        if(receiver != null) {
            receiver.send(message, timeStamp);
        }
    }

    /**
     * Sets the receiver to which this transmitter will deliver MIDI messages.
     * If a receiver is currently set, it is replaced with this one.
     *
     * @param receiver the desired receiver
     */
    @Override
    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
    }

    /**
     * Obtains the current receiver to which this transmitter will deliver MIDI
     * messages.
     *
     * @return the current receiver. If no receiver is currently set, returns
     * {@code null}.
     */
    @Override
    public Receiver getReceiver(){
        return receiver;
    }

    /**
     * Indicates that the application has finished using the transmitter, and
     * that limited resources it requires may be released or made available.
     * <p>
     * If the creation of this {@code Transmitter} resulted in implicitly
     * opening the underlying device, the device is implicitly closed by this
     * method. This is true unless the device is kept open by other
     * {@code Receiver} or {@code Transmitter} instances that opened the device
     * implicitly, and unless the device has been opened explicitly. If the
     * device this {@code Transmitter} is retrieved from is closed explicitly by
     * calling {@link MidiDevice#close MidiDevice.close}, the
     * {@code Transmitter} is closed, too. For a detailed description of
     * open/close behaviour see the class description of
     * {@link MidiDevice MidiDevice}.
     *
     * @see MidiSystem#getTransmitter
     */
    @Override
    public void close(){
        closed = true;
    }
}
