import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day23_1 {
	public static void main(String[] args) throws IOException {
		List<String> inputs = Files.readAllLines(new File("./in/day23.in").toPath());
		String startCups = inputs.get(0);

		int[] cups = new int[startCups.length()];
		for (int i = 0; i < cups.length; i++) {
			cups[startCups.charAt(i) - '1'] = startCups.charAt((i + 1) % cups.length) - '1';
		}
//		print(cups);
		int currentIndex = startCups.charAt(0) - '1';

		for (int round = 0; round < 100; round++) {
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
		}

		int i = cups[0];
		do {
			System.out.print(i + 1);
			System.out.print(" ");
			i = cups[i];
		} while (i != 0);
		System.out.println();
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
