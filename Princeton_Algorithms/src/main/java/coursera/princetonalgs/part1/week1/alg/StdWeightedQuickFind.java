package alg;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class StdWeightedQuickFind implements UnionFind {

    private final WeightedQuickUnionUF quickFindUF;

    public StdWeightedQuickFind(int n) {
        quickFindUF = new WeightedQuickUnionUF(n);
    }

    @Override
    public void union(int p, int q) {
        quickFindUF.union(p, q);
    }

    @Override
    public boolean connected(int p, int q) {
        return quickFindUF.connected(p, q);
    }
}
