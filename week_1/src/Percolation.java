import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// run with java -classpath /Users/pg7/workspace/algorithms-coursera/week_1/out/production/week_1:/Users/pg7/workspace/algorithms-coursera/algs4.jar Percolation

/*
By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site
 */
public class Percolation {
    private WeightedQuickUnionUF wquUF;
    private int[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be > 0");
        }
        grid = new int[size][size];
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                grid[i][j] = 0;
            }
        }
        wquUF = new WeightedQuickUnionUF(size * size);
    }

    // @throws IllegalArgumentException
    private void assertBoundaries(int index) {
        int size = grid.length;
        if (index < 1 || index > grid.length) {
            throw new IllegalArgumentException("index " + index + " is not between 1 and " + size);
        }
    }

    private int toIndex(int row, int col) {
        return (grid.length * (row - 1)) + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        assertBoundaries(row);
        assertBoundaries(col);
        int size = grid.length;
        grid[row - 1][col - 1] = 1;

        // west
        if (col > 1 && isOpen(row, col - 1)) wquUF.union(toIndex(row, col), toIndex(row, col - 1));

        // north
        if (row > 1 && isOpen(row - 1, col)) wquUF.union(toIndex(row, col), toIndex(row - 1, col));

        // east
        if (col < size && isOpen(row, col + 1)) wquUF.union(toIndex(row, col), toIndex(row, col + 1));

        // south
        if (row < size && isOpen(row + 1, col)) wquUF.union(toIndex(row, col), toIndex(row + 1, col));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        assertBoundaries(row);
        assertBoundaries(col);
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        assertBoundaries(row);
        assertBoundaries(col);
        return grid[row - 1][col - 1] == 0;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int open = 0;
        int size = grid.length;
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                if (grid[i][j] == 1) {
                    open++;
                }
            }
        }
        return open;
    }

    // does the system percolate?
    public boolean percolates() {
        // the last row has at least one root in common with first row?
        int size = grid.length;
        for (var row = 1; row <= size; row++) {
            for (var column = 1; column <= size; column++) {
                int rootTop = toIndex(1, column);
                int rootBottom = toIndex(size, row);
                if (wquUF.connected(rootTop, rootBottom)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String print() {
        int size = grid.length;
        StringBuilder output = new StringBuilder("---\n");
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                output.append(grid[i][j]).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    // test client (optional)
    public static void main(String[] args) {
        int size = 3;

        Percolation percolation = new Percolation(size);

        percolation.open(2, 3);
        StdOut.println(percolation.print());
        StdOut.println("is 1,1 open? " + percolation.isOpen(1, 1));
        StdOut.println("is 2,3 open? " + percolation.isOpen(2, 3));

        StdOut.println(percolation.percolates() + " = false");
        percolation.open(1, 3);
        percolation.open(3, 3);
        StdOut.println(percolation.print());
        StdOut.println("total: " + percolation.numberOfOpenSites());
        StdOut.println(percolation.percolates() + " = true");
    }
}
