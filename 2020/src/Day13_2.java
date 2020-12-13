import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13_2 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day13.in").toPath());

        List<String> buses = Arrays.stream(input.get(1).split(",")).collect(Collectors.toList());

        long cumulatedOffset = 0;
        long cumulatedInterval = 1;

        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i).equals("x")) {
                continue;
            }
            long interval = Integer.parseInt(buses.get(i));
            long offset = i;

            long j = 1;
            while (true) {
                long time = j * cumulatedInterval + cumulatedOffset;
                if ((time + offset) % interval == 0) {
                    cumulatedInterval *= interval;
                    cumulatedOffset = time % cumulatedInterval;
                    break;
                }
                j++;
            }
        }

        System.out.println(cumulatedOffset);
        System.out.println(cumulatedInterval);

    }

}
