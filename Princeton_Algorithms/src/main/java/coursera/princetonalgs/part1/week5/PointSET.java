import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : set) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (isEmpty()) {
            return Collections.emptyList();
        }

        List<Point2D> result = new ArrayList<>();

        for (Point2D point2D : set) {
            if (rect.contains(point2D)) {
                result.add(point2D);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }

        Point2D nearest = set.iterator().next();
        double minDistance = p.distanceSquaredTo(nearest);

        for (Point2D point2D : set) {
            double d = point2D.distanceSquaredTo(p);
            if (d < minDistance) {
                nearest = point2D;
                minDistance = d;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
