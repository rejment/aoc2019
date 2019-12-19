import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 {

    private static class State {
        public int x, y, k, s;
        public State(int x, int y, int k, int s) {
            this.x = x;
            this.y = y;
            this.k = k;
            this.s = s;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day18.txt"));

        int w = inputs.get(0).length();
        int h = inputs.size();
        char[][] m = new char[h][w];
        int x0 = 0;
        int y0 = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                m[i][j] = inputs.get(i).charAt(j);
                if (m[i][j] == '@') {
                    y0 = j;
                    x0 = i;
                }
            }
        }

        int part1 = search(m, x0, y0, 0);
        System.out.println("Part 1: " + part1);

        m[y0 - 1][x0 - 1] = '@';
        m[y0 - 1][x0] = '#';
        m[y0 - 1][x0 + 1] = '@';
        m[y0][x0 - 1] = '#';
        m[y0][x0] = '#';
        m[y0][x0 + 1] = '#';
        m[y0 + 1][x0 - 1] = '@';
        m[y0 + 1][x0] = '#';
        m[y0 + 1][x0 + 1] = '@';

        int k0a = 0;
        int k0b = 0;
        int k0c = 0;
        int k0d = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char c = m[i][j];
                if (c >= 'a' && c <= 'z') {
                    int bit = 1 << (c - 'a');
                    if (i < y0 && j < x0) k0a |= bit;
                    if (i < y0 && j > x0) k0b |= bit;
                    if (i > y0 && j < x0) k0c |= bit;
                    if (i > y0 && j > x0) k0d |= bit;
                }
            }
        }

        int p1 = search(m, x0 - 1, y0 - 1, 0x7fffffff - k0a);
        int p2 = search(m, x0 + 1, y0 - 1, 0x7fffffff - k0b);
        int p3 = search(m, x0 - 1, y0 + 1, 0x7fffffff - k0c);
        int p4 = search(m, x0 + 1, y0 + 1, 0x7fffffff - k0d);
        int part2 = p1 + p2 + p3 + p4;
        System.out.println("Part 2: " + part2);
    }

    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {-1, 0, 1, 0};

    private static int search(char[][] m, int x0, int y0, int k0) {
        List<State> states = new ArrayList<>();
        states.add(new State(x0, y0, k0, 0));
        Set<String> seen = new HashSet<>();
        seen.add(x0 + "," + y0 + "," + k0);
        int mostKeys = 0;
        int shortest = 1000000;
        while (!states.isEmpty()) {
            State state = states.remove(0);
            for (int dir = 0; dir < 4; dir++) {
                int nx = state.x + dx[dir];
                int ny = state.y + dy[dir];
                int nk = state.k;
                int c = m[ny][nx];
                if (c == '#') continue;
                if (c >= 'A' && c <= 'Z' && (state.k & (1 << (c - 'A'))) == 0) continue;
                if (c >= 'a' && c <= 'z') nk |= (1 << (c - 'a'));
                String newKey = nx + "," + ny + "," + nk;
                if (seen.contains(newKey)) continue;
                seen.add(newKey);
                int dist = state.s + 1;
                if (nk == mostKeys) {
                    if (dist < shortest) {
                        shortest = dist;
                    }
                } else if (nk > mostKeys) {
                    mostKeys = nk;
                    shortest = dist;
                }
                states.add(new State(nx, ny, nk, dist));
            }
        }

        return shortest;
    }
}
