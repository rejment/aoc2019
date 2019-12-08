import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day06 {

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day06.txt"));
        Map<String, String> direct = new HashMap<>();
        for (String input : inputs) {
            String[] split = input.split("\\)");
            direct.put(split[1], split[0]);
        }

        int sum = direct.keySet().stream().mapToInt(s -> getPath(direct, s, new ArrayList<>()).size()).sum();
        System.out.println(sum);

        List<String> path1 = getPath(direct, "YOU", new ArrayList<>());
        List<String> path2 = getPath(direct, "SAN", new ArrayList<>());
        while (path1.get(0).equals(path2.get(0))) {
            path1.remove(0);
            path2.remove(0);
        }
        System.out.println(path1.size() + path2.size());
    }

    private static List<String> getPath(Map<String, String> direct, String s, List<String> path) {
        String s1 = direct.get(s);
        if (s1 != null) {
            path.add(0,s1);
            return getPath(direct, s1, path);
        }
        return path;
    }
}
