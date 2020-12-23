import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day22_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day22.in").toPath());

        Queue<Integer> player1 = new LinkedList<>();
        Queue<Integer> player2 = new LinkedList<>();

        int i = 1;
        while (!inputs.get(i).equals("")) {
            player1.add(Integer.valueOf(inputs.get(i++)));
        }
        i += 2;
        while (i < inputs.size()) {
            player2.add(Integer.valueOf(inputs.get(i++)));
        }

        while (!player1.isEmpty() && !player2.isEmpty()) {
            int card1 = player1.poll();
            int card2 = player2.poll();
            if (card1 > card2) {
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }
        Queue<Integer> winner;
        if (player1.isEmpty()) {
            winner = player2;
        } else {
            winner = player1;
        }

        int score = 0;
        int multiply = winner.size();
        while (!winner.isEmpty()) {
            score += winner.poll() * multiply--;
        }
        System.out.println(score);
    }
}
