import java.util.Random;

/**
 * Basic Genetic Algorithm to evolve a string to match a target phrase.
 *
 * @version 1.1, 12 Dec 2012
 */
public class PracticalGAs {

    static final String TARGET = "HELLO WORLD";
    static char[] alphabet = new char[27];

    public static void main(String[] args) {
        int popSize = 100;
        int generations = 0;
        boolean found = false;
        initializeAlphabet();

        Random generator = new Random(System.currentTimeMillis());
        Individual[] population = new Individual[popSize];

        // Initialize the population with random characters
        for (int i = 0; i < popSize; i++) {
            char[] tempChromosome = new char[TARGET.length()];
            for (int j = 0; j < TARGET.length(); j++) {
                tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)];
            }
            population[i] = new Individual(tempChromosome);
        }

        while (!found && generations < 10000) { // Limiting to 10000 generations
            generations++;
            double[] fitnessScores = calculateFitness(population, TARGET);

            // Check if target is found
            for (int i = 0; i < population.length; i++) {
                if (fitnessScores[i] == 1.0) {
                    System.out.println("Found target at generation " + generations + ": " + population[i].genoToPhenotype());
                    found = true;
                    break;
                }
            }

            if (found) break;

            Individual[] newPopulation = new Individual[popSize];

            // Create new population using roulette wheel selection and crossover
            for (int i = 0; i < popSize; i++) {
                Individual parent1 = roulette(population, fitnessScores);
                Individual parent2 = roulette(population, fitnessScores);
                newPopulation[i] = crossover(parent1, parent2);
                mutate(newPopulation[i], 0.01); // Mutation rate is 1%
            }

            population = newPopulation;
        }

        if (!found) {
            System.out.println("Target not found within 10000 generations.");
        } else {
            System.out.println("Generations taken: " + generations);
        }
    }

    private static void initializeAlphabet() {
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabet[c - 'A'] = c;
        }
        alphabet[26] = ' ';
    }

    private static double[] calculateFitness(Individual[] pop, String target) {
        double[] scores = new double[pop.length];
        char[] targetChars = target.toCharArray();

        for (int i = 0; i < pop.length; i++) {
            double fitness = 0;
            char[] current = pop[i].getChromosome();

            for (int j = 0; j < current.length; j++) {
                if (current[j] == targetChars[j]) {
                    fitness++;
                }
            }

            double individualScore = fitness / target.length();
            scores[i] = individualScore;
            pop[i].setFitness(individualScore);
        }
        return scores;
    }

    private static Individual roulette(Individual[] pop, double[] scores) {
        double totalFitness = 0;

        for (double score : scores) {
            totalFitness += score;
        }

        double[] cumulativeProbabilities = new double[scores.length];
        cumulativeProbabilities[0] = scores[0] / totalFitness;

        for (int i = 1; i < scores.length; i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + (scores[i] / totalFitness);
        }

        Random rand = new Random();
        double randDouble = rand.nextDouble();

        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (randDouble <= cumulativeProbabilities[i]) {
                return pop[i];
            }
        }
        return pop[pop.length - 1]; // Return the last individual in case of rounding issues
    }

    private static Individual crossover(Individual parent1, Individual parent2) {
        Random random = new Random();
        char[] chromosome1 = parent1.getChromosome();
        char[] chromosome2 = parent2.getChromosome();
        char[] newChromosome = new char[chromosome1.length];
        for (int i = 0; i < chromosome1.length; i++) {
            newChromosome[i] = random.nextBoolean() ? chromosome1[i] : chromosome2[i];
        }
        return new Individual(newChromosome);
    }

    private static void mutate(Individual individual, double mutationRate) {
        Random random = new Random();
        char[] chromosome = individual.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            if (random.nextDouble() < mutationRate) {
                chromosome[i] = alphabet[random.nextInt(alphabet.length)];
            }
        }
        individual.setChromosome(chromosome);
    }
}

class Individual {
    private char[] chromosome;
    private double fitness;

    public Individual(char[] chromosome) {
        this.chromosome = chromosome;
    }

    public char[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(char[] chromosome) {
        this.chromosome = chromosome;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public String genoToPhenotype() {
        return new String(chromosome);
    }
}

class HeapSort {
    public static void sort(Individual[] array) {
        // Implement heap sort for Individual array
        // Sorting logic goes here
        //did not use heap sort for this
    }
}
