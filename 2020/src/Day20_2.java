import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day20_2 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day20.in").toPath());

        Map<Integer, SmallImage> images = parseInput(inputs);


        Map<String, Set<Integer>> borderToTileMap = new HashMap<>();
        for (Map.Entry<Integer, SmallImage> entry : images.entrySet()) {
            Set<String> borders = entry.getValue().getBorders();
            for (String border : borders) {
                if (!borderToTileMap.containsKey(border)) {
                    borderToTileMap.put(border, new HashSet<>());
                }
                borderToTileMap.get(border).add(entry.getKey());
            }
        }

        Map<Coord, SmallImage> bigImageTiles = new HashMap<>();
        Set<Coord> todoCoords = new HashSet<>();
        Coord startCoord = new Coord(0, 0);
        todoCoords.add(startCoord);
        bigImageTiles.put(startCoord, images.values().iterator().next());

        while (!todoCoords.isEmpty()) {
            Coord currentCoord = todoCoords.iterator().next();
            todoCoords.remove(currentCoord);
            if (!bigImageTiles.containsKey(currentCoord)) {
                continue;
            }
            SmallImage currentImage = bigImageTiles.get(currentCoord);

            Coord tryCoord = new Coord(currentCoord.x + 1, currentCoord.y);
            if (!bigImageTiles.containsKey(tryCoord)) {
                searchAndAddTile(tryCoord, bigImageTiles, Direction.LEFT, currentImage.getBorder(Direction.RIGHT), currentImage.id, borderToTileMap, images);
                todoCoords.add(tryCoord);
            }
            tryCoord = new Coord(currentCoord.x - 1, currentCoord.y);
            if (!bigImageTiles.containsKey(tryCoord)) {
                searchAndAddTile(tryCoord, bigImageTiles, Direction.RIGHT, currentImage.getBorder(Direction.LEFT), currentImage.id, borderToTileMap, images);
                todoCoords.add(tryCoord);
            }
            tryCoord = new Coord(currentCoord.x, currentCoord.y + 1);
            if (!bigImageTiles.containsKey(tryCoord)) {
                searchAndAddTile(tryCoord, bigImageTiles, Direction.UP, currentImage.getBorder(Direction.DOWN), currentImage.id, borderToTileMap, images);
                todoCoords.add(tryCoord);
            }
            tryCoord = new Coord(currentCoord.x, currentCoord.y - 1);
            if (!bigImageTiles.containsKey(tryCoord)) {
                searchAndAddTile(tryCoord, bigImageTiles, Direction.DOWN, currentImage.getBorder(Direction.UP), currentImage.id, borderToTileMap, images);
                todoCoords.add(tryCoord);
            }
        }

        BigImage image = new BigImage(bigImageTiles);

        while (true) {
            int countMonsters = countMonsters(image);
            if (countMonsters != 0) {
                int count = 0;
                for (int x = 0; x < image.size(); x++) {
                    for (int y = 0; y < image.size(); y++) {
                        if (image.get(x, y) == '#') {
                            count++;
                        }
                    }
                }
                System.out.println(count - countMonsters * 15);
                return;
            }
            image.mutate();
        }

    }

    private static int countMonsters(BigImage image) {
        int count = 0;
        for (int y = 0; y < image.size(); y++) {
            for (int x = 0; x < image.size(); x++) {
                if (isMonster(x, y, image)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static final String MONSTER =
            "                  # \n" +
                    "#    ##    ##    ###\n" +
                    " #  #  #  #  #  #   ";

    private static boolean isMonster(int startX, int startY, BigImage image) {
        int i = 0;
        int x = startX;
        int y = startY;
        while (i < MONSTER.length()) {
            if (x >= image.size() || y >= image.size()) {
                return false;
            }
            char c = MONSTER.charAt(i++);
            if (c == '\n') {
                x = startX;
                y++;
                continue;
            }
            if (c == '#' && image.get(x, y) != '#') {
                return false;
            }
            x++;
        }
        return true;
    }

    private static void searchAndAddTile(Coord tryCoord, Map<Coord, SmallImage> bigImageTiles, Direction direction, String border, int id, Map<String, Set<Integer>> borderToTileMap, Map<Integer, SmallImage> images) {
        Set<Integer> borderSet = borderToTileMap.get(border);
        borderSet.remove(id);
        if (borderSet.isEmpty()) {
            return;
        }
        if (borderSet.size() > 1) {
            throw new IllegalStateException();
        }

        int otherId = borderSet.iterator().next();
        SmallImage otherImage = images.get(otherId);
        bigImageTiles.put(tryCoord, otherImage);

        while (!otherImage.getBorder(direction).equals(border)) {
            otherImage.mutate();
        }
    }

    private static abstract class Image {
        private int mutationState = 0;

        abstract int size();

        protected abstract Character getInternal(int x, int y);

        public void mutate() {
            mutationState = (mutationState + 1) % 16;
        }

        public Character get(int x, int y) {
            return rotateAndGet(x, y);
        }

        private Character rotateAndGet(int x, int y) {
            int rotation = mutationState % 4;
            switch (rotation) {
                case 0:
                    return flipAndGet(x, y);
                case 1:
                    return flipAndGet(size() - 1 - y, x);
                case 2:
                    return flipAndGet(size() - 1 - x, size() - 1 - y);
                case 3:
                    return flipAndGet(y, size() - 1 - x);
            }
            throw new IllegalStateException();
        }

        protected Character flipAndGet(int x, int y) {
            int flippiness = mutationState / 4;
            switch (flippiness) {
                case 0:
                    return getInternal(x, y);
                case 1:
                    return getInternal(size() - 1 - x, y);
                case 2:
                    return getInternal(x, size() - 1 - y);
                case 3:
                    return getInternal(size() - 1 - x, size() - 1 - y);
            }
            throw new IllegalStateException();
        }
    }

    private static class BigImage extends Image {
        private final int smallImageSize;
        private final List<List<? extends Image>> data;

        public BigImage(Map<Coord, ? extends Image> ass) {
            smallImageSize = ass.values().iterator().next().size();
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Coord coord : ass.keySet()) {
                maxX = Math.max(maxX, coord.x);
                maxY = Math.max(maxY, coord.y);
                minX = Math.min(minX, coord.x);
                minY = Math.min(minY, coord.y);
            }
            data = new ArrayList<>();
            for (int y = minY; y <= maxY; y++) {
                ArrayList<Image> line = new ArrayList<>();
                for (int x = minX; x <= maxX; x++) {
                    line.add(ass.get(new Coord(x, y)));
                }
                data.add(line);
            }
        }

        public int size() {
            //TODO lets hope
            return smallImageSize * data.size();
        }

        public Character getInternal(int x, int y) {
            return data.get(y / smallImageSize).get(x / smallImageSize).get(x % smallImageSize, y % smallImageSize);
        }
    }

    private static class SmallImage extends Image {
        private final int offset;
        private final List<String> data;
        public final int id;
        private final int size;
        //        private boolean borderMode = true;
        private boolean borderMode = false;

        public SmallImage(int offset, List<String> data, int id) {
            this.offset = offset;
            this.data = data;
            this.id = id;
            this.size = data.get(offset).length();
        }

        public int size() {
            if (borderMode) {
                return size;
            } else {
                return size - 2;
            }
        }

        public Character getInternal(int x, int y) {
            if (borderMode) {
                return data.get(offset + y).charAt(x);
            } else {
                return data.get(offset + y + 1).charAt(x + 1);
            }
        }

        public Set<String> getBorders() {
            //names are wrong, but noone cares here
            StringBuilder upper = new StringBuilder();
            StringBuilder lower = new StringBuilder();
            StringBuilder left = new StringBuilder();
            StringBuilder right = new StringBuilder();
            for (int i = 0; i < size; i++) {
                upper.append(data.get(offset).charAt(i));
                lower.append(data.get(offset + size - 1).charAt(i));
                left.append(data.get(offset + i).charAt(0));
                right.append(data.get(offset + i).charAt(size - 1));
            }
            return Set.of(
                    upper.toString(),
                    upper.reverse().toString(),
                    lower.toString(),
                    lower.reverse().toString(),
                    left.toString(),
                    left.reverse().toString(),
                    right.toString(),
                    right.reverse().toString()
            );
        }

        public String getBorder(Direction direction) {
            this.borderMode = true;

            StringBuilder border = new StringBuilder();

            for (int i = 0; i < size; i++) {
                switch (direction) {
                    case LEFT:
                        border.append(get(0, i));
                        break;
                    case RIGHT:
                        border.append(get(size() - 1, i));
                        break;
                    case UP:
                        border.append(get(i, 0));
                        break;
                    case DOWN:
                        border.append(get(i, size() - 1));
                        break;
                }
            }

            this.borderMode = false;

            return border.toString();
        }
    }

    private static Map<Integer, SmallImage> parseInput(List<String> inputs) {
        Map<Integer, SmallImage> images = new HashMap<>();
        for (int i = 0; i < inputs.size(); i += 12) {
            String header = inputs.get(i);
            int id = Integer.parseInt(header.substring(5, header.length() - 1));

            images.put(id, new SmallImage(i + 1, inputs, id));
        }
        return images;
    }

    private static class Coord {

        private final int x;
        private final int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x &&
                    y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static enum Direction {
        LEFT, RIGHT, UP, DOWN;
    }
}
