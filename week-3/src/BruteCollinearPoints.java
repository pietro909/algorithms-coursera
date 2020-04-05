public class BruteCollinearPoints {
    Point[] points;

    public BruteCollinearPoints(Point[] _points) {
        //Throw an IllegalArgumentException
        //      if the argument to the constructor is null,
        //      if any point in the array is null, or
        //      if the argument to the constructor contains a repeated point.
        this.points = _points;
        if (points == null) throw new IllegalArgumentException("points is null");
        for (var i = 0; i < points.length; i++)
            if (points[i] == null) throw new IllegalArgumentException("point is null at " + i);
    }

    public int numberOfSegments() {
        return segments().length;
    }

    // The method segments() should include each line segment containing 4 points exactly once.
    // If 4 points appear on a line segment in the order p→q→r→s,
    // then you should include either the line segment p→s or s→p (but not both) and you
    // should not include subsegments such as p→r or q→r.
    //
    // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.

    // growth: n4 in the worst case
    public LineSegment[] segments() {
        LineSegment[] results = new LineSegment[points.length/4];
        int counter = 0;
        // To check whether the 4 points p, q, r, and s are collinear, check whether the three
        // slopes between p and q, between p and r, and between p and s are all equal.
        for (var p = 0; p < points.length - 4; p++) {
            for (var q = p + 1; q < points.length - 3; q++) {
                for (var r = q + 1; r < points.length - 2; r++) {
                    for (var s = r + 1; s < points.length - 1; s++) {
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r]) &&
                        points[p].slopeTo(points[q]) == points[p].slopeTo(points[s])) {
                            results[counter] = new LineSegment(points[p], points[s]);
                            counter++;
                        }
                    }
                }
            }
        }
        return results;
    }
}