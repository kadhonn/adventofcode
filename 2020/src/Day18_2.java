import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiFunction;

public class Day18_2 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day18.in").toPath());

        long sum = 0;
        for (String calc : inputs) {
            List<String> parts = tokenize(calc);

            long solution = new Solver(parts).solve();
            sum += solution;
            System.out.println(solution);
        }
        System.out.println(sum);
    }

    private static List<String> tokenize(String calc) {
        return Arrays.asList(calc.replaceAll(" ", "").split("").clone());
    }

    private static class Solver {
        private final List<String> parts;

        private Solver(List<String> parts) {
            this.parts = new LinkedList<>(parts);
        }

        public long solve() {
            solveAllParentheses();
            solveAllAdds();
            solveAllMults();
            return Long.parseLong(parts.get(0));
        }

        private void solveAllMults() {
            solveOperator("*", (first, second) -> first * second);
        }

        private void solveAllAdds() {
            solveOperator("+", (first, second) -> first + second);
        }

        private void solveOperator(String operand, BiFunction<Long, Long, Long> function) {
            ListIterator<String> iterator = parts.listIterator();
            while (iterator.hasNext()) {
                String firstNumber = iterator.next();
                if (!iterator.hasNext()) {
                    break;
                }
                iterator.remove();
                String operator = iterator.next();
                iterator.remove();
                if (operator.equals(operand)) {
                    String secondNumber = iterator.next();
                    iterator.remove();
                    long first = Long.parseLong(firstNumber);
                    long second = Long.parseLong(secondNumber);
                    iterator.add(String.valueOf(function.apply(first, second)));
                    iterator.previous();
                } else {
                    //oops
                    iterator.add(firstNumber);
                    iterator.add(operator);
                }
            }
        }

        private void solveAllParentheses() {
            ListIterator<String> iterator = parts.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals("(")) {
                    iterator.remove();
                    int nestedCount = 1;
                    List<String> nested = new LinkedList<>();
                    while (nestedCount > 0) {
                        String current = iterator.next();
                        nested.add(current);
                        iterator.remove();
                        if (current.equals(")")) {
                            nestedCount--;
                        } else if (current.equals("(")) {
                            nestedCount++;
                        }
                    }
                    nested.remove(nested.size() - 1);
                    String solved = String.valueOf(new Solver(nested).solve());
                    iterator.add(solved);
                }
            }
        }
    }

}
