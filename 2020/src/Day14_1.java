import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day14.in").toPath());

        Map<Long, Long> mem = new HashMap<>();

        long currentMask0 = 0;
        long currentMask1 = 0;

        for (String input : inputs) {
            if (input.startsWith("mask = ")) {
                String mask = input.substring("mask = ".length());

                currentMask0 = Long.parseLong(mask.replaceAll("X", "1"), 2);
                currentMask1 = Long.parseLong(mask.replaceAll("X", "0"), 2);
            } else {
                long index = Long.parseLong(input.substring(input.indexOf("[") + 1, input.indexOf("]")));
                long value = Long.parseLong(input.substring(input.indexOf("=") + 2));

                value = value & currentMask0;
                value = value | currentMask1;
                mem.put(index, value);
            }
        }

        long sum = 0;
        for (long value : mem.values()) {
            sum += value;
        }
        System.out.println(sum);
    }

}
