package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.AutoScheduler;
import main.Vector2D;

public class FullSimulator {
    private AutoScheduler autoScheduler;
    public FullSimulator(AutoScheduler autoScheduler){
        this.autoScheduler = autoScheduler;
    }

    public void run(){
        Vector2D pos = new Vector2D(0,0);
        autoScheduler.setPosSupplier(pos::clone);
        for (int i = 0; i < 1000 && !autoScheduler.isFinished(); i++) {
            pos.add(autoScheduler.getVelocity());
            System.out.println(pos);
        }
    }



}
