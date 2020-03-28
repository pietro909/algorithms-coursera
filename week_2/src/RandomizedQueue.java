import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] list = (Item[]) new Object[1];
    private int current = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return current == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return current;
    }

    private void resize(int newSize) {
        Item[] temp = (Item[]) new Object[newSize];
        for (int i = 0; i < current; i++) {
            temp[i] = list[i];
        }
        list = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
        if (current == list.length) resize(list.length * 2);
        list[current] = item;
        current++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("empty queue!");

        int index = StdRandom.uniform(0, current - 1);
        Item item = list[index];

        if (current == 1) {
            // only one item
            list[index] = null;
        } else {
            // swap with current, to avoid holes
            list[index] = list[current];
            int max = current - 1;
            Item lastItem = null;
            while (lastItem == null) {
                lastItem = list[max];
                StdOut.println("\t\tmax=" + max + " - lastItem="+lastItem);

                max--;
            }
            StdOut.println(" replacing " + index + " with " + lastItem + " from " + max);
            StdOut.println(" ---> return " + item);
            list[index] = lastItem;
            list[max+1] = null;

            if (current == list.length / 4) resize(list.length / 2);
        }
        current--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("empty queue!");
        int index = StdRandom.uniform(0, current - 1);
        return list[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    //  Each iterator must return the items in uniformly random order.
    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] order = StdRandom.permutation(current, current);
        int now = 0;

        public boolean hasNext() {
//            StdOut.println(" - now: " + now + "\t" + ";\torder: " + Arrays.toString(order));
            return now < order.length; // && list[order[now]] != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = list[order[now]];
            now++;
            return item;
        }
    }

    private String print() {
//        Iterator<Item> iterator = iterator();
//        StringBuilder output = new StringBuilder("[ ");
//        while (iterator.hasNext()) {
//            output.append(iterator.next() + " ");
//        }
//        output.append("]");
//        return output.toString();
        return Arrays.toString(list);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();


        randomizedQueue.enqueue(462);
        randomizedQueue.enqueue(366);
        randomizedQueue.enqueue(72);
        StdOut.println("[3]\t" + randomizedQueue.size());
        randomizedQueue.enqueue(390);
        randomizedQueue.enqueue(256);
        StdOut.println(randomizedQueue.print());

        StdOut.println(randomizedQueue.dequeue());
        StdOut.println(randomizedQueue.print());

        StdOut.println(randomizedQueue.dequeue());


//        StdOut.println("[true]\t" + randomizedQueue.isEmpty());
//        StdOut.println("[0]\t\t" + randomizedQueue.size());
//        StdOut.println("[]\t\t" + randomizedQueue.print());
//
//        randomizedQueue.enqueue("first");
//        StdOut.println(randomizedQueue.print());
//        randomizedQueue.enqueue("second");
//        StdOut.println(randomizedQueue.print());
//        randomizedQueue.enqueue("third");
//        StdOut.println(randomizedQueue.print());
//
//        StdOut.println("[false]\t" + randomizedQueue.isEmpty());
//        StdOut.println("[3]\t\t" + randomizedQueue.size());
//
//        StdOut.println(randomizedQueue.dequeue());
//
//        StdOut.println("[false]\t" + randomizedQueue.isEmpty());
//        StdOut.println("[2]\t\t" + randomizedQueue.size());
//
//        randomizedQueue.enqueue("fourth");
//        randomizedQueue.enqueue("fifth");
//        randomizedQueue.enqueue("sixth");
//
//        StdOut.println("[5]\t\t" + randomizedQueue.size());
//
//        StdOut.println("---- iterator ----");
//        StdOut.println(randomizedQueue.print());
//        StdOut.println("---- iterator ----");
//        StdOut.println(randomizedQueue.print());
//        StdOut.println("---- iterator ----");
//        StdOut.println(randomizedQueue.print());
    }

}