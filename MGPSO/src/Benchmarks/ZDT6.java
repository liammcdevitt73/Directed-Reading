package Benchmarks;

import java.util.Arrays;

/**
 * ZDT problem 6.
 *
 * @author Liam McDevitt
 */
public class ZDT6 extends Problem {

    /**
     * Initializes the problem with the optimized MGPSO parameters.
     */
    public ZDT6() {

        // Initializing the ZDT2 problem to the optimized parameters presented in the original MGPSO paper.
        super(10, 2, 1, 49, 3, 0.6, 1.85, 1.55, 1.80);

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
        return 1 - Math.exp(-4 * x[0]) * Math.pow(Math.sin(6 * Math.PI * x[0]), 6);
    } // f1

    @Override
    public double g(double[] x) {

        double sum = 0;

        for (int i = 1; i < this.n; i++)
            sum += x[i];

        return 1 + (9 * Math.pow(sum / (this.n - 1), 0.25));

    } // g

    @Override
    public double h(double f1, double g) {
        return 1.0 - Math.pow(f1 / g, 2);
    } // h

} // ZDT6
