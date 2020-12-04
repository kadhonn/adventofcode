import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4_2 {

    private static final Map<String, Validator> NEEDED_FIELDS;
    private static final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    static {
        NEEDED_FIELDS = new HashMap<>();
        NEEDED_FIELDS.put("byr", it -> {
            try {
                int year = Integer.parseInt(it);
                return 1920 <= year && year <= 2002;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        NEEDED_FIELDS.put("iyr", (it) -> {
            try {
                int year = Integer.parseInt(it);
                return 2010 <= year && year <= 2020;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        NEEDED_FIELDS.put("eyr", (it) -> {
            try {
                int year = Integer.parseInt(it);
                return 2020 <= year && year <= 2030;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        NEEDED_FIELDS.put("hgt", (it) -> {
            try {
                int height = Integer.parseInt(it.substring(0, it.length() - 2));
                String unit = it.substring(it.length() - 2);
                if (unit.equals("cm")) {
                    return 150 <= height && height <= 193;
                } else if (unit.equals("in")) {
                    return 59 <= height && height <= 76;
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        });
        NEEDED_FIELDS.put("hcl", (it) -> {
            Pattern pattern = Pattern.compile("#[0-9a-f]{6}");
            Matcher matcher = pattern.matcher(it);
            return matcher.find();
        });
        NEEDED_FIELDS.put("ecl", (it) -> {
            return EYE_COLORS.contains(it);
        });
        NEEDED_FIELDS.put("pid", (it) -> {
            Pattern pattern = Pattern.compile("^[0-9]{9}$");
            Matcher matcher = pattern.matcher(it);
            return matcher.find();
        });
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("./in/day4.in").toPath());

        String[] passports = input.split("\r\n\r\n");

        int count = 0;
        for (String passport : passports) {
            String[] passportFields = passport.replaceAll("\r\n", " ").split(" ");
            if (isValid(passportFields)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static boolean isValid(String[] passportFields) {
        fields:
        for (Map.Entry<String, Validator> neededField : NEEDED_FIELDS.entrySet()) {
            for (String passportField : passportFields) {
                if (passportField.startsWith(neededField.getKey())) {
                    if (neededField.getValue().isValid(passportField.split(":")[1])) {
                        continue fields;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private interface Validator {
        boolean isValid(String value);
    }
}
