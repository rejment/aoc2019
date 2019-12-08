import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day08 {

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day08.txt"));

        String in = inputs.get(0);
        char[] chars = in.toCharArray();
        int min = 1000;
        int minLayer = 0;
        for (int l = 0; l < chars.length / (25*6); l++) {
            int zeros = getCount(chars, l, '0');
            if (zeros < min) {
                min = zeros;
                minLayer = l;
            }
        }
        System.out.println(getCount(chars, minLayer, '1') * getCount(chars, minLayer, '2'));

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 25; x++) {
                int col = '2';
                for (int l = 0; l < chars.length / (25*6); l++) {
                    int p = chars[l*(25*6) + y*25 + x];
                    if (p != '2') {
                        col = p;
                        break;
                    }
                }
                System.out.print(col == '1' ? "o " : "  ");
            }
            System.out.println();
        }
    }

    private static int getCount(char[] chars, int l, char c) {
        int count = 0;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 25; x++) {
                int p = chars[l * (25 * 6) + y * 25 + x];
                if (p == c) {
                    count++;
                }
            }
        }
        return count;
    }
}
