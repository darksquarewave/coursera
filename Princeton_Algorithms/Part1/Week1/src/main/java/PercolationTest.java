import alg.QuickUnionFindWeightedPathCompression;
import alg.UnionFind;
import alg.UnionFindFactory;

public class PercolationTest {

    private final int n;
    private final boolean[] sites;
    private final UnionFind uf;

    public PercolationTest(int n) {
        this(n, QuickUnionFindWeightedPathCompression.class);
    }

    public PercolationTest(int n, Class<? extends UnionFind> unionFindClass) {
        this.n = n;
        sites = new boolean[n * n + 2];
        sites[0] = true;
        sites[n * n + 1] = true;
        uf = UnionFindFactory.getUnionFindInstance(unionFindClass, n * n + 2);
    }

    private int index(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid index passed");
        }

        return n * (row - 1) + col;
    }

    public void open(int row, int col) {
        int j = index(row, col);

        sites[j] = true;

        int i;

        if (col - 1 > 0) {
            i = index(row, col - 1);
            if (sites[i]) {
                uf.union(j, i);
            }
        }

        if (col + 1 <= n) {
            i = index(row, col + 1);
            if (sites[i]) {
                uf.union(j, i);
            }
        }

        if (row - 1 > 0) {
            i = index(row - 1, col);
        }
        else {
            i = 0;
        }

        if (sites[i]) {
            uf.union(j, i);
        }

        if (row + 1 <= n) {
            i = index(row + 1, col);
        }
        else {
            i = n * n + 1;
        }

        if (sites[i]) {
            uf.union(j, i);
        }
    }

    public boolean isOpen(int row, int col) {
        return sites[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            return uf.connected(0, index(row, col));
        }
        else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        int count = 0;

        for (int i = 0; i < sites.length; i++) {
            if (sites[i]) {
                count++;
            }
        }

        return count;
    }

    public boolean percolates() {
        return uf.connected(0, n * n + 1);
    }

    public static void main(String[] args) {
        PercolationTest percolation = new PercolationTest(3);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 1);
        percolation.open(3, 2);
        System.out.printf(Boolean.toString(percolation.percolates()));
    }

}
