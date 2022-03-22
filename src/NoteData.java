public class NoteData {

    private final ChannelNoteData[] channelNoteData;

    public NoteData(){
        channelNoteData = new ChannelNoteData[16];
        for(int i = 0; i < 16; ++i){
            channelNoteData[i] = new ChannelNoteData();
        }
    }

    /**
     * Returns true if the specified note has been pressed since the last update, false otherwise
     * @param note - the specified note
     * @return true if the specified note has been pressed since the last update, false otherwise
     */
    public boolean hasBeenPressed(int channel, int note){
        return channelNoteData[channel].getNote(note);
    }

    public void pressNote(int channel, int note){
        channelNoteData[channel].pressNote(note);
    }

    public void releaseNote(int channel, int note){
        channelNoteData[channel].releaseNote(note);
    }

    public void updateNotes(){
        for(int i = 0; i < 16; ++i){
            channelNoteData[i].updateNotes();
        }
    }

    public void reset(){
        for(int i = 0; i < 16; ++i){
            channelNoteData[i].reset();
        }
    }
}
