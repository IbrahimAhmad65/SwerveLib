package main.discrete;

import java.util.Arrays;

public class PointCollection {
    private int[] point;
    public PointCollection(int... points){
        this.point = points;
    }

    public int[] getPoint() {
        return point;
    }

    public boolean isPointIn(int n){
        for (int j: point){
            if(j == n){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointCollection that = (PointCollection) o;
        return Arrays.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }
}
