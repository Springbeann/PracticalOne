import java.lang.annotation.Target;
import java.util.Random;
import java.util.function.DoublePredicate;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 * 
 * @author Jo Stevens
 * @version 1.0, 14 Nov 2008
 * 
 * @author Alard Roebroeck
 * @version 1.1, 12 Dec 2012
 * 
 */

public class PracticalGAs {

    static final String TARGET = "HELLO WORLD";
    static char[] alphabet = new char[27];

    /**
     * @param args
     */
    public static void main(String[] args) {
        int popSize = 100;
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabet[c - 'A'] = c;
        }
        alphabet[26] = ' ';
        Random generator = new Random(System.currentTimeMillis());
        Individual[] population = new Individual[popSize];
        // we initialize the population with random characters
        for (int i = 0; i < popSize; i++) {
            char[] tempChromosome = new char[TARGET.length()];
            for (int j = 0; j < TARGET.length(); j++) {
                tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; //choose a random letter in the alphabet
            }
            population[i] = new Individual(tempChromosome);
        }
        // What does your population look like?
        for (int i = 0; i < population.length; i++) {
            System.out.println(population[i].genoToPhenotype());
        }
        
        // do your own cool GA here
        double[] fitnessScores; 
        Individual[] parents; 
        for(int i = 0; i < population.length; i++){
            fitnessScores = calculateFitness(population, TARGET);
            for(int j = 0; j < fitnessScores.length; j++){
                System.out.print(fitnessScores[j] + " ");
            }
            System.out.println();

            parents = selectParents(population, fitnessScores); 
            for(int l = 0; l< parents.length; l++){
                char[] chromosome = parents[l].getChromosome();
                for(int k = 0; k < chromosome.length; k++){
                    System.out.print(chromosome[k] + " "); 
                }
                System.out.println(); 
            }
            System.out.println();
        }
    }

    public static double[] calculateFitness(Individual[] pop, String target){
        double fitness = 0; 
        double[] scores = new double[pop.length]; 
        char[] ch = new char[target.length()]; 
        double totalScore = 0; 
        for(int i = 0; i<target.length(); i++){
            ch[i] = target.charAt(i); 
        }
        
        for(int i = 0; i<pop.length; i++){
            fitness = 0; 
            char[] current = pop[i].getChromosome(); 
            for(int j = 0; j<current.length; j++){
                if(current[j] == ch[j]){
                    fitness++; 
                }
            }
            scores[i] = fitness; 
            double individualScore = fitness / target.length(); 
            scores[i] = individualScore; 
            pop[i].setFitness(individualScore); 
        }
        return scores; 
    }

    public static Individual[] selectParents(Individual[] pop, double[] scores){
        Random rand = new Random(); 
        HeapSort.sort(pop); 
        double totalFitness = 0; 
        double randDouble = 0;  
        Individual parentOne = null; 
        Individual parentTwo = null; 
        Individual[] parents = new Individual[2]; 

        for(int i = 0; i<pop.length; i++){
            totalFitness += scores[i]; 
        }
        for(int i = 0; i<pop.length; i++){
            scores[i] = scores[i] / totalFitness; 
        }
        
        while(parentOne == null || parentTwo == null){
            int parentIndx = 0; 
            double tempSum = 0; 
            randDouble = rand.nextDouble(); 
            for(int i = 0; i<pop.length; i++){
                tempSum += scores[i]; 
                if(tempSum >= randDouble){
                    parentIndx = i;
                    break; 
                }
            }
            if(parentOne == null){
                parentOne = pop[parentIndx]; 
            } else if(parentTwo == null && parentOne != pop[parentIndx]){
                parentTwo = pop[parentIndx]; 
            }
        }
        parents[0] = parentOne; 
        parents[1] = parentTwo; 

        return parents; 
    }

    public static double Mutation(){
        Random rand = new Random(); 
        return rand.nextDouble(); 
    }

    public static char mutatedLetter(){
        Random rand = new Random(); 
        return (char)(rand.nextInt(26) + 'A'); 
    }

    public static void offSpring(Individual[] parents, double rndmChance){
        Random rand = new Random(); 
        double rndmCh = Mutation(); 
        
        Individual[] offspring = new Individual[1]; 
        for(int i = 0; i<parents.length; i++){
            double temp = 0; 
            for(int j = 0; j<parents[i].getChromosome().length; j++){
                temp = Mutation(); 
                if(temp < rndmChance){
                    char newGene =  mutatedLetter(); 
                    char[] chromosome = parents[i].getChromosome(); 
                    chromosome[j] = newGene; 
                    parents[i].setChromosome(chromosome); 
                }
            }
        }
    }
}