import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day12_1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> input = Files.readAllLines(new File("./in/day12.in").toPath());

        int x = 0;
        int y = 0;
        int currentDir = 90;

        for (String cmd : input) {
            char dir = cmd.charAt(0);
            int value = Integer.parseInt(cmd.substring(1));

            switch (dir) {
                case 'F':
                    switch (currentDir) {
                        case 0:
                            x += value;
                            break;
                        case 90:
                            y += value;
                            break;
                        case 180:
                            x -= value;
                            break;
                        case 270:
                            y -= value;
                            break;
                        default:
                            throw new IllegalStateException("" + currentDir);
                    }
                    break;
                case 'L':
                    currentDir = (currentDir - value + 360) % 360;
                    break;
                case 'R':
                    currentDir = (currentDir + value) % 360;
                    break;
                case 'N':
                    x += value;
                    break;
                case 'S':
                    x -= value;
                    break;
                case 'E':
                    y += value;
                    break;
                case 'W':
                    y -= value;
                    break;
            }
        }

        System.out.println(Math.abs(x) + Math.abs(y));
    }
}
