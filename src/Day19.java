import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day19.txt"));
        String input = inputs.get(0);
        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();

        int sum = 0;
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 50; x++) {
                if (inBeam(rom, y, x)) {
                    sum += 1;
                }
            }
        }
        System.out.println("Part 1: " + sum);

        int x = 10;
        int y = 0;
        while (true) {
            if (inBeam(rom, y, x)) {
                if (inBeam(rom, y + 99, x - 99)) break;
                x++;
            } else {
                y++;
            }
        }
        System.out.println("Part2: " + ((10000 * (x - 99)) + y));
    }

    private static boolean inBeam(long[] rom, int y, int x) {
        Intcode intcode = new Intcode(rom);
        intcode.input.add((long) x);
        intcode.input.add((long) y);
        intcode.simulate();
        return intcode.output.remove(0) == 1L;
    }
}
