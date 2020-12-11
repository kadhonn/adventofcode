import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day11_1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> input = Files.readAllLines(new File("./in/day11.in").toPath());

        int[][] field = new int[input.size()][input.get(0).length()];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = input.get(i).charAt(j);
            }
        }

        while (runOnce(field)) {
//            printField(field);
        }

        int count = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == '#') {
                    count++;
                }
            }
        }

        System.out.println(count);
    }

    private static void printField(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.print((char) field[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean runOnce(int[][] field) {
        boolean changed = false;
        int[][] copy = copy(field);
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                int current = copy[i][j];
                if (current != '.') {
                    int count = countNeighbours(copy, i, j);
                    if (current == '#') {
                        if (count >= 4) {
                            field[i][j] = 'L';
                            changed = true;
                        }
                    } else {
                        if (count == 0) {
                            field[i][j] = '#';
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    private static int countNeighbours(int[][] field, int initI, int initJ) {
        int count = 0;
        for (int i = initI - 1; i <= initI + 1; i++) {
            for (int j = initJ - 1; j <= initJ + 1; j++) {
                if ((i != initI || j != initJ) && 0 <= i && i < field.length && 0 <= j && j < field[i].length && field[i][j] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    private static int[][] copy(int[][] field) {
        int[][] copy = field.clone();
        for (int i = 0; i < copy.length; i++) {
            copy[i] = copy[i].clone();
        }
        return copy;
    }

}
