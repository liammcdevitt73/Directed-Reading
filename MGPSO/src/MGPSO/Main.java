package MGPSO;

import Benchmarks.*;
import java.util.*;

/**
 * This class is the main class of the MGPSO package. It acts as a place to run the algorithm.
 *
 * Running this will run MGPSO on the ZDT problems for 2000 iterations for 30 runs. This can be changed within the
 * creation of the main constructor.
 *
 * @author Liam McDevitt
 */
public class Main {

    private Random r;    // Our shared instance of random throughout the package
    private long   seed; // The seed we use to initialize our instance of random

    /**
     * This is the main constructor to the MGPSO package.
     * @param run             The current run we're on.
     * @param problemName     The current problem we're trying to solve.
     * @param numOfIterations The number of iterations we want to run the MGPSO for.
     */
    public Main (int run, String problemName, int numOfIterations) {

        // Initialize our instance of random with a random seed
        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed);
        System.out.println("Seed: " + seed + "\n");

        // Initialize problem
        Problem problem = null;
        switch(problemName) {
            case "ZDT1" -> problem = new ZDT1();
            case "ZDT2" -> problem = new ZDT2();
            case "ZDT3" -> problem = new ZDT3();
            case "ZDT4" -> problem = new ZDT4();
            case "ZDT6" -> problem = new ZDT6();
            default -> System.out.println("Problem not found.");
        }

        // Initialize MGPSO
        MGPSO mgpso = new MGPSO(r, problem, 50);

        // Iterate MGPSO
        while (mgpso.getT() < numOfIterations)
            mgpso.iterate();

        // If you would just like to see the archive at the end, uncomment this line.
        //mgpso.Archive.printArchive();

        // Print out final POF
        mgpso.Archive.printArchiveMinimal();

        // Print archive to file
        mgpso.Archive.printArchiveToFile(run, problemName);

    } // Constructor

    public static void main(String[] args) {

        // Setting up run settings
        int numOfRuns = 30;
        int numOfIterations = 2000;
        String [] problems = {"ZDT1", "ZDT2", "ZDT3", "ZDT4", "ZDT6"};

        // Running the program
        Main m;
        for (int i = 1; i <= numOfRuns; i++)
            for (String problem : problems)
                m = new Main(i, problem, numOfIterations);

        // If you'd like to just do one run, use this line and comment out the above loops.
        //m = new Main (1, "ZDT1", 2000);

    }
}
