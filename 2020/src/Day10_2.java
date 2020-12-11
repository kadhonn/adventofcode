import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day10_2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> input = Files.readAllLines(new File("./in/day10.in").toPath());

        List<Long> adapters = input.stream().map(Long::parseLong).collect(Collectors.toList());
        adapters.add(0, 0L);

        for (int i = 0; i < 10; i++) {
            run(new ArrayList<>(adapters));
        }
    }

    private static void run(List<Long> adapters) {
        long time = System.nanoTime();

        adapters.sort(Comparator.naturalOrder());

        long time2 = System.nanoTime();

        List<Long> counts = new ArrayList<>();
        counts.add(1L);
        for (int i = 1; i < adapters.size(); i++) {
            counts.add(0L);
        }

        for (int i = 0; i < adapters.size(); i++) {
            check(i, counts, adapters);
        }

        long time3 = System.nanoTime();
        System.out.println(time3 - time);
        System.out.println(time3 - time2);
        System.out.println(counts.get(counts.size() - 1));
    }

    private static void check(int index, List<Long> counts, List<Long> adapters) {
        long currentCount = counts.get(index);
        long current = adapters.get(index);
        int i = index;
        while (++i < adapters.size()) {
            long next = adapters.get(i);
            if (next - current > 3) {
                break;
            }
            counts.set(i, counts.get(i) + currentCount);
        }
    }

}
