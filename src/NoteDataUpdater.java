import util.observer.AbstractPushObserver;
import util.tuple.Tuple2;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class NoteDataUpdater implements AbstractPushObserver<Tuple2<MidiMessage, Long>> {

    private NoteData noteData;

    public NoteDataUpdater(){
        noteData = null;
    }
    public NoteDataUpdater(NoteData noteData){
        this.noteData = noteData;
    }

    public NoteData getNoteData() {
        return noteData;
    }

    public void setNoteData(NoteData noteData) {
        this.noteData = noteData;
    }

    @Override
    public void update(Tuple2<MidiMessage, Long> data) {
        MidiMessage message = data.a();
        if(message instanceof ShortMessage shortMessage){
            switch(shortMessage.getStatus()){
                case ShortMessage.NOTE_ON -> {
                    int velocity = shortMessage.getData2();
                    if(velocity > 0) {
                        noteData.pressNote(shortMessage.getChannel(), shortMessage.getData1());
                    }
                    else{
                        noteData.releaseNote(shortMessage.getChannel(), shortMessage.getData1());
                    }
                }
                case ShortMessage.NOTE_OFF -> {
                    noteData.releaseNote(shortMessage.getChannel(), shortMessage.getData1());
                }
                case ShortMessage.CONTROL_CHANGE -> {
                    switch(shortMessage.getData1()){
                        case MidiConstants.ALL_SOUND_OFF, MidiConstants.ALL_NOTES_OFF -> {
                            noteData.reset();
                        }
                    }
                }
            }
        }
    }
}
