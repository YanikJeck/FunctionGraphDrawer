package drawer;

/**
 * ScaleElement represents a number on an axis, representing the current
 * value associated with the current pixel.
 */
public class ScaleElement {
    public String value;
    public int x;
    public int y;

    public ScaleElement(Double d, int x, int y) {
        this.value = format(d);
        this.x = x;
        this.y = y;
    }

    /**
     * Formats a double value in such a way that it only
     * contains to digits after the point.
     * @param d Value to be transformed.
     * @return Returns the transformed value.
     */
    private static String format(Double d) {
        String s = String.valueOf(d);
        if (s.indexOf('.') != -1 && s.length()-s.indexOf('.') > 4)
            s = s.substring(0,s.indexOf('.')+3);
        if (s.equals("-0.00"))
            s = "0.00";
        return s;
    }
}
