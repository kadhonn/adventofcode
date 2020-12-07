import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day7_1 {


    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day7.in").toPath());

        Map<String, Set<String>> canBeInBags = new HashMap<>();
        for (String input : inputs) {
            String trimmedInput = input.substring(0, input.length() - 1);
            if (trimmedInput.endsWith("no other bags")) {
                continue;
            }

            String[] inputSplit = trimmedInput.split(" contain ");

            String mainBag = inputSplit[0].substring(0, inputSplit[0].lastIndexOf(" "));

            String[] subBags = inputSplit[1].split(", ");
            for (String subBag : subBags) {
                String colorOfSubBag = subBag.substring(subBag.indexOf(" ") + 1, subBag.lastIndexOf(" "));

                if (!canBeInBags.containsKey(colorOfSubBag)) {
                    canBeInBags.put(colorOfSubBag, new HashSet<>());
                }
                Set<String> bagSet = canBeInBags.get(colorOfSubBag);
                bagSet.add(mainBag);
            }
        }

        Set<String> parentBags = new HashSet<>();
        collect(parentBags, canBeInBags, "shiny gold");
        System.out.println(parentBags.size() - 1);
    }

    private static void collect(Set<String> parentBags, Map<String, Set<String>> canBeInBags, String bagColor) {
        if (parentBags.contains(bagColor)) {
            return;
        }
        parentBags.add(bagColor);

        if (!canBeInBags.containsKey(bagColor)) {
            return;
        }

        for (String parentBagColor : canBeInBags.get(bagColor)) {
            collect(parentBags, canBeInBags, parentBagColor);
        }
    }
}
