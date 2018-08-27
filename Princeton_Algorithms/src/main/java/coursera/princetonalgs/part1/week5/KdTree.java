import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private class Node {
        private Node left;
        private Node right;
        private final boolean vertical;
        private final Point2D point;
        private final RectHV rect;

        Node(Point2D p, RectHV r, boolean v) {
            point = p;
            vertical = v;
            rect = r;
        }
    }

    private class Champion {
        private Point2D point;
        private double distance;

        Champion(Point2D p, double d) {
            point = p;
            distance = d;
        }
    }

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(null, root, p, true);
    }

    private Node insert(Node parent, Node n, Point2D p, boolean left) {
        if (n == null) {
            size++;

            RectHV rect;

            if (parent == null) {
                return new Node(p, new RectHV(0d, 0d, 1d, 1d), true);

            }
            else if (parent.vertical) {
                if (left) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
                }
                else {
                    rect = new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
            else {
                if (left) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
                }
                else {
                    rect = new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            }

            return new Node(p, rect, !parent.vertical);
        }

        if (n.point.equals(p)) {
            return n;
        }

        int cmp;
        if (n.vertical) {
            cmp = Double.compare(n.point.x(), p.x());
        }
        else {
            cmp = Double.compare(n.point.y(), p.y());
        }

        if (cmp >= 0) {
            n.left = insert(n, n.left, p, true);
        }
        else {
            n.right = insert(n, n.right, p, false);
        }

        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) {
            return false;
        }

        if (n.point.equals(p)) {
            return true;
        }

        int cmp;
        if (n.vertical) {
            cmp = Double.compare(n.point.x(), p.x());
        }
        else {
            cmp = Double.compare(n.point.y(), p.y());
        }

        if (cmp >= 0) {
            return contains(n.left, p);
        }
        else {
            return contains(n.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, null, true);
    }

    private void draw(Node n, Node parent, boolean isLeft) {
        if (n == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        n.point.draw();
        StdDraw.setPenRadius(0.002);

        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
            if (parent == null) {
                StdDraw.line(n.point.x(), 0d, n.point.x(), 1d);
            }
            else if (isLeft) {
                StdDraw.line(n.point.x(), 0d, n.point.x(), parent.point.y());
            }
            else {
                StdDraw.line(n.point.x(), parent.point.y(), n.point.x(), 1d);
            }
        }
        else {
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            if (isLeft) {
                StdDraw.line(0d, n.point.y(), parent.point.x(), n.point.y());
            }
            else {
                StdDraw.line(parent.point.x(), n.point.y(), 1d, n.point.y());
            }
        }
        draw(n.left, n, true);
        draw(n.right, n, false);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> results = new ArrayList<>();
        range(root, rect, results);
        return results;
    }

    private void range(Node n, RectHV rangeRect, List<Point2D> results) {
        if (n == null) {
            return;
        }

        if (rangeRect.contains(n.point)) {
            results.add(n.point);
        }

        if (n.left != null && n.left.rect.intersects(rangeRect)) {
            range(n.left, rangeRect, results);
        }
        if (n.right != null && n.right.rect.intersects(rangeRect)) {
            range(n.right, rangeRect, results);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Champion champion = nearest(root, p, null);
        if (champion != null) {
            return champion.point;
        }
        else {
            return null;
        }
    }

    private Champion nearest(Node n, Point2D target, Champion champion) {
        if (n == null) {
            return champion;
        }

        if (champion != null && n.rect.distanceSquaredTo(target) >= champion.distance) {
            return champion;
        }

        double distance = target.distanceSquaredTo(n.point);

        if (champion == null) {
            champion = new Champion(n.point, distance);
        }

        if (distance < champion.distance) {
            champion.point = n.point;
            champion.distance = distance;
        }

        double ld = Double.POSITIVE_INFINITY;
        double rd = Double.POSITIVE_INFINITY;

        if (n.left != null) {
            ld = n.left.rect.distanceSquaredTo(target);
        }
        if (n.right != null) {
            rd = n.right.rect.distanceSquaredTo(target);
        }

        if (ld < rd) {
            champion = nearest(n.left, target, champion);
            champion = nearest(n.right, target, champion);
        }
        else {
            champion = nearest(n.right, target, champion);
            champion = nearest(n.left, target, champion);
        }

        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
