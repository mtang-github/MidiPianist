package midi;

import util.Tuple2;
import util.observer.ISubject;
import util.observer.Subject;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * A {@code MidiMessageInterceptor} intercepts and rebroadcasts messages.
 */
class MidiMessageInterceptor implements Transmitter, Receiver {

    private final Subject<Tuple2<MidiMessage, Long>> midiMessageBroadcaster;

    private boolean closed;
    private Receiver receiver;

    /**
     * Constructs a new {@code MidiMessageInterceptor} with no receiver.
     */
    public MidiMessageInterceptor(){
        closed = false;
        midiMessageBroadcaster = new Subject<>();
        receiver = null;
    }

    public ISubject<Tuple2<MidiMessage, Long>> getMidiMessageBroadcaster() {
        return midiMessageBroadcaster;
    }

    /**
     * Sends a MIDI message and time-stamp to this receiver. If time-stamping is
     * not supported by this receiver, the time-stamp value should be -1.
     *
     * @param message   the MIDI message to send.
     * @param timeStamp the time-stamp for the message, in microseconds.
     * @throws IllegalStateException if the receiver is closed.
     */
    @Override
    public void send(MidiMessage message, long timeStamp){
        if(closed){
            throw new IllegalStateException("This MidiEventInterceptor is closed");
        }
        midiMessageBroadcaster.broadcast(new Tuple2<>(message, timeStamp));
        if(receiver != null) {
            receiver.send(message, timeStamp);
        }
    }

    /**
     * Sets the receiver to which this transmitter will deliver MIDI messages.
     * If a receiver is currently set, it is replaced with this one.
     *
     * @param receiver the desired receiver.
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
     * Closes this interceptor, meaning all calls to {@link #send} will throw exceptions.
     */
    @Override
    public void close(){
        closed = true;
    }
}
