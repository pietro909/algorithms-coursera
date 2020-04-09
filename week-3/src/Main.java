import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Main {
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
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

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//        StdOut.println("I'm done");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        /*
        (9000, 1000) -> (16000, 22000)
        (1000, 2000) -> (1000, 26000)
        (6000, 2000) -> (19000, 28000)
        (18000, 13000) -> (18000, 27000)
        (1000, 23000) -> (24000, 23000)
        (1000, 26000) -> (18000, 26000)
         */
        StdDraw.show();
    }
}