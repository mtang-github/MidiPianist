import java.util.Arrays;

/**
 * A {@code ChannelNoteData} represents the graphical state of all MIDI notes (0-128) on a single channel.
 */
public class ChannelNoteData {

    private final NoteState[] noteStates;

    /**
     * Constructs a new {@code ChannelNoteData} with all notes off.
     */
    public ChannelNoteData(){
        noteStates = new NoteState[128];
        Arrays.fill(noteStates, NoteState.RELEASED);
    }

    /**
     * Returns true if the specified note has been pressed since the last update, false otherwise.
     * @param note the note to check
     * @return true if the specified note has been pressed since the last update, false otherwise.
     */
    public boolean getNote(int note){
        return noteStates[note] != NoteState.RELEASED;
    }

    /**
     * Presses down the specified note.
     * @param note the specified note
     */
    public void pressNote(int note){
        noteStates[note] = NoteState.PRESSED;
    }

    /**
     * Releases the specified note. Notes pressed and then released within a single update
     * will still be counted as pressed for the next update.
     * @param note the specified note
     */
    public void releaseNote(int note){
        if(noteStates[note] != NoteState.RELEASED){
            noteStates[note] = NoteState.PRESSED_AND_RELEASED;
        }
        else{
            noteStates[note] = NoteState.RELEASED;
        }
    }

    /**
     * Completely releases all notes which were pressed and then released since the previous update.
     */
    public void updateNotes(){
        for(int i = 0; i < 128; ++i){
            if(noteStates[i] == NoteState.PRESSED_AND_RELEASED){
                noteStates[i] = NoteState.RELEASED;
            }
        }
    }

    /**
     * Sets all notes as released.
     */
    public void reset(){
        Arrays.fill(noteStates, NoteState.RELEASED);
    }

    /**
     * A {@code NoteState} represents one of three possible states for a single note.
     *
     * @see #PRESSED
     * @see #PRESSED_AND_RELEASED
     * @see #RELEASED
     */
    private enum NoteState{
        /**
         * The note is currently pressed.
         */
        PRESSED,
        /**
         * The note was pressed and released within the previous update.
         */
        PRESSED_AND_RELEASED,
        /**
         * The note is currently released.
         */
        RELEASED
    }
}
