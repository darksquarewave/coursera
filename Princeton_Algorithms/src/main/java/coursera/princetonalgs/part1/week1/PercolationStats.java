import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private static final double CONFIDENCE_COEFF = 1.96d;

    private final double[] x;
    private final double mu;
    private double sigma;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("invalid arguments");
        }

        x = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = 1 + StdRandom.uniform(n);
                int col = 1 + StdRandom.uniform(n);
                percolation.open(row, col);
            }

            x[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }

        mu = StdStats.mean(x);

        if (x.length == 1) {
            sigma = Double.NaN;
        }
        else {
            sigma = StdStats.stddev(x);
        }
    }

    public double mean() {
        return mu;
    }

    public double stddev() {
        return sigma;
    }

    public double confidenceLo() {
        return mu - (CONFIDENCE_COEFF * sigma)/Math.sqrt(x.length);
    }

    public double confidenceHi() {
        return mu + (CONFIDENCE_COEFF * sigma)/Math.sqrt(x.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        Stopwatch sw = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, trials);
        double elapsedTime = sw.elapsedTime();

        System.out.printf("Mean: %.6f, Std: %.6f, Lo: %.6f, Hi: %.6f ", stats.mean(),
                stats.stddev(), stats.confidenceHi(), stats.confidenceLo());

        System.out.printf("Ellapsed time: %.2f %n", elapsedTime);
    }
}
