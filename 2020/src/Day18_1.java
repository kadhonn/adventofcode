import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day18_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day18.in").toPath());

        long sum = 0;
        for (String calc : inputs) {
            String[] parts = calc.split(" ");

            long solution = new Solver(parts).solve();
            sum += solution;
            System.out.println(solution);
        }
        System.out.println(sum);
    }

    private static class Solver {
        private final String[] parts;
        private int i;

        private Solver(String[] parts) {
            this.parts = parts;
            this.i = parts.length - 1;
        }

        public long solve() {
            if (parts[i].startsWith("(")) {
                parts[i] = parts[i].substring(1);
                int ret = Integer.parseInt(parts[i].substring(parts[i].lastIndexOf("(") + 1));
                if (!parts[i].startsWith("(")) {
                    i--;
                }
                return ret;
            }
            if (i == 0) {
                return Integer.parseInt(parts[i--]);
            }
            long secondOperand;
            String secondOperandString = parts[i];
            if (secondOperandString.endsWith(")")) {
                parts[i] = secondOperandString.substring(0, secondOperandString.length() - 1);
                secondOperand = solve();
                if (i >= 0 && parts[i].startsWith("(")) {
                    parts[i] = parts[i].substring(1);
                    if (!parts[i].startsWith("(")) {
                        i--;
                    }
                    return secondOperand;
                }
            } else {
                i--;
                secondOperand = Integer.parseInt(secondOperandString);
            }
            if (i <= 0) {
                return secondOperand;
            }
            String operator = parts[i--];
            long firstOperand = solve();
            if (operator.equals("+")) {
                return firstOperand + secondOperand;
            } else {
                return firstOperand * secondOperand;
            }
        }
    }

}
