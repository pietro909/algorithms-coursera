import edu.princeton.cs.algs4.StdOut;

import java.lang.reflect.Array;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] results;
    private int counter = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points is null");
        LineSegment[] allSegments = findSegments(points);
        results = new LineSegment[counter];
        var j = 0;
//        StdOut.println("all segments  " + allSegments.length);

        for (var i = 0; i < allSegments.length; i++) {
//            StdOut.println("check " + allSegments[i]);

            if (allSegments[i] != null) {
                results[j] = allSegments[i];
                j++;
            }
        }
    }

    private LineSegment[] findSegments(Point[] points) {
        LineSegment[] atMostResults = new LineSegment[points.length];
        int size = points.length - 1;
        Point[] sortedPoints = new Point[size];

        double[] slopes = new double[points.length];

//        Double previousSlope = Double.NEGATIVE_INFINITY;

        for (var p = 0; p < size - 1; p++) {
            Point pointP = points[p];

            if (pointP == null) {
                throw new IllegalArgumentException("point at is null at " + p);
            }

            // mutation ;-(
//            StdOut.println("copy " + points.length + " to " + sortedPoints.length + " except " + p);
            copyOtherPoints(points, sortedPoints, p);
//            copyOtherPoints(points, sortedPoints, p);
            // sort by slope of P

            Arrays.sort(sortedPoints, pointP.slopeOrder());
//            Arrays.sort(sortedPoints);

            printArray(sortedPoints);

            // TODO: check for duplicates

            for (var q = 0; q < size - 1; q++) {
                Point pointQ = sortedPoints[0];
                double currentSlope = pointP.slopeTo(pointQ);

//                StdOut.println("... " + previousSlope + " vs " + currentSlope);

//                if (currentSlope == previousSlope) {
//                    StdOut.println("skipping " + currentSlope);
//                    continue;
//                }

                StdOut.println("proceeding with " + pointP + ", " + pointQ + " = " + currentSlope);
//                previousSlope = currentSlope;

                Point startPoint = pointP;
                // candidate as end point
                Point endPoint = sortedPoints[q++];
                // actual end point
                Point previousPoint = endPoint;
                while (endPoint != null &&
                        currentSlope == pointP.slopeTo(endPoint)
                ) {
                    StdOut.println(" - compare " + startPoint + " to " + endPoint + " = " + startPoint.compareTo(endPoint));
                    if (startPoint.compareTo(endPoint) > 0) {
                        // invert points
                        previousPoint = startPoint;
                        startPoint = endPoint;
                        endPoint = sortedPoints[q++];
                    } else {
                        previousPoint = endPoint;
                        endPoint = sortedPoints[q++];
                    }
                }

                StdOut.println(" slope: " + startPoint.slopeTo(previousPoint) + " == " + startPoint.slopeTo(pointQ));


                if (previousPoint != null) {
                    atMostResults[counter] = new LineSegment(pointP, previousPoint);
                    counter++;
                    break;
                }
            }
        }
        return atMostResults;
    }

    private void printArray(Point[] a) {
        StringBuilder sb = new StringBuilder("[ ");
        for (var i = 0; i < a.length; i++) {
            sb.append(a[i].toString() + ", ");
        }
        sb.append("]");
        StdOut.println(sb);
    }

    private void copyOtherPoints(Point[] from, Point[] to, int except) {
        var j = 0;
        for (var i = 0; i < from.length; i++) {
            if (i != except) {
                to[j] = from[i];
                j++;
            }
        }
    }

    public int numberOfSegments() {
        return counter;
    }

    public LineSegment[] segments() {
        // TODO: null
        return results;
    }
}