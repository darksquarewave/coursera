package coursera.princetonalgs.part1.week3;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;
import java.util.Objects;

public class Point implements Comparable<Point> {

    private final int x;

    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void draw() {
        StdDraw.circle(x, y, 1d);
    }

    public void drawTo(Point point) {
        StdDraw.line(this.x, this.y, point.x, point.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        int result = Integer.compare(that.y, this.y);
        if (result == 0) {
            return Integer.compare(that.x, this.x);
        }
        else {
            return result;
        }
    }

    public double slopeTo(Point that) {
        int dy = that.y - this.y;
        int dx = that.x - this.x;

        if (dx == 0) {
            if (that.y != this.y) {
                return Double.POSITIVE_INFINITY;
            }
            else {
                return Double.NEGATIVE_INFINITY;
            }
        }
        else {
            return (double) dy / dx;
        }
    }

    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                int slopeResult = Double.compare(slopeTo(o1), slopeTo(o2));
                if (slopeResult == 0) {
                    return o2.compareTo(o1);
                }
                else {
                    return slopeResult;
                }
            }
        };
    }
}
