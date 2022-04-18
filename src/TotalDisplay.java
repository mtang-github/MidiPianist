import resource.IResourceManager;
import util.DoublePoint;

import java.awt.*;
import java.awt.image.BufferedImage;

import static midi.MidiConstants.NUM_CHANNELS;

/**
 * A {@code TotalDisplay} represents the graphical representation of all MIDI channels and is also capable
 * of displaying itself with a {@code Graphics2D}.
 */
public class TotalDisplay {
    //notes are 30 pixels tall
    private static final int Y_SHIFT = 30;

    private final ChannelDisplay[] channelDisplays;

    /**
     * Constructs a {@code TotalDisplay} with the upper-left corner at (0, 0).
     */
    public TotalDisplay(){
        channelDisplays = makeChannelDisplays(new DoublePoint());
    }

    /**
     * Creates a {@code ChannelDisplay} array representing the display information of all channels.
     *
     * @param pos the position of the top-left corner to be fed into the individual {@code ChannelDisplay} objects.
     *
     * @return a {@code ChannelDisplay} array representing the display information of all channels.
     */
    private static ChannelDisplay[] makeChannelDisplays(DoublePoint pos){
        ChannelDisplay[] channelDisplays = new ChannelDisplay[NUM_CHANNELS];
        double y = pos.y();
        for(int channel = 0; channel < NUM_CHANNELS; ++channel){
            channelDisplays[channel] = new ChannelDisplay(channel, new DoublePoint(pos.x(), y));
            y += Y_SHIFT;
        }
        return channelDisplays;
    }

    /**
     * Updates the graphical representation of all channels according to the specified {@code TotalNoteData}. Updates
     * the specified {@code TotalNoteData} afterwards.
     *
     * @param totalNoteData the {@code TotalNoteData} to read from and to update.
     */
    public void readAndUpdateNoteData(TotalNoteData totalNoteData){
        for(int channel = 0; channel < NUM_CHANNELS; ++channel){
            channelDisplays[channel].readNoteData(totalNoteData);
        }
        totalNoteData.updateNotes();
    }

    /**
     * Draws a graphical representation of all channels with the given {@code Graphics2D} and image manager.
     *
     * @param g2d the {@code Graphics2D} to draw with.
     * @param imageManager the image manager which holds the required {@code BufferedImage} sprites.
     */
    public void drawOn(Graphics2D g2d, IResourceManager<BufferedImage> imageManager){
        for(int channel = 0; channel < NUM_CHANNELS; ++channel){
            channelDisplays[channel].drawOn(g2d, imageManager);
        }
    }
}