import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] results;

    public BruteCollinearPoints(Point[] points) {
        //Throw an IllegalArgumentException
        //      if the argument to the constructor is null,
        //      if any point in the array is null, or
        //      if the argument to the constructor contains a repeated point.

        if (points == null) throw new IllegalArgumentException("points is null");

        results = findSegments(points);
    }

    private LineSegment[] findSegments(Point[] points) {
        // The method segments() should include each line segment containing 4 points exactly once.
        // If 4 points appear on a line segment in the order p→q→r→s,
        // then you should include either the line segment p→s or s→p (but not both) and you
        // should not include subsegments such as p→r or q→r.
        //
        // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.

        // growth: n4 in the worst case
        LineSegment[] results = new LineSegment[points.length / 4];
        int counter = 0;
        // To check whether the 4 points p, q, r, and s are collinear, check whether the three
        // slopes between p and q, between p and r, and between p and s are all equal.
        for (var p = 0; p < points.length - 1; p++) {
            for (var q = p + 1; q < points.length - 1; q++) {
                for (var r = q + 1; r < points.length - 1; r++) {
                    for (var s = r + 1; s < points.length - 1; s++) {
//                        StdOut.println("... " + p + ", " + q + ", " + r + ", " + s);

                        double slopePToQ = points[p].slopeTo(points[q]);
                        double slopePToR = points[p].slopeTo(points[r]);
                        double slopePToS = points[p].slopeTo(points[s]);

                        if (slopePToQ == Double.NEGATIVE_INFINITY ||
                                slopePToR == Double.NEGATIVE_INFINITY ||
                                slopePToS == Double.NEGATIVE_INFINITY
                        ) {
                            throw new IllegalArgumentException("same point at " + p + ", " + q + ", " + r + ", " + s);
                        }
//                        StdOut.println("checking "+slopePToQ+" == "+slopePToR+" == "+slopePToS);
                        if (slopePToQ == slopePToR && slopePToQ == slopePToS) {
                            results[counter] = new LineSegment(points[p], points[s]);
                            counter++;
                        }
                    }
                }
            }
        }
        return results;
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {
        return results;
    }
}