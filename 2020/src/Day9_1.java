import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Day9_1 {

    private static final int PREAMBLE_LENGTH = 25;

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day9.in").toPath());

        List<Long> numbers = input.stream().map(Long::parseLong).collect(Collectors.toList());

        for (int i = PREAMBLE_LENGTH; i < numbers.size(); i++) {
            if (!findTwoNumbers(numbers, i)) {
                System.out.println(numbers.get(i));
                break;
            }
        }
    }

    private static boolean findTwoNumbers(List<Long> numbers, int nr) {
        for (int i = nr - PREAMBLE_LENGTH; i < nr - 1; i++) {
            for (int j = i + 1; j < nr; j++) {
                if (numbers.get(i) + numbers.get(j) == numbers.get(nr)) {
                    return true;
                }
            }
        }
        return false;
    }

}
