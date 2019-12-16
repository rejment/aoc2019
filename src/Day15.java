import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Day15 {
    private static class Pos {
        int x, y;
        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return x == pos.x && y == pos.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    private static class State {
        Intcode copy;
        Pos pos;
        int depth;
        int found;

        public State(Intcode copy, Pos pos, int depth) {
            this.copy = copy;
            this.pos = pos;
            this.depth = depth;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day15.txt"));
        String input = inputs.get(0);
        long[] rom = Stream.of(input.split(",")).mapToLong(Long::parseLong).toArray();
        Intcode intcode = new Intcode(rom);

        search(intcode);
        fill(found);
    }

    private static void fill(Pos found) {
        List<Pos> current = new ArrayList<>();
        current.add(found);

        int i = 0;
        while (current.size() > 0) {
            List<Pos> news = new ArrayList<>();
            for (Pos remove : current) {
                for (int dir = 1; dir <= 4; dir++) {
                    Pos newpos = new Pos(remove.x + dx[dir - 1], remove.y + dy[dir - 1]);
                    State state = seen.get(newpos);
                    if (state.found == 0) {
                        state.found = 1;
                        news.add(newpos);
                    }
                }
            }
            if (!news.isEmpty()) {
                i++;
            }
            current = news;
        }
        System.out.println("part 2: " + i);
    }

    private static int[] dx = {0, 0, -1, 1};
    private static int[] dy = {-1, 1, 0, 0};
    private static Pos found = new Pos(0, 0);
    private static Map<Pos, State> seen = new HashMap<>();

    private static void search(Intcode intcode) {
        List<State> states = new ArrayList<>();
        states.add(new State(intcode, new Pos(0, 0), 0));
        while (states.size() > 0) {
            State state = states.remove(0);
            for (int dir = 1; dir <= 4; dir++) {
                Pos newpos = new Pos(state.pos.x + dx[dir - 1], state.pos.y + dy[dir - 1]);
                if (seen.containsKey(newpos)) continue;
                Intcode copy = state.copy.copy();
                State newstate = new State(copy, newpos, state.depth + 1);
                seen.put(newpos, newstate);
                copy.input.add((long) dir);
                copy.simulate();
                long remove = copy.output.remove(0);
                if (remove == 0) {
                    newstate.found = 3;
                } else if (remove == 2) {
                    System.out.println("part1: " + (state.depth + 1));
                    found = newpos;
                    states.add(newstate);
                    newstate.found = 1;
                } else if (remove == 1) {
                    states.add(newstate);
                }
            }
        }
    }
}
