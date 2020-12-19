import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19_2 {

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 10; i++) {
            long time = System.currentTimeMillis();
            run();
            System.out.println(System.currentTimeMillis() - time);
        }

    }

    private static void run() throws IOException {
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
        Pattern p = Pattern.compile(regex);

        int count = 0;
        current = inputs.get(i++);
        while (i < inputs.size()) {
            Matcher matcher = p.matcher(current);
            if (matcher.matches()) {
                count++;
            }

            current = inputs.get(i++);
        }
        Matcher matcher = p.matcher(current);
        if (matcher.matches()) {
            count++;
        }
        System.out.println(count);
    }

    private static String calcRegex(String ruleNr, Map<String, String> rules) {
        String rule = rules.get(ruleNr);
        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        }
        if (ruleNr.equals("8")) {
            return "(" + calcRegex("42", rules) + ")+";
        }
        if (ruleNr.equals("11")) {
            String rule11 = calc11Recursion(10, rules);
            return rule11.substring(0, rule11.length() - 1);
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

    private static String calc11Recursion(int i, Map<String, String> rules) {
        if (i == 0) {
            return "";
        }
        return "(" + calcRegex("42", rules) + calc11Recursion(i - 1, rules) + calcRegex("31", rules) + ")?";
    }

}
