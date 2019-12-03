import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day03 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/day03.txt"));
        List<String> line1 = Stream.of(input.get(0).split(",")).collect(Collectors.toList());
        List<String> line2 = Stream.of(input.get(1).split(",")).collect(Collectors.toList());

        Map<String, Integer> seen = simulate(line1);
        Map<String, Integer> seen2 = simulate(line2);
        int minDist = 10000000;
        int minSteps = 10000000;
        for (String s : seen.keySet()) {
            if (seen2.containsKey(s)) {
                minDist = Math.min(minDist, Math.abs(Integer.parseInt(s.split(" ")[0])) + Math.abs(Integer.parseInt(s.split(" ")[1])));
                minSteps = Math.min(minSteps, (seen.get(s) + seen2.get(s)));
            }
        }
        System.out.println(minDist);
        System.out.println(minSteps);
    }

    private static Map<String, Integer> simulate(List<String> collect) {
        Map<String, Integer> seen = new HashMap<>();
        int steps = 0;
        int x = 0;
        int y = 0;
        for (String dir : collect) {
            int d = Integer.parseInt(dir.substring(1));
            for (int i = 0; i < d; i++) {
                steps++;
                switch (dir.substring(0, 1)) {
                    case "U":
                        seen.put(x + " " + (--y), steps);
                        break;
                    case "D":
                        seen.put(x + " " + (++y), steps);
                        break;
                    case "L":
                        seen.put((--x) + " " + y, steps);
                        break;
                    case "R":
                        seen.put((++x) + " " + y, steps);
                        break;
                }
            }
        }
        return seen;
    }
}
