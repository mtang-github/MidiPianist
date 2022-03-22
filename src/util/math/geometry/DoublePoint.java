package util.math.geometry;

public record DoublePoint(double x, double y) {

    public DoublePoint(){
        this(0, 0);
    }

    public DoublePoint(DoublePoint toCopy){
        this(toCopy.x, toCopy.y);
    }

    public boolean isZero(){
        return x == 0 && y == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
