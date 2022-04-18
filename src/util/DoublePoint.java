package util;

/**
 * A {@code DoublePoint} is an immutable point with double x and y coordinates.
 */
public record DoublePoint(double x, double y) {

    /**
     * Constructs a point at (0, 0).
     */
    public DoublePoint(){
        this(0, 0);
    }

    /**
     * Returns true if this point is exactly (0, 0).
     * @return true if this point is exactly (0, 0).
     */
    public boolean isZero(){
        return x == 0 && y == 0;
    }

    /**
     * Returns a new point with the given x coordinate.
     * @param x the new x coordinate.
     * @return a new point with the given x coordinate.
     */
    public DoublePoint setX(double x){
        return new DoublePoint(x, y);
    }

    /**
     * Returns a new point with the given y coordinate.
     * @param y the new y coordinate.
     * @return a new point with the given y coordinate.
     */
    public DoublePoint setY(double y){
        return new DoublePoint(x, y);
    }

    /**
     * Returns a string representation of this point.
     * @return a string representation of this point.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
