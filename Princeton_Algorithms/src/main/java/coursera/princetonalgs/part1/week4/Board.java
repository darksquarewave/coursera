import java.util.Iterator;

public class Board {

    private int[][] blocks;
    private int n;
    private int size;
    private int hamming;
    private int manhattan;

    private int i0;
    private int j0;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] arr) {
        this(arr, null, null);
    }

    private Board(int[][] arr, Integer i00, Integer j00) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        if (arr[0].length != arr.length) {
            throw new IllegalArgumentException();
        }

        blocks = arr;
        n = blocks.length;
        size = n * n;

        if (i00 == null || j00 == null) {
            searchZeroBlock();
        }
        else {
            i0 = i00;
            j0 = j00;
        }

        calculateHamming();
        calculateManhattan();
    }

    private void searchZeroBlock() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    return;
                }
            }
        }
    }

    private void calculateHamming() {
        hamming = 0;
        int count = 1;
        for (int[] block : blocks) {
            for (int j = 0; j < n && count != size; j++, count++) {
                if (block[j] != count) {
                    hamming++;
                }
            }
        }
    }

    private void calculateManhattan() {
        manhattan = 0;
        int count = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n && count <= size; j++, count++) {
                int num = blocks[i][j];
                if (num == 0) {
                    continue;
                }
                if (num != count) {
                    manhattan += Math.abs((num - 1) / n - i);
                    manhattan += Math.abs((num - 1) % n - j);
                }
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i;
        int j = 0;
        boolean found = false;

        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (i != i0 && j != j0) {
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        Board result = new NeighborsSequence(this, i, j).iterator().next();
        result.i0 = i0;
        result.j0 = j0;

        return result;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != Board.class) {
            return false;
        }
        Board board = (Board) y;
        if (n != board.n) {
            return false;
        }
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != board.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new NeighborsSequence(this, this.i0, this.j0);
    }

    private static class NeighborsSequence implements Iterable<Board> {

        private final Board board;
        private final int i;
        private final int j;

        NeighborsSequence(Board b, int i0, int j0) {
            board = b;
            i = i0;
            j = j0;
        }

        @Override
        public Iterator<Board> iterator() {
            return new NeighborIterator(board, i, j);
        }
    }

    private static class NeighborIterator implements Iterator<Board> {

        private static final int[][] OFFSETS = { {-1, 0}, {0, -1}, {1, 0}, {0, 1} };
        private final int i0;
        private final int j0;
        private final int n;
        private final Board board;
        private int offset = -1;

        NeighborIterator(Board b, int i, int j) {
            if (b == null) {
                throw new IllegalArgumentException();
            }
            board = b;
            n = board.n;
            i0 = i;
            j0 = j;
            precalculateNextOffset();
        }

        private void precalculateNextOffset() {
            offset++;

            while (offset < OFFSETS.length) {
                if (i0 + OFFSETS[offset][0] >= 0
                    && i0 + OFFSETS[offset][0] < n
                    && j0 + OFFSETS[offset][1] >= 0
                    && j0 + OFFSETS[offset][1] < n
                    && board.blocks[i0 + OFFSETS[offset][0]][j0 + OFFSETS[offset][1]] != 0) {

                    break;
                }

                offset++;
            }
        }

        @Override
        public boolean hasNext() {
            return offset < OFFSETS.length;
        }

        @Override
        public Board next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }

            int[][] copy = new int[n][n];
            int i00 = i0 + OFFSETS[offset][0];
            int j00 = j0 + OFFSETS[offset][1];

            for (int k = 0; k < n; k++) {
                for (int m = 0; m < n; m++) {
                    if (k == i0 && m == j0) {
                        copy[k][m] = board.blocks[i00][j00];
                    }
                    else if (k == i00 && m == j00) {
                        copy[k][m] = board.blocks[i0][j0];
                    }
                    else {
                        copy[k][m] = board.blocks[k][m];
                    }
                }
            }

            Board neighbor = new Board(copy, i00, j00);
            precalculateNextOffset();

            return neighbor;
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("\r\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(blocks[i][j]);
                sb.append(" ");
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    // unit tests (not notgraded)
    public static void main(String[] args) {
        int[][] initial = {
            { 1,  2,  3,  4,  5,  6,  7,  8,  9,  10 },
            { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
            { 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 },
            { 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 },
            { 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 },
            { 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 },
            { 61, 62, 63, 64, 65, 66, 67, 68, 69, 70 },
            { 71, 72, 73, 74, 75, 76, 77, 78, 79, 80 },
            { 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 },
            { 91, 92, 93, 94, 95, 96, 97, 98, 99,  0 }
        };
        Board b = new Board(initial);
        System.out.println(b);
        System.out.println("hamming = " + b.hamming());
        System.out.println("manhattan = " + b.manhattan());
        for (Board board : b.neighbors()) {
            System.out.println(board);
        }
        Board twin = b.twin();
        System.out.println(twin);
    }
}
