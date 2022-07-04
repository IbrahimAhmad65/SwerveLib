package main;

import java.util.ArrayList;

public class Concave2D {

    public Vector2D[] arr;
    public int head;
    public Concave2D(Vector2D[] arr){
        this.arr = arr;
        head = 0;
    }

    public Concave2D add(Vector2D value){
        arr[head] = value;
        head ++;
        head %= arr.length;
        return this;
    }


    public Vector2D concavity(){
        Vector2D out = arr[1];
        for (int i = 1; i < arr.length -1 ; i++){
            Vector2D low = arr[i+1].getX() > arr[i-1].getX() ? arr[i - 1]: arr[i+1];
            Vector2D high = arr[i+1].getX() < arr[i-1].getX() ? arr[i - 1]: arr[i+1];

            double xRatio = (arr[i].getX() - low.getX())/(high.getX() - low.getX());

            Line b = new Line(low,high);
            double j = b.compute(arr[i].getX());
            if(arr[i].getY() > j){

            }


        }
        return out;
    }



    public static void main(String[] args) {
        double low = -3;
        double high = 3;
        Vector2D[] arr = new Vector2D[100];
        double diff = high - low;
        double h = diff/ arr.length;
        Function<Double,Double> f = (x)->{return x*x;};
        int counter = 0;
        for (double i = low; i < high; i+= h) {
            arr[counter] = new Vector2D(i,f.compute(i));
//            System.out.println(arr[counter]);
            counter ++;
        }
        Concave2D concave2D = new Concave2D(arr);
//        concave2D.concavity();
    }
}
