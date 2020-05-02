import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PointSET {
    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    private void nonNull(Object a) {
        if (a == null)
            throw new IllegalArgumentException("");
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        nonNull(p);
        set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        nonNull(p);
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.004);

        for (Point2D next : set) {
            StdDraw.point(next.x(), next.y());
        }
    }

    // it should support nearest() and range() in time proportional to the number of points in the set.

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        nonNull(rect);
        Point2D[] results = new Point2D[set.size()];
        int i = 0;
        for (Point2D next : set) {
            if (rect.contains(next)) {
                results[i++] = next;
            }
        }
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return new Iterator<Point2D>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < results.length && results[index] != null;
                    }

                    @Override
                    public Point2D next() {
                        Point2D n = results[index++];
                        if (n == null)
                            throw new NoSuchElementException();
                        return n;
                    }
                };
            }
        };
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        nonNull(p);
        Point2D champion = null;
        double best = Double.NEGATIVE_INFINITY;
        double current = Double.NEGATIVE_INFINITY;
        for (Point2D next : set) {
            current = next.distanceSquaredTo(p);
            if (best == Double.NEGATIVE_INFINITY || current < best) {
                champion = next;
                best = current;
            }
        }
        return champion;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        PointSET pset1 = new PointSET();
        for (var i = 0; i < 50; i += 1) {
            double x = StdRandom.uniform(0, 1.0);
            double y = StdRandom.uniform(0, 1.0);
            pset1.insert(new Point2D(x, y));
        }
        pset1.draw();

        Point2D pset1Nearest1Query = new Point2D(0.5, 0.75);
        Point2D pset1Nearest1Result = pset1.nearest(pset1Nearest1Query);
        StdOut.println("\tnearest\t[" + pset1Nearest1Query + "]\t" + pset1Nearest1Result);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.setPenRadius(0.001);
        StdDraw.circle(pset1Nearest1Query.x(), pset1Nearest1Query.y(), pset1Nearest1Query.distanceTo(pset1Nearest1Result));
        StdDraw.setPenRadius(0.01);
        StdDraw.point(pset1Nearest1Query.x(), pset1Nearest1Query.y());
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.point(pset1Nearest1Result.x(), pset1Nearest1Result.y());
        StdDraw.setPenColor();
//        StdOut.println("\trange\t[( 1.0, 2.0 ), (5.0, 2.0)]\t");
        for (Point2D p : pset1.range(new RectHV(0.2, 0.5, 0.3, 0.6))) {
            StdOut.println("\t\t" + p);
        }

        StdDraw.show();
    }
}