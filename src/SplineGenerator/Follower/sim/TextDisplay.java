package SplineGenerator.Follower.sim;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import main.Vector2D;

import java.util.function.Supplier;

public class TextDisplay implements Displayable {
    private Supplier<DisplayData> displayDataSupplier;
    private Vector2D headPosition;
    private int lineSpacing;

    enum VerbosityType {
        MINIMAL,
        SKELETON,
        WAYPOINT,
        REQUIRED_POINT,
        ALL,
    }

    public VerbosityType verbosityType = VerbosityType.ALL;

    public TextDisplay(Supplier<DisplayData> displayDataSupplier, Vector2D headPosition, int lineSpacing) {
        this.displayDataSupplier = displayDataSupplier;
        this.headPosition = headPosition;
        this.lineSpacing = lineSpacing;
    }

    @Override
    public void display(DisplayGraphics graphics) {
        DisplayData displayData = displayDataSupplier.get();
        switch (verbosityType) {
            case ALL:
                graphics.getGraphics().drawString("Name: " + displayData.name, (int) headPosition.getX(), (int) headPosition.getY());
                graphics.getGraphics().drawString("Position: " + displayData.pos.toString(), (int) headPosition.getX(), (int) headPosition.getY() + lineSpacing);
                graphics.getGraphics().drawString("T: " + displayData.t + "", (int) headPosition.getX(), (int) headPosition.getY() + lineSpacing + 2 * lineSpacing);
                graphics.getGraphics().drawString("Angle: " + displayData.angle + "", (int) headPosition.getX(), (int) headPosition.getY() + 2 * lineSpacing);

                for (int i = 0; i < displayData.waypoints.getWaypoints().size(); i++) {
                    graphics.getGraphics().drawString("Waypoint (" + displayData.waypoints.getWaypoints().get(i).getT() + "): " + "Speed: " + displayData.waypoints.getWaypoints().get(i).getSpeed() + " Has Run: "
                                    + displayData.waypoints.getWaypoints().get(i).hasRun()
                            , (int) headPosition.getX(), (int) headPosition.getY() + (4 + i) * lineSpacing);
                }
                for (int i = 0; i < displayData.requiredFollowerPoints.getRequiredFollowerPoints().length; i++) {
                    graphics.getGraphics().drawString("Required Point (" + displayData.requiredFollowerPoints.getRequiredFollowerPoints()[i].getT() + "): " + "Angle: " + displayData.requiredFollowerPoints.getRequiredFollowerPoints()[i].getAngle()
                            , (int) headPosition.getX(), (int) headPosition.getY() + (4 + displayData.waypoints.getWaypoints().size()+ i) * lineSpacing);
                }
                break;
        }
    }
}
