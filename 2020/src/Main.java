import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File(".\\in\\day1.in").toPath());

        System.out.println(input);

        List<Integer> inputs = Arrays.stream(input.split("\n")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        ArrayList<Integer> list = listToArrayList(new LinkedList<Integer>());
    }

    public static <T> ArrayList<T> listToArrayList(List<T> list) {
        return new ArrayList<>(list);
    }
}