import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Solver {
    private final LinkedList<Board> solution = new LinkedList<Board>();


    //the initial board, 0 moves, and a null previous search node
    private class SearchNode {
        final Board board;
        final int moves;
        final int manhattan;
        final SearchNode previous;
        final boolean isGoal;

        public SearchNode(Board b, int mo, SearchNode p, int m, boolean goal) {
            board = b;
            moves = mo;
            previous = p;
            manhattan = m;
            isGoal = goal;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        if (initial.isGoal()) {
            solution.addFirst(initial);
            return;
        }

        Comparator<SearchNode> comparator = new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                return (o1.moves + o1.manhattan) - (o2.moves + o2.manhattan);
            }
        };

        MinPQ<SearchNode> mainQueue = new MinPQ<SearchNode>(1, comparator);
        MinPQ<SearchNode> controlQueue = new MinPQ<SearchNode>(1, comparator);

        mainQueue.insert(new SearchNode(initial, 0, null, initial.manhattan(), initial.isGoal()));
        controlQueue.insert(new SearchNode(initial.twin(), 0, null, initial.twin().manhattan(), initial.isGoal()));

        while (true) {
            SearchNode main = solveNext(mainQueue);
            if (main != null) {
//                StdOut.println("== solved");
                packSolution(mainQueue, main);
                break;
            }
            SearchNode control = solveNext(controlQueue);
            if (control != null) {
                break;
            }
        }
    }

    private SearchNode solveNext(MinPQ<SearchNode> queue) {
        SearchNode node = queue.delMin();
        if (node.isGoal) {
            return node;
        }
        Iterator<Board> neighbors = node.board.neighbors().iterator();

        while (neighbors.hasNext()) {
            Board neighbor = neighbors.next();
            if (node.previous == null || !neighbor.equals(node.previous.board)) {
                queue.insert(new SearchNode(neighbor, node.moves + 1, node, neighbor.manhattan(), neighbor.isGoal()));
            }
        }
        return null;
    }

    // side effects: updates the result
    private void packSolution(MinPQ<SearchNode> queue, SearchNode node) {
//        solution.addFirst(start.board);
        if (queue.isEmpty()) return;

//        SearchNode node = queue.delMin();
        while (node != null) {
//            StdOut.println(" - packing " + node.moves);
//            StdOut.println(" -- " + node.board);
            solution.addFirst(node.board);
            node = node.previous;
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return !solution.isEmpty();
    }

    // min number of moves to solve initial board
    public int moves() {
        int moves = solution.size();
//        if (moves == 0) return moves;
        return moves - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (solution.isEmpty()) return null;
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] three1 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] unsolvable = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
        int[][] unsolvable2 = {{1, 0}, {2,3}};

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
        StdOut.println("== three ==");
        StdOut.println(" - [4] moves:\t" + s2.moves());
        s2.solution().forEach(new Consumer<Board>() {
            @Override
            public void accept(Board board) {
                StdOut.println(board);
            }
        });

        Solver s3 = new Solver(new Board(unsolvable));
        StdOut.println("== unsolvable ==");
        StdOut.println(" - [false] isSolvable:\t" + s3.isSolvable());
        StdOut.println(" - [0] moves:\t\t" + s3.moves());

        Solver s4 = new Solver(new Board(unsolvable));
        StdOut.println("== unsolvable ==");
        StdOut.println(" - [false] isSolvable:\t" + s4.isSolvable());
        StdOut.println(" - [0] moves:\t\t" + s4.moves());
    }

}