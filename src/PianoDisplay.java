import util.math.geometry.DoublePoint;

public class PianoDisplay {

    //notes are 10x30
    //notes start from C, go up to G 10 octaves

    private final NoteDisplay[][] noteDisplay;

    public PianoDisplay(){
        noteDisplay = new NoteDisplay[16][128];
        int y = 0;
        for(int channel = 0; channel < 16; ++channel){
            //todo: temp all white notes
            int x = 0;
            for(int note = 0; note < 128; ++note){
                noteDisplay[channel][note] = new NoteDisplay(
                        new DoublePoint(x, y),
                        "white_up",
                        "white_down"
                );
                x += 10;
            }
            y += 30;
        }
    }

    public void readNoteData(NoteData noteData){
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
    }
}