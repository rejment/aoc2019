import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day09 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day09.txt"));
        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();

        Intcode intcode = new Intcode(rom);
        intcode.input.add(1L);
        intcode.simulate();

        intcode = new Intcode(rom);
        intcode.input.add(2L);
        intcode.simulate();

    }

    static class Intcode {
        long pc = 0;
        long[] ram;
        List<Long> input = new ArrayList<>();
        List<Long> output = new ArrayList<>();
        long result;
        long lastOutput;
        long base = 0;
        long instruction;

        public Intcode(long[] ram) {
            this.ram = Arrays.copyOf(ram, 1000000);
        }

        public long simulate() {
            while (true) {
                this.instruction = ram[(int) pc];
                long opcode = instruction % 100;

                switch ((int) opcode) {
                    case 1: {
                        write(3, read(1) + read(2));
                        pc += 4;
                        break;
                    }
                    case 2: {
                        write(3, read(1) * read(2));
                        pc += 4;
                        break;
                    }
                    case 3: {
                        if (input.isEmpty()) return -1;
                        write(1, input.remove(0));
                        pc += 2;
                        break;
                    }
                    case 4: {
                        long value = read(1);
                        System.out.println("OUTPUT: " + value);
                        output.add(value);
                        lastOutput = value;
                        pc += 2;
                        break;
                    }
                    case 5: {
                        if (read(1) != 0) {
                            pc = read(2);
                        } else {
                            pc += 3;
                        }
                        break;
                    }
                    case 6: {
                        if (read(1) == 0) {
                            pc = read(2);
                        } else {
                            pc += 3;
                        }
                        break;
                    }
                    case 7: {
                        write(3, read(1) < read(2) ? 1L : 0L);
                        pc += 4;
                        break;
                    }
                    case 8: {
                        write(3, read(1) == read(2) ? 1 : 0);
                        pc += 4;
                        break;
                    }
                    case 9: {
                        base += read(1);
                        pc += 2;
                        break;
                    }
                    case 99:
                        this.result = ram[0];
                        return 0;
                    default:
                        throw new RuntimeException("error, invalid opcode " + ram[(int) pc] + " at pc " + pc);
                }
            }
        }
        private void write(int p, long value) {
            long arg = ram[(int) pc + p];
            long mode = (instruction / (long) Math.pow(10, 1+p)) % 10;
            if (mode == 0) {
                ram[(int) arg] = value;
            } else if (mode == 2) {
                ram[(int) (arg + base)] = value;
            } else {
                throw new RuntimeException();
            }
        }

        private long read(int p) {
            long arg = ram[(int) pc + p];
            long mode = (instruction / (long) Math.pow(10, 1+p)) % 10;
            if (mode == 0) {
                return ram[(int) arg];
            } else if (mode == 1) {
                return arg;
            } else if (mode == 2) {
                return ram[(int) (arg + base)];
            }

            throw new RuntimeException();
        }
    }

}
