package MGPSO;

import Benchmarks.Problem;
import java.util.Random;

/**
 * This class is an implementation of the MGPSO algorithm based on the original PhD thesis and resulting MGPSO paper.
 *
 * @author Liam McDevitt
 * Date: 2021-07-06
 */
public class MGPSO {

    private Random   r;               // The random instance used for this run's seed
    private Problem  p;               // The multi-objective problem we're trying to solving

    public Archive  Archive;          // The archive management strategy
    private Swarm [] S;               // One swarm per objective

    private int      t;               // To keep track of the current MGPSO iteration

    /**
     * Initialize an MGPSO object.
     * @param rand            The instance of random used throughout the implementation.
     * @param prob            The problem we're trying to optimize.
     * @param numOfParticles  The total number of particles split between all swarms.
     */
    public MGPSO (Random rand, Problem prob, int numOfParticles) {

        // Initialize instance of random
        r = rand;

        // Initialize problem
        p = prob;

        // Initialize the archive
        Archive = new Archive(r, p, numOfParticles);

        // Initialize the swarms
        S = new Swarm [p.nObj];

        // Initialize S1
        S[0] = new Swarm(r, p, 0, p.S1);

        // Initialize S2
        S[1] = new Swarm(r, p, 1, p.S2);

        // Initialize the MGPSO starting iteration
        t = 0;

    } // Constructor

    /**
     * This method performs one iteration of the MGPSO algorithm when called.
     */
    public void iterate () {

        // Update swarms & archive
        for (int m = 0; m < p.nObj; m++) {
            for (int i = 0; i < S[m].size(); i++) {
                Particle particle = S[m].getParticles()[i];
                double [] f = p.objectives(particle.getPosition());
                particle.setFitnesses(f);
                if (f[m] < particle.getBestFitness()) {
                    particle.setBestPosition(particle.getPosition());
                    particle.setBestFitness(f[m]);
                    if (f[m] < S[m].getBestFitness())
                        S[m].setiBest(i);
                }
                Archive.update(new Particle (particle));
            }
        }

        // Move particles through the search space
        for (int m = 0; m < p.nObj; m++) {
            for (int i = 0; i < S[m].size(); i++) {
                Particle particle = S[m].getParticles()[i];
                double [] a = Archive.selection(p.T);
                double [] v = new double [p.n];
                double lambda = S[m].getLambda();
                for (int d = 0; d < p.n; d++) {
                    double r1 = Helper.exclusive0to1(r);
                    double r2 = Helper.exclusive0to1(r);
                    double r3 = Helper.exclusive0to1(r);
                    v[d] =  (p.w * particle.getVelocity()[d]) +
                            (p.c1 * r1 * (particle.getBestPosition()[d] - particle.getPosition()[d])) +
                            (lambda * p.c2 * r2 * (S[m].getBestPosition()[d] - particle.getPosition()[d])) +
                            ((1 - lambda) * p.c3 * r3 * (a[d] - particle.getPosition()[d]));
                }
                particle.setVelocity(v);
                particle.setPosition(Helper.add(particle.getPosition(), v));
            }
        }

        // Increment iteration counter
        t++;

    } // iterate

    /**
     * @return int, the current iteration of the MGPSO algorithm.
     */
    public int getT() {
        return t;
    } // getT

} // MGPSO
