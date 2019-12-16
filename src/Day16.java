import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
    public static void main(String[] args) throws Exception {
        List<String> inputs = Files.readAllLines(Path.of("src/day16.txt"));
        String input = inputs.get(0);
        char[] chars = input.toCharArray();
        int[] nums = new int[chars.length];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = chars[i % chars.length] - '0';
        }

        int[] output = fft(nums);
        System.out.println(IntStream.of(output).limit(8).mapToObj(a -> "" + a).collect(Collectors.joining()));

        nums = new int[chars.length * 10000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = chars[i % chars.length] - '0';
        }
        output = fft2(nums);
        int offset = Integer.parseInt(input.substring(0, 7));
        System.out.println(IntStream.of(output).skip(offset).limit(8).mapToObj(a -> "" + a).collect(Collectors.joining()));
    }

    private static int[] fft(int[] nums) {
        int[] base = {0, 1, 0, -1};
        int[] out = new int[nums.length];
        for (int i = 0; i < 100; i++) {
            for (int p = 0; p < nums.length; p++) {
                int x = 0;
                for (int p2 = 0; p2 < nums.length; p2++) {
                    x = (x + (nums[p2] * base[(((p2 + 1) / (p + 1))) & 3]));
                }
                out[p] = Math.abs(x) % 10;
            }
            System.arraycopy(out, 0, nums, 0, nums.length);
        }
        return out;
    }

    private static int[] fft2(int[] nums) {
        int[] res = new int[nums.length];
        for (int p = 0; p < 100; p++) {
            int x = 0;
            for (int i = 1; i < nums.length / 2; i++) {
                x += nums[nums.length - i];
                res[res.length - i] = x;
            }
            for (int i = 0; i < res.length; i++) {
                res[i] = Math.abs(res[i]) % 10;
            }
            System.arraycopy(res, 0, nums, 0, nums.length);
        }
        return res;
    }
}
