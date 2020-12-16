import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day16_2 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day16.in").toPath());


        Map<String, Rule> rules = new HashMap<>();
        int i = 0;
        while (true) {
            String input = inputs.get(i++);
            if (input.isEmpty()) {
                break;
            }
            String[] ruleSplit = input.split(": ");
            String ruleName = ruleSplit[0];
            String[] rangeSplit = ruleSplit[1].split(" or ");
            String[] range1Split = rangeSplit[0].split("-");
            String[] range2Split = rangeSplit[1].split("-");
            rules.put(ruleName, new Rule(Integer.parseInt(range1Split[0]), Integer.parseInt(range1Split[1]), Integer.parseInt(range2Split[0]), Integer.parseInt(range2Split[1])));
        }
        i++;
        int[] myTicket = parseTicket(inputs.get(i));
        i += 3;

        List<int[]> otherTickets = new ArrayList<>();
        while (i < inputs.size()) {
            otherTickets.add(parseTicket(inputs.get(i++)));
        }

        Iterator<int[]> ticketsIterator = otherTickets.iterator();
        while (ticketsIterator.hasNext()) {
            int[] ticket = ticketsIterator.next();
            numberLoop:
            for (int number : ticket) {
                for (Rule rule : rules.values()) {
                    if (((rule.min1 <= number && number <= rule.max1) || (rule.min2 <= number && number <= rule.max2))) {
                        continue numberLoop;
                    }
                }
                ticketsIterator.remove();
            }
        }


        List<Set<String>> possibleRules = new ArrayList<>();
        for (i = 0; i < myTicket.length; i++) {
            possibleRules.add(new HashSet<>(rules.keySet()));
        }

        for (int[] ticket : otherTickets) {
            for (int j = 0; j < ticket.length; j++) {
                int number = ticket[j];
                for (Map.Entry<String, Rule> entry : rules.entrySet()) {
                    Rule rule = entry.getValue();
                    if (!((rule.min1 <= number && number <= rule.max1) || (rule.min2 <= number && number <= rule.max2))) {
                        possibleRules.get(j).remove(entry.getKey());
                    }
                }
            }
        }

        String[] foundFields = new String[rules.size()];
        int foundPositions = 0;
        while (foundPositions < rules.size()) {
            for (int j = 0; j < possibleRules.size(); j++) {
                Set<String> possibleRule = possibleRules.get(j);
                if (possibleRule.size() == 1) {
                    String rule = possibleRule.iterator().next();
                    foundFields[j] = rule;
                    foundPositions++;
                    for (Set<String> otherPossibleRules : possibleRules) {
                        otherPossibleRules.remove(rule);
                    }
                }
            }
        }

        long product = 1;
        for (int j = 0; j < foundFields.length; j++) {
            if (foundFields[j].startsWith("departure")) {
                product *= myTicket[j];
            }
        }

        System.out.println(product);
    }

    private static int[] parseTicket(String s) {
        String[] unparsedTickets = s.split(",");
        int[] ticket = new int[unparsedTickets.length];
        for (int i = 0; i < unparsedTickets.length; i++) {
            ticket[i] = Integer.parseInt(unparsedTickets[i]);
        }
        return ticket;
    }

    private static class Rule {
        final int min1;
        final int max1;
        final int min2;
        final int max2;

        public Rule(int min1, int max1, int min2, int max2) {
            this.min1 = min1;
            this.max1 = max1;
            this.max2 = max2;
            this.min2 = min2;
        }
    }
}
