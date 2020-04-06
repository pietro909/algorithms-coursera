import edu.princeton.cs.algs4.StdOut;

import java.lang.reflect.Array;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] results;
    private int counter = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points is null");
        if (points.length == 0) {
            results = new LineSegment[0];
            return;
        }

        LineSegment[] allSegments = findSegments(points);
        results = new LineSegment[counter];

        // remove null segments
        var j = 0;
        for (var i = 0; i < allSegments.length; i++) {
            if (allSegments[i] != null) {
                results[j] = allSegments[i];
                j++;
            }
        }
    }

    public void resetCache(Point[] cache) {
        Point next;
        int i;
        for (i = 0; i < cache.length; i++) {
//            StdOut.println("resetting "+i);
            next = cache[i];
            if (next == null) return;
            cache[i] = null;
        }
    }

    public int sizeOfCache(Point[] cache) {
        Point next;
        int i;
        for (i = 0; i < cache.length; i++) {
            next = cache[i];
            if (next == null) return i;
        }
        return i + 1;
    }

    public LineSegment[] findSegments(Point[] _points) {
//        StdOut.println("Input:");
//        printArray(_points);

        int size = _points.length;
        LineSegment[] atMostResults = new LineSegment[size];
        Point[] cache = new Point[size];

        Point[] sortedBySlopePoints = new Point[size - 1];
        Point[] points = new Point[size];
        copyOtherPoints(_points, points, -1);
        Arrays.sort(points);

//        StdOut.println("Sorted:");
//        printArray(points);

        for (var p = 0; p < size; p++) {
            Point pointP = points[p];
            cache[0] = pointP;
            int cacheCounter = 1;

            if (pointP == null) {
                throw new IllegalArgumentException("point at is null at " + p);
            }

            // mutation ;-(
            copyOtherPoints(points, sortedBySlopePoints, p);
            // sort by slope of P
            Arrays.sort(sortedBySlopePoints, pointP.slopeOrder());

//            printArray(sortedPoints);

            StdOut.println("Working with " + pointP);
            printArray(cache);

            for (var q = 0; q < sortedBySlopePoints.length; q++) {
                Point current = sortedBySlopePoints[q];

                StdOut.println("\t["+q+"] current " + current);
                StdOut.print("\t");
                printArray(cache);

                if (q == 0) {
                    cache[cacheCounter] = current;
//                    StdOut.println("\t1 - cache[" + cacheCounter + "] = " + current);
                    cacheCounter++;
                } else if (pointP.slopeTo(current) == pointP.slopeTo(cache[1])) {
//                    StdOut.println("\tn - cache[" + cacheCounter + "] = " + current);
                    cache[cacheCounter] = current;
                    cacheCounter++;
                } else {
                    // slope has changed: need to check the cache
                    boolean done = checkForLines(pointP, cache, atMostResults);
                    StdOut.println("\t\tslope changed: " + done);

                    if (done) printArray(atMostResults);
//                    else StdOut.println("\tnext: " + current);
                    resetCache(cache);
                    cache[0] = current;
                    cacheCounter = 1;
                }
            }

            boolean done = checkForLines(pointP, cache, atMostResults);
//            StdOut.println("end: " + done);
//            if (done) printArray(atMostResults);
//            else printArray(cache);

            resetCache(cache);
        }
//        StdOut.println("The end");
//        printArray(atMostResults);

        return atMostResults;
    }

    public boolean checkForLines(Point firstPoint, Point[] cache, LineSegment[] destination) {
        int sizeOfCache = sizeOfCache(cache);
        if (sizeOfCache > 2) {
            Point[] nextSegment = Arrays.copyOf(cache, sizeOfCache - 1);
            Arrays.sort(nextSegment);
            //  if pointP is at beginning, take it and the last
            StdOut.println("\tfirst? " + firstPoint + " in ");
            printArray(nextSegment);
            if (nextSegment[0] == firstPoint) {
                LineSegment segment = new LineSegment(firstPoint, nextSegment[sizeOfCache - 2]);
                destination[counter] = segment;
                counter++;

                return true;
            }
        }
        return false;
    }

    public void printArray(Object[] a) {
        StringBuilder sb = new StringBuilder("[ ");
        for (Object point : a) {
            if (point == null) {
                sb.append("null, ");
            } else {
                sb.append(point.toString() + ", ");
            }
        }
        sb.append("]");
        StdOut.println(sb);
    }

    public void copyOtherPoints(Point[] from, Point[] to, int except) {
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

    /**
     * Unit tests
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

        Point[] fourCollinearY = {new Point(1, 2), new Point(2, 2), new Point(3, 2)};//, new Point(4, 2)};
        Point[] fourCollinearX = {new Point(3, 8), new Point(3, 1), new Point(3, 5)};//, new Point(3, 8)};
        Point[] twoLines = {
                new Point(3, 8), new Point(3, 1), new Point(3, 5), new Point(3, 7),
                new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2)
        };

//        FastCollinearPoints fcp1 = new FastCollinearPoints(fourCollinearY);
//        StdOut.println("[1]\tnumber of segments\t" + fcp1.numberOfSegments());
//        fcp1.printArray(fcp1.segments());

//        FastCollinearPoints fcp2 = new FastCollinearPoints(fourCollinearX);
//        StdOut.println("[1]\tnumber of segments\t" + fcp2.numberOfSegments());
//        fcp2.printArray(fcp2.segments());

        FastCollinearPoints fcp3 = new FastCollinearPoints(twoLines);
        StdOut.println("[2]\tnumber of segments\t" + fcp3.numberOfSegments());
        fcp3.printArray(fcp3.segments());

//        StdOut.println("[4]\tnumber of segments\t" + fcp1.sizeOfCache(fourCollinearX));

    }
}