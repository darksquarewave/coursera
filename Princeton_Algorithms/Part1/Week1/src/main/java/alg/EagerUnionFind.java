package alg;

public class EagerUnionFind implements UnionFind {

    private int[] id;

    public EagerUnionFind(int n) {
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

    @Override
    public void union(int p, int q) {
        validate(p);
        validate(q);

        int pid = id[p];
        int qid = id[q];

        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    @Override
    public boolean connected(int p, int q) {
        validate(p);
        validate(q);

        return id[p] == id[q];
    }
}
