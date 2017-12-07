package alg;

import edu.princeton.cs.algs4.QuickFindUF;

public class StdQuickFind implements UnionFind {

    private final QuickFindUF quickFindUF;

    public StdQuickFind(int n) {
        quickFindUF = new QuickFindUF(n);
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
