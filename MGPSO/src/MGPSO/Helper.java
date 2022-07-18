package MGPSO;

import java.util.Random;

/**
 * This class is used as a place for general helpful methods for the MGPSO implementation.
 *
 * @author Liam McDevitt
 * Date: 2021-07-09
 */
public class Helper {

    /**
     * Normalizes/Bounds an arrays contents by a lower and upper bound.
     * @param x  The position we want in the bounds.
     * @param lb The lower bound.
     * @param ub The upper bound.
     * @return   double [], the resulting normalized position within the bounds.
     */
    public static double [] normalize (double [] x, double [] lb, double [] ub) {
        double [] result = new double [x.length];
        for (int i = 0; i < x.length; i++) {
            double value = x[i];
            if (value < lb[i])
                result[i] = lb[i];
            else if (value > ub[i])
                result[i] = ub[i];
            else
                result[i] = x[i];
        }
        return result;
    } // normalize

    /**
     * @return double, range (0, 1).
     */
    public static double exclusive0to1 (Random r) {
        double result = r.nextDouble();
        while (result == 0)
            result = r.nextDouble();
        return result;
    } // exclusive0to1

    /**
     * Adds two double arrays together (assuming they are the same size).
     * @param a The first array.
     * @param b The second array.
     * @return  double [], a new array with the contents of a + b.
     */
    public static double [] add (double [] a, double [] b) {
        double [] result = new double [a.length];
        for (int i = 0; i < result.length; i++)
            result[i] = a[i] + b[i];
        return result;
    } // add

} // Helper
