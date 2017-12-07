package alg;

public class QuickUnionFindWeighted implements UnionFind {

    private int[] id;
    private int[] sz;

    public QuickUnionFindWeighted(int n) {
        id = new int[n];
        sz = new int[n];

        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    private void validate(int p) {
        if (p < 0 || p >= id.length) {
            throw new IllegalArgumentException("invalid index");
        }
    }

    private int root(int p) {
        validate(p);

        while (id[p] != p) {
            p = id[p];
        }

        return p;
    }

    @Override
    public void union(int p, int q) {
        int rp = root(p);
        int rq = root(q);

        if (rp == rq) {
            return;
        }

        if (sz[rp] < sz[rq]) {
            id[rp] = rq;
            sz[rq] += sz[rp];
        }
        else {
            id[rq] = rp;
            sz[rp] += sz[rq];
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }
}
