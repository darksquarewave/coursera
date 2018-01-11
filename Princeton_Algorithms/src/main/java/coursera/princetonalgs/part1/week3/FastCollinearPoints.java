import java.util.Arrays;

public class FastCollinearPoints {

    private static final int MIN_SEGMENT_SIZE = 3;

    private LineSegment[] lineSegments;
    private int lineSegmentsSize = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        lineSegments = new LineSegment[1];

        Point[] orderedPoints = new Point[points.length];
        System.arraycopy(points, 0, orderedPoints, 0, points.length);

        Point segmentStart = null;
        Point segmentEnd = null;
        int segmentSize;

        for (Point point : points) {
            Arrays.sort(orderedPoints, point.slopeOrder());

            segmentSize = 0;

            for (int i = 1; i < orderedPoints.length; i++) {
                Point curr = orderedPoints[i];

                if (curr.compareTo(point) == 0 && curr != point) {
                    throw new IllegalArgumentException();
                }

                double slope = curr.slopeTo(point);
                boolean isSameSegment = segmentSize > 0 && segmentStart.slopeTo(curr) == slope;
                if (isSameSegment) {
                    if (curr.compareTo(segmentStart) < 0) {
                        segmentStart = curr;
                    }
                    else if (curr.compareTo(segmentEnd) > 0) {
                        segmentEnd = curr;
                    }

                    segmentSize++;
                }

                if (i == orderedPoints.length - 1 || !isSameSegment) {
                    if (segmentSize >= MIN_SEGMENT_SIZE && point.compareTo(segmentStart) < 0) {
                        addToLineSegments(point, segmentEnd);
                    }

                    segmentStart = curr;
                    segmentEnd = curr;
                    segmentSize = 1;
                }
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
