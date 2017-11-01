package alg;

public class QuickUnionFindLazy implements UnionFind {

    private int[] id;

    public QuickUnionFindLazy(int n) {
        id = new int[n];

        for (int i = 0; i < id.length; i++) {
            id[i] = i;
        }
    }

    private void validate(int p) {
        if (p < 0 || p >= id.length) {
            throw new IllegalArgumentException("invalid index");
        }
    }

    private int root(int p) {
        while (id[p] != p) {
            p = id[p];
        }
        return p;
    }


    @Override
    public void union(int p, int q) {
        validate(p);
        validate(q);
        int rp = root(p);
        int rq = root(q);
        id[rp] = rq;
    }

    @Override
    public boolean connected(int p, int q) {
        validate(p);
        validate(q);
        return root(p) == root(q);
    }
}
