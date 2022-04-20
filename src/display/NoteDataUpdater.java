package display;

import midi.MidiConstants;
import util.Tuple2;
import util.observer.IObserver;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/**
 * A {@code display.NoteDataUpdater} receives MIDI messages and correspondingly updates a {@link TotalNoteData} object
 * passed at construction.
 */
@SuppressWarnings("ClassCanBeRecord")
public class NoteDataUpdater implements IObserver<Tuple2<MidiMessage, Long>> {

    private final TotalNoteData totalNoteData;

    /**
     * Constructs a {@code display.NoteDataUpdater} which updates the specified {@link TotalNoteData}.
     * @param totalNoteData the {@code NoteData} to update.
     */
    public NoteDataUpdater(TotalNoteData totalNoteData){
        this.totalNoteData = totalNoteData;
    }

    /**
     * Receives a MIDI message, and updates the {@link TotalNoteData} passed at construction if the message is
     * a note on, note off, all notes off, or all sound off message.
     *
     * @param messageTimeStampTuple a tuple containing the midi message and a time-stamp (unused).
     */
    @Override
    public void update(Tuple2<MidiMessage, Long> messageTimeStampTuple) {
        MidiMessage message = messageTimeStampTuple.a();
        if(message instanceof ShortMessage shortMessage){
            handleShortMessage(shortMessage);
        }
    }

    /**
     * Handles MIDI short messages, updating the {@link TotalNoteData} passed at construction if the message is
     * a note on, note off, all notes off, or all sound off message.
     *
     * @param shortMessage the MIDI short message to handle.
     */
    private void handleShortMessage(ShortMessage shortMessage) {
        switch(shortMessage.getCommand()){
            case ShortMessage.NOTE_ON -> {
                int velocity = shortMessage.getData2();
                if(velocity > 0) {
                    totalNoteData.pressNote(shortMessage.getChannel(), shortMessage.getData1());
                }
                //a note on event with velocity 0 is equivalent to a note off event
                else{
                    totalNoteData.releaseNote(shortMessage.getChannel(), shortMessage.getData1());
                }
            }
            case ShortMessage.NOTE_OFF ->
                totalNoteData.releaseNote(shortMessage.getChannel(), shortMessage.getData1());
            case ShortMessage.CONTROL_CHANGE ->
                handleControlChange(shortMessage);
        }
    }

    /**
     * Handles MIDI control change messages, updating the {@link TotalNoteData} passed at construction if the
     * message is an all notes off or an all sound off.
     *
     * @param controlChangeMessage the MIDI control change message to handle.
     */
    private void handleControlChange(ShortMessage controlChangeMessage){
        switch(controlChangeMessage.getData1()){
            case MidiConstants.ALL_SOUND_OFF, MidiConstants.ALL_NOTES_OFF -> totalNoteData.reset();
        }
    }
}
