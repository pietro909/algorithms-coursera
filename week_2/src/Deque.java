import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        final Item item;
        Node next;
        Node prev;

        public Node(Item i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    // construct an empty deque
    public Deque() {
        // is already empty :-)
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");

        Node newFirst = new Node(item, null, null);
        if (first == null) {
            first = newFirst;
            last = newFirst;
        } else {
            // chain was formed already
            Node oldFirst = first;
            oldFirst.prev = newFirst;
            first = newFirst;
            first.next = oldFirst;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
        if (isEmpty()) {
            addFirst(item);
            return;
        }

        Node newLast = new Node(item, null, null);
        size++;

        Node oldLast = last;
        oldLast.next = newLast;
        last = newLast;
        last.prev = oldLast;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        Item item = first.item;
        first = first.next;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        if (size == 1) return removeFirst();

        size--;

        Item item = last.item;
        Node nextLast = last.prev;
        nextLast.next = null;
        last = nextLast;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    private String print() {
        Iterator<Item> iterator = iterator();
        StringBuilder output = new StringBuilder("[ ");
        while (iterator.hasNext()) {
            output.append(iterator.next() + " ");
        }
        output.append("]");
        return output.toString();
    }

    // unit testing (required)
    public static void main(String[] args) {
//        Deque<String> deque = new Deque<String>();

//        StdOut.println("[true]\t" + deque.isEmpty());
//        StdOut.println("[0]\t\t" + deque.size());
//        deque.addFirst("third");
//        deque.addFirst("second");
//        deque.addFirst("first");
//
//        StdOut.println("[false]\t" + deque.isEmpty());
//        StdOut.println("[3]\t\t" + deque.size());
//
//        deque.addLast("fourth");
//        deque.addLast("fifth");
//        deque.addLast("sixth");
//
//        StdOut.println("[false]\t" + deque.isEmpty());
//        StdOut.println("[6]\t\t" + deque.size());
//
//        StdOut.println(deque.print());
//
//        StdOut.println("[first]\t" + deque.removeFirst());
//        StdOut.println("[sixth]\t" + deque.removeLast());
//
//        StdOut.println("[false]\t" + deque.isEmpty());
//        StdOut.println("[4]\t\t" + deque.size());

        // From Coursera
        Deque<Integer> deque = new Deque<Integer>();

        StdOut.println("[0]\tsize\t\t" + deque.size());
        deque.addFirst(1);
//        deque.addFirst(2);
        StdOut.println("\t\t" + deque.print());

        StdOut.println("[1]\tremoveLast\t" + deque.removeLast());

//        deque.addFirst(5);
//        deque.addFirst(6);
//
//        StdOut.println("\t\t" + deque.print());
//        StdOut.println("[3]\tsize\t\t" + deque.size());
//        StdOut.println("[2]\tremoveLast\t" + deque.removeLast());

//        StdOut.println("[2]\t\t" + deque.size());
    }

}
