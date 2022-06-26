package main;

import java.util.Arrays;
import java.util.HashMap;

public class LinearlyInterpLUT {
    // data and pairs must match, data must be sorted
    private double[] data;
    private double[] pairs;

    public LinearlyInterpLUT(double[] data, double[] pairs) {
        this.data = data;
        this.pairs = pairs;
    }


    public double get(double in) {
        if (pairs[pairs.length - 1] <= in) {
            double b = (binarySearch(data, in));
            int intb = (int)b;
            double c = data[intb];
            double diff = (in - c)/ (-data[intb - 1] + c);
            double d = pairs[intb];
            return d + diff * (-pairs[intb-1] + d);
        }
        double b = (binarySearch(data, in));
        int intb = (int)b;
        double c = data[intb];
        double diff = (in - c)/ (data[intb + 1] - c);
        double d = pairs[intb];
        return d + diff * (pairs[intb+1] - d);
    }

    //-42567 should not be in the array
    private static double binarySearch(double[] arr, double key) {
        int low = 0;
        int high = arr.length;
        int start = (low + high) / 2;
        for (int j = 0; j < arr.length; j++) {
            if (arr[start] < key) {
                low = start;
                start = (low + high) / 2;
                System.out.println("Low: " + low + " High: " + high + " start: " + start);
            } else if (arr[start] > key) {
                high = start;
                start = (low + high) / 2;
                System.out.println("Low: " + low + " High: " + high + " start: " + start);
            } else {
                return start;
            }
            if (high - low == 1) {
                return (low + (key - Math.floor(key)));
            }
        }
        return Double.MIN_NORMAL;
    }

    public static void main(String[] args) {
        LinearlyInterpLUT bd = new LinearlyInterpLUT(new double[]{0, 1.6, 8, 54, 96}, new double[]{1, 2, 3, 4, 5});
        double b = bd.get(138);
        System.out.println(b);
    }
}
