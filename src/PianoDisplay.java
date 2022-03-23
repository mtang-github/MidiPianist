import resource.AbstractResourceManager;
import util.DoublePoint;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PianoDisplay {

    private final AbstractResourceManager<BufferedImage> imageManager;

    //notes are 10x30
    //notes start from C, go up to G 10 octaves

    private final NoteDisplay[][] noteDisplay;

    public PianoDisplay(AbstractResourceManager<BufferedImage> imageManager){
        this.imageManager = imageManager;
        noteDisplay = new NoteDisplay[16][128];
        int y = 0;
        for(int channel = 0; channel < 16; ++channel){
            int startingX = 0;
            for(int pitchClass = 0; pitchClass < 12; ++pitchClass){
                boolean whiteNote = isWhiteNote(pitchClass);
                int x = startingX;
                for(int note = pitchClass; note < 128; note += 12){
                    noteDisplay[channel][note] = new NoteDisplay(
                            new DoublePoint(x, y),
                            whiteNote ? "white_up" : "black_up",
                            whiteNote ? "white_down" : "black_down"
                    );
                    x += 70;
                }
                startingX += 5;
                if(pitchClass == 4){
                    startingX += 5;
                }
            }
            y += 30;
        }
    }

    private static boolean isWhiteNote(int pitchClass){
        if(pitchClass < 0){
            throw new RuntimeException("invalid pitch class : " + pitchClass);
        }
        pitchClass %= 12;
        switch(pitchClass){
            case 0: //C
            case 2: //D
            case 4: //E
            case 5: //F
            case 7: //G
            case 9: //A
            case 11://B
                return true;
            case 1: //C#
            case 3: //D#
            case 6: //F#
            case 8: //G#
            case 10://A#
                return false;
            default:
                throw new RuntimeException();
        }
    }

    public void readAndUpdateNoteData(NoteData noteData){
        for(int channel = 0; channel < 16; ++channel){
            for(int note = 0; note < 128; ++note){
                if(noteData.hasBeenPressed(channel, note)){
                    noteDisplay[channel][note].press();
                }
                else{
                    noteDisplay[channel][note].release();
                }
            }
        }
        noteData.updateNotes();
    }

    public void drawOn(Graphics2D g2d){
        //todo: draw each pitch class at a time, i.e. += 12
        for(int channel = 0; channel < 16; ++channel){
            //draw all the white notes
            for(int pitchClass = 0; pitchClass < 12; ++pitchClass){
                if(isWhiteNote(pitchClass)) {
                    for (int note = pitchClass; note < 128; note += 12) {
                        NoteDisplay noteDisplay = this.noteDisplay[channel][note];
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
            for(int pitchClass = 0; pitchClass < 12; ++pitchClass){
                if(!isWhiteNote(pitchClass)) {
                    for (int note = pitchClass; note < 128; note += 12) {
                        NoteDisplay noteDisplay = this.noteDisplay[channel][note];
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
}