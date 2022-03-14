import java.util.Arrays;

public class NoteData {

    private final NoteState[] noteStates = new NoteState[128];

    public void pressNote(byte note){
        noteStates[note] = NoteState.PRESSED;
    }

    public void releaseNote(byte note){
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
