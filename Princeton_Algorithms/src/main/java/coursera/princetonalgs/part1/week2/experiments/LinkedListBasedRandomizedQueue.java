package experiments;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListBasedRandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;

    private Node start;

    private class Node {

        private Node next;

        private final Item value;

        Node(Item item, Node next) {
            this.value = item;
            this.next = next;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        this.start = new Node(item, this.start);
        this.size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int randomIndex = StdRandom.uniform(this.size);
        Node prev = null;
        Node iterator = this.start;

        for (int i = 0; i < randomIndex; i++) {
            prev = iterator;
            iterator = iterator.next;
        }

        Item value = iterator.value;
        if (prev != null) {
            prev.next = iterator.next;
        }
        else {
            start = iterator.next;
        }

        size--;

        return value;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int randomIndex = StdRandom.uniform(this.size);

        Node iterator = this.start;

        for (int i = 0; i < randomIndex; i++) {
            iterator = iterator.next;
        }

        return iterator.value;
    }

    private class RandomizedIterator implements Iterator<Item> {

        private final int[] randomNumbers;
        private int counter;

        public RandomizedIterator() {
            final int length = LinkedListBasedRandomizedQueue.this.size;
            randomNumbers = new int[length];
            counter = 0;

            for (int i = 0; i < length; i++) {
                randomNumbers[i] = i;
            }

            for (int i = length - 1; i > 0; i--) {
                int index = StdRandom.uniform(length);
                int a = randomNumbers[index];
                randomNumbers[index] = randomNumbers[i];
                randomNumbers[i] = a;
            }
        }

        @Override
        public boolean hasNext() {
            return counter < LinkedListBasedRandomizedQueue.this.size();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int next = randomNumbers[counter];

            Node iterator = LinkedListBasedRandomizedQueue.this.start;

            for (int i = 0; i < next; i++) {
                iterator = iterator.next;
            }

            counter++;

            return iterator.value;
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    public static void main(String[] args) {
        LinkedListBasedRandomizedQueue<String> queue = new LinkedListBasedRandomizedQueue<>();
        queue.enqueue("hello");
        queue.enqueue("world");
        queue.enqueue("test");
        queue.enqueue("bla");
        queue.enqueue("alex");
        queue.enqueue("ddfv");

        for (String s : queue) {
            System.out.println("s = " + s);
        }

        System.out.println("=======");

        for (String s : queue) {
            System.out.println("s = " + s);
        }

        System.out.println("=======");

        for (String s : queue) {
            System.out.println("s = " + s);
        }
    }
}
