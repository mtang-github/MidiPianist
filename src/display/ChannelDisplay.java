package display;

import resource.IResourceManager;
import util.DoublePoint;

import java.awt.*;
import java.awt.image.BufferedImage;

import static midi.MidiConstants.NUM_CHANNELS;
import static midi.MidiConstants.NUM_NOTES;

/**
 * A {@code display.ChannelDisplay} represents the graphical representation of a single MIDI channel and is also capable
 * of displaying itself with a {@code Graphics2D}.
 */
public class ChannelDisplay {
    //notes are 10 pixels wide
    private static final int X_HALF_SHIFT = 5;
    private static final int X_OCTAVE_SHIFT = 70;

    //notes start from C, go up to G 10 octaves
    private static final int NUM_PITCH_CLASSES = 12;

    private static final int PITCH_CLASS_C = 0;
    private static final int PITCH_CLASS_C_SHARP = 1;
    private static final int PITCH_CLASS_D = 2;
    private static final int PITCH_CLASS_D_SHARP = 3;
    private static final int PITCH_CLASS_E = 4;
    private static final int PITCH_CLASS_F = 5;
    private static final int PITCH_CLASS_F_SHARP = 6;
    private static final int PITCH_CLASS_G = 7;
    private static final int PITCH_CLASS_G_SHARP = 8;
    private static final int PITCH_CLASS_A = 9;
    private static final int PITCH_CLASS_A_SHARP = 10;
    private static final int PITCH_CLASS_B = 11;

    private final int channel;
    private final NoteDisplay[] noteDisplays;

    /**
     * Constructs a {@code display.ChannelDisplay} for the specified channel at the given position.
     *
     * @param channel the channel this {@code display.ChannelDisplay} is to represent.
     * @param pos the position of the top-left corner.
     */
    public ChannelDisplay(int channel, DoublePoint pos){
        throwIfInvalidChannel(channel);
        this.channel = channel;
        noteDisplays = makeNoteDisplays(pos);
    }

    /**
     * Throws an exception if the specified integer is not a valid MIDI channel.
     *
     * @param channel the integer to check.
     *
     * @throws RuntimeException if the specified integer is not a valid MIDI channel.
     */
    private static void throwIfInvalidChannel(int channel){
        if(channel < 0 || channel >= NUM_CHANNELS){
            throw new RuntimeException(channel + " is not a valid channel");
        }
    }

    /**
     * Creates a {@code display.NoteDisplay} array representing the display information of all notes.
     *
     * @param pos the position of the top-left corner to be fed into the individual {@code display.NoteDisplay} objects.
     *
     * @return a {@code display.NoteDisplay} array representing the display information of all notes.
     */
    private static NoteDisplay[] makeNoteDisplays(DoublePoint pos){
        NoteDisplay[] noteDisplay = new NoteDisplay[NUM_NOTES];
        double startingX = pos.x();
        for(int pitchClass = 0; pitchClass < NUM_PITCH_CLASSES; ++pitchClass){
            boolean isWhiteNote = isWhiteNote(pitchClass);
            double x = startingX;

            for(int note = pitchClass; note < NUM_NOTES; note += NUM_PITCH_CLASSES){
                noteDisplay[note] = new NoteDisplay(
                        new DoublePoint(x, pos.y()),
                        isWhiteNote ? "white_up" : "black_up",
                        isWhiteNote ? "white_down" : "black_down"
                );
                x += X_OCTAVE_SHIFT;
            }

            startingX += X_HALF_SHIFT;
            //if the note is E, move onto F (we do not need a case for B to C)
            if(pitchClass == PITCH_CLASS_E){
                startingX += X_HALF_SHIFT;
            }
        }
        return noteDisplay;
    }

    /**
     * Returns true if the specified integer pitch class represents a white note, false otherwise.
     *
     * @param pitchClass the integer pitch class to check.
     *
     * @return true if the specified integer pitch class represents a white note, false otherwise.
     *
     * @throws RuntimeException if the specified integer is not a valid pitch class.
     */
    @SuppressWarnings("EnhancedSwitchMigration")
    private static boolean isWhiteNote(int pitchClass){
        if(pitchClass < 0 || pitchClass >= NUM_PITCH_CLASSES){
            throw new RuntimeException("invalid pitch class : " + pitchClass);
        }
        switch(pitchClass){
            case PITCH_CLASS_C:
            case PITCH_CLASS_D:
            case PITCH_CLASS_E:
            case PITCH_CLASS_F:
            case PITCH_CLASS_G:
            case PITCH_CLASS_A:
            case PITCH_CLASS_B:
                return true;
            case PITCH_CLASS_C_SHARP:
            case PITCH_CLASS_D_SHARP:
            case PITCH_CLASS_F_SHARP:
            case PITCH_CLASS_G_SHARP:
            case PITCH_CLASS_A_SHARP:
                return false;
            default:
                throw new RuntimeException();
        }
    }

    /**
     * Returns true if the specified integer pitch class represents a black note, false otherwise.
     *
     * @param pitchClass the integer pitch class to check.
     *
     * @return true if the specified integer pitch class represents a black note, false otherwise.
     *
     * @throws RuntimeException if the specified integer is not a valid pitch class.
     */
    private static boolean isBlackNote(int pitchClass){
        return !isWhiteNote(pitchClass);
    }

    /**
     * Updates the graphical representation of the channel represented by this {@code display.ChannelDisplay} according to the
     * specified {@code display.TotalNoteData}.
     *
     * @param totalNoteData the {@code display.TotalNoteData} to read from.
     */
    public void readNoteData(TotalNoteData totalNoteData){
        for(int note = 0; note < 128; ++note){
            if(totalNoteData.hasBeenPressed(channel, note)){
                noteDisplays[note].press();
            }
            else{
                noteDisplays[note].release();
            }
        }
    }

    /**
     * Draws a graphical representation of the channel represented by this {@code display.ChannelDisplay} with the given
     * {@code Graphics2D} and image manager.
     *
     * @param g2d the {@code Graphics2D} to draw with.
     * @param imageManager the image manager which holds the required {@code BufferedImage} sprites.
     */
    public void drawOn(Graphics2D g2d, IResourceManager<BufferedImage> imageManager){
        //draw all the white notes
        for(int pitchClass = 0; pitchClass < NUM_PITCH_CLASSES; ++pitchClass){
            if(isWhiteNote(pitchClass)) {
                for (int note = pitchClass; note < 128; note += NUM_PITCH_CLASSES) {
                    NoteDisplay noteDisplay = this.noteDisplays[note];
                    g2d.drawImage(
                            imageManager.getResource(noteDisplay.getSimage()).getData(),
                            (int) noteDisplay.getPos().x(),
                            (int) noteDisplay.getPos().y(),
                            null
                    );
                }
            }
        }
        //draw all the black notes
        for(int pitchClass = 0; pitchClass < NUM_PITCH_CLASSES; ++pitchClass){
            if(isBlackNote(pitchClass)) {
                for (int note = pitchClass; note < 128; note += NUM_PITCH_CLASSES) {
                    NoteDisplay noteDisplay = this.noteDisplays[note];
                    g2d.drawImage(
                            imageManager.getResource(noteDisplay.getSimage()).getData(),
                            (int) noteDisplay.getPos().x(),
                            (int) noteDisplay.getPos().y(),
                            null
                    );
                }
            }
        }
    }
}
