import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] results;
    private int counter = 0;

    public BruteCollinearPoints(Point[] points) {
        //Throw an IllegalArgumentException
        //      if the argument to the constructor is null,
        //      if any point in the array is null, or
        //      if the argument to the constructor contains a repeated point.

        if (points == null) throw new IllegalArgumentException("points is null");

        Point[] validated = new Point[points.length];

        if (points.length < 4) {
            for (var p = 0; p < points.length; p++)
                for (var q = 0; q < points.length; q++) {
                    if (points[p] == null || points[q] == null)
                        throw new IllegalArgumentException("null " + p);
                    if (p != q && points[p].slopeTo(points[q]) == Double.NEGATIVE_INFINITY)
                        throw new IllegalArgumentException("illegal " + p);
                }
        }

        for (var p = 0; p < points.length; p++) {
            if (points[p] == null)
                throw new IllegalArgumentException("illegal " + p);

            validated[p] = points[p];
        }

        findSegments(validated);
    }

    private void findSegments(Point[] points) {
        // The method segments() should include each line segment containing 4 points exactly once.
        // If 4 points appear on a line segment in the order p→q→r→s,
        // then you should include either the line segment p→s or s→p (but not both) and you
        // should not include subsegments such as p→r or q→r.
        //
        // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.

        // growth: n4 in the worst case

        results = new LineSegment[points.length / 2];


        Arrays.sort(points);

        for (var p = 0; p < points.length; p++) {
            Point pointP = points[p];
            for (var q = 0; q < points.length; q++) {
                if (q == p || q < p) continue;

                for (var r = 0; r < points.length; r++) {
                    if (r == p || r == q || r < q) continue;

                    for (var s = 0; s < points.length; s++) {
                        if (s == r || s == q || s == p || s < r) continue;

                        double slopePToQ = pointP.slopeTo(points[q]);
                        double slopePToR = pointP.slopeTo(points[r]);
                        double slopePToS = pointP.slopeTo(points[s]);
                        double slopeQToR = points[q].slopeTo(points[r]);
                        double slopeQToS = points[q].slopeTo(points[s]);
                        double slopeRToS = points[r].slopeTo(points[s]);

//                        StdOut.println("check " + pointP + " -> " + points[q] + " -> " + points[r] + " -> " + points[s]);

                        boolean duplicates = Double.compare(slopePToQ, Double.NEGATIVE_INFINITY) == 0 ||
                                Double.compare(slopePToR, Double.NEGATIVE_INFINITY) == 0 ||
                                Double.compare(slopePToS, Double.NEGATIVE_INFINITY) == 0 ||
                                Double.compare(slopeQToR, Double.NEGATIVE_INFINITY) == 0 ||
                                Double.compare(slopeQToS, Double.NEGATIVE_INFINITY) == 0 ||
                                Double.compare(slopeRToS, Double.NEGATIVE_INFINITY) == 0;

                        if (duplicates) {
                            throw new IllegalArgumentException("same point at " + p + ", " + q + ", " + r + ", " + s);
                        }

                        boolean sameSlope = slopePToQ == slopePToR && slopePToQ == slopePToS;
                        boolean sorted = pointP.compareTo(points[q]) < 0 && points[q].compareTo(points[r]) < 0 && points[r].compareTo(points[s]) < 0;
                        if (sameSlope && sorted) {
                            results[counter++] = new LineSegment(points[p], points[s]);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[counter];
        for (var i = 0; i < counter; i++)
            segments[i] = results[i];

        return segments;
    }

    /**
     * Unit tests
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

//        Point[] twoSamePoints = {new Point(1, 2), new Point(1, 2)};
//        Point[] samePoints = {
//                new Point(1, 2), new Point(1, 2),
//                new Point(1, 2), new Point(1, 2),
//                new Point(1, 2)
//        };
//
//
//        try {
//            new BruteCollinearPoints(twoSamePoints);
//        } catch (Throwable e) {
//            StdOut.println(e.getMessage());
//        }
//
//        try {
//            new BruteCollinearPoints(samePoints);
//        } catch (Throwable e) {
//            StdOut.println(e.getMessage());
//        }

        Point[] fourCollinearX = {
                new Point(3, 8), new Point(3, 1),
                new Point(3, 5), new Point(3, 9)
        };
        Point[] twoLines = {
                new Point(3, 8), new Point(3, 1),
                new Point(3, 5), new Point(3, 9),
                new Point(1, 1), new Point(32, -5),
                new Point(5, 12), new Point(-4, 12),
                new Point(6, 12), new Point(1, 12),
        };

        Point[] input8 = {
                new Point(10000, 0), new Point(0, 10000),
                new Point(3000, 7000), new Point(7000, 3000),
                new Point(20000, 21000), new Point(3000, 4000),
                new Point(14000, 15000), new Point(6000, 7000)
        };

        Point[] test13 = {new Point(18555, 28511), new Point(11386, 12291), new Point(18555, 28511)};
//        new BruteCollinearPoints(test13);

        Point[] test14 = {new Point(12018, 17959), new Point(13495, 16576), new Point(18656, 9961), new Point(12018, 17959)};
//        new BruteCollinearPoints(test14);

        BruteCollinearPoints bcp1 = new BruteCollinearPoints(fourCollinearX);
        StdOut.println("[1]\tnumber of segments\t" + bcp1.numberOfSegments());
        StdOut.println("\tS1 =\t3,1 - 3,9");
        for (LineSegment line : bcp1.segments()) {
            StdOut.println(line);
        }

        BruteCollinearPoints bcp2 = new BruteCollinearPoints(twoLines);
        StdOut.println("[2]\tnumber of segments\t" + bcp2.numberOfSegments());
        StdOut.println("\tS1 =\t3,1 - 3,9");
        StdOut.println("\tS1 =\t-4,12 - 6,12");
        for (LineSegment line : bcp2.segments()) {
            StdOut.println(line);
        }

        BruteCollinearPoints bcp3 = new BruteCollinearPoints(input8);
        StdOut.println("[2]\tnumber of segments\t" + bcp3.numberOfSegments());
        StdOut.println("\tS1 =\t3,1 - 3,9");
        StdOut.println("\tS1 =\t-4,12 - 6,12");
        for (LineSegment line : bcp3.segments()) {
            StdOut.println(line);
        }
    }
}