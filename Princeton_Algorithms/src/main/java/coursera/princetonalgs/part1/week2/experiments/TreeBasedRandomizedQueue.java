package experiments;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TreeBasedRandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;

    private Node root = null;

    private class Node {

        final Item value;

        private Node left;

        private Node right;

        Node(Item value) {
            this.value = value;
        }
    }

    private class RandomizedIterator implements Iterator<Item> {

        private final int[] randomNumbers;
        private int counter;

        public RandomizedIterator() {
            final int length = TreeBasedRandomizedQueue.this.size;
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
            return counter < TreeBasedRandomizedQueue.this.size();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int index = randomNumbers[counter];
            Node n = TreeBasedRandomizedQueue.this.getNode(index + 1);
            counter++;

            return n.value;
        }
    }

    private int log2(int value) {
        return (int)(Math.log(value) / Math.log(2));
    }

    private Node getNode(int index) {
        if (index == 1) {
            return root;
        }

        int layerIndex = log2(index);
        int a = (int)Math.pow(2, layerIndex);
        int b = (int)Math.pow(2, layerIndex + 1) - 1;

        Node target = root;

        while (b - a > 2) {
            int c = a + (b - a) / 2;
            if (index <= c) {
                target = target.left;
                b = c;
            } else if (index > c) {
                target = target.right;
                a = c;
            }
        }

        if (index == b) {
            return target.right;
        } else {
            return target.left;
        }
    }

    private void insertItem(int index, Item item) {
        if (index == 1) {
            root = new Node(item);
            return;
        }

        int layerIndex = log2(index);
        int a = (int)Math.pow(2, layerIndex);
        int b = (int)Math.pow(2, layerIndex + 1) - 1;

        Node target = root;

        while (b - a > 2) {
            int c = a + (b - a) / 2;
            if (index <= c) {
                target = target.left;
                b = c;
            } else if (index > c) {
                target = target.right;
                a = c;
            }
        }

        Node toInsert = null;
        if (item != null) {
            toInsert = new Node(item);
        }

        if (index == b) {
            target.right = toInsert;
        } else {
            target.left = toInsert;
        }
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
        this.size++;
        insertItem(this.size, item);
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node node = this.getNode(this.size);
        Item result = node.value;
        this.insertItem(this.size, null);

        this.size--;

        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int random = StdRandom.uniform(1, this.size + 1);

        return getNode(random).value;
    }
}
