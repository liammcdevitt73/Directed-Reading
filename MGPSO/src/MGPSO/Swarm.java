package MGPSO;

import Benchmarks.Problem;

import java.util.Random;

/**
 * This class represents a swarm of particles. Each swarm optimizes one objective of our multi-objective problem.
 *
 * @author Liam McDevitt
 * Date: 2021-07-05
 *
 */
public class Swarm {

    private Random      r;            // The random instance used for this run's seed
    private Problem     p;            // The multi-objective problem we're trying to solving
    private int         m;            // Objective index - the specific objective this swarm is optimizing
    private int         s;            // The number of particles in the swarm

    private Particle [] particles;    // The particles which make up the swarm
    private int         iBest;        // The index of the best particle in the swarm
    private double      lambda;       // The archive balance coefficient [Exploitation vs. Exploration]

    /**
     * Initializes a swarm of particles.
     * @param rand     The instance of random used throughout the implementation.
     * @param prob     The problem we're trying to optimize.
     * @param objIndex The specific objective of the problem this swarm is optimizing.
     * @param size     The size of the swarm, i.e, the number of particles.
     */
    public Swarm (Random rand, Problem prob, int objIndex, int size) {

        // Initialize instance of random
        r = rand;

        // Initialize problem
        p = prob;

        // Initialize objective index
        m = objIndex;

        // Initialize size of swarm
        s = size;

        // Initialize particles array
        particles = new Particle [s];

        // DEFAULT: setting the best particle in the swarm to the first one that's created
        // This will be altered when determining the neighbourhood best
        iBest = 0;

        // Initialize the particles of the swarm randomly within the search space
        for (int i = 0; i < particles.length; i++) {

            double [] pos = new double [p.n]; // Position for a new particle
            double [] vel = new double [p.n]; // Velocity for a new particle

            // Initialize position randomly within the search space
            for (int j = 0; j < p.n; j++) {
                pos[j] = (r.nextDouble() * (p.maxs[j] - p.mins[j])) + p.mins[j];
                vel[j] = 0; // Initialize velocity to zero
            }

            // Create particle with the newly generated position and velocity
            particles[i] = new Particle (pos, vel, p, m);

            // Set the particle's best position so far to its first
            particles[i].setBestPosition(pos);

            // Calculate the newly generated particles fitness on its objective function
            double [] f = p.objectives(pos);
            particles[i].setFitnesses(f);
            particles[i].setBestFitness(f[m]);

            // Determining the neighbourhood best position
            if (particles[i].getFitness() < getBestFitness())
                iBest = i;

        }

        // Initialize archive balance coefficient -> U(0, 1)
        lambda = Helper.exclusive0to1(r);

    } // Constructor

    /**
     * @return Particle [], the swarm's particle's reference.
     */
    public Particle [] getParticles () {
        return particles;
    } // getParticles

    /**
     * @return Particle [], a copy of the swarm's particles.
     */
    public Particle [] getParticlesCopy () {
        Particle [] result = new Particle [particles.length];
        for (int i = 0; i < result.length; i++)
            result[i] = new Particle (particles[i]);
        return result;
    } // getParticlesCopy

    /**
     * @return double [], the swarm's best position reference.
     */
    public double [] getBestPosition () {
        return particles[iBest].getPosition();
    } // getBestPosition

    /**
     * @return double [], a copy of the swarm's best position.
     */
    public double [] getBestPositionCopy () {
        return particles[iBest].getBestPositionCopy();
    } // getBestPositionCopy

    /**
     * @return double, the fitness of the best particle found so far within the swarm.
     */
    public double getBestFitness() {
        return particles[iBest].getFitness();
    } // getBestFitness

    /**
     * @return double, the archive balance coefficient.
     */
    public double getLambda () {
        return lambda;
    } // getLambda

    /**
     * @return int, the objective this swarm is optimizing.
     */
    public int getM() {
        return m;
    } // getM

    /**
     * @return int, the number of particles in the swarm.
     */
    public int size () {return particles.length; }; // size

    /**
     * Setting the new found best particle index in the swarm to the current one.
     * @param iBest The new index of the best particle within the swarm.
     */
    public void setiBest(int iBest) {
        this.iBest = iBest;
    } // setiBest

} // Swarm
