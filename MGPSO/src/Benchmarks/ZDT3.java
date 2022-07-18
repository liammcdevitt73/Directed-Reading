package Benchmarks;

import java.util.Arrays;

/**
 * ZDT problem 3.
 *
 * @author Liam McDevitt
 */
public class ZDT3 extends Problem {

    /**
     * Initializes the problem with the optimized MGPSO parameters.
     */
    public ZDT3() {

        // Initializing the ZDT3 problem to the optimized parameters presented in the original MGPSO paper.
        super(30, 2, 8, 42, 3, 0.05, 1.85, 1.90, 1.90);

        // Initializing the mins
        double [] mins = new double [this.n];
        Arrays.fill(mins, 0);
        setMins(mins);

        // Initializing the maxs
        double [] maxs = new double [this.n];
        Arrays.fill(maxs, 1);
        setMaxs(maxs);

    } // Constructor

    @Override
    public double f1(double[] x) {
        return x[0];
    } // f1

    @Override
    public double g(double[] x) {

        double sum = 0;

        for (int i = 1; i < this.n; i++)
            sum += (x[i] / (this.n - 1));

        return 1 + (9 * sum);

    } // g

    @Override
    public double h(double f1, double g) {
        return 1.0 - Math.sqrt(f1 / g) - ((f1 / g) * Math.sin(10 * Math.PI * f1));
    } // h

} // ZDT3
