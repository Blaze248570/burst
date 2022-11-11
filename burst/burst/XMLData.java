package burst;

public class XMLData {
    public String name;
    public int x;
    public int y;
    public int width;
    public int height;
    public float pivotX;
    public float pivotY;
    public int frameX;
    public int frameY;
    public int frameWidth;
    public int frameHeight;

    public String toString() {
        return 
        "Name: " + name +
        " X: " + x + 
        " Y: " + y +
        " Width: " + width +
        " Height: " + height +
        " PivotX: " + pivotX +
        " PivotY: " + pivotY +
        " FrameX: " + frameX +
        " FrameY: " + frameY +
        " FrameWidth: " + frameWidth +
        " FrameHeight: " + frameHeight
        ;
    }
}