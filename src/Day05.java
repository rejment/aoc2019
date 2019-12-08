import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Day05 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day05.txt"));
        int[] rom = Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray();
        simulate(rom, new ArrayList<>(Collections.singletonList(1)));
        simulate(rom, new ArrayList<>(Collections.singletonList(5)));
    }

    private static int simulate(int[] rom, List<Integer> input) {
        int[] ram = Arrays.copyOf(rom, rom.length);
        int pc = 0;
        while (true) {
            int instruction = ram[pc];
            int opcode = instruction % 100;
            int arg1 = read(ram, pc + 1, 0);
            int arg2 = read(ram, pc + 2, 0);
            int arg3 = read(ram, pc + 3, 0);
            int parameter1 = read(ram, arg1, (instruction / 100) % 10);
            int parameter2 = read(ram, arg2, (instruction / 1000) % 10);

            switch (opcode) {
                case 1:
                    ram[arg3] = parameter1 + parameter2;
                    pc += 4;
                    break;
                case 2:
                    ram[arg3] = parameter1 * parameter2;
                    pc += 4;
                    break;
                case 3:
                    ram[arg1] = input.remove(0);
                    pc += 2;
                    break;
                case 4:
                    System.out.println(parameter1);
                    pc += 2;
                    break;
                case 5: {
                    if (parameter1 != 0) {
                        pc = parameter2;
                    } else {
                        pc += 3;
                    }
                    break;
                }
                case 6: {
                    if (parameter1 == 0) {
                        pc = parameter2;
                    } else {
                        pc += 3;
                    }
                    break;
                }
                case 7: {
                    ram[arg3] = parameter1 < parameter2 ? 1 : 0;
                    pc += 4;
                    break;
                }
                case 8: {
                    ram[arg3] = parameter1 == parameter2 ? 1 : 0;
                    pc += 4;
                    break;
                }

                case 99:
                    return ram[0];
                default:
                    throw new RuntimeException("error, invalid opcode " + ram[pc] + " at pc " + pc);
            }
        }
    }

    private static int read(int[] ram, int par, int mode) {
        if (mode == 0) {
            return par >= 0 && par < ram.length ? ram[par] : -999999;
        } else if (mode == 1) {
            return par;
        }
        throw new RuntimeException("error");
    }
}
