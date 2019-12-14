import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 {

    private static class NameQty {
        public String name;
        public int qty;
        public List<NameQty> requirements;

        public NameQty(String r) {
            String[] s = r.split(" ");
            qty = Integer.parseInt(s[0]);
            name = s[1].trim();
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, NameQty> targets = new HashMap<>();
        List<String> inputs = Files.readAllLines(Path.of("src/day14.txt"));
        for (String input : inputs) {
            String[] split = input.split(" => ");
            NameQty target = new NameQty(split[1]);
            target.requirements = Stream.of(split[0].split(", ")).map(NameQty::new).collect(Collectors.toList());
            targets.put(target.name, target);
        }

        System.out.println(ores(targets, new HashMap<>(), "FUEL", 1));

        long lo = 0;
        long hi = 1000000000L;
        while (lo < hi) {
            long mid = (lo + hi + 1) >> 1;
            if (ores(targets, new HashMap<>(), "FUEL", mid) <= 1000000000000L) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        System.out.println(lo);
    }

    private static long ores(Map<String, NameQty> targets, Map<String, Long> rests, String type, long i) {
        if ("ORE".equals(type)) return i;
        NameQty makes = targets.get(type);
        long multiplier = (i + makes.qty - 1) / makes.qty;
        long sum = 0;
        for (NameQty other : makes.requirements) {
            long needs = multiplier * other.qty;
            long got = rests.getOrDefault(other.name, 0L);
            if (got > 0) {
                long use = Math.min(needs, got);
                rests.put(other.name, got - use);
                needs -= use;
            }
            if (needs > 0) {
                sum += ores(targets, rests, other.name, needs);
            }
        }
        rests.put(type, multiplier * makes.qty - i);
        return sum;
    }
}
