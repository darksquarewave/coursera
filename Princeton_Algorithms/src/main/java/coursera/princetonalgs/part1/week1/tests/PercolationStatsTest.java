package tests;

import alg.QuickUnionFindLazy;
import alg.QuickUnionFindWeighted;
import alg.QuickUnionFindWeightedPathCompression;
import alg.QuickUnionFindWeightedPathCompressionTwoPass;
import alg.StdWeightedQuickFind;
import alg.UnionFind;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStatsTest {

    private static final double CONFIDENCE_COEFF = 1.96d;

    private final int trials;
    private final int n;

    private double[] x;

    public PercolationStatsTest(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("invalid arguments");
        }

        this.trials = trials;
        this.n = n;
    }

    public double mean() {
        return StdStats.mean(x);
    }

    public double stddev() {
        if (x.length == 1) {
            return Double.NaN;
        }
        else {
            return StdStats.stddev(x);
        }
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_COEFF * stddev())/Math.sqrt(x.length);
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_COEFF * stddev())/Math.sqrt(x.length);
    }

    public void runExperiment(Class<? extends UnionFind> unionFindClass) {
        x = new double[trials];

        Stopwatch sw = new Stopwatch();

        for (int i = 0; i < trials; i++) {
            PercolationTest percolation = new PercolationTest(n, unionFindClass);
            while (!percolation.percolates()) {
                int row = 1 + StdRandom.uniform(n);
                int col = 1 + StdRandom.uniform(n);
                percolation.open(row, col);
            }

            x[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }

        double elapsedTime = sw.elapsedTime();
        System.out.printf("%s, Mean: %.6f, Std: %.6f, Lo: %.6f, Hi: %.6f ",
            unionFindClass.toString(), mean(), stddev(), confidenceHi(), confidenceLo());
        System.out.printf("Ellapsed time: %.2f %n", elapsedTime);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStatsTest stats = new PercolationStatsTest(n, trials);

        stats.runExperiment(QuickUnionFindLazy.class);
        stats.runExperiment(QuickUnionFindWeighted.class);
        stats.runExperiment(StdWeightedQuickFind.class);
        stats.runExperiment(QuickUnionFindWeightedPathCompression.class);
        stats.runExperiment(QuickUnionFindWeightedPathCompressionTwoPass.class);
    }
}
