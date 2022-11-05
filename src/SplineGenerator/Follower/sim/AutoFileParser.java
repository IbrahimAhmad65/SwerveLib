package SplineGenerator.Follower.sim;

import SplineGenerator.Follower.*;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;
import main.Vector2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class AutoFileParser {
    File deployDir;
    String deployPath;

    String outputData;
    ArrayList<String[]> inputControl;
    ArrayList<String[]> inputWaypoint;
    ArrayList<String[]> inputRequired;

    String autoName = "";

    enum WaypointCommands {
        PRINT, NONE
    }

    private SingleAuto[] singleAutos;

    enum ReadState {
        CONTROL, WAYPOINT, REQUIRED
    }

    public AutoFileParser() {
//        deployDir = Filesystem.getDeployDirectory();
//        deployPath = deployDir.getAbsolutePath();
//        getDataString(new File("/home/ibrahim/robotics/SassiTator/src/main/deploy/auto.txt"));
    }

    public ArrayList<String[]>[] getDataString(File myObj) {
        inputControl = new ArrayList<String[]>();
        inputWaypoint = new ArrayList<String[]>();
        inputRequired = new ArrayList<String[]>();
        ReadState readState = ReadState.CONTROL;
        try {
//            File myObj = new File(Filesystem.getDeployDirectory().getAbsolutePath() + "/auto.txt");
//            File myObj = new File("/home/ibrahim/robotics/SassiTator/src/main/deploy/auto.txt");

            Scanner myReader = new Scanner(myObj);
            autoName = myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                if (data.equals("ControlPoints")) {
                    readState = ReadState.CONTROL;
                } else if (data.equals("WayPoints")) {
                    readState = ReadState.WAYPOINT;
                } else if (data.equals("Required")) {
                    readState = ReadState.REQUIRED;
                } else {
                    if (!(data.equals("{") || data.equals("}") || data.startsWith("//"))) {
                        switch (readState) {
                            case CONTROL:
                                inputControl.add(data.split(","));
                                break;
                            case WAYPOINT:
                                inputWaypoint.add(data.split(","));
                                break;
                            case REQUIRED:
                                inputRequired.add(data.split(","));
                                break;
                        }
                    }
                }
            }
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String[]>[] out = new ArrayList[3];
        out[0] = inputControl;
        out[1] = inputWaypoint;
        out[2] = inputRequired;
        return out;
    }

    public SingleAuto getSingleAuto(ArrayList<String[]> inputControl, ArrayList<String[]> inputWaypoint, ArrayList<String[]> inputRequired) {
        SingleAuto singleAuto;
        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);
        DVector a;
        DVector b;
        int counter = 0;
        for (String[] s : inputControl) {
//            System.out.println(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
            a = new DVector(Double.valueOf(s[0]), Double.valueOf(s[1]));
            b = new DVector(Double.valueOf(s[2]), Double.valueOf(s[3]));
            System.out.println(counter);
            if (!(counter == 0 || counter == inputControl.size() - 1)) {
                System.out.println(a + " " + b);
                spline.addControlPoint(new DControlPoint(a, b));
            } else {
                System.out.println(a + " " + b + " " + new DVector(0, 0));
                spline.addControlPoint(new DControlPoint(a, b, new DVector(0, 0)));
            }
            counter++;
        }
        addSplineInterpNGeneration(spline);
        Waypoint[] waypoints = new Waypoint[inputWaypoint.size()];
        for (int i = 0; i < waypoints.length; i++) {
            Runnable r;
            if (Objects.equals(inputWaypoint.get(i)[3], "print")) {
                System.out.println(findTOnSpline(new Vector2D(Double.parseDouble(inputWaypoint.get(i)[0]), Double.parseDouble(inputWaypoint.get(i)[1])), spline, .01));
                r = () -> {
                    System.out.println("hi i exist");
                };
            } else {
                r = () -> {
                };
            }
            waypoints[i] = new Waypoint(findTOnSpline(new Vector2D(Double.parseDouble(inputWaypoint.get(i)[0]), Double.parseDouble(inputWaypoint.get(i)[1])), spline, .01), r, Double.parseDouble(inputWaypoint.get(i)[2]));
        }
        RequiredFollowerPoint[] rb = new RequiredFollowerPoint[inputRequired.size()];
        for (int i = 0; i < rb.length; i++) {
            rb[i] = new RequiredFollowerPoint(findTOnSpline(new Vector2D(Double.valueOf(inputRequired.get(i)[0]), Double.valueOf(inputRequired.get(i)[1])), spline, .01), Double.valueOf(inputRequired.get(i)[2]));
        }
        PosExtraEnhancedSplineFollowerButWithSpin follower = new PosExtraEnhancedSplineFollowerButWithSpin(spline,
                .1, .01, .5, .01, new Waypoints(waypoints), new RequiredFollowerPoints(.1, .01, rb));
        return new SingleAuto(autoName, spline, follower);

    }

    public SingleAuto[] getSingleAutoArr() {
        int counter = 0;
        ArrayList<String> fileNames = new ArrayList<String>();
        fileNames.add("/home/ibrahim/robotics/SassiTator/src/main/deploy/auto.txt");
        counter = 1;

        singleAutos = new SingleAuto[counter];
        for (int i = 0; i < counter; i++) {
            ArrayList<String[]>[] b = getDataString(new File(fileNames.get(i)));
            singleAutos[i] = getSingleAuto(b[0],b[1],b[2]);
        }
        return singleAutos;
    }

    public static void addSplineInterpNGeneration(Spline spline) {
        spline.closed = false;
        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);
        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c2);
        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = Spline.InterpolationType.Linked;
        c3.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c3);
        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = Spline.InterpolationType.Linked;
        c4.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c4);
        spline.generate();
        spline.takeNextDerivative();
    }

    public static double findTOnSpline(Vector2D pos, Spline spline, double splineRes) {
        Vector2D min = new Vector2D(99999.0, 99999.0);
        double newT = 0.0;
        new Vector2D();

        for (double i = 0; i < spline.getNumPieces(); i += splineRes) {
            double b = i > 0.0 ? i : 1.0E-4;
            Vector2D check = spline.get(b < (double) spline.getNumPieces() ? b : (double) spline.getNumPieces() - 1.0E-4).toVector2D().subtract(pos);
            if (check.getMagnitude() < min.getMagnitude()) {
                min = check.clone();
                newT = b < (double) spline.getNumPieces() ? b : (double) spline.getNumPieces() - 1.0E-4;
            }
        }
        return newT;
    }

    public ArrayList<String[]> getInputControl() {
        return inputControl;
    }

    public ArrayList<String[]> getInputWaypoint() {
        return inputWaypoint;
    }

    public ArrayList<String[]> getInputRequired() {
        return inputRequired;
    }

    public static void main(String[] args) {
        // proof this actually works
        AutoFileParser autoFileParser = new AutoFileParser();
        SingleAuto auto = autoFileParser.getSingleAutoArr()[0];
        Vector2D pos = new Vector2D(0, 0);
        AutoRegistry registry = new AutoRegistry();
        registry.addAuto(auto.getAutoName(), auto);
        AutoScheduler autoScheduler = new AutoScheduler(registry, pos::clone);
        autoScheduler.setCurrentAuto(auto.getAutoName());
        for (int i = 0; i < 1000 && !autoScheduler.isFinished(); i++) {
            pos.add(autoScheduler.getVelocity());
            autoScheduler.runWaypoints();
            System.out.println(pos);
        }
    }
}
