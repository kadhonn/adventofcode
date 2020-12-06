import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6_1 {


	public static void main(String[] args) throws IOException {
		String input = Files.readString(new File("./in/day6.in").toPath());

		int collect = Arrays.stream(input.split("\r\n\r\n"))
				.map((group) -> group.chars()
						.filter((c) -> c != '\r' && c != '\n')
						.distinct()
						.count()
				)
				.mapToInt(Long::intValue)
				.sum();
		System.out.println(collect);
	}
}
