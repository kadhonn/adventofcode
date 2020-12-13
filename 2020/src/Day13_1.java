import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13_1 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day13.in").toPath());

        int currentTime = Integer.parseInt(input.get(0));
        List<String> buses = Arrays.stream(input.get(1).split(",")).collect(Collectors.toList());

        int minBusId = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < buses.size(); i++) {
            String busIdString = buses.get(i);
            if (busIdString.equals("x")) {
                continue;
            }
            int busId = Integer.parseInt(busIdString);
            int waitTime = busId - (currentTime % busId);
            if (waitTime < minValue) {
                minBusId = busId;
                minValue = waitTime;
            }
        }

        System.out.println(minBusId * minValue);
    }

}
