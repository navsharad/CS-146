import edu.princeton.cs.algs4.StdRandom;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int trials;
    private Percolation perc;
    private double[] threshold;
    private double mean;
    private double stdDev;

    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();
        perc = new Percolation(N);
        trials = T;
        threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            int numOfOpenSites = 0;
            perc = new Percolation(N);
            while (!perc.percolates()) {
                int temp1 = StdRandom.uniform(0, N);
                int temp2 = StdRandom.uniform(0, N);
                if (!perc.isOpen(temp1, temp2)) {
                    perc.open(temp1, temp2);
                    numOfOpenSites++;
                }
            }
            double num = (double) numOfOpenSites / (N * N);
            threshold[i] = num;
        }

        mean = StdStats.mean(threshold);
        stdDev = StdStats.stddev(threshold);
    }


    // Sample mean of percolation threshold.
    public double mean() {
        return mean;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return stdDev;
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}




