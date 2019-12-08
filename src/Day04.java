public class Day04 {
    public static void main(String[] args) {
        int MIN = 168630;
        int MAX = 718098;

        int count = 0;
        int count2 = 0;
        for (int code = MIN; code <= MAX; code++) {
            char[] chars = (code + "  ").toCharArray();
            boolean increasing = true;
            boolean twosame = false;
            boolean onedouble = false;
            int run = 0;
            for (int i = 0; i < 6; i++) {
                if (i < 5 && chars[i + 1] < chars[i]) {
                    increasing = false;
                }
                if (chars[i + 1] == chars[i]) {
                    twosame = true;
                    run += 1;
                } else {
                    if (run == 1) {
                        onedouble = true;
                    }
                    run = 0;
                }
            }
            if (increasing && twosame) {
                count++;
            }
            if (increasing && onedouble) {
                count2++;
            }
        }
        System.out.println(count);
        System.out.println(count2);
    }
}
