package experiments;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayBasedRandomizedQueue<Item> implements Iterable<Item> {

    private class RandomizedIterator implements Iterator<Item> {

        private final int[] randomNumbers;
        private int counter;

        public RandomizedIterator() {
            final int length = ArrayBasedRandomizedQueue.this.size;
            randomNumbers = new int[length];
            counter = 0;

            for (int i = 0; i < length; i++) {
                randomNumbers[i] = i;
            }

            for (int i = 0; i < length; i++) {
                int change = i + StdRandom.uniform(length - i);
                int a = randomNumbers[change];
                randomNumbers[change] = randomNumbers[i];
                randomNumbers[i] = a;
            }
        }

        @Override
        public boolean hasNext() {
            return counter < ArrayBasedRandomizedQueue.this.size();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int index = randomNumbers[counter];
            counter++;
            return ArrayBasedRandomizedQueue.this.queue[index];
        }
    }

    private int size = 0;

    private Item[] queue;

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    public ArrayBasedRandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (size == queue.length) {
            resize(2 * queue.length);
        }
        queue[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(this.size);
        Item item = queue[random];
        Item last = queue[this.size - 1];

        this.queue[this.size - 1] = item;
        this.queue[random] = last;

        size--;

        queue[size] = null;
        if (size > 0 && size == queue.length / 4) {
            resize(queue.length / 2);
        }

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(this.size);
        return queue[random];
    }
}
