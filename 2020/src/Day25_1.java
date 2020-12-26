import java.io.IOException;

public class Day25_1 {
	public static void main(String[] args) throws IOException {
//		for (int i = 0; i < 10; i++) {
//			long time = System.currentTimeMillis();
		run();
//			System.out.println(System.currentTimeMillis() - time);
//		}
	}

	private static final int WANTED = 6929599;
	private static final int DIVISOR = 20201227;
	private static final int SUBJECT = 7;
	private static final int SECOND = 2448427;


	public static void run() throws IOException {
		long nr = 1;
		int count = 0;
		while (nr != WANTED) {
			nr = (nr * SUBJECT) % DIVISOR;
			count++;
		}

		nr = 1;
		for (int i = 0; i < count; i++) {
			nr = (nr * SECOND) % DIVISOR;
		}

		System.out.println(nr);
	}
}
