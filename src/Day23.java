import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day23.txt"));
        String input = inputs.get(0);
        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();

        Intcode[] intcode = new Intcode[50];
        for (int i = 0; i < 50; i++) {
            intcode[i] = new Intcode(rom);
            intcode[i].input.add((long) i);
        }
        for (int i = 0; i <50; i++) {
            intcode[i].others = intcode;
        }
        for (int k = 0; k < 1000; k++) {
            for (int i = 0; i < 50; i++) {
                intcode[i].simulate();
            }
        }
        System.out.println("Part 1:" + Intcode.firsty);


        long lasty=0;
        while (true) {
            for (int k = 0; k < 100; k++) {
                for (int i = 0; i < 50; i++) {
                    intcode[i].simulate();
                }
            }
            intcode[0].input.add(Intcode.natx);
            intcode[0].input.add(Intcode.naty);
            if (Intcode.naty == lasty) {
                break;
            }
            lasty = Intcode.naty;
        }
        System.out.println("Part2 :" + lasty);
    }


    public static class Intcode {
        long pc = 0;
        long[] ram;
        List<Long> input = new ArrayList<>();
        List<Long> output = new ArrayList<>();
        long result;
        long lastOutput;
        long base = 0;
        long instruction;
        Intcode[] others;
        static long firsty=-1;
        static long natx=0;
        static long naty=0;

        public Intcode(long[] ram) {
            this.ram = Arrays.copyOf(ram, 10000);
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
                        long val = (input.isEmpty()) ? -1 : input.remove(0);
                        write(1, val);
                        pc += 2;
                        return -1;
                    }
                    case 4: {
                        long value = read(1);
                        output.add(value);
                        if (output.size()>=3) {
                            int k = (int) (long) output.remove(0);
                            long x = output.remove(0);
                            long y = output.remove(0);
                            if (k>=0 && k<50) {
                                others[k].input.add(x);
                                others[k].input.add(y);
                                others[k].simulate();
                            } else if (k==255) {
                                if (firsty == -1) {
                                    firsty = y;
                                }
                                natx = x;
                                naty = y;
                            }
                        }
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
                        //System.out.prlongln("Halt");
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
