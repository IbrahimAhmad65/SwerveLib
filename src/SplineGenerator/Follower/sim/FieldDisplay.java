package SplineGenerator.Follower.sim;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.Util.DPoint;

import java.awt.*;

public class FieldDisplay implements Displayable {
    private Line[] lines;
    public FieldDisplay(){
//        lines = new Line[5];
//        lines[0] = new Line(-6.81,-3.975,-6.81,1.33);
//        lines[1] = new Line(-4.87,1.33,-6.81,1.33);
//        lines[2] = new Line(-4.87,1.33,-4.87,-.044);
//        lines[3] = new Line(-3.35,-.044,-4.87,-.044);
//        lines[4] = new Line(-3.35,-.044,-3.35,-3.975);




    }
    @Override
    public void display(DisplayGraphics graphics) {
        for (Line l : lines) {
            l.display(graphics);
        }


    }

    private class Line implements Displayable{
        public double x1, y1, x2, y2;

        public Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public void display(DisplayGraphics graphics) {
            graphics.paintLine(new DPoint(x1,y1) , new DPoint(x2,y2), 1, new Color(255, 112, 0), 0, 1);
        }
    }
}
