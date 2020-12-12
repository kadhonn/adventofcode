import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day12_2 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(new File("./in/day12.in").toPath());

        int x = 0;
        int y = 0;
        int wayX = 10;
        int wayY = 1;

        for (String cmd : input) {
            char dir = cmd.charAt(0);
            int value = Integer.parseInt(cmd.substring(1));

            switch (dir) {
                case 'F':
                    x += value * wayX;
                    y += value * wayY;
                    break;
                case 'L':
                    value = 360 - value;
                case 'R':
                    boolean flipX = false;
                    switch (value) {
                        case 180:
                            wayX = wayX * -1;
                            wayY = wayY * -1;
                            break;
                        case 270:
                            flipX = true;
                        case 90:
                            int tmp = wayX;
                            wayX = wayY;
                            wayY = tmp;
                            if (flipX) {
                                wayX *= -1;
                            } else {
                                wayY *= -1;
                            }
                    }
                    break;
                case 'N':
                    wayY += value;
                    break;
                case 'S':
                    wayY -= value;
                    break;
                case 'E':
                    wayX += value;
                    break;
                case 'W':
                    wayX -= value;
                    break;
            }
        }

        System.out.println(Math.abs(x) + Math.abs(y));
    }

}
