import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day10_1 {

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day10.in").toPath());

        List<Long> adapters = input.stream().map(Long::parseLong).collect(Collectors.toList());

        adapters.sort(Comparator.naturalOrder());

        Map<Long, Long> diffs = new HashMap<>();
        long last = 0;
        for (int i = 0; i < adapters.size(); i++) {
            long next = adapters.get(i);
            long diff = next - last;
            if (!diffs.containsKey(diff)) {
                diffs.put(diff, 1L);
            } else {
                diffs.put(diff, diffs.get(diff) + 1L);
            }
            last = next;
        }
        System.out.println(diffs);

        System.out.println(diffs.get(1L) * (diffs.get(3L) + 1));

    }

}
