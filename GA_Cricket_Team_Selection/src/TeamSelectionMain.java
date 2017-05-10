import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
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
            .build();
        
	 
        // 4.) Start the execution (evolution) and
        //     collect the result.
        final EvolutionResult<IntegerGene, Double> result = Engine.builder(TeamSelectionMain::eval, gtf)
        		 .populationSize(2)
        		 .offspringFraction(1)
        	     .optimize(Optimize.MAXIMUM).build()
        	     .stream()
        	     .limit(1)
        	     .collect(EvolutionResult.toBestEvolutionResult());
         
        System.out.println("\n ++++++++++++++++++++++++++ RESULTS : ++++++++++++++++++++ \n");
        System.out.println(result.getBestFitness());
        System.out.println(result.getTotalGenerations());
        
        Phenotype<IntegerGene, Double> finalResults = result.getBestPhenotype();
        Integer length_ = finalResults.getGenotype().length();
        for (int i = 0; i < length_; i++) {
			System.out.println(finalResults.getGenotype().getChromosome(i));
		}
    }
}

