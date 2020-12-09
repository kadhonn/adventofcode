import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Day9_2 {

//    private static final long SEARCHING = 127;
    private static final long SEARCHING = 1504371145;

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day9.in").toPath());

        List<Long> numbers = input.stream().map(Long::parseLong).collect(Collectors.toList());

        int start = 0;
        int end = 1;
        long sum = numbers.get(start) + numbers.get(end);
        while (sum != SEARCHING) {
            if (sum > SEARCHING) {
                sum -= numbers.get(start);
                start++;
            } else {
                end++;
                sum += numbers.get(end);
            }
        }

        long min = Integer.MAX_VALUE;
        long max = Integer.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            min = Math.min(min, numbers.get(i));
            max = Math.max(max, numbers.get(i));
        }

        System.out.println(min + max);
    }

}
