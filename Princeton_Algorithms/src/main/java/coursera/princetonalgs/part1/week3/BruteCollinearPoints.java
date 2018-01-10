package coursera.princetonalgs.part1.week3;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;
    private int lineSegmentsSize = 0;

    public BruteCollinearPoints(Point[] points) {
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        lineSegments = new LineSegment[1];

        Point[] array = new Point[4];

        for (int i = 0; i < pointsCopy.length; i++) {
            array[0] = pointsCopy[i];

            for (int j = i + 1; j < pointsCopy.length; j++) {
                array[1] = pointsCopy[j];
                if (array[1].compareTo(array[0]) < 0) {
                    break;
                }

                for (int k = j + 1; k < pointsCopy.length; k++) {
                    array[2] = pointsCopy[k];
                    if (array[2].compareTo(array[1]) < 0) {
                        break;
                    }

                    for (int m = k + 1; m < pointsCopy.length; m++) {
                        array[3] = pointsCopy[m];
                        if (array[3].compareTo(array[2]) < 0) {
                            break;
                        }

                        double slope1 = array[0].slopeTo(array[1]);
                        double slope2 = array[1].slopeTo(array[2]);
                        double slope3 = array[2].slopeTo(array[3]);

                        if (slope1 == slope2 && slope2 == slope3) {
                            addToLineSegments(new LineSegment(array[0], array[3]));
                        }
                    }
                }
            }
        }
    }

    private void addToLineSegments(LineSegment lineSegment) {
        if (lineSegmentsSize == lineSegments.length) {
            LineSegment[] copy = new LineSegment[lineSegmentsSize * 2];
            System.arraycopy(lineSegments, 0, copy, 0, lineSegments.length);
            lineSegments = copy;
        }
        lineSegments[lineSegmentsSize] = lineSegment;
        lineSegmentsSize++;
    }

    public int numberOfSegments() {
        return lineSegmentsSize;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegmentsSize);
    }
}
