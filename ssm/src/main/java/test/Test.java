package test;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        double a = 2.3;
        double b = 100000;
        System.out.println(a*b);
        BigDecimal a1 = new BigDecimal(Double.toString(a));
        BigDecimal b1 = new BigDecimal(Double.toString(b));
        System.out.println(a1+"===="+b1);
        //System.out.println(a1.m);
    }

}
