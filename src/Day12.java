import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {

    public static void main(String[] args) throws IOException {
        List<long[]> input = read();
        for (int time = 0; time < 1000; time++) step(input);
        long part1 = input.stream().mapToLong(moon -> (Math.abs(moon[0]) + Math.abs(moon[1]) + Math.abs(moon[2])) * (Math.abs(moon[3]) + Math.abs(moon[4]) + Math.abs(moon[5]))).sum();
        System.out.println(part1);

        List<long[]> s0 = read();
        long part2 = 1;
        for (int d = 0; d < 3; d++) {
            List<long[]> s1 = read();

            for (long t = 0; t < 10000000; t++) {
                step(s1);
                int dd = d;
                if (IntStream.range(0, 4).allMatch(m -> s1.get(m)[dd] == s0.get(m)[dd] && s1.get(m)[dd + 3] == s0.get(m)[dd + 3])) {
                    part2 = lcm(t + 1, part2);
                    break;
                }
            }
        }
        System.out.println(part2);
    }


    private static long lcm(long x, long y) {
        return x * y / gcd(x, y);
    }

    private static long gcd(long x, long y) {
        return y == 0 ? x : gcd(y, x % y);
    }

    private static void step(List<long[]> moons) {
        for (int i = 0; i < 3; i++) {
            long[] moon1 = moons.get(i);
            for (int j = i + 1; j < 4; j++) {
                long[] moon2 = moons.get(j);
                for (int d = 0; d < 3; d++) {
                    int sign = (int) Math.signum(moon1[d] - moon2[d]);
                    moon1[d + 3] -= sign;
                    moon2[d + 3] += sign;
                }
            }
        }
        moons.forEach(moon -> {
            for (int d = 0; d < 3; d++) moon[d] += moon[d + 3];
        });
    }

    private static List<long[]> read() throws IOException {
        return Files.lines(Path.of("src/day12.txt")).map(l -> Stream.of(l.replaceAll("[><xyz =]", "").split(",")).mapToLong(Long::parseLong).toArray()).map(x -> Arrays.copyOf(x, 6)).collect(Collectors.toList());
    }
}
