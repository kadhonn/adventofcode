import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day20_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day20.in").toPath());

        Map<Integer, Image> images = parseInput(inputs);


        Map<String, Set<Integer>> borderToTileMap = new HashMap<>();
        for (Map.Entry<Integer, Image> entry : images.entrySet()) {
            Set<String> borders = getAllBordersForImage(entry.getValue());
            for (String border : borders) {
                if (!borderToTileMap.containsKey(border)) {
                    borderToTileMap.put(border, new HashSet<>());
                }
                borderToTileMap.get(border).add(entry.getKey());
            }
        }

        Map<Integer, Integer> singleBorderCounts = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> borderToTile : borderToTileMap.entrySet()) {
            if (borderToTile.getValue().size() == 1) {
                int singleId = borderToTile.getValue().iterator().next();
                singleBorderCounts.merge(singleId, 1, Integer::sum);
            }
        }

        long prod = 1;
        for (Map.Entry<Integer, Integer> singleBorderCount : singleBorderCounts.entrySet()) {
            if (singleBorderCount.getValue() == 4) {
                prod *= singleBorderCount.getKey();
            }
        }
        System.out.println(prod);
    }

    private static class Image {
        private final List<List<Character>> data;

        public Image(List<List<Character>> data) {
            this.data = data;
        }

        public int size() {
            return data.size();
        }

        public Character get(int x, int y) {
            return data.get(x).get(y);
        }
    }

    private static Set<String> getAllBordersForImage(Image value) {
        StringBuilder upper = new StringBuilder();
        StringBuilder lower = new StringBuilder();
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            upper.append(value.get(0, i));
            lower.append(value.get(value.size() - 1, i));
            left.append(value.get(i, 0));
            right.append(value.get(i, value.size() - 1));
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

    private static Map<Integer, Image> parseInput(List<String> inputs) {
        Map<Integer, Image> images = new HashMap<>();
        for (int i = 0; i < inputs.size(); i += 12) {
            String header = inputs.get(i);
            int id = Integer.parseInt(header.substring(5, header.length() - 1));

            List<List<Character>> image = new ArrayList<>();
            for (int j = 1; j < 11; j++) {
                String input = inputs.get(i + j);
                List<Character> line = new ArrayList<>();
                for (Character c : input.toCharArray()) {
                    line.add(c);
                }
                image.add(line);
            }

            images.put(id, new Image(image));
        }
        return images;
    }
}
