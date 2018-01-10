package coursera.princetonalgs.part1.week3;

import java.util.Arrays;

public class FastCollinearPoints {

    private static final int MIN_SEGMENT_SIZE = 3;

    private LineSegment[] lineSegments;
    private int lineSegmentsSize = 0;

    public FastCollinearPoints(Point[] points) {
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        lineSegments = new LineSegment[1];

        Point[] aux = new Point[points.length];
        System.arraycopy(points, 0, aux, 0, points.length);

        for (Point point : points) {
            Arrays.sort(aux, point.slopeOrder());

            double slope = 0d;

            Point end = null;

            int count = 1;
            boolean validOrder = true;
            boolean slopeInitialized = false;

            for (Point curr : aux) {
                if (curr == point) {
                    continue;
                }

                double currSlope = point.slopeTo(curr);

                if (!slopeInitialized) {
                    slope = currSlope;
                    validOrder = curr.compareTo(point) < 0;
                    slopeInitialized = true;

                    continue;
                }

                if (slope != currSlope) {
                    if (count >= MIN_SEGMENT_SIZE) {
                        addToLineSegments(point, end);
                    }

                    validOrder = curr.compareTo(point) < 0;
                    count = 1;
                }
                else if (validOrder) {
                    count++;
                }

                slope = currSlope;
                end = curr;
            }

            if (count >= MIN_SEGMENT_SIZE) {
                addToLineSegments(point, end);
            }
        }
    }

    private void addToLineSegments(Point begin, Point end) {
        if (lineSegmentsSize == lineSegments.length) {
            LineSegment[] copy = new LineSegment[lineSegmentsSize * 2];
            System.arraycopy(lineSegments, 0, copy, 0, lineSegments.length);
            lineSegments = copy;
        }
        lineSegments[lineSegmentsSize] = new LineSegment(begin, end);
        lineSegmentsSize++;
    }

    public int numberOfSegments() {
        return lineSegmentsSize;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegmentsSize);
    }
}
