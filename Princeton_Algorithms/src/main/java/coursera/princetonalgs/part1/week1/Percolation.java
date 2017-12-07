import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final boolean[] sites;
    private final WeightedQuickUnionUF percolateUnionFind;
    private final WeightedQuickUnionUF unionFind;
    private int openCount = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("invalid arguments");
        }

        this.n = n;
        sites = new boolean[n * n + 2];
        sites[0] = true;
        sites[n * n + 1] = true;
        unionFind = new WeightedQuickUnionUF(n * n + 1);
        percolateUnionFind = new WeightedQuickUnionUF(n * n + 2);
    }

    private int index(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid index passed");
        }

        return n * (row - 1) + col;
    }

    public void open(int row, int col) {
        int j = index(row, col);

        if (sites[j]) {
            return;
        }

        sites[j] = true;
        openCount++;

        int i;

        if (col - 1 > 0) {
            i = index(row, col - 1);
            if (sites[i]) {
                unionFind.union(j, i);
                percolateUnionFind.union(j, i);
            }
        }

        if (col + 1 <= n) {
            i = index(row, col + 1);
            if (sites[i]) {
                unionFind.union(j, i);
                percolateUnionFind.union(j, i);
            }
        }

        if (row - 1 > 0) {
            i = index(row - 1, col);
        }
        else {
            i = 0;
        }

        if (sites[i]) {
            unionFind.union(j, i);
            percolateUnionFind.union(j, i);
        }

        if (row + 1 <= n) {
            i = index(row + 1, col);
            unionFind.union(j, i);
            percolateUnionFind.union(j, i);
        }
        else {
            i = n * n + 1;
            percolateUnionFind.union(j, i);
        }
    }

    public boolean isOpen(int row, int col) {
        return sites[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            return unionFind.connected(0, index(row, col));
        }
        else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return percolateUnionFind.connected(0, n * n + 1);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(3, 1);
        percolation.open(1, 3);
        percolation.open(2, 3);
        percolation.open(3, 3);
        System.out.printf("is full: %s", Boolean.toString(percolation.isFull(3, 1)));
        System.out.printf("Percolates: %s", Boolean.toString(percolation.percolates()));
    }

}
