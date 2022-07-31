package SplineGenerator.Follower;

import main.Vector2D;

public class Waypoint implements Comparable<Waypoint>{
    private double t;
    private Runnable action;
    private double speed;
    private boolean hasRun;
    public Waypoint(double t, Runnable action, double speed){
        this.t = t;
        this.action = action;
        this.speed = speed;
        hasRun = false;
    }

    public double getSpeed() {
        return speed;
    }

    public Runnable getAction() {
        return action;
    }

    public double getT() {
        return t;
    }

    public void run(double t){
        if(this.t > t){
            action.run();
            hasRun = true;
        }
    }

    public boolean hasRun() {
        return hasRun;
    }

    @Override
    public int compareTo(Waypoint o) {
        return (int)(t*1000 - 1000*((Waypoint)o).getT());
    }
}
