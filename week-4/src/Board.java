import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private final int[] board;
    private final int size;
    private int indexOfZero;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size * size];

        int counter = 0;
        for (var row = 0; row < size; row++) {
            for (var col = 0; col < size; col++) {
                if (tiles[row][col] == 0) indexOfZero = counter;
                board[counter] = tiles[row][col];
                counter++;
            }
        }
    }

    private int[][] toArray() {
        int[][] result = new int[size][size];
        int counter = 0;
        for (var row = 0; row < size; row++) {
            for (var col = 0; col < size; col++)
                result[row][col] = board[counter++];
        }
        return result;
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
        int k = board.length;
        while (k > 0) {
            k--;
            if (board[k] == n) return k;
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
            int currentDistance = Math.abs(toRow(indexGoal) - toRow(i)) + Math.abs(toCol(indexGoal) - toCol(i));
//            StdOut.println("\tmanhattan of " + current + " = " + currentDistance);
            distance += currentDistance;
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0 && manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass().equals(this.getClass())) {
            Board that = (Board) y;
            if (that.board.length != this.board.length) return false;
            for (int i = 0; i < that.board.length; i++) {
                if (that.board[i] != this.board[i]) return false;
            }
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> result = new LinkedList<Board>();

        int row = toRow(indexOfZero);
        int col = toCol(indexOfZero);

        if (row > 1) {
            int[][] up = toArray();
            int toMove = up[row - 2][col - 1];
            up[row - 2][col - 1] = 0;
            up[row - 1][col - 1] = toMove;
            result.add(new Board(up));
        }

        if (col % size > 0) {
            int[][] right = toArray();
            int toMove = right[row - 1][col];
            right[row - 1][col] = 0;
            right[row - 1][col - 1] = toMove;
            result.add(new Board(right));
        }

        if (row < size) {
            int[][] bottom = toArray();
            int toMove = bottom[row][col - 1];
            bottom[row][col - 1] = 0;
            bottom[row - 1][col - 1] = toMove;
            result.add(new Board(bottom));
        }

        if (col > 1) {
            int[][] left = toArray();
            int toMove = left[row - 1][col - 2];
            left[row - 1][col - 2] = 0;
            left[row - 1][col - 1] = toMove;
            result.add(new Board(left));
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    // zero is not allowed
    public Board twin() {
        int[][] nextBoard = toArray();
        int[] indexes = StdRandom.permutation(board.length, board.length);

        Integer indexOfA = null;
        for (int i : indexes) {
            if (i == indexOfZero) continue;
            if (indexOfA != null) {
                int indexOfB = i;
                int a = nextBoard[toRow(indexOfA) - 1][toCol(indexOfA) - 1];
                int b = nextBoard[toRow(indexOfB) - 1][toCol(indexOfB) - 1];
                nextBoard[toRow(indexOfA) - 1][toCol(indexOfA) - 1] = b;
                nextBoard[toRow(indexOfB) - 1][toCol(indexOfB) - 1] = a;
                break;
            } else {
                indexOfA = i;
            }
        }

        return new Board(nextBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] three1 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] three2 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] puzzle04 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] twoByTwo0 = {{1, 2}, {3, 0}};

        int[][] tenByTen = {
                {
                        1, 2, 3, 4, 5, 6, 7, 8, 9
                },
                {
                        10, 11, 12, 13, 14, 15, 16, 17, 18
                },
                {
                        19, 20, 21, 22, 23, 24, 25, 26, 27
                },
                {
                        28, 29, 30, 31, 32, 33, 34, 35, 36
                },
                {
                        37, 38, 39, 40, 41, 42, 43, 44, 45
                },
                {
                        46, 47, 48, 49, 50, 51, 52, 53, 54
                },
                {
                        55, 56, 57, 58, 59, 60, 61, 62, 63
                },
                {
                        64, 0, 65, 67, 68, 78, 69, 70, 72
                },
                {
                        73, 74, 66, 75, 76, 77, 79, 71, 80
                }
        };

        StdOut.println("goal board");
        Board b1 = new Board(goal);
        StdOut.println("\ttoString()");
        StdOut.println(b1.toString());
        StdOut.println("\tHamming distance (0):\t" + b1.hamming());
        StdOut.println("\tManhattan distance (0):\t" + b1.manhattan());
        StdOut.println("\tis goal? (T):\t" + b1.isGoal());
        StdOut.println("\tneighbors (0):");
        var it1 = b1.neighbors().iterator();
        while (it1.hasNext())
            StdOut.println(it1.next());
        StdOut.println("\ttwin:");
        StdOut.println(b1.twin());
        StdOut.println("=============================================");

        StdOut.println("three board");
        Board b2 = new Board(three1);
        StdOut.println("\ttoString()");
        StdOut.println(b2.toString());
        StdOut.println("\tHamming distance (5):\t" + b2.hamming());
        StdOut.println("\tManhattan distance (10):\t" + b2.manhattan());
        StdOut.println("\tis goal? (F):\t" + b2.isGoal());
        StdOut.println("\tneighbors (4):");
        var it2 = b2.neighbors().iterator();
        while (it2.hasNext())
            StdOut.println(it2.next());
        StdOut.println("\ttwin:");
        StdOut.println(b2.twin());
        StdOut.println("=============================================");

        StdOut.println("three2 board");
        Board b3 = new Board(three2);
        StdOut.println("\ttoString()");
        StdOut.println(b3.toString());
        StdOut.println("\tHamming distance (3):\t" + b3.hamming());
        StdOut.println("\tManhattan distance (3):\t" + b3.manhattan());
        StdOut.println("\tis goal? (F):\t" + b3.isGoal());
        StdOut.println("\tneighbors (3):");
        var it3 = b3.neighbors().iterator();
        while (it3.hasNext())
            StdOut.println(it3.next());
        StdOut.println("\ttwin:");
        StdOut.println(b3.twin());
        StdOut.println("=============================================");

        StdOut.println("zero4 board");
        Board b4 = new Board(puzzle04);
        StdOut.println("\ttoString()");
        StdOut.println(b4.toString());
        StdOut.println("\tHamming distance (3):\t" + b4.hamming());
        StdOut.println("\tManhattan distance (3):\t" + b4.manhattan());
        StdOut.println("\tis goal? (F):\t" + b4.isGoal());
        StdOut.println("\tneighbors (3):");
        var it4 = b4.neighbors().iterator();
        while (it4.hasNext())
            StdOut.println(it4.next());
        StdOut.println("\ttwin:");
        StdOut.println(b4.twin());
        StdOut.println("=============================================");

        StdOut.println("twoByTwo0 board");
        Board b5 = new Board(twoByTwo0);
        StdOut.println("\ttoString()");
        StdOut.println(b5.toString());
        StdOut.println("\tHamming distance (3):\t" + b5.hamming());
        StdOut.println("\tManhattan distance (3):\t" + b5.manhattan());
        StdOut.println("\tis goal? (F):\t" + b5.isGoal());
        StdOut.println("\tneighbors (3):");
        var it5 = b5.neighbors().iterator();
        while (it5.hasNext())
            StdOut.println(it5.next());
        StdOut.println("\ttwin:");
        StdOut.println(b5.twin());
        StdOut.println(b5.twin());
        StdOut.println(b5.twin());
        StdOut.println("=============================================");

        StdOut.println("tenByTen board");
        Board b6 = new Board(tenByTen);
        StdOut.println("\ttoString()");
        StdOut.println(b6.toString());
        StdOut.println("=============================================");

        StdOut.println("equals");
        StdOut.println("same board\t\t\t[true]\t" + b1.equals(b1));
        StdOut.println("object\t\t\t\t[false]\t" + b1.equals(new Object()));
        StdOut.println("different board\t\t[false]\t" + b1.equals(b2));
        StdOut.println("board and twin\t\t[false]\t" + b1.equals(b1.twin()));
        StdOut.println("consistent twins\t[true]\t" + b6.twin().equals(b6.twin()));
        StdOut.println("equal board\t\t\t[true]\t" + b5.equals(new Board(twoByTwo0)));
    }

}