import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Day4_1 {

    private static final String[] NEEDED_FIELDS = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};

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
        for (String neededField : NEEDED_FIELDS) {
            for (String passportField : passportFields) {
                if (passportField.startsWith(neededField)) {
                    continue fields;
                }
            }
            return false;
        }
        return true;
    }
}
