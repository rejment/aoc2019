import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day01 {
    public static void main(String[] args) throws IOException {
        int[] integers = Files.lines(Path.of("src/day01.txt")).mapToInt(Integer::parseInt).toArray();
        int s1 = 0;
        for (int integer : integers) {
            s1 += (integer / 3) - 2;
        }
        System.out.println(s1);

        int s2 = 0;
        for (int integer : integers) {
            while (integer >= 0) {
                integer = (integer / 3) -2;
                if (integer >= 0) {
                    s2 += integer;
                }
            }
        }
        System.out.println(s2);
    }
}
