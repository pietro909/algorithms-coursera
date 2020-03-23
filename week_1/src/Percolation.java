import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// run with java -classpath /Users/pg7/workspace/algorithms-coursera/week_1/out/production/week_1:/Users/pg7/workspace/algorithms-coursera/algs4.jar Percolation

/*
By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site
 */
public class Percolation {
    private final WeightedQuickUnionUF wquUF;
    // this is to avoid backwash
    private final WeightedQuickUnionUF wquUFNoBottom;
    private final boolean[][] grid;
    // index of the set of all the top rows
    private final int topVirtualIndex;
    // index of the set of all the bottom rows
    private final int bottomVirtualIndex;
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be > 0");
        }
        grid = new boolean[size][size];
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                grid[i][j] = false;
            }
        }
        wquUF = new WeightedQuickUnionUF(size * size + 2);
        wquUFNoBottom = new WeightedQuickUnionUF(size * size + 1);
        topVirtualIndex = size * size;
        bottomVirtualIndex = size * size + 1;
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

        // already open
        if (grid[row - 1][col - 1]) {
            return;
        }

        int size = grid.length;
        grid[row - 1][col - 1] = true;
        openSites += 1;
        int site = toIndex(row, col);

        if (row == 1) {
            // if in the top row, connect to the virtual site in the top
            wquUF.union(site, topVirtualIndex);
            wquUFNoBottom.union(site, topVirtualIndex);
        }
        if (row == size) {
            // if in the bottom row, connect to the virtual site in the bottom
            wquUF.union(site, bottomVirtualIndex);
        }

        // west
        if (col > 1 && isOpen(row, col - 1)) {
            wquUF.union(site, toIndex(row, col - 1));
            wquUFNoBottom.union(site, toIndex(row, col - 1));
        }

        // north
        if (row > 1 && isOpen(row - 1, col)) {
            wquUF.union(site, toIndex(row - 1, col));
            wquUFNoBottom.union(site, toIndex(row - 1, col));
        }

        // east
        if (col < size && isOpen(row, col + 1)) {
            wquUF.union(site, toIndex(row, col + 1));
            wquUFNoBottom.union(site, toIndex(row, col + 1));
        }

        // south
        if (row < size && isOpen(row + 1, col)) {
            wquUF.union(site, toIndex(row + 1, col));
            wquUFNoBottom.union(site, toIndex(row + 1, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        assertBoundaries(row);
        assertBoundaries(col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    /*
    A full site refers to an open site that is connected to another open site in the top row.
    You can check if a site is full by using a virtual site that is connected to every site in the top row.
     */
    public boolean isFull(int row, int col) {
        assertBoundaries(row);
        assertBoundaries(col);

        // not open? can't be full
        if (!grid[row-1][col-1]) return false;

        return wquUFNoBottom.connected(toIndex(row, col), topVirtualIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    /*
     * does the system percolate?
     *
     * As was said in the lecture,
     * we should use two virtual nodes at the top and the bottom of the grid connected to every node in the top and
     * the bottom rows respectively.
     *  Then we can just check if the top and the bottom nodes are connected.
     *
     */
    public boolean percolates() {
        if (grid.length == 1) {
            return grid[0][0];
        }
        return  wquUF.connected(bottomVirtualIndex, topVirtualIndex);
    }

    private String print() {
        int size = grid.length;
        StringBuilder output = new StringBuilder("--------------------------\n");
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                output.append(grid[i][j]).append(" ");
            }
            output.append("\n");
        }
        output.append("--------------------------");
        return output.toString();
    }

    // test client (optional)
    public static void main(String[] args) {
        int size = 3;

        Percolation percolation = new Percolation(size);

        percolation.open(2, 3);
        StdOut.println("[false]\tis 1,1 open?\t\t" + percolation.isOpen(1, 1));
        StdOut.println("[true]\tis 2,3 open?\t\t" + percolation.isOpen(2, 3));
        StdOut.println("[false]\tis 1,1 full?\t\t" + percolation.isFull(1, 1));
        StdOut.println("[false]\tis 2,3 full?\t\t" + percolation.isFull(2, 3));
        percolation.open(2, 3);
        percolation.open(2, 3);

        StdOut.println("[false]\tpercolates?\t\t\t" + percolation.percolates());
        StdOut.println("[1]\t\tnumberOfOpenSites:\t" + percolation.numberOfOpenSites());

        percolation.open(1, 3);
        percolation.open(3, 3);
        StdOut.println("[true]\tis 2,3 full?\t\t" + percolation.isFull(2, 3));
        StdOut.println("[3]\t\tnumberOfOpenSites:\t" + percolation.numberOfOpenSites());
        StdOut.println("[true]\tpercolates?\t\t\t" + percolation.percolates());
        percolation.open(3, 1);
        StdOut.println("[false]\tis 3,1 full?\t\t" + percolation.isFull(3, 1));

        StdOut.println(percolation.print());

    }
}
