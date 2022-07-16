package BSpline;

public class Waypoint {
    private double t;
    private Runnable[] commands;
    private boolean run = false;
    public Waypoint(double t, Runnable... commands) {
        this.t = t;
        this.commands = commands;
    }

    public boolean execute(double t){
        if(!run && t > this.t){
            for (Runnable b:commands) {
                b.run();
            }
            run = true;
        }
        return run;
    }

}
