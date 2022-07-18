package Benchmarks;

/**
 * ZDT problem 4.
 *
 * @author Liam McDevitt
 */
public class ZDT4 extends Problem {


    /**
     * Initializes the problem with the optimized MGPSO parameters.
     */
    public ZDT4() {

        // Initializing the ZDT4 problem to the optimized parameters presented in the original MGPSO paper.
        super(10, 2, 5, 45, 2, 0.175, 1.85, 1.35, 1.85);

        // Initializing the mins
        double [] mins = new double [this.n];
        mins[0] = 0;
        for (int i = 1; i < mins.length; i++) mins[i] = -5;
        setMins(mins);

        // Initializing the maxs
        double [] maxs = new double [this.n];
        maxs[0] = 1;
        for (int i = 1; i < maxs.length; i++) maxs[i] = 5;
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
            sum += (Math.pow(x[i], 2)) - (10 * Math.cos(4 * Math.PI * x[i]));

        return 1 + 10 * (this.n - 1) + sum;

    } // g

    @Override
    public double h(double f1, double g) {
        return 1.0 - Math.sqrt(f1 / g);
    } // h

} // ZDT4
