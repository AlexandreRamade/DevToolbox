package tools;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class MathTools {
	
	// méthode générées par l'IA de Google
	public static String generatePi(int numberOfDecimal) {
    	MathContext mc = new MathContext(numberOfDecimal);
        BigDecimal a = BigDecimal.ONE;
        BigDecimal b = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.sqrt(2)), mc);
        BigDecimal t = BigDecimal.valueOf(0.25);
        BigDecimal p = BigDecimal.ONE;
        BigDecimal pi;
        
        for (int i = 0; i < 10; i++) {
            BigDecimal aNext = a.add(b).divide(BigDecimal.valueOf(2), mc);
            BigDecimal bNext = BigDecimal.valueOf(Math.sqrt(a.multiply(b).doubleValue()));
            BigDecimal tNext = t.subtract(p.multiply(a.subtract(aNext).multiply(a.subtract(aNext), mc), mc), mc);
            a = aNext;
            b = bNext;
            t = tNext;
            p = p.multiply(BigDecimal.valueOf(2));
        }
        
        pi = a.add(b).multiply(a.add(b)).divide(t.multiply(BigDecimal.valueOf(4)), mc);
        return pi.toString();
    }
	
	// méthode générées par l'IA de Google
	public static String generatePhi(int numberOfDecimal) {
        // Define the precision
        MathContext mc = new MathContext(numberOfDecimal, RoundingMode.HALF_UP);

        // BigDecimal representation of 5
        BigDecimal five = new BigDecimal("5");

        // Calculate the square root of 5
        BigDecimal sqrtFive = sqrt(five, mc);

        // Calculate (1 + sqrt(5)) / 2
        BigDecimal one = new BigDecimal("1");
        BigDecimal two = new BigDecimal("2");
        BigDecimal phi = one.add(sqrtFive, mc).divide(two, mc);

        return phi.toString();
    }

    // Method to calculate the square root of a BigDecimal
    public static BigDecimal sqrt(BigDecimal value, MathContext mc) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, mc);
            x1 = x1.add(x0, mc);
            x1 = x1.divide(new BigDecimal("2"), mc);
        }
        return x1;
    }
}
