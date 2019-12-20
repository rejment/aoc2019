import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day20 {

    private static class State {
        public final int x, y, s, l;
        public State(int x, int y, int s, int l) {
            this.x = x;
            this.y = y;
            this.s = s;
            this.l = l;
        }
    }


    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day20.txt"));
        int h = inputs.size();
        int w = 130;
        int[][] m = new int[h + 1][w + 1];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                m[i][j] = j >= inputs.get(i).length() ? ' ' : inputs.get(i).charAt(j);
            }
        }

        Map<String, List<State>> from = getPortalPoints(m);
        Map<String, State> portals = getPortalsConnections(from);

        State start = from.get("AA").get(0);
        State end = from.get("ZZ").get(0);

        int search = search(m, start.x, start.y, end.x, end.y, portals);
        System.out.println(search);

        int search2 = search2(m, start.x, start.y, end.x, end.y, portals);
        System.out.println(search2);
    }

    private static int[] dx = {1, 0, -1, 0};
    private static int[] dy = {0, 1, 0, -1};

    private static int search(int[][] m, int x0, int y0, int x1, int y1, Map<String, State> portals) {
        List<State> states = new ArrayList<>();
        states.add(new State(x0, y0, 0, 0));
        Set<String> seen = new HashSet<>();
        seen.add(x0 + "," + y0);
        int shortest = 1;
        while (!states.isEmpty()) {
            State state = states.remove(0);
            for (int dir = 0; dir < 4; dir++) {
                int nx = state.x + dx[dir];
                int ny = state.y + dy[dir];
                if (nx == x1 && ny == y1) {
                    return (state.s + 1);
                }
                if (m[ny][nx] != '.') continue;
                if (!seen.add(nx + "," + ny)) continue;

                State ns = new State(nx, ny, state.s + 1, 0);
                states.add(ns);
                State p = portals.get(nx + "," + ny);
                if (p != null) {
                    states.add(new State(p.x, p.y, ns.s + 1, 0));
                }
            }
        }

        return shortest;
    }

    private static int search2(int[][] m, int x0, int y0, int x1, int y1, Map<String, State> portals) {
        List<State> states = new ArrayList<>();
        states.add(new State(x0, y0, 0, 0));
        Set<String> seen = new HashSet<>();
        seen.add(x0 + "," + y0 + ",0");
        while (!states.isEmpty()) {
            State state = states.remove(0);
            if (state.x == x1 && state.y == y1 && state.l == 0) return state.s;

            for (int dir = 0; dir < 4; dir++) {
                int nx = state.x + dx[dir];
                int ny = state.y + dy[dir];
                if (m[ny][nx] != '.') continue;
                if (!seen.add(nx + "," + ny + "," + state.l)) continue;
                states.add(new State(nx, ny, state.s + 1, state.l));
            }

            State p = portals.get(state.x + "," + state.y);
            if (p != null) {
                if (p.x==2 || p.y==2 || p.x==122 || p.y==118) {
                    states.add(new State(p.x, p.y, state.s + 1, state.l + 1));
                } else if (state.l > 0) {
                    states.add(new State(p.x, p.y, state.s + 1, state.l - 1));
                }
            }
        }
        return -1;
    }


    private static Map<String, State> getPortalsConnections(Map<String, List<State>> from) {
        Map<String, State> portals = new HashMap<>();
        for (Map.Entry<String, List<State>> e : from.entrySet()) {
            List<State> value = e.getValue();
            if (value.size() == 2) {
                portals.put(value.get(0).x + "," + value.get(0).y, value.get(1));
                portals.put(value.get(1).x + "," + value.get(1).y, value.get(0));
            }
        }
        return portals;
    }

    private static Map<String, List<State>> getPortalPoints(int[][] m) {
        Map<String, List<State>> from = new HashMap<>();
        for (int y = 0; y < m.length - 1; y++) {
            for (int x = 0; x < m[2].length - 1; x++) {
                if (Character.isUpperCase(m[y][x])) {
                    for (int dir = 0; dir < 2; dir++) {
                        int y1 = y + dy[dir];
                        int x1 = x + dx[dir];
                        if (Character.isUpperCase(m[y1][x1])) {
                            String name = ((char) m[y][x]) + "" + ((char) m[y1][x1]);
                            int y2 = y1 + dy[dir];
                            int x2 = x1 + dx[dir];
                            if (y2 > 0 && x2 > 0 && m[y2][x2] == '.') {
                                from.computeIfAbsent(name, n->new ArrayList<>()).add(new State(x2, y2, 0, 0));
                            }
                            int y3 = y - dy[dir];
                            int x3 = x - dx[dir];
                            if (y3 > 0 && x3 > 0 && m[y3][x3] == '.') {
                                from.computeIfAbsent(name, n->new ArrayList<>()).add(new State(x3, y3, 0, 0));
                            }
                        }
                    }
                }
            }
        }
        return from;
    }
}
