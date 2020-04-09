import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] allSegments;
    private int counter = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points is null");
        if (points.length == 0) {
            return;
        }

        allSegments = findSegments(points);
    }

    private void resetCache(Point[] cache) {
        Point next;
        int i;
        for (i = 0; i < cache.length; i++) {
            next = cache[i];
            if (next == null) return;
            cache[i] = null;
        }
    }

    private int sizeOfCache(Point[] cache) {
        Point next;
        int i;
        for (i = 0; i < cache.length; i++) {
            next = cache[i];
            if (next == null) return i;
        }
        return i;
    }

    private LineSegment[] findSegments(Point[] points) {
        int size = points.length;
        Point[] sortedPoints = new Point[size];
        copyOtherPoints(points, sortedPoints, null);
        Arrays.sort(sortedPoints);

        Point[] sortedBySlopePoints = new Point[size - 1];

        LineSegment[] allResults = new LineSegment[size * size];

        double previousSlope = Double.NEGATIVE_INFINITY;
        int nextCache = 0;
        Point[] cache = new Point[size];

        for (var p = 0; p < size; p++) {
            Point pointP = sortedPoints[p];
            if (pointP == null)
                throw new IllegalArgumentException("null at" + p);

            copyOtherPoints(points, sortedBySlopePoints, pointP);
            // WTF this is a different array!
            Arrays.sort(sortedBySlopePoints, pointP.slopeOrder());

//            StdOut.println("pointP: " + pointP);
//            printArray(sortedBySlopePoints);
            for (var q = 0; q < sortedBySlopePoints.length; q++) {
                Point pointQ = sortedBySlopePoints[q];
                if (pointQ == null)
                    throw new IllegalArgumentException("null at" + q);
                if (pointP.slopeTo(pointQ) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("same point found");

                if (previousSlope == Double.NEGATIVE_INFINITY) {
                    previousSlope = pointP.slopeTo(pointQ);
                    nextCache = 0;
                    cache[nextCache++] = pointP;
                }

                if (previousSlope == pointP.slopeTo(pointQ)) {
//                    StdOut.println("\t\t" + previousSlope + " -> " + pointQ);
                    cache[nextCache++] = pointQ;
                } else {
                    checkForLines(cache, allResults);
                    resetCache(cache);
                    nextCache = 0;
                    cache[nextCache++] = pointP;
                    cache[nextCache++] = pointQ;
                    previousSlope = pointP.slopeTo(pointQ);
//                    StdOut.println("\t" + previousSlope + " -> " + pointQ);
                }
            }

            if (sizeOfCache(cache) > 0) {
                checkForLines(cache, allResults);
                resetCache(cache);
                nextCache = 0;
                previousSlope = Double.NEGATIVE_INFINITY;
            }

        }

        return allResults;
    }

    private boolean checkForLines(Point[] cache, LineSegment[] destination) {
        Point firstPoint = cache[0];

        int sizeOfCache = sizeOfCache(cache);
//        StdOut.println("size = " + sizeOfCache + " for: ");
//        printArray(cache);

        if (sizeOfCache > 3) {
            Point[] nextSegment = Arrays.copyOf(cache, sizeOfCache);
            Arrays.sort(nextSegment);
            //  if pointP is at beginning, take it and the last
//            StdOut.println("checkLines in");
//            StdOut.print("\t");

//            StdOut.println("\tfirst? " + firstPoint + " in ");
//            StdOut.print("\t");
//            printArray(nextSegment);
            if (nextSegment[0] == firstPoint) {
                LineSegment segment = new LineSegment(firstPoint, nextSegment[sizeOfCache - 1]);
                destination[counter] = segment;
                counter++;

                return true;
            }
        }
        return false;
    }

    private void printArray(Object[] a) {
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

    private void copyOtherPoints(Point[] from, Point[] to, Point except) {
        var j = 0;
        for (var i = 0; i < from.length; i++) {
            if (from[i] == null)
                throw new IllegalArgumentException("null found");
            if (except == null || from[i].slopeTo(except) != Double.NEGATIVE_INFINITY) {
                to[j] = from[i];
                j++;
            }
        }
//        printArray(from);
//        printArray(to);
    }

    public int numberOfSegments() {
        return counter;
    }

    public LineSegment[] segments() {
        LineSegment[] results = new LineSegment[counter];

        // remove null segments
        var j = 0;
        for (var i = 0; i < allSegments.length; i++) {
            if (allSegments[i] != null) {
                results[j] = allSegments[i];
                j++;
            }
        }

        return results;
    }

    /**
     * Unit tests
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

//        Point[] fourCollinearY = {new Point(1, 2), new Point(2, 2), new Point(3, 2)};//, new Point(4, 2)};
//        Point[] fourCollinearX = {new Point(3, 8), new Point(3, 1), new Point(3, 5)};//, new Point(3, 8)};
        Point[] twoLines = {
                new Point(3, 8), new Point(3, 1), new Point(3, 5), new Point(3, 7),
                new Point(1, 2), new Point(2, 2), new Point(9, 2), new Point(4, 2)
        };

        Point[] horizontal = {
                new Point(6028, 3619), new Point(11035, 3619),
                new Point(13276, 3619), new Point(12870, 3619)
        };

        Point[] randomDuplicated = {
                new Point(12258, 4724), new Point(2246, 4724), null,
                new Point(19739, 4724), new Point(19443, 4724)
        };
        FastCollinearPoints fcp5 = new FastCollinearPoints(randomDuplicated);
        StdOut.println("[1]\tnumber of segments\t" + fcp5.numberOfSegments());
//        fcp5.printArray(fcp5.segments());

//        FastCollinearPoints fcp1 = new FastCollinearPoints(fourCollinearY);
//        StdOut.println("[1]\tnumber of segments\t" + fcp1.numberOfSegments());
//        fcp1.printArray(fcp1.segments());

//        FastCollinearPoints fcp2 = new FastCollinearPoints(fourCollinearX);
//        StdOut.println("[1]\tnumber of segments\t" + fcp2.numberOfSegments());
//        fcp2.printArray(fcp2.segments());

//        FastCollinearPoints fcp3 = new FastCollinearPoints(twoLines);
//        StdOut.println("[2]\tnumber of segments\t" + fcp3.numberOfSegments());
//        for  ( LineSegment line : fcp3.segments()) {
//            StdOut.println(line+" = "+line.slope());
//        }

//        FastCollinearPoints fcp4 = new FastCollinearPoints(horizontal);
//        StdOut.println("[1]\tnumber of segments\t" + fcp4.numberOfSegments());
//        for (LineSegment line : fcp4.segments()) {
//            StdOut.println(line + " = " + line.slope());
//        }

//        StdOut.println("[4]\tnumber of segments\t" + fcp1.sizeOfCache(fourCollinearX));

    }
}