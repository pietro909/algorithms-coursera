import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/*
// smaller is E, larger is S (from the book)
    private Node rotateLeft(Node smaller) {
        Node larger = smaller.right;
        smaller.right = larger.left;
        larger.left = smaller;
        larger.color = smaller.color;
        smaller.color = RED;
        larger.size = smaller.size;
        smaller.size = 1 + size(smaller.left) + size(smaller.right);
        return larger;
    }

    private Node rotateRight(Node larger) {
        Node smaller = larger.right;
        larger.left = smaller.right;
        smaller.right = larger;
        smaller.color = larger.color;
        larger.color = RED;
        smaller.size = larger.size;
        larger.size = 1 + size(larger.left) + size(larger.right);
        return smaller;
    }
 */

public class KdTree {
    private Node root = null;

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private class Node {
        Point2D value;
        int size = 0;
        boolean color;
        boolean alignment;
        Node left, right;

        Node(Point2D p, boolean v) {
            value = p;
            alignment = v;
        }
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    public void put(Point2D point) {
        nonNull(point);
        if (root == null) {
            root = new Node(point, VERTICAL);
        } else {
            root = put(point, root);
        }
    }

    private Node put(Point2D point, Node destination) {
        boolean toLeft = destination.alignment == VERTICAL ? point.x() < destination.value.x() : point.y() < destination.value.y();
        if (toLeft) {
            // to the left
            if (destination.left == null) {
                destination.left = new Node(point, !destination.alignment);
            } else {
                destination.left = put(point, destination.left);
            }
        } else {
            // to the right
            if (destination.right == null) {
                destination.right = new Node(point, !destination.alignment);
            } else {
                destination.right = put(point, destination.right);
            }
        }
        return destination;
    }

    // construct an empty set of points
    public void KdTree() {
        // ???
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return root.size;
    }

    private void nonNull(Object a) {
        if (a == null)
            throw new IllegalArgumentException("can't be null");
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        nonNull(p);
        put(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        nonNull(p);
        return false;
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        double x = node.value.x();
        double y = node.value.y();

//        StdDraw.setPenRadius(0.002);
        if (node.alignment == VERTICAL) {
            StdDraw.setPenColor(Color.RED);
//            StdDraw.line(x, 0, x, 1);
        } else {
            StdDraw.setPenColor(Color.BLUE);
//            StdDraw.line(0, y, 0, y);
        }
        StdDraw.setPenRadius(0.008);
        StdDraw.point(x, y);

        draw(node.left);
        draw(node.right);
    }


    // it should support nearest() and range() in time proportional to the number of points in the set.

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        nonNull(rect);
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, queue, rect);
        return queue;
    }

    private void range(Node node, Queue<Point2D> queue, RectHV rect) {
        if (node == null) return;
        if (rect.contains(node.value)) {
            queue.enqueue(node.value);
            range(node.left, queue, rect);
            range(node.right, queue, rect);
        } else {
            if (node.alignment == VERTICAL) {
                if (Double.compare(rect.xmax(), node.value.x()) < 0) range(node.left, queue, rect);
                else range(node.right, queue, rect);
            } else {
                if (Double.compare(rect.ymax(), node.value.y()) < 0) range(node.right, queue, rect);
                else range(node.right, queue, rect);
            }
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        nonNull(p);
        return null;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        KdTree pset1 = new KdTree();
//        for (var i = 0; i < 10; i += 1) {
//            double x = StdRandom.uniform(0, 1.0);
//            double y = StdRandom.uniform(0, 1.0);
//            pset1.insert(new Point2D(x, y));
//        }
        pset1.insert(new Point2D(0.7, 0.2));
        pset1.insert(new Point2D(0.5, 0.4));
        pset1.insert(new Point2D(0.2, 0.3));
        pset1.insert(new Point2D(0.4, 0.7));
        pset1.insert(new Point2D(0.9, 0.6));
        pset1.draw();

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
        StdOut.println("\trange\t[(0.0, 0.0 ), (1.0, 1.0)]");
        for (Point2D p : pset1.range(new RectHV(0, 0, 1, 1))) {
            StdOut.println("\t\t" + p);
        }

        StdDraw.show();
    }
}