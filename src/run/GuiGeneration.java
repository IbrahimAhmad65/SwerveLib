package run;

import BSpline.BSplineH;
import BSpline.SplinePoint;
import main.Vector2D;

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
            File myObj = new File("C:\\Users\\galaly\\in.txt");
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
        SplinePoint[] splinePoints = new SplinePoint[input.size()];
        for (int i = 0; i < input.size(); i++) {
//            String temp = input.get(i).s;
            Vector2D v1 = new Vector2D(Double.parseDouble(input.get(i)[0]),Double.parseDouble(input.get(i)[1]));
            Vector2D v2 = new Vector2D(Double.parseDouble(input.get(i)[2]),Double.parseDouble(input.get(i)[3]));

            splinePoints[i] = new SplinePoint(v1,v2);
        }
        BSplineH bSplineH = new BSplineH(.01,.01,splinePoints);
            try {
                FileWriter myWriter = new FileWriter("C:\\Users\\galaly\\out.txt");
                for (double i = 0; i < bSplineH.getEquationNumber(); i+= bSplineH.getT()) {
                    myWriter.write("" + bSplineH.evaluatePos(i)+"\n2");
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

}
