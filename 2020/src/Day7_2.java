import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day7_2 {


    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day7.in").toPath());

        Map<String, List<Pair>> containsBags = new HashMap<>();
        for (String input : inputs) {
            String trimmedInput = input.substring(0, input.length() - 1);

            String[] inputSplit = trimmedInput.split(" contain ");

            String mainBag = inputSplit[0].substring(0, inputSplit[0].lastIndexOf(" "));

            if (inputSplit[1].equals("no other bags")) {
                containsBags.put(mainBag, Collections.emptyList());
            } else {
                String[] subBags = inputSplit[1].split(", ");
                List<Pair> pairs = new ArrayList<>();
                for (String subBag : subBags) {
                    int countOfSubBag = Integer.parseInt(subBag.substring(0, subBag.indexOf(" ")));
                    String colorOfSubBag = subBag.substring(subBag.indexOf(" ") + 1, subBag.lastIndexOf(" "));
                    pairs.add(new Pair(countOfSubBag, colorOfSubBag));
                }
                containsBags.put(mainBag, pairs);
            }
        }

        Map<String, Integer> subBagCounts = new HashMap<>();
        collect(subBagCounts, containsBags, "shiny gold");
        System.out.println(subBagCounts.get("shiny gold"));
    }

    private static void collect(Map<String, Integer> subBagCounts, Map<String, List<Pair>> containsBags, String bagColor) {
        int subBagCount = 0;

        for (Pair pair : containsBags.get(bagColor)) {
            if (!subBagCounts.containsKey(pair.bagColor)) {
                collect(subBagCounts, containsBags, pair.bagColor);
            }
            subBagCount += pair.count * (subBagCounts.get(pair.bagColor) + 1);
        }

        subBagCounts.put(bagColor, subBagCount);
    }

    public static class Pair {
        final int count;
        final String bagColor;

        public Pair(int count, String bagColor) {
            this.count = count;
            this.bagColor = bagColor;
        }
    }

}
