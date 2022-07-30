package run;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// very epic and cool i know
// x y vx vy
public class GuiGeneration {
    public static void main(String[] args) {
        ArrayList<String[]> input = new ArrayList<String[]>();
        try {
            File myObj = new File("C:\\Users\\galaly\\robotics\\splineUtil\\in.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                input.add(data.split(","));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        DControlPoint[] arr = new DControlPoint[input.size()];
        for (int i = 0; i < input.size(); i++) {
//            String temp = input.get(i).s;
            if(i == 0 || i == input.size() - 1){
                DVector v1 = new DVector(Double.parseDouble(input.get(i)[0]),Double.parseDouble(input.get(i)[1]));
                DVector v2 = new DVector(Double.parseDouble("1"),Double.parseDouble("2"));
                arr[i] = new DControlPoint(v1,v2, new DVector(0,0));
            } else{
                DVector v1 = new DVector(Double.parseDouble(input.get(i)[0]),Double.parseDouble(input.get(i)[1]));
                DVector v2 = new DVector(Double.parseDouble("1"),Double.parseDouble("2"));
                arr[i] = new DControlPoint(v1,v2);
            }
        }




        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);
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

        double counter = 0;
        // Create Necessary Equations
        for(DControlPoint splinePoint : arr){
            spline.addControlPoint(splinePoint);
        }
        spline.generate();
        spline.takeNextDerivative();


            try {
                FileWriter myWriter = new FileWriter("C:\\Users\\galaly\\robotics\\splineUtil\\out.txt");
                for (double i = 0; i < input.size() - 1; i+= .05) {
                    myWriter.write(spline.get(i).get(0) + ","+ spline.get(i).get(1) +"\n");
                    System.out.println(spline.get(i));
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

}
