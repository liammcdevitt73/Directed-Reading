package MGPSO;

import Benchmarks.Problem;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class represents an Archive based on the original MGPSO thesis and resulting MGPSO paper.
 *
 * An archive is used in the MGPSO implementation to help guide particles towards the POF.
 *
 * @author Liam McDevitt
 * Date: 2021-07-09
 */
public class Archive {

    private Random              r;               // The random instance used for this run's seed
    private Problem             p;               // The multi-objective problem we're trying to solving

    private int                capacity;        // The maximum capacity of the archive
    public ArrayList<Particle> pool;            // The set of non-dominated solutions

    /**
     * Initializes the archive.
     * @param rand The instance of random used throughout the implementation.
     * @param prob The problem we're trying to optimize.
     * @param c    The total number of particle allowed in the archive (capacity).
     */
    public Archive (Random rand, Problem prob, int c) {

        // Initialize instance of random
        r = rand;

        // Initialize problem
        p = prob;

        // Initialize capacity of the archive
        capacity = c;

        // Initialize archive pool
        pool = new ArrayList<>();

    } // Constructor

    /**
     * Tournament selection on the archive for the MGPSO velocity update.
     * @param k The number of selected competitors.
     * @return  double [], the position of the winner of the tournament (largest crowding distance).
     */
    public double [] selection (int k) {

        // Calculating the crowding distances
        calculateCrowdingDistance();

        // Creating a competing list
        ArrayList<Particle> competing = new ArrayList<>();

        // Randomly selecting k particles from the pool to compete
        for (int i = 0; i < k; i++)
            competing.add(pool.get(r.nextInt(pool.size())));

        // Sorting the particles based on their crowding distance
        competing.sort(Comparator.comparingDouble(particle -> particle.crowdingDistance));

        // Return the position of the particle with the largest crowding distance (the least crowded)
        return competing.get(competing.size() - 1).getPositionCopy();

    } // selection

    /**
     * Checks if a particle is not dominated by any other in the pool.
     * @param x The particle we're checking for non-domination.
     * @return  boolean, true is the particle is non-dominated, false otherwise.
     */
    public boolean isNonDominated (Particle x) {

        double [] f_x = x.getFitnesses();

        for (Particle particle : pool) {
            double[] f_i = particle.getFitnesses();
            if (Arrays.equals(f_x, f_i)) continue;
            if (f_i[0] <= f_x[0] && f_i[1] <= f_x[1])
                return false;
        }

        return true;

    } // isNonDominated

    /**
     * Checking if one particle dominates another.
     * A particle dominates another if it is at least as fit in all objective than the other and strictly more fit for
     * at least one of them.
     * @param a Checking if this particle dominates b.
     * @param b Checking if this particle is dominated by a.
     * @return boolean, whether of not particle a dominates particle b.
     */
    public static boolean dominates (Particle a, Particle b) {

        if (Arrays.equals(a.getFitnesses(), b.getFitnesses())) return false;

        return a.getFitnesses()[0] <= b.getFitnesses()[0] && a.getFitnesses()[1] <= b.getFitnesses()[1];

    } // dominates

    /**
     * Calculates the crowding distance between particles within the archive. This is used in our archive management
     * system as well as our tournament selection process.
     *
     * Crowding distance is the normalized sum of individual distance values corresponding to each objective.
     *
     * A low crowding distance for a particle means it is similar to other (not great for diversity).
     *
     * A higher crowding distance for a particle means is it much different from the others.
     */
    public void calculateCrowdingDistance () {

        // Initialize the crowding distance to 0
        for (Particle particle : pool) particle.crowdingDistance = 0;

        // For each objective
        for (int m = 0; m < p.nObj; m++) {

            // Need this to use a lambda expression with the loop index
            final int finalM = m;
            // Sort archive based on the mth objective fitness values
            pool.sort(Comparator.comparingDouble(particle -> particle.getFitnesses()[finalM]));

            // Calculating the crowding distance for each particle in the pool
            for (int i = 0; i < pool.size(); i++) {

                // The particles on either end of the axis have only an inside neighbour
                if (i == 0 || i == pool.size() - 1)
                    pool.get(i).crowdingDistance = Double.MAX_VALUE;

                else {

                    // Getting the maximum and minimum fitness values for this objective
                    double objectiveMax = pool.get(pool.size() - 1).getFitnesses()[m];
                    double objectiveMin = pool.get(0).getFitnesses()[m];

                    // Getting the fitnesses of the particles to the left and right of the current
                    double left = pool.get(i - 1).getFitnesses()[m];
                    double right = pool.get(i + 1).getFitnesses()[m];

                    // Setting the crowding distance
                    if (objectiveMax != objectiveMin)
                        pool.get(i).crowdingDistance += (right - left) / (objectiveMax - objectiveMin);

                }

            }

        }

    } // calculateCrowdingDistance

    /**
     * This method acts as the archive management strategy (AMS).
     * @param x The particle we're attempting to add to the archive.
     */
    public void update (Particle x) {

        if (isNonDominated(x) && !duplicate(x)) {
            pool.removeIf(particle -> dominates(x, particle));
            pool.add(x);
            if (pool.size() > capacity) {
                removeMostCrowded();
            }
        }

    } // update

    /**
     * Checks if the passed in particle is in the archive.
     * We can't use .contains on the list for the particle directly because the particles in the archive has
     * crowding distances set after they enter the archive.
     * [There may be a better way to represent members of the archive instead of particles]
     * @param x The particle.
     * @return  boolean, true if the particle is in the archive. Otherwise, not.
     */
    public boolean duplicate (Particle x) {

        for (Particle particle : pool) {
            if (Arrays.equals(particle.getFitnesses(), x.getFitnesses()))
                return true;
        }

        return false;

    } // isDuplicate

    /**
     * Removes the most crowded individual from the archive.
     */
    public void removeMostCrowded () {

        // Calculate the crowding distances of the particle's in the archive to ensure they're up to date
        calculateCrowdingDistance();

        // Sort the archive based on the crowding distances of the particles from smallest to largest
        pool.sort(Comparator.comparingDouble(particle -> particle.crowdingDistance));

        // Remove the first particle from the archive because it is the most crowded after sorting
        pool.remove(0);

    } // removeMostCrowded

    /**
     * This method prints the fitness for each objective for each particle in the archive in a readable format.
     */
    public void printArchive () {

        System.out.println("                   Archive");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("        f1        f2          Crowding Distance");
        for (int i = 0; i < pool.size(); i++) {
            String label = "P[" + i + "]: ";
            double f1 = pool.get(i).getFitnesses()[0];
            double f2 = pool.get(i).getFitnesses()[1];
            double cD = pool.get(i).crowdingDistance;
            System.out.printf("%-7s %-10f %-10f %-10f\n", label, f1, f2, cD);
        }

    } // printPool

    /**
     * This method prints the fitness for each objective for each particle in the archive for making plots.
     *
     * Comma separated format.
     */
    public void printArchiveMinimal () {
        System.out.println("f1,f2");
        for (Particle particle : pool)
            System.out.println(particle.getFitnesses()[0] + "," + particle.getFitnesses()[1]);
    }

    /**
     * Prints the contents of the archive to the file.
     * @param run     The specific fun we're printing.
     * @param problem The problem we're solving.
     */
    public void printArchiveToFile (int run, String problem) {

        try {
            FileWriter f = new FileWriter("RunsTemp/" + problem + "_" + run + ".txt");
            for (Particle particle : pool)
                f.write(particle.getFitnesses()[0] + "," + particle.getFitnesses()[1] + "\n");
            f.close();
        }
        catch (IOException e) {
            System.out.println("Unable to print archive to file.");
        }

    } // printArchiveToFile

} // Archive
