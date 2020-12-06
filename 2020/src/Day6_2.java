import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6_2 {


	public static void main(String[] args) throws IOException {
		String input = Files.readString(new File("./in/day6.in").toPath());

		int collect = Arrays.stream(input.split("\r\n\r\n"))
				.map((group) -> Arrays.stream(group.split("\r\n"))
						.map((person) -> person.chars().boxed().collect(Collectors.toSet()))
						.reduce((s1, s2) -> {
							Set<Integer> set = new HashSet<>(s1);
							set.retainAll(s2);
							return set;
						})
						.get().size()
				)
				.mapToInt(Integer::intValue)
				.sum();
		System.out.println(collect);
	}
}
