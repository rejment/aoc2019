import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day17.txt"));
        String input = inputs.get(0);
        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();
        Intcode intcode = new Intcode(rom);

        intcode.simulate();

        // collect board from outputs
        Map<String, Integer> board = new HashMap<>();
        String[] lines = intcode.output.stream().map(a -> "" + (char) (long) a).collect(Collectors.joining()).split("\n");
        int rows = lines.length;
        int cols = lines[0].length();
        for (int y1 = 0; y1 < rows; y1++) {
            for (int x1 = 0; x1 < cols; x1++) {
                board.put(x1 + "," + y1, (int) lines[y1].charAt(x1));
            }
        }

        // place O at intersections and count their sum
        int sum = 0;
        int px = -1;
        int py = -1;
        int dir = 0;
        String dirs = "^>v<";
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                String key0 = x + "," + y;
                char center = (char) (int) board.getOrDefault(key0, 0);
                if (center == '#' &&
                        board.getOrDefault((x + 1) + "," + y, 0) == '#' && board.getOrDefault(x + "," + (y + 1), 0) == '#' &&
                        board.getOrDefault((x - 1) + "," + y, 0) == '#' && board.getOrDefault(x + "," + (y - 1), 0) == '#') {
                    board.put(key0, (int) 'O');
                    sum += x * y;
                }
                int idx = dirs.indexOf(center);
                // find the starting position
                if (idx >= 0) {
                    px = x;
                    py = y;
                    dir = idx;
                }
            }
        }
        System.out.println("Part 1: " + sum);

        // make path through the "maze"
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        StringBuilder out = new StringBuilder();
        outer:
        while (true) {
            int steps = 0;
            while (true) {
                int px1 = px + dx[dir & 3];
                int py1 = py + dy[dir & 3];
                if (board.getOrDefault((px1 + "," + py1), (int) '.') == (int) '.') {
                    if (steps != 0) out.append(steps).append(",");
                    // have to turn, check if left
                    px1 = px + dx[(dir - 1) & 3];
                    py1 = py + dy[(dir - 1) & 3];
                    if (board.getOrDefault((px1 + "," + py1), (int) '.') != (int) '.') {
                        out.append("L,");
                        dir = dir - 1;
                        break;
                    }
                    // have to turn, check if right
                    px1 = px + dx[(dir + 1) & 3];
                    py1 = py + dy[(dir + 1) & 3];
                    if (board.getOrDefault((px1 + "," + py1), (int) '.') != (int) '.') {
                        out.append("R,");
                        dir = dir + 1;
                        break;
                    }
                    break outer;
                } else {
                    steps++;
                    px = px1;
                    py = py1;
                }
            }
        }
        String path = out.toString();

        // compress the path
        Matcher matcher = Pattern.compile("(.{1,21})\\1*(.{1,21})(?:\\1|\\2)*(.{1,21})(?:\\1|\\2|\\3)*").matcher(path);
        if (matcher.matches()) {
            String A = matcher.group(1);
            String B = matcher.group(2);
            String C = matcher.group(3);
            String prog = path.replace(A, "A,").replace(B, "B,").replace(C, "C,");

            // run last simulation
            intcode = new Intcode(rom);
            intcode.ram[0] = 2;
            intcode.input.addAll(makeLine(prog));
            intcode.input.addAll(makeLine(A));
            intcode.input.addAll(makeLine(B));
            intcode.input.addAll(makeLine(C));
            intcode.input.addAll(makeLine("n"));
            while (true) {
                long simulate = intcode.simulate();
                if (simulate == 0) {
                    break;
                }
            }
        }
        System.out.println("Part 2: " + intcode.lastOutput);

    }

    private static List<Long> makeLine(String str) {
        List<Long> a = new ArrayList<>();
        if (str.endsWith(",")) str = str.substring(0, str.length() - 1);
        for (int i = 0; i < str.length(); i++) {
            a.add((long) str.charAt(i));
        }
        a.add((long) 10);
        return a;
    }
}
