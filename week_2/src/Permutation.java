import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue randomizedQueue = new RandomizedQueue<String>();

        int k = StdIn.readInt();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            randomizedQueue.enqueue(item);
        }

        Iterator<RandomizedQueue> iterator = randomizedQueue.iterator();
        while (iterator.hasNext() && k > 0) {
            StdOut.println(iterator.next());
            k--;
        }
    }
}