package run;

import SplineGenerator.Follower.Followable;
import SplineGenerator.Follower.sim.JSONAutoParser;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GuiGenerationType2 {
    public static void main(String[] args) throws IOException {
        // read input at args[0] for file input
        // write output at args[1] for file output
        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(args[0]))));
        Followable out = JSONAutoParser.generateFollowableFromJSON(json);


        try {
            FileWriter myWriter = new FileWriter(args[1]);
            for (double i = 0; i < out.getNumPieces(); i+= .05) {
                if((i - Math.floor(i)) <.04){
                    myWriter.write("Control Point: " + Math.floor(i));
                }
                myWriter.write(out.get(i).get(0) + ","+ out.get(i).get(1) +"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
