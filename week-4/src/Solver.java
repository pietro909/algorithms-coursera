import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;

public class Solver {
    private MinPQ<SearchNode> queue = new MinPQ<SearchNode>();

    //the initial board, 0 moves, and a null previous search node
    private class SearchNode {
        final Board board;
        final int moves;
        final SearchNode previous;

        public SearchNode(Board _b, int _m, SearchNode _p) {
            board = _b;
            moves = _m;
            previous = _p;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        SearchNode node = new SearchNode(initial, initial.manhattan(), null);
        queue.insert(node);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return 0;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new LinkedList<Board>();
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}