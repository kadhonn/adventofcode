import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day17_2 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day17.in").toPath());

        PocketDimension dim = new PocketDimension();

        for (int i = 0; i < inputs.size(); i++) {
            String line = inputs.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    dim.revive(new Coord(i, j, 0, 0));
                }
            }
        }

//        debugPrint(dim);

        for (int round = 1; round <= 6; round++) {
            Coord[] bounds = dim.getBounds();
            Coord min = bounds[0];
            Coord max = bounds[1];

            Set<Coord> toKill = new HashSet<>();
            Set<Coord> toRevive = new HashSet<>();

            for (int x = min.x - 1; x <= max.x + 1; x++) {
                for (int y = min.y - 1; y <= max.y + 1; y++) {
                    for (int z = min.z - 1; z <= max.z + 1; z++) {
                        for (int w = min.w - 1; w <= max.w + 1; w++) {
                            Coord coord = new Coord(x, y, z, w);
                            boolean isAlive = dim.isAlive(coord);
                            int count = dim.getAliveNeighbours(coord);
                            if (isAlive) {
                                if (count < 2 || count > 3) {
                                    toKill.add(coord);
                                }
                            } else {
                                if (count == 3) {
                                    toRevive.add(coord);
                                }
                            }
                        }
                    }
                }
            }
            for (Coord coord : toKill) {
                dim.kill(coord);
            }
            for (Coord coord : toRevive) {
                dim.revive(coord);
            }

//            debugPrint(dim);
        }

        System.out.println(dim.getAliveCount());
    }

    private static void debugPrint(PocketDimension dim) {
        Coord[] bounds = dim.getBounds();
        Coord min = bounds[0];
        Coord max = bounds[1];

        for (int z = min.z; z <= max.z; z++) {
            for (int w = min.w; w <= max.w; w++) {
                System.out.println("z: " + z + ", w: " + w);
                for (int x = min.x; x <= max.x; x++) {
                    for (int y = min.y; y <= max.y; y++) {
                        System.out.print(dim.isAlive(new Coord(x, y, z, w)) ? "#" : ".");
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();
        System.out.println("---------------------");
        System.out.println();
    }

    private static class Coord {
        public final int x;
        public final int y;
        public final int z;
        public final int w;

        public Coord(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x &&
                    y == coord.y &&
                    z == coord.z &&
                    w == coord.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }
    }

    private static class PocketDimension {
        private final Set<Coord> aliveCells = new HashSet<>();

        public void revive(Coord coord) {
            aliveCells.add(coord);
        }

        public void kill(Coord coord) {
            aliveCells.remove(coord);
        }

        public int getAliveCount() {
            return aliveCells.size();
        }

        public boolean isAlive(Coord coord) {
            return aliveCells.contains(coord);
        }

        public Coord[] getBounds() {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;
            int minW = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;
            int maxW = Integer.MIN_VALUE;
            for (Coord coord : aliveCells) {
                minX = Math.min(minX, coord.x);
                minY = Math.min(minY, coord.y);
                minZ = Math.min(minZ, coord.z);
                minW = Math.min(minW, coord.w);
                maxX = Math.max(maxX, coord.x);
                maxY = Math.max(maxY, coord.y);
                maxZ = Math.max(maxZ, coord.z);
                maxW = Math.max(maxW, coord.w);
            }

            return new Coord[]{new Coord(minX, minY, minZ, minW), new Coord(maxX, maxY, maxZ, maxW)};
        }

        public int getAliveNeighbours(Coord coord) {
            int sum = 0;
            for (int x = coord.x - 1; x <= coord.x + 1; x++) {
                for (int y = coord.y - 1; y <= coord.y + 1; y++) {
                    for (int z = coord.z - 1; z <= coord.z + 1; z++) {
                        for (int w = coord.w - 1; w <= coord.w + 1; w++) {
                            if (x != coord.x || y != coord.y || z != coord.z || w != coord.w) {
                                if (aliveCells.contains(new Coord(x, y, z, w))) {
                                    sum++;
                                }
                            }
                        }
                    }
                }
            }
            return sum;
        }
    }
}
