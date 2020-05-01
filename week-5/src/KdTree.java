import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import static java.lang.Thread.sleep;

public class KdTree {
    private Node root = null;

    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;

    private class Node {
        Point2D point;
        int size = 1;
        Node left = null, right = null;

        Node(Point2D point) {
            this.point = point;
        }
    }

    private boolean isVertical(int level) {
        return level % 2 > 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    private void nonNull(Object a) {
        if (a == null)
            throw new IllegalArgumentException("can't be null");
    }

    public void insert(Point2D point)              // add the point to the set (if it is not already in the set)
    {
        nonNull(point);
        root = put(root, point, null, 1);
    }

    private Node put(Node destination, Point2D point, Node parent, int level) {
        // empty tree
        if (destination == null) {
            return new Node(point);
        }

        if (destination.point.equals(point)) return destination;

        if (isVertical(level)) {
            // Key = x
            // Val = y
            int compare = Double.compare(point.x(), destination.point.x());
            if (compare < 0) destination.left = put(destination.left, point, destination, level + 1);
                // same x, got to the right tree
            else destination.right = put(destination.right, point, destination, level + 1);
        } else {
            // Key = y
            // Val = x
            int compare = Double.compare(point.y(), destination.point.y());
            if (compare < 0) destination.left = put(destination.left, point, destination, level + 1);
            else destination.right = put(destination.right, point, destination, level + 1);
        }

        destination.size = 1 + size(destination.left) + size(destination.right);

        return destination;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        nonNull(p);
        return contains(root, p, 1);
    }

    private boolean contains(Node node, Point2D point, int level)            // does the set contain point p?
    {
        nonNull(point);
        if (node == null) return false;

        if (node.point.compareTo(point) == 0) return true;
        if (isVertical(level)) {
            int compare = Double.compare(point.x(), node.point.x());
            if (compare < 0) return contains(node.left, point, level + 1);
            return contains(node.right, point, level + 1);
        } else {
            int compare = Double.compare(point.y(), node.point.y());
            if (compare < 0) return contains(node.left, point, level + 1);
            return contains(node.right, point, level + 1);
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root, 1, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, int level, RectHV box) {
        if (node == null) return;
        double x = node.point.x();
        double y = node.point.y();

        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.008);
        StdDraw.point(x, y);
        StdDraw.text(x, y, "(" + x + " " + y + ")");
//        StdDraw.setPenRadius(0.001);
//        StdDraw.line(box.xmin(), box.ymin(), box.xmax(), box.ymin());
//        StdDraw.line(box.xmax(), box.ymin(), box.xmax(), box.ymax());
//        StdDraw.line(box.xmax(), box.ymax(), box.xmin(), box.ymax());
//        StdDraw.line(box.xmin(), box.ymax(), box.xmin(), box.ymin());

        StdDraw.setPenRadius(0.002);
        if (isVertical(level)) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, box.ymin(), x, box.ymax());
            // this is  left/right
            draw(node.left, level + 1, new RectHV(box.xmin(), box.ymin(), node.point.x(), box.ymax()));
            draw(node.right, level + 1, new RectHV(node.point.x(), box.ymin(), box.xmax(), box.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(box.xmin(), y, box.xmax(), y);
            // this is bottom/top
            draw(node.left, level + 1, new RectHV(box.xmin(), box.ymin(), box.xmax(), node.point.y()));
            draw(node.right, level + 1, new RectHV(box.xmin(), node.point.y(), box.xmax(), box.ymax()));
        }
    }


    // it should support nearest() and range() in time proportional to the number of points in the set.

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        nonNull(rect);
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, queue, rect, 1);
        return queue;
    }

    private void range(Node node, Queue<Point2D> queue, RectHV rect, int level) {
        if (node == null) return;

//        StdOut.println("--> " + node.point + " ? " + rect.contains(node.point));

        if (rect.contains(node.point)) {
            range(node.left, queue, rect, level + 1);
            queue.enqueue(node.point);
            range(node.right, queue, rect, level + 1);
        } else {
            if (isVertical(level)) {
                if (Double.compare(rect.xmax(), node.point.x()) < 0) range(node.left, queue, rect, level + 1);
                else range(node.right, queue, rect, level + 1);
            } else {
                if (Double.compare(rect.ymax(), node.point.y()) < 0) range(node.right, queue, rect, level + 1);
                else range(node.right, queue, rect, level + 1);
            }
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        nonNull(p);
        if (isEmpty()) return null;

        return null;
    }

    public static void main(String[] args) throws InterruptedException                  // unit testing of the methods (optional)
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        KdTree pset1 = new KdTree();
//        for (var i = 0; i < 20; i += 1) {
//            double x = StdRandom.uniform(0, 1.0);
//            double y = StdRandom.uniform(0, 1.0);
//            pset1.insert(new Point2D(x, y));
//        }
//        pset1.insert(new Point2D(0.7, 0.2));
//        pset1.insert(new Point2D(0.5, 0.4));
//        pset1.insert(new Point2D(0.2, 0.3));
//        pset1.insert(new Point2D(0.4, 0.7));
//        pset1.insert(new Point2D(0.9, 0.6));
//        pset1.insert(new Point2D(0.9, 0.6));

        int t = 500;

        Point2D[] points = {
                new Point2D(0.7, 0.4),
                new Point2D(0.3, 0.2),
                new Point2D(0.8, 0.5),
                new Point2D(0.52, 0.25),
                new Point2D(0.80, 0.15),
                new Point2D(0.6, 0.75),
                new Point2D(0.45, 0.90),
                new Point2D(0.75, 0.30),
                new Point2D(0.25, 0.70),
                new Point2D(0.50, 0.80),
                new Point2D(0.38, 0.38)
        };

        for (int i = 0; i < points.length; i++) {
            pset1.insert(points[i]);
            pset1.draw();
            StdDraw.show();
            sleep(t);
        }


//        Point2D pset1Nearest1Query = new Point2D(0.5, 0.75);
//        Point2D pset1Nearest1Result = pset1.nearest(pset1Nearest1Query);
//        StdOut.println("\tnearest\t[" + pset1Nearest1Query + "]\t" + pset1Nearest1Result);
//        StdDraw.setPenColor(Color.CYAN);
//        StdDraw.setPenRadius(0.001);
//        StdDraw.circle(pset1Nearest1Query.x(), pset1Nearest1Query.y(), pset1Nearest1Query.distanceTo(pset1Nearest1Result));
//        StdDraw.setPenRadius(0.01);
//        StdDraw.point(pset1Nearest1Query.x(), pset1Nearest1Query.y());
//        StdDraw.setPenColor(Color.ORANGE);
//        StdDraw.point(pset1Nearest1Result.x(), pset1Nearest1Result.y());
//        StdDraw.setPenColor();
        StdOut.println("\tsize\t[5]\t" + pset1.size());

        StdOut.println("\trange\t[(0.0, 0.0 ), (1.0, 1.0)]");
        for (Point2D p : pset1.range(new RectHV(0, 0, 1, 1))) {
            StdOut.println("\t\t" + p);
        }

//        RectHV range = new RectHV(0.42, 0.64, 0.86, 0.93);
//        StdDraw.setPenColor(StdDraw.CYAN);
//        StdDraw.setPenRadius(0.004);
//        StdDraw.rectangle(
//                range.xmin() + ((range.xmax() - range.xmin()) / 2),
//                range.ymin() + ((range.ymax() - range.ymin()) / 2),
//                range.width() / 2,
//                range.height() / 2
//        );
//        StdDraw.setPenColor(StdDraw.ORANGE);
//        for (Point2D p : pset1.range(range)) {
//            StdOut.println("\t\t" + p);
//            StdDraw.circle(p.x(), p.y(), 0.1);
//        }
        StdDraw.show();
    }
}