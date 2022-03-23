import util.DoublePoint;

public class NoteDisplay {
    private final DoublePoint pos;
    private final String upSimage;
    private final String downSimage;
    private boolean pressed;

    public NoteDisplay(DoublePoint pos, String upSimage, String downSimage) {
        this.pos = pos;
        this.upSimage = upSimage;
        this.downSimage = downSimage;
        pressed = false;
    }

    public DoublePoint getPos() {
        return pos;
    }

    public String getSimage() {
        return pressed ? downSimage : upSimage;
    }

    public void press(){
        pressed = true;
    }

    public void release(){
        pressed = false;
    }
}
