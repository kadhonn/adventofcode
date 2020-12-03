import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day3_1 {

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day3.in").toPath());

        char[][] field = new char[inputs.get(0).length()][inputs.size()];

        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                field[x][y] = inputs.get(y).charAt(x);
            }
        }

        System.out.println(testSlope(field, 3, 1));
    }

    private static int testSlope(char[][] field, int slopeX, int slopeY) {
        int count = 0;
        int x = 0;
        int y = 0;
        while (y < field[0].length) {
            if (field[x][y] == '#') {
                count++;
            }
            x += slopeX;
            x = x % field.length;
            y += slopeY;
        }
        return count;
    }
}
