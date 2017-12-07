package tests;

public class QuizQuestion3 {

    private final Integer[] numbers;

    public QuizQuestion3(int n) {
        numbers = new Integer[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i;
        }
    }

    private Integer root(Integer p) {
        while (p != null && p != numbers[p]) {
            if (numbers[p] != null) {
                numbers[p] = numbers[numbers[p]];
            }
            p = numbers[p];
        }

        return p;
    }

    public void remove(int p) {
        if (p == numbers.length - 1) {
            numbers[p] = null;
            return;
        }

        Integer q = root(p + 1);
        numbers[p] = q;
    }

    public Integer find(int i) {
        if (i == numbers.length - 1) {
            return null;
        }
        return root(i + 1);
    }

    public static void main(String[] args) {
        QuizQuestion3 question3 = new QuizQuestion3(8);
        question3.remove(5);
        question3.remove(3);
        question3.remove(4);

        Integer result = question3.find(3);
        System.out.println(result);
    }
}
