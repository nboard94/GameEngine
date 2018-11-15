package objects.components;

public interface Movable {

    void move();

    public int getX();
    public int getY();
    public void setX(int x);
    public void setY(int y);

    public void replayPositionSave();
    public void replayPositionRestore();
    public void savePosition();
    public void restorePosition();

    public void replaySpeedSave();
    public void replaySpeedRestore();
    public void saveSpeed();
    public void restoreSpeed();
}
