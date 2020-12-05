import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day5_2_Math {

	public static void main(String[] args) throws IOException {
		List<String> boardingPasses = Files.readAllLines(new File("./in/day5.in").toPath());

		int max = -1;
		int min = 1024;
		int sum = 0;
		for (String boardingPass : boardingPasses) {
			int seatNumber = Integer.parseInt(boardingPass
							.replaceAll("F", "0")
							.replaceAll("B", "1")
							.replaceAll("L", "0")
							.replaceAll("R", "1")
					, 2);

			min = Math.min(min, seatNumber);
			max = Math.max(max, seatNumber);
			sum += seatNumber;
		}

		int mySeat = (max * (max + 1)) / 2 - (min * (min - 1)) / 2 - sum;

		System.out.println(mySeat);
	}
}
