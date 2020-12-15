import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14_2 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day14.in").toPath());

        Map<Long, Long> mem = new HashMap<>();

        long currentMask1 = 0;
        long currentXMask = 0;

        for (String input : inputs) {
            if (input.startsWith("mask = ")) {
                String mask = input.substring("mask = ".length());

                currentMask1 = Long.parseLong(mask.replaceAll("X", "0"), 2);
                currentXMask = Long.parseLong(mask.replaceAll("1", "0").replaceAll("X", "1"), 2);
            } else {
                long index = Long.parseLong(input.substring(input.indexOf("[") + 1, input.indexOf("]")));
                long value = Long.parseLong(input.substring(input.indexOf("=") + 2));

                index = index | currentMask1;
                recursivePut(mem, value, currentXMask, 0, index);
            }
        }

        long sum = 0;
        for (long value : mem.values()) {
            sum += value;
        }
        System.out.println(sum);
    }

    private static void recursivePut(Map<Long, Long> mem, long value, long currentXMask, int depth, long index) {
        if (depth == 36) {
            mem.put(index, value);
            return;
        }
        recursivePut(mem, value, currentXMask, depth + 1, index);
        boolean isX = ((currentXMask >> depth) & 1L) == 1L;
        if (isX) {
            recursivePut(mem, value, currentXMask, depth + 1, index ^ (1L << depth));
        }
    }

}
