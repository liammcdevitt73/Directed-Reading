package Benchmarks;

/**
 * Base class for the ZDT problems.
 *
 * Each problem consists of minimizing two functions: f1 & f2.
 *
 * The problems came from Zitzler et al. in https://dl.acm.org/doi/10.1162/106365600568202.
 *
 * @author Liam McDevitt
 *
 * NOTES:
 *
 * (1) The optimized MGPSO parameters are taken from the original paper.
 *
 * (2) The min and max are defined as arrays because for the fourth FDT problem the domain for the first decision
 *     variable is different from the rest.
 */
public abstract class Problem {

    // Problem variables
    public int       n;                   // Number of problem dimensions
    public double [] mins;                 // The min of the range
    public double [] maxs;                 // The max of the range

    // Optimized MGPSO parameters
    public int    nObj;                   // The number of objective for the problem
    public int    S1;                     // The cardinality of swarm 1
    public int    S2;                     // The cardinality of swarm 2
    public int    T;                      // Tournament size (for the archive guide selection process)
    public double w;                      // Inertia weight
    public double c1;                     // Cognitive acceleration coefficient
    public double c2;                     // Social acceleration coefficient
    public double c3;                     // Archive acceleration coefficient

    /**
     * Initializes the problem with the optimized MGPSO parameters.
     * @param n     Number of problem dimensions
     * @param nObj  Number of objectives for the problem
     * @param S1    Cardinality of the first swarm
     * @param S2    Cardinality of the second swarm
     * @param T     Tournament size
     * @param w     Inertia weight
     * @param c1    Cognitive acceleration coefficient
     * @param c2    Social acceleration coefficient
     * @param c3    Archive acceleration coefficient
     */
    public Problem (int n, int nObj, int S1, int S2, int T, double w, double c1, double c2, double c3) {

        // Initializing the number of dimensions
        this.n = n;

        // Initializing the number of objectives
        this.nObj = nObj;

        // Initializing the cardinality of swarm 1
        this.S1 = S1;

        // Initializing the cardinality of swarm 2
        this.S2 = S2;

        // Initializing the tournament size
        this.T = T;

        // Initializing the inertia weight
        this.w = w;

        // Initializing the cognitive acceleration coefficient
        this.c1 = c1;

        // Initializing the social acceleration coefficient
        this.c2 = c2;

        // Initializing the archive acceleration coefficient
        this.c3 = c3;

        // Initializing min range array
        mins = new double [n];

        // Initializing max range array
        maxs = new double [n];

    } // Constructor

    /**
     * Function f1.
     * @param x The decision vector.
     * @return  double, f1(x).
     */
    public abstract double f1 (double [] x);

    /**
     * Function g.
     * @param x The decision vector.
     * @return  double, g(x).
     */
    public abstract double g (double [] x);

    /**
     * Function h.
     * @param f1 The resulting value from evaluating f1.
     * @param g  The resulting value from evaluating g.
     * @return   double, h(f1, g).
     */
    public abstract double h (double f1, double g);

    /**
     * Function f2, which is the result of multiplying the result of function g by function h.
     * @param g The resulting value from evaluating g.
     * @param h The resulting value from evaluating h.
     * @return  double, g * h.
     */
    public double f2 (double g, double h) {return g * h;}

    /**
     * Gives the fitness of a decision vector on a specified objective.
     * @param objIndex The objective (either 1 or 2)
     * @param x        The decision vector.
     * @return         double, f1(x) or f2(g, h) depending on the objective selected.
     */
    public double fitness (int objIndex, double [] x) {

        double f1 = f1(x);
        double g = g(x);
        double h = h(f1, g);
        double f2 = f2(g, h);

        if (objIndex == 1)
            return f1;
        else if (objIndex == 2)
            return f2;
        else
            return Double.MAX_VALUE;

    } // fitness

    /**
     * Used for knowing the fitness of a decision vector on both objectives.
     * @param x The decision vector.
     * @return  double [], the resulting fitness values for x on both objectives.
     */
    public double [] objectives (double [] x) {

        double f1 = f1(x);
        double g = g(x);
        double h = h(f1, g);
        double f2 = f2(g, h);

        double [] result = new double [nObj];
        result[0] = f1;
        result[1] = f2;

        return result;

    } // objectives

    /**
     * Setting the min domains for each decision variable.
     * @param mins The min value for each decision variable in the decision vector.
     */
    public void setMins (double [] mins) {
        System.arraycopy(mins, 0, this.mins, 0, mins.length);
    } // setMins

    /**
     * Setting the max domains for each decision variable.
     * @param maxs The max value for each decision variable in the decision vector.
     */
    public void setMaxs (double [] maxs) {
        System.arraycopy(maxs, 0, this.maxs, 0, maxs.length);
    } // setMaxs

} // Problem
