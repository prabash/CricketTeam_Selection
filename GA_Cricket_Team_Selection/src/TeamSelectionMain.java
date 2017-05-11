import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.MeanAlterer;
import org.jenetics.Mutator;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.util.Factory;
 
public class TeamSelectionMain {
    // 2.) Definition of the fitness function.
    private static Double eval(Genotype<IntegerGene> gt) {
    	
    	Double fitnessVal = 0.0;
    	Double ChromosomeFitness = 0.0;
    	
    	System.out.println("+++++++ EVAL START +++++++++");
    	
    	Integer counter = gt.length();
        for (int i = 0; i < counter; i++) {
        	
        	Chromosome<IntegerGene> ch = gt.getChromosome(i);
        	ChromosomeFitness = 0.0;
        	
        	for (int j = 0; j < ch.length(); j++) {
        		ChromosomeFitness += ch.getGene(i).intValue();
			}
        	System.out.println("Chromosome : " + gt.getChromosome(i) + ", Fitness value : " + ChromosomeFitness);
        	
        	if (ChromosomeFitness > fitnessVal)
        		fitnessVal = ChromosomeFitness;
		}
        System.out.println("+++++++ EVAL END +++++++++");
        
        
        System.out.println(fitnessVal);
        return fitnessVal;
    }
 
    
    public static void main(String[] args) {
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<IntegerGene>> gtf =
            Genotype.of(IntegerChromosome.of(1, 25, 11), 5);

        
        Genotype<IntegerGene> genotypeSet = ((Genotype<IntegerGene>)gtf);
        Integer counter = genotypeSet.length();
        for (int i = 0; i < counter; i++) {
        	System.out.println(genotypeSet.getChromosome(i));
		}
        
        // 3.) Create the execution environment.
        Engine<IntegerGene, Double> engine = Engine
            .builder(TeamSelectionMain::eval, gtf)
            .populationSize(1000)
			.optimize(Optimize.MAXIMUM)
			.alterers(
					new Mutator<>(0.03),
					new MeanAlterer<>(0.6))
            .build();
        
        // Create evolution statistics consumer.
     		final EvolutionStatistics<Double, ?>
     			statistics = EvolutionStatistics.ofNumber();
     		
        // 4.) Start the execution (evolution) and
        //     collect the result.
     		final Phenotype<IntegerGene, Double> best = engine.stream()
     				// Truncate the evolution stream after 20 "steady"
     				// generations.
     				.limit(bySteadyFitness(20))
     				// The evolution will stop after maximal 100
     				// generations.
     				.limit(100)
     				// Update the evaluation statistics after
     				// each generation
     				.peek(statistics)
     				//.peek(compStatistics)
     				// Collect (reduce) the evolution stream to
     				// its best phenotype.
     				.collect(toBestPhenotype());
         
     		System.out.println(statistics);
    		//System.out.println(compStatistics);
    		System.out.println(best);
    }
}

