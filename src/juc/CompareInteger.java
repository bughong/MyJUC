package juc;

/**
 * @author HongTaiwei
 * @date 2021/8/9
 */
public class CompareInteger {

    static int a = 1000;
    static Integer b = -128;
    static Integer c = -128;

    public static void main(String[] args) {
        System.out.println(intCompare(a, b));
        System.out.println(IntegerCompare(b, c));
    }

    private static boolean intCompare(int a, Integer b) {
        return a == b;
    }

    private static boolean IntegerCompare(Integer b, Integer c) {
        return b == c;
    }
}
