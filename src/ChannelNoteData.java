import java.util.Arrays;

/**
 * Represents the graphical state of a single note (from 0-128) on a single channel
 */
public class ChannelNoteData {

    private final NoteState[] noteStates = new NoteState[128];

    /**
     * Returns true if the specified note has been pressed since the last update, false otherwise
     * @param note - the specified note
     * @return true if the specified note has been pressed since the last update, false otherwise
     */
    public boolean getNote(int note){
        return noteStates[note] != NoteState.RELEASED;
    }

    public void pressNote(int note){
        noteStates[note] = NoteState.PRESSED;
    }

    public void releaseNote(int note){
        if(noteStates[note] != NoteState.RELEASED){
            noteStates[note] = NoteState.PRESSED_AND_RELEASED;
        }
        else{
            noteStates[note] = NoteState.RELEASED;
        }
    }

    public void updateNotes(){
        for(int i = 0; i < 128; ++i){
            if(noteStates[i] == NoteState.PRESSED_AND_RELEASED){
                noteStates[i] = NoteState.RELEASED;
            }
        }
    }

    public void reset(){
        Arrays.fill(noteStates, NoteState.RELEASED);
    }

    public enum NoteState{
        PRESSED,
        PRESSED_AND_RELEASED,
        RELEASED,
        ;
    }
}
