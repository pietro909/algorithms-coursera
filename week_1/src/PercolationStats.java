import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final int times;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("both n and trials must be > 0");
        }
        thresholds = new double[trials];
        times = trials;

        for (var t = 0; t < trials; t++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            double threshold = ((double) percolation.numberOfOpenSites()) / (((double) n) * ((double) n));
            thresholds[t] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(times));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(times));
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(20, 10);

        StdOut.println("mean\t\t= " + stats.mean());
        StdOut.println("stddev\t\t= " + stats.stddev());
        StdOut.println("95% confidence interval\t= [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}