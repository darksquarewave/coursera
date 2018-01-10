package coursera.princetonalgs.part1.week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.net.URL;

public class FastClient {

    public static void main(String[] args) {

        URL resource = FastClient.class.getClassLoader().getResource(args[0]);

        // read the n points from a file
        In in = new In(resource.getFile());
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        StdDraw.setPenColor(Color.BLUE);

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (int i = 0; i < collinear.numberOfSegments(); i++) {
            LineSegment segment = collinear.segments()[i];
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();

        System.out.println("finish");
    }
}
