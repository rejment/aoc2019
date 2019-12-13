import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day13 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day13.txt"));
        String input = inputs.get(0);

        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();
        Intcode intcode = new Intcode(rom);

        intcode.ram[0] = 2;
        Map<String, Long> screen = new HashMap<>();
        long score = 0;
        long part1 = 0;
        while (true) {
            long res = intcode.simulate();

            while (intcode.output.size() > 2) {
                long x = intcode.output.remove(0);
                long y = intcode.output.remove(0);
                long z = intcode.output.remove(0);
                String key = x + "," + y;
                if (x == -1 && y == 0) {
                    score = z;
                } else {
                    screen.put(key, z);
                }
            }

            int blocks = (int) screen.values().stream().filter(value -> value == 2).count();
            if (part1 == 0) {
                part1 = blocks;
                System.out.println("Part1: " + blocks);
            }
            if (blocks == 0) {
                System.out.println("Part2: " + score);
            }

            long ballX = -1;
            long minpaddle = 1000;
            long maxpaddle = -1;
            for (Map.Entry<String, Long> e : screen.entrySet()) {
                int x = Integer.parseInt(e.getKey().split(",")[0]);
                if (e.getValue() == 4) ballX = x;
                if (e.getValue() == 3) {
                    minpaddle = Math.min(minpaddle, x);
                    maxpaddle = Math.max(maxpaddle, x);
                }
            }
            long joystick = 0;
            if (minpaddle > ballX) joystick = -1;
            if (maxpaddle < ballX) joystick = 1;
            intcode.input.add(joystick);

            if (res == 0) break;
        }
    }
}
