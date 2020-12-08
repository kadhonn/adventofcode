import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8_1 {


    public static void main(String[] args) throws IOException {
        List<String> cmds = Files.readAllLines(new File("./in/day8.in").toPath());

        int acc = 0;
        int pc = 0;
        Set<Integer> alreadyVisited = new HashSet<>();

        while (!alreadyVisited.contains(pc)) {
            alreadyVisited.add(pc);
            String[] cmd = cmds.get(pc).split(" ");

            switch (cmd[0]) {
                case "nop":
                    pc++;
                    break;
                case "acc":
                    acc += Integer.parseInt(cmd[1]);
                    pc++;
                    break;
                case "jmp":
                    pc += Integer.parseInt(cmd[1]);
                    break;
            }
        }

        System.out.println(acc);
    }
}
