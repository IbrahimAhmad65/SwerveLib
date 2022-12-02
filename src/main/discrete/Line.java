package main.discrete;

import java.util.Objects;

public class Line {
    private int point1 = 0;
    private int point2 = 0;

    public Line(int point1, int point2){
        this.point1 = point1;
        this.point2 = point2;
    }

    public int getPoint1() {
        return point1;
    }

    public int getPoint2() {
        return point2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return point1 == line.point1 && point2 == line.point2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point1, point2);
    }
}
