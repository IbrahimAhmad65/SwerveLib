package BSpline.Graphics;

import BSpline.BSplineH;
import BSpline.BSplineFollowerBasic;
import BSpline.SplinePoint;
import BSpline.Waypoint;
import main.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class SplineGrapher extends JFrame {
    private BSplineH b;
    private  BSplineFollowerBasic followerBasic;
    private Vector2D pos = new Vector2D(0,0);
    private Supplier<Vector2D> posSupplier;
    private int width;
    private int height;
    public SplineGrapher(BSplineH b, int width, int height){
        pos = b.evaluatePos(0);
//        double xS = (int) (pos.getX() * width) + width / 2;
//        double yS = height - (int) (pos.getY() * height) + height/2;
//        pos.setXY(xS,yS);
        posSupplier = ()->pos;
        this.followerBasic = new BSplineFollowerBasic(b,new Waypoint[]{},.1,.01,-.1,0);
        this.b =b;
        frameInit();
        this.width = width;
        this.height = height;

        int[] x = new int[(int)Math.ceil(b.getEquationNumber()/b.getT())];
        int[] y = new int[(int)Math.ceil(b.getEquationNumber()/b.getT())];
        for (int i = 0; i < x.length; i++) {
            x[i] = (int) (b.evaluatePos(i*b.getT()).getX() * width) + width / 2;
            y[i] = height - (int) (b.evaluatePos(i*b.getT()).getY() * height) + height/2;
        }
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < x.length; i++) {
                    g.drawRect(x[i],y[i],5,5);
                }
                double x = (int) (followerBasic.get(pos).getX() * width) + width / 2;
                double y = height - (int) (followerBasic.get(pos).getY() * height) + height/2;
//                System.out.println("x:" + x);
//                System.out.println("y:" + y);
//                System.out.println(followerBasic.get());
                pos.add(new Vector2D(x,y).scale(.001));
                g.drawRoundRect((int)pos.getX(),(int)pos.getY(),5,5,1,1);
            }
        };
        this.add(p);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponents(g);

    }

    public static void main(String[] args) throws InterruptedException {
        SplinePoint[] splinePoints1 = {
                new SplinePoint(new Vector2D(.5,1),new Vector2D(1,3)),
                new SplinePoint(new Vector2D(.6,1),new Vector2D(1,3)),
                new SplinePoint(new Vector2D(.1,.1),new Vector2D(1,1))};
        SplinePoint[] splinePoints = {new SplinePoint(new Vector2D(.1,.1),new Vector2D(1,1)),
                new SplinePoint(new Vector2D(.1,1),new Vector2D(1,2))};
        BSplineH b = new BSplineH(new Vector2D(-1,-1),new Vector2D(1,1),.01,.1,splinePoints1);
        int x = 1920/2;
        int y = 720/2;
        SplineGrapher s = new SplineGrapher(b,x,y);
        while (true){
            s.repaint((long) 1,10,10,x,y);
            Thread.sleep(100);
        }
    }
}