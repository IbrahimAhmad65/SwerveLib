package SplineGenerator.Follower;

import java.util.HashMap;
// Class for looking up autos
public class AutoRegistry {
    HashMap<String,SingleAuto> autos;
    public AutoRegistry(){
        autos = new HashMap();
    }

    public void setAutos(HashMap<String,SingleAuto> autos) {
        this.autos = autos;
    }

    public void addAuto(String name, SingleAuto singleAuto){
        autos.putIfAbsent(name,singleAuto);
    }

    public SingleAuto get(String name){
        return autos.get(name);
    }

    public HashMap<String, SingleAuto> getAutos() {
        return autos;
    }
}
