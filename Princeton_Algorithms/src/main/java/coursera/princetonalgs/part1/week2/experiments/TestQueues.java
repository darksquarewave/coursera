package experiments;

public class TestQueues {

    private static void measure(Iterable<Integer> queue) {

        long t1 = System.nanoTime();

        for (Integer i : queue);

        long t2 = System.nanoTime();

        double diff = (t2 - t1) / 1e9;

        System.out.println("total time = " + diff + " secs");
    }

    private static LinkedListBasedRandomizedQueue<Integer> buildLinkedListQueue(int count) {
        LinkedListBasedRandomizedQueue<Integer> queue = new LinkedListBasedRandomizedQueue<>();
        for (int i = 0; i < count; i++) {
            queue.enqueue(i);
        }
        return queue;
    }

    private static TreeBasedRandomizedQueue<Integer> buildTreeQueue(int count) {
        TreeBasedRandomizedQueue<Integer> queue = new TreeBasedRandomizedQueue<>();
        for (int i = 0; i < count; i++) {
            queue.enqueue(i);
        }
        return queue;
    }

    private static ArrayBasedRandomizedQueue<Integer> buildArrayQueue(int count) {
        ArrayBasedRandomizedQueue<Integer> queue = new ArrayBasedRandomizedQueue<>();
        for (int i = 0; i < count; i++) {
            queue.enqueue(i);
        }

        return queue;
    }

    public static void main(String[] args) {
        TreeBasedRandomizedQueue<Integer> tQueue = buildTreeQueue(2048000);
        measure(tQueue);

        ArrayBasedRandomizedQueue<Integer> arQueue = buildArrayQueue(2048000);
        measure(arQueue);

        LinkedListBasedRandomizedQueue<Integer> llQueue = buildLinkedListQueue(65536);
        measure(llQueue);
    }
}
