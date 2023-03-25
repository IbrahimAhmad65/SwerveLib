package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.AutoRegistry;
import SplineGenerator.Follower.AutoScheduler;
import SplineGenerator.Follower.GoodestFollower;
import SplineGenerator.Follower.SingleAuto;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

import javax.xml.crypto.dsig.keyinfo.KeyName;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.Key;

// dont ask (sorry in advance)
public class FullJSONSimulator {
    AutoScheduler autoScheduler;
    Vector2D pos;
    private GoodestFollower follower;
    private DisplayData displayData;
    private double timePerTick;
    private double scalePerTick;
    private SplineDisplay splineDisplay;

    private boolean paused;


    FullJSONSimulator(String... autoPaths) throws IOException {
        AutoRegistry autoRegistry = new AutoRegistry();
        for (String auto : autoPaths) {
            SingleAuto singleAuto = JSONAutoParser.generateAutoFromJSON(auto, null);
            autoRegistry.addAuto(singleAuto.getAutoName(), singleAuto);
        }
        pos = new Vector2D();
        autoScheduler = new AutoScheduler(autoRegistry, pos::clone);
    }

    public void simAll(double scalePerTick, double timePerTick) throws Exception {
        for (int i = 0; i < autoScheduler.getRegistry().getAutoNames().size(); i++) {
            this.sim(autoScheduler.getRegistry().getAutoNames().get(i), scalePerTick, timePerTick);
            Thread.sleep(700);
        }
    }

    public void sim(String autoName, double scalePerTick, double timePerTick) throws Exception {
        KeyBoardListener.initialize();
        KeyBoardListener.keyCodes.add(KeyEvent.VK_X);
        KeyBoardListener.keyValues.add(true);
        KeyBoardListener.keyCodes.add(KeyEvent.VK_R);
        KeyBoardListener.keyValues.add(true);
        KeyBoardListener.keyCodes.add(KeyEvent.VK_SPACE);
        KeyBoardListener.keyValues.add(true);
        KeyBoardListener.keyCodes.add(KeyEvent.VK_C);
        KeyBoardListener.keyValues.add(true);
        KeyBoardListener.keyCodes.add(KeyEvent.VK_LEFT);
        KeyBoardListener.keyValues.add(true);
        KeyBoardListener.keyCodes.add(KeyEvent.VK_RIGHT);
        KeyBoardListener.keyValues.add(true);


        autoScheduler.setCurrentAuto(autoName);
        splineDisplay = new SplineDisplay(autoScheduler.getRegistry().get(autoName).getSpline(), 0, 1, 1920, 1080,"/home/ibrahim/robotics/TatorEyes2/AutoFollower/src/SplineGenerator/Follower/sim/field.png");
//        splineDisplay.displayables.add(new FieldDisplay());
        splineDisplay.displayables.add((GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower());
        autoScheduler.setPosSupplier(pos::clone);
        displayData = new DisplayData();
        follower = (GoodestFollower) autoScheduler.getRegistry().get(autoName).getFollower();
        pos = follower.getFollowable().get(0).toVector2D();

        TextDisplay textDisplay = new TextDisplay(() -> {
            return displayData;
        }, new Vector2D(3, 20), 15);
        splineDisplay.displayables.add(textDisplay);
        this.timePerTick = timePerTick;
        this.scalePerTick = scalePerTick;
        paused = false;
        displayData.pos = pos.clone();
        displayData.t = follower.getT();
        displayData.angle = follower.getSpin(pos);
        displayData.waypoints = autoScheduler.getRegistry().get(autoScheduler.getCurrentAuto()).getFollower().getWaypoints();
        displayData.name = autoName;
        displayData.requiredFollowerPoints = autoScheduler.getRegistry().get(autoScheduler.getCurrentAuto()).getFollower().getRequiredPoints();
        textDisplay.verbosityType = TextDisplay.VerbosityType.ALL;

        splineDisplay.display();
        Robot robot = new Robot();
        Integer[] arr = new Integer[KeyBoardListener.keyCodes.size()];
        KeyBoardListener.keyCodes.toArray(arr);
        for (int k : arr) {
            robot.keyPress(k);
            Thread.sleep(50);
            robot.keyRelease(k);
        }
        Thread.sleep(100);
        while (KeyBoardListener.get(KeyEvent.VK_X)) {
            Thread.sleep(1);

        }

        while (!KeyBoardListener.get(KeyEvent.VK_X)) {
            if (KeyBoardListener.get(KeyEvent.VK_R)) {
                pos = follower.getFollowable().get(0).toVector2D();
                follower.reset(pos);
            } else {
                run();
            }
        }
        splineDisplay.dispatchEvent(new WindowEvent(splineDisplay, WindowEvent.WINDOW_CLOSING));
    }

    private void run() throws Exception {

        displayData.pos = pos.clone();
        displayData.t = follower.getT();
        displayData.angle = follower.getSpin(pos);
        displayData.waypoints = autoScheduler.getRegistry().get(autoScheduler.getCurrentAuto()).getFollower().getWaypoints();
        autoScheduler.runWaypoints();
        if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
            paused = true;
        }
        if (KeyBoardListener.get(KeyEvent.VK_C)) {
            paused = false;
        }


        if (!follower.finished() && !paused || KeyBoardListener.get(KeyEvent.VK_RIGHT)) {
            pos.add(follower.get(pos).scale(scalePerTick));

        } else if (paused && KeyBoardListener.get(KeyEvent.VK_LEFT)) {
            pos.add(follower.goBack(pos).scale(scalePerTick));

        }

        Thread.sleep((long) timePerTick);
        splineDisplay.repaint();

    }

    public static void main(String[] args) throws Exception {
        FullJSONSimulator fullJSONSimulator = new FullJSONSimulator(
//                 "/home/ibrahim/robotics/washatator/src/main/deploy/autoReal2.txt"



//                "C:\\Users\\senti\\code\\washatator\\src\\main\\deploy\\autoReal2.txt"
                "C:\\Users\\senti\\code\\washatator\\src\\main\\deploy\\autoAroundStationConeCone.txt"
        );
        fullJSONSimulator.simAll(.01, 10);

//        System.out.println("hi");
//        fullJSONSimulator.sim("figure_eight.txt", .01, 10);

    }
}
