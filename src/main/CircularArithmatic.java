package main;

public class CircularArithmatic {

    private double min;
    private double max;
    private  double range;
    public CircularArithmatic(double min, double max){
        this.min = min;
        this.max = max;
        this.range = max-min;
    }

    public double add(double a, double b){
        double sum = a+b;
        return map(sum);
    }
    public double operate(Function2Args<Double,Double> operator, double a, double b){
        return map(operator.compute(map(a),map(b)));
    }

    public double multiply(double a, double b){
        return operate((x,y)->{return x*y;},a,b);
    }

    public double divide(double a, double b){
        return operate((x,y)->{return x/y;},a,b);
    }

    public double mod(double a, double b){
        return operate((x,y)->{return x%y;},a,b);
    }

    public double minDistance(double a, double b){
        double aT,bT;
        aT = map(a);
        bT = map(b);
        double low = Math.min(aT,bT);
        double high = Math.max(aT,bT);

        return Math.min(high - low, max - high + low - min);
    }

    public double maxDistance(double a, double b){
        double aT,bT;
        aT = map(a);
        bT = map(b);
        double low = Math.min(aT,bT);
        double high = Math.max(aT,bT);

        return Math.max(high - low, max - high + low - min);
    }

    public double maxDistanceSigned(double a, double b){
        return maxDistance(a,b)* (a > b ? -1 : 1);
    }

    public double minDistanceSigned(double a, double b){
        return minDistance(a,b)* (a > b ? -1 : 1);
    }

    public double map(double d){
        return (d-min)%range+min;
    }

    public void setMin(double min) {
        this.min = min;
        range = max -min;
    }

    public void setMax(double max) {
        this.max = max;
        range = max -min;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getRange() {
        return range;
    }

    public static void main(String[] args) {
        CircularArithmatic circularArithmatic = new CircularArithmatic(2,21);
//        System.out.println(circularArithmatic.add(59,67));
        System.out.println(circularArithmatic.minDistanceSigned(23,1));
    }
}
