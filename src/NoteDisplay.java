import util.DoublePoint;

/**
 * A {@code NoteDisplay} represents the graphical representation of a single MIDI note on a single MIDI channel.
 */
public class NoteDisplay {
    private final DoublePoint pos;
    private final String upSimage;
    private final String downSimage;
    private boolean pressed;

    /**
     * Constructs a {@code NoteDisplay} at the specified position with the given string images for its up and down
     * sprites.
     *
     * @param pos the position of the top-left corner.
     * @param upSimage the string image for the note-up sprite.
     * @param downSimage the string image for the note-down sprite.
     */
    public NoteDisplay(DoublePoint pos, String upSimage, String downSimage) {
        this.pos = pos;
        this.upSimage = upSimage;
        this.downSimage = downSimage;
        pressed = false;
    }

    /**
     * Returns the position of the top-left corner.
     * @return the position of the top-left corner.
     */
    public DoublePoint getPos() {
        return pos;
    }

    /**
     * Returns either the up or down string image depending on if this {@code NoteDisplay} has been pressed or not.
     * @return either the up or down string image depending on if this {@code NoteDisplay} has been pressed or not.
     */
    public String getSimage() {
        return pressed ? downSimage : upSimage;
    }

    /**
     * Presses down this {@code NoteDisplay}, setting the string image to the down state.
     */
    public void press(){
        pressed = true;
    }

    /**
     * Releases this {@code NoteDisplay}, setting the string image to the up state.
     */
    public void release(){
        pressed = false;
    }
}
