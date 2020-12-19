import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day19.in").toPath());
        int i = 0;

        Map<String, String> rules = new HashMap<>();
        String current = inputs.get(i++);
        while (!current.equals("")) {
            String[] split = current.split(": ");
            rules.put(split[0], split[1]);

            current = inputs.get(i++);
        }

        String regex = "^" + calcRegex("0", rules) + "$";

        int count = 0;
        current = inputs.get(i++);
        while (i < inputs.size()) {
            if (current.matches(regex)) {
                count++;
            }

            current = inputs.get(i++);
        }
        System.out.println(count);
    }

    private static String calcRegex(String ruleNr, Map<String, String> rules) {
        String rule = rules.get(ruleNr);
        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        }
        StringBuilder sb = new StringBuilder("(");
        for (String part : rule.split(" ")) {
            if (part.equals("|")) {
                sb.append("|");
            } else {
                sb.append(calcRegex(part, rules));
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
