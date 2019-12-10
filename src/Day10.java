import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {
    static class Pos {
        int x, y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Pos k) {
            return Math.sqrt((x - k.x) * (x - k.x) + (y - k.y) * (y - k.y));
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/day10.txt"));
        List<Pos> pos = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                if (input.get(y).charAt(x) == '#') {
                    pos.add(new Pos(x, y));
                }
            }
        }

        Map<Double, List<Pos>> angles = new TreeMap<>(Comparator.comparingDouble(k -> k));
        for (Pos po : pos) {
            Map<Double, List<Pos>> directions = pos.stream()
                    .filter(x -> x != po)
                    .sorted(Comparator.comparingDouble(k -> k.dist(po)))
                    .collect(Collectors.groupingBy(o -> atan2(o.y - po.y, o.x - po.x)));

            if (directions.size() > angles.size()) {
                angles.clear();
                angles.putAll(directions);
            }
        }
        System.out.println(angles.size());
        Pos pos1 = angles.entrySet().stream().skip(199).findFirst().get().getValue().get(0);
        System.out.println(pos1.x*100 + pos1.y);
    }

    private static double atan2(double dy, double dx) {
        double v = Math.atan2(dx, -dy);
        while (v < 0) v += 2 * Math.PI;
        return v % (2 * Math.PI);
    }
}
