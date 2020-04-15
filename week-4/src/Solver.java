import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Solver {
    private final LinkedList<Board> solution = new LinkedList<Board>();
    private MinPQ<SearchNode> queue = new MinPQ<SearchNode>(1, new Comparator<SearchNode>() {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return (o1.moves + o1.board.manhattan()) - (o2.moves + o2.board.manhattan());
        }
    });

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

//        StdOut.println("init solver");

        int moves = 0;
        SearchNode node = new SearchNode(initial, moves, null);
        queue.insert(node);

        while (!node.board.isGoal()) {
            node = queue.delMin();
            Iterator<Board> neighbors = node.board.neighbors().iterator();
//            StdOut.println(" --> node " + node.board);
            if (node.board.isGoal()) {
//                StdOut.println(" --> GOAL");
                break;
            }

            moves++;

//            StdOut.println(" --> move " + moves);

            while (neighbors.hasNext()) {
                Board neighbor = neighbors.next();
//                StdOut.println(" --> queue " + neighbor);
                queue.insert(new SearchNode(neighbor, moves, node));
            }
        }

//        StdOut.println("done solver");

        while (node != null) {
//            if (!node.board.isGoal()) {
                solution.addFirst(node.board);
//            }
            node = node.previous;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solution.size()-1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] three1 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};

        Solver s1 = new Solver(new Board(goal));
        StdOut.println("== goal ==");
        StdOut.println(" - [0] moves:\t" + s1.moves());
        s1.solution().forEach(new Consumer<Board>() {
            @Override
            public void accept(Board board) {
                StdOut.println(board);
            }
        });

        Solver s2 = new Solver(new Board(three1));
        StdOut.println("== goal ==");
        StdOut.println(" - [4] moves:\t" + s2.moves());
        s2.solution().forEach(new Consumer<Board>() {
            @Override
            public void accept(Board board) {
                StdOut.println(board);
            }
        });
    }

}