import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private static final int TWIN_CHECK_RATE = 10;

    private final boolean solvable;
    private final GameTreeNode result;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<GameTreeNode> mainHeap = new MinPQ<>();
        MinPQ<GameTreeNode> twinHeap = new MinPQ<>();

        Board twin = initial.twin();

        GameTreeNode initialNode = new GameTreeNode(initial);
        GameTreeNode twinNode = new GameTreeNode(twin);

        mainHeap.insert(initialNode);
        twinHeap.insert(twinNode);

        boolean isInitial = true;
        MinPQ<GameTreeNode> heap;
        GameTreeNode node;
        long i = 0;

        while (true) {
            if (isInitial) {
                heap = mainHeap;
            }
            else {
                heap = twinHeap;
            }

            node = heap.delMin();

            if (node.board.isGoal()) {
                break;
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.parent == null || !node.parent.board.equals(neighbor)) {
                    heap.insert(new GameTreeNode(neighbor, node));
                }
            }

            isInitial = ++i % TWIN_CHECK_RATE != 0;
        }

        solvable = isInitial;
        result = node;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return result.moves;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        List<Board> list = new ArrayList<>();
        GameTreeNode node = result;
        while (node != null) {
            list.add(node.board);
            node = node.parent;
        }
        Collections.reverse(list);
        return list;
    }

    private static final class GameTreeNode implements Comparable<GameTreeNode> {
        private final Board board;
        private final GameTreeNode parent;
        private final int manhattan;
        private final int moves;

        GameTreeNode(Board b) {
            board = b;
            parent = null;
            manhattan = b.manhattan();
            moves = 0;
        }

        GameTreeNode(Board b, GameTreeNode p) {
            board = b;
            parent = p;
            moves = p.moves + 1;
            manhattan = board.manhattan() + moves;
        }

        @Override
        public int compareTo(GameTreeNode node) {
            return Integer.compare(manhattan, node.manhattan);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}