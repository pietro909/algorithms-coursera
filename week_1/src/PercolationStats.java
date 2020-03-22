import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        Percolation percolation = new Percolation(n);

        while(!percolation.percolates()) {
            int row = StdRandom.uniform(1, n+1);
            int col = StdRandom.uniform(1, n+1);
            percolation.open(row, col);
        }

        StdOut.println(percolation.print());
        StdOut.println("percolated with " + percolation.numberOfOpenSites());
    }

    // sample mean of percolation threshold
    public double mean() {
        return 0.42;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return 0.42;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return 0.42;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return 0.42;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(6, 2);

    }
}