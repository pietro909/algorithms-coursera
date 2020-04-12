import edu.princeton.cs.algs4.StdOut;

public class Board {
    int[] board;
    int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size * size];

        int counter = 0;
        for (var row = 0; row < size; row++) {
            for (var col = 0; col < size; col++)
                board[counter++] = tiles[row][col];
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size);
        for (var r = 0; r < board.length; r += size) {
            sb.append("\n");
            for (var c = 0; c < size; c++)
                sb.append(board[r + c]).append(" ");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // convert index to row
    private int toRow(int i) {
        int index = i + 1;
        boolean isRightEnd = index % size == 0;
        int row = (index / size);
        if (isRightEnd) return row;
        return row + 1;
    }

    // convert index to column
    private int toCol(int i) {
        int index = i + 1;
        int remainder = index % size;
        if (remainder == 0) return size;
        return remainder;
    }

    private int goalAt(int i) {
        int index = i + 1;
        return index == board.length ? 0 : index;
    }

    private int indexAsGoal(int goal) {
        if (goal == 0) return board.length - 1;
        return goal - 1;
    }

    // number of tiles out of place
    public int hamming() {
        int offItems = 0;
        for (var i = 0; i < board.length; i++) {
            int current = board[i];
            if (current == 0) continue;
            if (current != goalAt(i)) offItems++;
        }
        return offItems;
    }

    private int indexOf(int n) {
        int l = board.length;
        while (--l > -1) {
            if (board[l] == n) return l;
        }
        return -1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (var i = 0; i < board.length; i++) {
            int current = board[i];
            int expected = goalAt(i);

            if (current == 0) continue;
            if (current == expected) continue;

            int indexGoal = indexAsGoal(current);
            int row = toRow(i);
            int col = toCol(i);
            int goalRow = toRow(indexGoal);
            int goalCol = toCol(indexGoal);

//            StdOut.println("\t\t[" + i + "] = " + current + " should be " + expected + " at (" + row + ", " + col + ")");
//            StdOut.println("\t\t\tgoal has index "+indexGoal+" and is at " + goalRow + ", " + goalCol);
//            StdOut.println("\t\t\tmanhattan " + Math.abs(goalRow - row) + " + " + Math.abs(goalCol - col));

            distance = distance + Math.abs(goalRow - row) + Math.abs(goalCol - col);
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0 && manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        return toString().equals(that.toString());
    }

    // all neighboring boards
//    public Iterable<Board> neighbors()

    // a board that is obtained by exchanging any pair of tiles
//    public Board twin()

    // unit testing (not graded)
    public static void main(String[] args) {

        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] three = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        StdOut.println("goal board");
        Board b1 = new Board(goal);
        StdOut.println("\ttoString()");
        StdOut.println(b1.toString());
        StdOut.println("\tHamming distance (0):\t" + b1.hamming());
        StdOut.println("\tManhattan distance (0):\t" + b1.manhattan());
        StdOut.println("\tis goal? (T):\t" + b1.isGoal());

        StdOut.println("three board");
        Board b2 = new Board(three);
        StdOut.println("\ttoString()");
        StdOut.println(b2.toString());
        StdOut.println("\tHamming distance (5):\t" + b2.hamming());
        StdOut.println("\tManhattan distance (10):\t" + b2.manhattan());
        StdOut.println("\tis goal? (F):\t" + b2.isGoal());
    }
}