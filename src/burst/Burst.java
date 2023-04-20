package burst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Burst extends javax.swing.JFrame 
{
    /**
     * Time in milliseconds since program began
     */
    public static float total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public static float elapsed;
    private static Instant startTime;

    public BurstCamera defaultCam;
    public ArrayList<BurstBasic> members;

    public Burst(int frameWidth, int frameHeight) 
    {
        super();

        setLayout(null);
        setTitle("Burst");
        setSize(frameWidth, frameHeight);
        setLocation(525, 200);
        setDefaultCloseOperation(3);

        startTime = Instant.now();
        
        defaultCam = new BurstCamera(this);
        add(defaultCam);

        members = new ArrayList<>();
    }

    public void update()
    {
        elapsed = getTime() - total;
        total = getTime();

        for(BurstBasic basic : members) 
        {
            if(basic != null && basic.exists)
            {
                basic.update(elapsed);
            }
        }

        defaultCam.repaint();
    }

    public BurstBasic add(BurstBasic basic)
    {
        members.add(basic);
        defaultCam.add(basic);
        return basic;
    }

    private long getTime()
    {
        return Duration.between(startTime, Instant.now()).toMillis();
    }
}
