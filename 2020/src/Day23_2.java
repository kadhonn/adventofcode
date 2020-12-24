import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day23_2 {
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10; i++) {
			long time = System.currentTimeMillis();
			run();
			System.out.println(System.currentTimeMillis() - time);
		}
	}

	public static void run() throws IOException {
		List<String> inputs = Files.readAllLines(new File("./in/day23.in").toPath());
		String startCups = inputs.get(0);

		int[] cups = new int[1_000_000];
		for (int i = 0; i < startCups.length(); i++) {
			cups[startCups.charAt(i) - '1'] = startCups.charAt((i + 1) % startCups.length()) - '1';
		}
		for (int i = startCups.length(); i < cups.length - 1; i++) {
			cups[i] = i + 1;
		}
		cups[startCups.charAt(startCups.length() - 1) - '1'] = cups[startCups.length()] - 1;
		cups[cups.length - 1] = startCups.charAt(0) - '1';
//		print(cups);
		int currentIndex = startCups.charAt(0) - '1';

		for (int round = 0; round < 10_000_000; round++) {
			int searchForIndex = currentIndex;
			int thirdIndex;
			search:
			while (true) {
				searchForIndex = (searchForIndex + cups.length - 1) % cups.length;

				thirdIndex = currentIndex;
				for (int i = 0; i < 3; i++) {
					thirdIndex = cups[thirdIndex];
					if (thirdIndex == searchForIndex) {
						continue search;
					}
				}
				break;
			}
			int cutAwayIndex = cups[currentIndex];
			cups[currentIndex] = cups[thirdIndex];
			cups[thirdIndex] = cups[searchForIndex];
			cups[searchForIndex] = cutAwayIndex;

			currentIndex = cups[currentIndex];
//			print(cups);
//			if (true) throw new RuntimeException();
		}

		System.out.println(cups[0] + 1);
		System.out.println(cups[cups[0]] + 1);
		long solution = (long) (cups[0] + 1) * (long) (cups[cups[0]] + 1);
		System.out.println(solution);
	}

	private static final int START = 2;

	private static void print(int[] cups) {
		int i = START;
		do {
			System.out.print(i + 1);
			System.out.print(" ");
			i = cups[i];
		} while (i != START);
		System.out.println();
	}
}
