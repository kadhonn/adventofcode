import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day24_2 {
	public static void main(String[] args) throws IOException {
//		for (int i = 0; i < 10; i++) {
//			long time = System.currentTimeMillis();
		run();
//			System.out.println(System.currentTimeMillis() - time);
//		}
	}

	public static void run() throws IOException {
		List<String> inputs = Files.readAllLines(new File("./in/day24.in").toPath());

		Set<Way> blackTiles = getInitialBlackTiles(inputs);

		System.out.println(blackTiles.size());
		for (int i = 0; i < 100; i++) {
			blackTiles = doDay(blackTiles);
			System.out.println(blackTiles.size());
		}
	}

	private static Set<Way> doDay(Set<Way> blackTiles) {
		Set<Way> interestedTiles = collectInterestedTiles(blackTiles);

		Set<Way> newBlackTiles = new HashSet<>();

		for (Way w : interestedTiles) {
			int blackNeighboursCount = countBlackNeighbours(w, blackTiles);
			boolean isBlack = blackTiles.contains(w);
			if (isBlack) {
				if (!(blackNeighboursCount == 0 || blackNeighboursCount > 2)) {
					newBlackTiles.add(w);
				}
			} else {
				if (blackNeighboursCount == 2) {
					newBlackTiles.add(w);
				}
			}
		}

		return newBlackTiles;
	}

	private static Set<Way> collectInterestedTiles(Set<Way> blackTiles) {
		Set<Way> tiles = new HashSet<>();
		for (Way way : blackTiles) {
			tiles.addAll(getNeighbours(way));
			tiles.add(way);
		}
		return tiles;
	}

	private static int countBlackNeighbours(Way way, Set<Way> blackTiles) {
		Set<Way> neighbours = getNeighbours(way);
		int count = 0;
		for (Way neighbour : neighbours) {
			if (blackTiles.contains(neighbour)) {
				count++;
			}
		}
		return count;
	}

	private static Set<Way> getNeighbours(Way way) {
		Way e = new Way(way);
		e.e++;
		simplify(e);
		Way w = new Way(way);
		w.w++;
		simplify(w);
		Way se = new Way(way);
		se.se++;
		simplify(se);
		Way ne = new Way(way);
		ne.ne++;
		simplify(ne);
		Way sw = new Way(way);
		sw.sw++;
		simplify(sw);
		Way nw = new Way(way);
		nw.nw++;
		simplify(nw);
		return Set.of(e, w, se, ne, sw, nw);
	}

	private static Set<Way> getInitialBlackTiles(List<String> inputs) {
		Set<Way> flippedTiles = new HashSet<>();

		for (String cWay : inputs) {
			Way way = parse(cWay);
			simplify(way);
			if (flippedTiles.contains(way)) {
				flippedTiles.remove(way);
			} else {
				flippedTiles.add(way);
			}
		}
		return flippedTiles;
	}

	private static void simplify(Way way) {
		Way oldWay = null;
		while (oldWay == null || !oldWay.equals(way)) {
			oldWay = new Way(way);
			int tmp = Math.min(way.e, way.w);
			way.e -= tmp;
			way.w -= tmp;

			tmp = Math.min(way.se, way.nw);
			way.se -= tmp;
			way.nw -= tmp;

			tmp = Math.min(way.ne, way.sw);
			way.ne -= tmp;
			way.sw -= tmp;

			tmp = Math.min(way.ne, way.se);
			way.ne -= tmp;
			way.se -= tmp;
			way.e += tmp;

			tmp = Math.min(way.nw, way.sw);
			way.nw -= tmp;
			way.sw -= tmp;
			way.w += tmp;

			tmp = Math.min(way.se, way.w);
			way.se -= tmp;
			way.w -= tmp;
			way.sw += tmp;

			tmp = Math.min(way.sw, way.e);
			way.sw -= tmp;
			way.e -= tmp;
			way.se += tmp;

			tmp = Math.min(way.ne, way.w);
			way.ne -= tmp;
			way.w -= tmp;
			way.nw += tmp;

			tmp = Math.min(way.nw, way.e);
			way.nw -= tmp;
			way.e -= tmp;
			way.ne += tmp;

			tmp = Math.min(way.e, way.w);
			way.e -= tmp;
			way.w -= tmp;
		}
	}

	private static Way parse(String cWay) {
		Way way = new Way();
		int i = 0;
		while (i < cWay.length()) {
			char first = cWay.charAt(i);
			if (first == 'e') {
				way.e++;
				i++;
			} else if (first == 'w') {
				way.w++;
				i++;
			} else {
				char second = cWay.charAt(i + 1);
				i += 2;
				if (first == 's') {
					if (second == 'e') {
						way.se++;
					} else {
						way.sw++;
					}
				} else {
					if (second == 'e') {
						way.ne++;
					} else {
						way.nw++;
					}
				}
			}
		}
		return way;
	}

	private static class Way {
		public int e = 0;
		public int ne = 0;
		public int se = 0;
		public int w = 0;
		public int nw = 0;
		public int sw = 0;

		public Way() {
		}

		public Way(Way way) {
			this.e = way.e;
			this.ne = way.ne;
			this.se = way.se;
			this.w = way.w;
			this.nw = way.nw;
			this.sw = way.sw;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Way way = (Way) o;
			return e == way.e && ne == way.ne && se == way.se && w == way.w && nw == way.nw && sw == way.sw;
		}

		@Override
		public int hashCode() {
			return Objects.hash(e, ne, se, w, nw, sw);
		}

		@Override
		public String toString() {
			return "Way{" +
				"e=" + e +
				", ne=" + ne +
				", se=" + se +
				", w=" + w +
				", nw=" + nw +
				", sw=" + sw +
				'}';
		}
	}
}
