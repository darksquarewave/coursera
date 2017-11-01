package alg;

public class QuickUnionFindWeightedPathCompressionTwoPass implements UnionFind {

    private int[] id;
    private int[] sz;

    public QuickUnionFindWeightedPathCompressionTwoPass(int n) {
        id = new int[n];
        sz = new int[n];

        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    private int root(int p) {
        validate(p);

        int i = p;

        while (id[i] != i) {
            id[i] = id[id[i]];
            i = id[i];
        }

        while (p != i) {
            p = id[p];
            id[p] = i;
        }

        return i;
    }

    private void validate(int p) {
        if (p < 0 || p >= id.length) {
            throw new IllegalArgumentException("invalid index");
        }
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
