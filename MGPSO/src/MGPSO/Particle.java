package MGPSO;

import java.util.Arrays;
import Benchmarks.*;

/**
 * This class represents a Particle. Particle's will be initialized within a Swarm.
 * This can be used for any PSO-like implementation.
 *
 * @author Liam McDevitt
 * Date: 2021-06-29
 *
 */
public class Particle {

    private double [] position;          // Current position
    private double [] velocity;          // Current velocity
    private double [] bestPosition;      // Particle's personal best position
    private double [] fitness;           // Current position fitness on each objective function
    private double    bestFitness;       // Personal best fitness
    private Problem   p;                 // The multi-objective problem we're trying to solving
    private int       m;                 // This particle's primary objective relative to its swarm

    // Used in MGPSO's Archive
    public  double    crowdingDistance;  // The crowding distance of a particle in the POF

    /**
     * Initializes a particle to a particle which is passed in. Making this particle a copy.
     * @param copy The particle to copy.
     */
    public Particle (Particle copy) {

        // Copy the particle's problem
        p = copy.getP();

        // Copy the particle's primary objective
        m = copy.getM();

        // Copy the particle's position
        position = new double [copy.getPosition().length];
        setPosition(copy.getPositionCopy());

        // Copy the particle's velocity
        velocity = new double [copy.getVelocity().length];
        setVelocity(copy.getVelocityCopy());

        // Copy the particle's best position
        bestPosition = new double [position.length];
        setBestPosition(copy.getBestPositionCopy());

        // Copy the particle's fitness on each objective function
        fitness = new double [p.nObj];
        setFitnesses(copy.getFitnessesCopy());

        // Copy particle's best fitness
        setBestFitness(copy.getBestFitness());

    } // Copy constructor

    /**
     * Initializes a particle's position, velocity, bestPosition, and the corresponding fitness values.
     * @param pos      The initial position of the particle.
     * @param vel      The initial velocity of the particle.
     * @param prob     The multi-objective problem.
     * @param objIndex The particle's primary objective.
     */
    public Particle (double [] pos, double [] vel, Problem prob, int objIndex) {

        // Initialize the particle's position
        position = new double [pos.length];
        System.arraycopy(pos, 0, position, 0, position.length);

        // Initialize the particle's velocity
        velocity = new double [vel.length];
        System.arraycopy(vel, 0, velocity, 0, velocity.length);

        // Initialize problem
        p = prob;

        // Initialize this particle's primary objective
        m = objIndex;

        // Initialize the particle's best position
        bestPosition = new double [position.length];
        System.arraycopy(position, 0, bestPosition, 0, bestPosition.length);

        // Initialize particle's fitness to the worst possible case
        fitness = new double [p.nObj];
        Arrays.fill(fitness, Double.MAX_VALUE);

        // Initialize a particle's best fitness to the worst possible case
        bestFitness = Double.MAX_VALUE;

    } // Constructor

    /**
     * @return double [], the particle's position reference.
     */
    public double [] getPosition () {
        return position;
    } // getPosition

    /**
     * @return double [], a copy of the particle's position.
     */
    public double [] getPositionCopy () {
        double [] copy = new double[position.length];
        System.arraycopy(position, 0, copy, 0, copy.length);
        return copy;
    } // getPositionCopy

    /**
     * @return double [], the particle's velocity reference.
     */
    public double [] getVelocity () {
        return velocity;
    } //getVelocity

    /**
     * @return double [], a copy of the particle's velocity.
     */
    public double [] getVelocityCopy () {
        double [] copy = new double [velocity.length];
        System.arraycopy(velocity, 0, copy, 0, copy.length);
        return copy;
    } // getVelocityCopy

    /**
     * @return double [], the particle's best position reference.
     */
    public double [] getBestPosition () {
        return bestPosition;
    } // getBestPosition

    /**
     * @return double [], a copy of the particle's best position.
     */
    public double [] getBestPositionCopy () {
        double [] copy = new double [bestPosition.length];
        System.arraycopy(bestPosition, 0, copy, 0, copy.length);
        return copy;
    } // getBestPositionCopy

    /**
     * @return double, the fitness of the particle's current position.
     */
    public double getFitness () {
        return fitness[m];
    } // getFitness

    /**
     * @return double [], the fitness of the particle on each objective function reference.
     */
    public double [] getFitnesses () {
        return fitness;
    } // getFitnesses

    /**
     * @return double [], a copy of the fitness of the particle on each objective function.
     */
    public double [] getFitnessesCopy () {
        double [] result = new double[fitness.length];
        System.arraycopy(fitness, 0, result, 0, result.length);
        return result;
    } // getFitnessesCopy

    /**
     * @return double, the fitness of the best position found so far by the particle.
     */
    public double getBestFitness () {
        return bestFitness;
    } // getBestFitness

    /**
     * @return double, the crowding distance of the particle relative to others in the archive.
     */
    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    /**
     * @return Problem, the overarching multi-objective problem.
     */
    public Problem getP() {
        return p;
    } // getP

    /**
     * @return int, the primary objective of this particle relative to its swarm.
     */
    public int getM() {
        return m;
    } // getM

    /**
     * Copies the position past in to the current position of the particle.
     * @param position The particle's new position.
     */
    public void setPosition (double [] position) {
        // Keeps the particle's position within the bounds of the search space so we can evaluate the position
        // on the objective functions.
        position = Helper.normalize(position, p.mins, p.maxs);
        System.arraycopy(position, 0, this.position, 0, position.length);
    } // setPosition
//
    /**
     * Copies the velocity past in to the current velocity of the particle.
     * @param velocity The particle's new velocity.
     */
    public void setVelocity(double [] velocity) {
        System.arraycopy(velocity, 0, this.velocity, 0, velocity.length);
    } // setVelocity

    /**
     * Copies the position past in to the current best position found so far of the particle.
     * @param bestPosition The new best position found by the particle.
     */
    public void setBestPosition(double [] bestPosition) {
        System.arraycopy(bestPosition, 0, this.bestPosition, 0, bestPosition.length);
    } // setBestPosition

    /**
     * @param fitness The new fitness of the particle's position.
     */
    public void setFitness(double fitness) {
        this.fitness[m] = fitness;
    } // setFitness

    /**
     * @param fitness The new fitnesses of the particle's position on each objective.
     */
    public void setFitnesses (double [] fitness) {
        System.arraycopy(fitness, 0, this.fitness, 0, fitness.length);
    } // setFitnesses

    /**
     * @param bestFitness The new fitness of the particle's best found position.
     */
    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    } // setBestFitness

} // Particle
