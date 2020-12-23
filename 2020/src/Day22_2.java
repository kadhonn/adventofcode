import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day22_2 {
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

        Result result = playGame(new LinkedList<>(player1), new LinkedList<>(player2));

        int score = 0;
        int multiply = result.winner.size();
        while (!result.winner.isEmpty()) {
            score += result.winner.poll() * multiply--;
        }
        System.out.println(score);
    }

    private static Result playGame(Queue<Integer> player1, Queue<Integer> player2) {
        Set<State> set = new HashSet<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            State state = new State(player1, player2);
            if (set.contains(state)) {
                return new Result(true, player1);
            }
            set.add(state);
            int card1 = player1.poll();
            int card2 = player2.poll();
            if (player1.size() >= card1 && player2.size() >= card2) {
                Result result = playGame(
                        new LinkedList<>(((LinkedList) player1).subList(0, card1)),
                        new LinkedList<>(((LinkedList) player2).subList(0, card2)));
                if (result.player1Won) {
                    player1.add(card1);
                    player1.add(card2);
                } else {
                    player2.add(card2);
                    player2.add(card1);
                }
            } else {
                if (card1 > card2) {
                    player1.add(card1);
                    player1.add(card2);
                } else {
                    player2.add(card2);
                    player2.add(card1);
                }
            }
        }

        if (player1.isEmpty()) {
            return new Result(false, player2);
        } else {
            return new Result(true, player1);
        }
    }

    private static class State {
        private final Queue<Integer> player1;
        private final Queue<Integer> player2;

        public State(Queue<Integer> player1, Queue<Integer> player2) {
            this.player1 = new LinkedList<>(player1);
            this.player2 = new LinkedList<>(player2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(player1, state.player1) &&
                    Objects.equals(player2, state.player2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player1, player2);
        }
    }

    private static class Result {
        public final boolean player1Won;
        public final Queue<Integer> winner;

        private Result(boolean player1Won, Queue<Integer> winner) {
            this.player1Won = player1Won;
            this.winner = winner;
        }
    }
}
