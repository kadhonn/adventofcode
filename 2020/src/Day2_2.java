import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day2_2 {

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day2.in").toPath());

        int count = 0;
        for (String input : inputs) {
            String[] split1 = input.split(": ");
            String wholePolicy = split1[0];
            String password = split1[1];
            String[] split2 = wholePolicy.split(" ");
            String[] split3 = split2[0].split("-");
            char character = split2[1].charAt(0);
            int min = Integer.parseInt(split3[0]);
            int max = Integer.parseInt(split3[1]);

            if (isValid(password, min, max, character)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static boolean isValid(String password, int min, int max, char character) {
        return (password.charAt(min - 1) == character) ^ (password.charAt(max - 1) == character);
    }
}
