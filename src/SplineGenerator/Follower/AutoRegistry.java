package SplineGenerator.Follower;

import java.util.ArrayList;
import java.util.HashMap;
// Class for looking up autos
public class AutoRegistry {
    HashMap<String,SingleAuto> autos;
    ArrayList<String> autoNames;
    public AutoRegistry(){
        autos = new HashMap();
        autoNames = new ArrayList<String>();
    }

    public void setAutos(HashMap<String,SingleAuto> autos) {
        this.autos = autos;
    }

    public void addAuto(String name, SingleAuto singleAuto){
        autos.putIfAbsent(name,singleAuto);
        if(!autoNames.contains(name)){
            autoNames.add(name);
        }
    }

    public ArrayList<String> getAutoNames() {
        return autoNames;
    }

    public SingleAuto get(String name){
        return autos.get(name);
    }

    public HashMap<String, SingleAuto> getAutos() {
        return autos;
    }
}
