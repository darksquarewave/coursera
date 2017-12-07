import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node start;

    private Node end;

    private int size = 0;

    private class Node {

        private Node prev;
        private Node next;
        private final Item value;

        Node(Item item, Node next, Node prev) {
            this.value = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node iterator;

        public DequeIterator() {
            iterator = Deque.this.start;
        }

        @Override
        public boolean hasNext() {
            return iterator != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item value = iterator.value;
            iterator = iterator.next;

            return value;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node s = new Node(item, this.start, null);
        if (this.start != null) {
            this.start.prev = s;
        }

        this.start = s;

        if (this.end == null) {
            this.end = this.start;
        }

        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node e = new Node(item, null, this.end);
        if (this.end != null) {
            this.end.next = e;
        }

        this.end = e;

        if (this.start == null) {
            this.start = this.end;
        }

        this.size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = this.start.value;
        this.start = this.start.next;

        this.size--;

        if (this.size == 0) {
            this.start = null;
            this.end = null;
        } else {
            this.start.prev = null;
        }

        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = this.end.value;
        this.end = this.end.prev;

        this.size--;

        if (this.size == 0) {
            this.start = null;
            this.end = null;
        } else {
            this.end.next = null;
        }

        return item;
    }

    public Iterator<Item> iterator() {
       return new DequeIterator();
    }

    public static void main(String[] args) {

        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addLast(4);
        deque.addFirst(5);
        deque.removeLast(); //==> 4
        for (Integer integer : deque) {
            System.out.println("integer = " + integer);

        }
        System.out.println("=====");

        deque = new Deque<>();
        deque.addLast(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.removeLast(); //==> 1
        for (Integer integer : deque) {
            System.out.println("integer = " + integer);
        }

        System.out.println("=====");

        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.removeLast(); //==> 3
        for (Integer integer : deque) {
            System.out.println("integer = " + integer);
        }

        System.out.println("=====");

        deque.addFirst(1);
        System.out.println(deque.removeFirst()); //==> 1
        deque.addLast(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.addFirst(6);
        deque.removeLast(); //==> 5
        for (Integer integer : deque) {
            System.out.println("integer = " + integer);
        }

        System.out.println("=====");

        deque = new Deque<Integer>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.addFirst(5);
        deque.removeFirst(); //==> 5
        deque.removeFirst(); //==> 3
        deque.addLast(8);
        deque.addFirst(9);
        deque.removeFirst(); //==> 9
        deque.removeLast();  //==> 8
        for (Integer integer : deque) {
            System.out.println("integer = " + integer);
        }

        System.out.println("=====");
    }
}
