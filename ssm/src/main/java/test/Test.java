package test;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        double a = 14.5;
        double b = 0.5;
      
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        double c = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        
    }

}
