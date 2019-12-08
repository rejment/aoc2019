import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day07 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day07.txt"));
        int[] rom = Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray();

        int max = 0;
        for (List<Integer> permutation : permutations(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)))) {
            Intcode[] m = permutation.stream().map(i -> new Intcode(rom, i)).toArray(Intcode[]::new);
            m[0].input.add(0);
            for (int i=0; i<4; i++) {
                m[i].output = m[i+1].input;
            }
            while (true) {
                m[0].simulate();
                m[1].simulate();
                m[2].simulate();
                m[3].simulate();
                int ret = m[4].simulate();
                if (ret == 0) {
                    max = Math.max(max, m[4].lastoutput);
                    break;
                }
            }
        }
        System.out.println(max);

        max = 0;
        for (List<Integer> permutation : permutations(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9)))) {
            Intcode[] m = permutation.stream().map(i -> new Intcode(rom, i)).toArray(Intcode[]::new);
            m[0].input.add(0);
            for (int i=0; i<4; i++) {
                m[i].output = m[i+1].input;
            }
            m[4].output = m[0].input;
            while (true) {
                m[0].simulate();
                m[1].simulate();
                m[2].simulate();
                m[3].simulate();
                int ret = m[4].simulate();
                if (ret == 0) {
                    max = Math.max(max, m[4].lastoutput);
                    break;
                }
            }
        }
        System.out.println(max);
    }

    public static <E> List<List<E>> permutations(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = permutations(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    private static class Intcode {
        int pc = 0;
        int[] ram;
        List<Integer> input = new ArrayList<>();
        List<Integer> output = new ArrayList<>();
        int result;
        int lastoutput;

        public Intcode(int[] ram, int input) {
            this.ram = Arrays.copyOf(ram, ram.length);
            this.input.add(input);
        }

        int simulate() {
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
                        if (input.isEmpty()) return -1;
                        ram[arg1] = input.remove(0);
                        pc += 2;
                        break;
                    case 4:
                        //System.out.println("OUTPUT: " + parameter1);
                        output.add(parameter1);
                        lastoutput = parameter1;
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
                        //System.out.println("Halt");
                        this.result = ram[0];
                        return 0;
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
            throw new RuntimeException("err0r");
        }
    }
}
