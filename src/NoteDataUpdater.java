import midi.MidiConstants;
import util.observer.AbstractPushObserver;
import util.Tuple2;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class NoteDataUpdater implements AbstractPushObserver<Tuple2<MidiMessage, Long>> {

    private final NoteData noteData;

    public NoteDataUpdater(NoteData noteData){
        this.noteData = noteData;
    }

    @Override
    public void update(Tuple2<MidiMessage, Long> data) {
        MidiMessage message = data.a();
        if(message instanceof ShortMessage shortMessage){
            switch(shortMessage.getCommand()){
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
