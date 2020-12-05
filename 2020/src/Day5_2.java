import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day5_2 {


	public static void main(String[] args) throws IOException {
		List<String> boardingPasses = Files.readAllLines(new File("./in/day5.in").toPath());

		Set<Integer> seats = new HashSet<>();
		for (String boardingPass : boardingPasses) {
			int row = calcRow(boardingPass.substring(0, 7));
			int col = calcCol(boardingPass.substring(7));
			int seatNumber = row * 8 + col;
			seats.add(seatNumber);
		}
		for (int i = 0; i < 1024; i++) {
			if (!seats.contains(i) && seats.contains(i - 1) && seats.contains(i + 1)) {
				System.out.println(i);
				break;
			}
		}
	}

	private static int calcCol(String encodedCol) {
		return binaryEncoding(encodedCol, 0, 7, 'L', 'R');
	}

	private static int calcRow(String encodedRow) {
		return binaryEncoding(encodedRow, 0, 127, 'F', 'B');
	}

	private static int binaryEncoding(String encodedRow, int min, int max, char lower, char upper) {
		int step = (max - min + 1) / 2;

		for (char c : encodedRow.toCharArray()) {
			if (c == lower) {
				max -= step;
			} else if (c == upper) {
				min += step;
			} else {
				throw new RuntimeException("unknown char");
			}
			step = step / 2;
		}

		if (min != max) {
			throw new RuntimeException("min != max");
		}

		return min;
	}

}
