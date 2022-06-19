package main.cas;

import java.util.Objects;

public class Variable implements Argument{

    private String name;
    private Variable[] dOrI;

    private Blob b;
    public Variable(String name){
        this.name = name;
        this.b = this;
    }

    public Variable(String name, Variable... differentiableOrIntegrtable){
        this.name = name;
        this.dOrI = differentiableOrIntegrtable;

    }


    public boolean isConstant() {
        return false;
    }

    public Argument derivative(Argument d) {
        return null;
    }

    public boolean isIntegralDefined(Argument d) {
        for (Variable a: dOrI) {
            if(a.equals(d)){
                return true;
            }
        }
        return false;
    }

    public Argument integral(Argument d) {
        return null;
    }

    public String getName(){
        return name;
    }

    public Blob getInstance() {
        return b;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return name.equals(variable.name);
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public Blob[] peel() {
        return new Blob[]{b};
    }

}
