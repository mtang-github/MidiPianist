/**
 * A {@code TotalNoteData} represents the graphical state of all notes (0-128) on all channels (1-16).
 */
public class TotalNoteData {

    private final ChannelNoteData[] channelNoteData;

    /**
     * Constructs a new {@code TotalNoteData} with all notes off.
     */
    public TotalNoteData(){
        channelNoteData = new ChannelNoteData[16];
        for(int i = 0; i < 16; ++i){
            channelNoteData[i] = new ChannelNoteData();
        }
    }

    /**
     * Returns true if the specified note on the specified channel has been pressed since the last update,
     * false otherwise.
     *
     * @param channel the channel on which to check the note.
     * @param note the note to check.
     *
     * @return true if the specified note on the specified channel has been pressed since the last update,
     * false otherwise.
     */
    public boolean hasBeenPressed(int channel, int note){
        return channelNoteData[channel].getNote(note);
    }

    /**
     * Presses down the specified note on the specified channel.
     *
     * @param channel the channel on which to press the note.
     * @param note the specified note.
     */
    public void pressNote(int channel, int note){
        channelNoteData[channel].pressNote(note);
    }

    /**
     * Releases the specified note on the specified channel. Notes pressed and then released within a single update
     * will still be counted as pressed for the next update.
     *
     * @param channel the channel on which to release the note.
     * @param note the specified note.
     */
    public void releaseNote(int channel, int note){
        channelNoteData[channel].releaseNote(note);
    }

    /**
     * Completely releases all notes which were pressed and then released since the previous update.
     */
    public void updateNotes(){
        for(int i = 0; i < 16; ++i){
            channelNoteData[i].updateNotes();
        }
    }

    /**
     * Sets all notes as released.
     */
    public void reset(){
        for(int i = 0; i < 16; ++i){
            channelNoteData[i].reset();
        }
    }
}
