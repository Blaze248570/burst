package burst;

import java.util.ArrayList;

public class BurstBasic extends javax.swing.JComponent {
    public final int ID = idEnumerator++;

    static int idEnumerator = 0;

    public BurstCamera camera;
    public ArrayList<BurstCamera> cameras;

    public boolean active = true;

    public boolean visible = true;

    public boolean alive = true;

    public boolean exists = true;

    public void destroy() {
        exists = false;
    }

    public void kill() {
        alive = false;
        exists = false;
    }

    public void revive() {
        alive = true;
        exists = true;
    }

    public void update(Float elapsed) {}

    boolean set_visible(boolean value) {
        return visible = value;
    }

    boolean set_active(boolean value) {
        return active = value;
    }

    boolean set_exists(boolean value) {
        return exists = value;
    }

    boolean set_alive(boolean value) {
        return alive = value;
    }
}
