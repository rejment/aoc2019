import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day22 {

    private static class BigPair {
        public BigInteger a;
        public BigInteger b;

        public BigPair(BigInteger a, BigInteger b) {
            this.a = a;
            this.b = b;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Path.of("src/day22.txt"));

        BigInteger cards = BigInteger.valueOf(10007L);
        BigPair f = factors(inputs, cards);
        BigInteger o = f.b.multiply(BigInteger.ONE.subtract(f.a)).multiply(BigInteger.ONE.subtract(f.a).mod(cards).modInverse(cards)).mod(cards);
        for (int i = 0; i < 10007; i++) {
            if (2019L == f.a.multiply(BigInteger.valueOf(i)).add(o).mod(cards).longValue()) {
                System.out.println(i);
                break;
            }
        }

        cards = BigInteger.valueOf(119315717514047L);
        BigInteger reps = BigInteger.valueOf(101741582076661L);
        f = factors(inputs, cards);
        BigInteger x = f.a.modPow(reps, cards);
        o = f.b.multiply(BigInteger.ONE.subtract(x)).multiply(BigInteger.ONE.subtract(f.a).mod(cards).modInverse(cards)).mod(cards);
        System.out.println(x.multiply(BigInteger.valueOf(2020)).add(o).mod(cards));

    }

    public static BigPair factors(List<String> inputs, BigInteger cards) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ZERO;
        for (String input : inputs) {
            if (input.startsWith("deal with increment ")) {
                int inc = Integer.parseInt(input.substring("deal with increment ".length()));
                a = a.multiply(BigInteger.valueOf(inc).modInverse(cards)).mod(cards);
            } else if (input.equals("deal into new stack")) {
                a = a.negate().mod(cards);
                b = b.add(a).mod(cards);
            } else if (input.startsWith("cut ")) {
                int cut = Integer.parseInt(input.substring(4));
                b = b.add(a.multiply(BigInteger.valueOf(cut))).mod(cards);
            }
        }
        return new BigPair(a, b);
    }
}
