import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8_2 {


    public static void main(String[] args) throws IOException {
        List<String> cmds = Files.readAllLines(new File("./in/day8.in").toPath());

        for (int i = 0; i < cmds.size(); i++) {
            if (swap(cmds, i)) {
                try {
                    int acc = tryRun(cmds);
                    System.out.println(acc);
                    System.exit(0);
                } catch (IllegalStateException e) {

                }
                swap(cmds, i);
            }
        }
    }

    private static boolean swap(List<String> cmds, int i) {
        String[] cmd = cmds.get(i).split(" ");
        switch (cmd[0]) {
            case "nop":
                cmd[0] = "jmp";
                break;
            case "acc":
                return false;
            case "jmp":
                cmd[0] = "nop";
                break;
        }
        cmds.set(i, cmd[0] + " " + cmd[1]);
        return true;
    }

    private static int tryRun(List<String> cmds) {
        int acc = 0;
        int pc = 0;
        Set<Integer> alreadyVisited = new HashSet<>();

        while (pc != cmds.size()) {
            if (alreadyVisited.contains(pc)) {
                throw new IllegalStateException();
            }
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
        return acc;
    }


}
