import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Day15_1 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("./in/day15.in").toPath());

        String[] startingNumbers = input.split(",");

        Map<Integer, Integer> lastSeenInTurn = new HashMap<>();
        int turn = 1;
        int nextNumber = 0;

        for (String startingNumber : startingNumbers) {
            int number = Integer.parseInt(startingNumber);
            nextNumber = getNextNumber(lastSeenInTurn, number, turn);
            lastSeenInTurn.put(number, turn);
            turn++;
        }

        while (turn < 2020) {
            int number = nextNumber;
            nextNumber = getNextNumber(lastSeenInTurn, number, turn);
            lastSeenInTurn.put(number, turn);
            turn++;
        }

        System.out.println(nextNumber);
    }

    private static int getNextNumber(Map<Integer, Integer> lastSeenInTurn, int number, int turn) {
        if (lastSeenInTurn.containsKey(number)) {
            return turn - lastSeenInTurn.get(number);
        } else {
            return 0;
        }
    }

}
