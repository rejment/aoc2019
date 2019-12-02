import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day02 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day02.txt"));
        int[] rom = Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println(simulate(rom, 12, 2));

        for (int noun = 0; noun < rom.length; noun++) {
            for (int verb = 0; verb < rom.length; verb++) {
                if (simulate(rom, noun, verb) == 19690720) {
                    System.out.println(100 * noun + verb);
                }
            }
        }
    }

    private static int simulate(int[] rom, int noun, int verb) {
        int[] ram = Arrays.copyOf(rom, rom.length);
        ram[1] = noun;
        ram[2] = verb;
        int pc = 0;
        while (true) {
            switch (ram[pc]) {
                case 1:
                    ram[ram[pc + 3]] = ram[ram[pc + 1]] + ram[ram[pc + 2]];
                    pc += 4;
                    break;
                case 2:
                    ram[ram[pc + 3]] = ram[ram[pc + 1]] * ram[ram[pc + 2]];
                    pc += 4;
                    break;
                case 99:
                    return ram[0];
                default:
                    throw new RuntimeException("error, invalid opcode " + ram[pc] + " at pc " + pc);
            }
        }
    }
}
