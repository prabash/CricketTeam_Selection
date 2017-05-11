import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

import java.util.List;

import org.jenetics.*;
import org.jenetics.engine.*;
import org.jenetics.util.IntRange;

import enums.OtherPlayerTypes;
import enums.PitchType;
import ga.FitnessCalculator;
import ga.PlayerDetails;
import objects.PlayerStatistics;

public class TestMain {
    
    private static double fitness(final int [] x) {
        double val=0;
        for ( int i = 0 ; i <11; ++i ) {
              if (x[i] == 1) val += 25;
              if (x[i] == 2) val += 24;
              if (x[i] == 3) val += 23;
              if (x[i] == 4) val += 22;
              if (x[i] == 5) val += 21;
              if (x[i] == 6) val += 20;
              if (x[i] == 7) val += 19;
              if (x[i] == 8) val += 18;
              if (x[i] == 9) val += 17;
              if (x[i] == 10) val += 16;
              if (x[i] == 11) val += 15;
              if (x[i] == 12) val += 14;
              if (x[i] == 13) val += 13;
              if (x[i] == 14) val += 12;
              if (x[i] == 15) val += 11;
              if (x[i] == 16) val += 10;
              if (x[i] == 17) val += 9;
              if (x[i] == 18) val += 8;
              if (x[i] == 19) val += 7;
              if (x[i] == 20) val += 6;
              if (x[i] == 21) val += 5;
              if (x[i] == 22) val += 4;
              if (x[i] == 23) val += 3;
              if (x[i] == 24) val += 2;
              if (x[i] == 25) val += 1;
        }
        return val;
  	}
    
    private static double teamFitness(final int[] team)
    {
    	return FitnessCalculator.calculateTeamFitness(team, 6, 2, 3, PitchType.spinning);
    }
    
	public static void main(final String[] args) {
		PlayerDetails.ReadStatistics();
		//testFitness();
		
		final Engine<IntegerGene, Double> engine = Engine
			// Create a new builder with the given fitness
			// function and chromosome.
			.builder(TestMain::teamFitness,
				codecs.ofVector(IntRange.of(1, 26), 11))
			.populationSize(1000)
			.optimize(Optimize.MAXIMUM)
			.alterers(
					new Mutator<>(0.05),
					new MeanAlterer<>(0.2))
			.build();
		
		// Create evolution statistics consumer.
		final EvolutionStatistics<Double, ?>
			statistics = EvolutionStatistics.ofNumber();
		final EvolutionStatistics<Double, ?>
			compStatistics = EvolutionStatistics.ofComparable();

		final Phenotype<IntegerGene, Double> best = engine.stream()
			// Truncate the evolution stream after 20 "steady"
			// generations.
			.limit(bySteadyFitness(20))
			// The evolution will stop after maximal 100
			// generations.
			.limit(1000)
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
	
	//this is just a test method
	private static void testFitness(){

		int [] team = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		
		for (PlayerStatistics playerStatistics : PlayerDetails.getTeamDetails(team)) {
			
			System.out.println(playerStatistics.playerName);
			System.out.println("Other Types : ");
			for (OtherPlayerTypes i : playerStatistics.otherPlayerTypes) {
				System.out.println(i);
			}
			System.out.println("-------------------------------");
		}
		
		System.out.println("All Rounders : " + PlayerDetails.getAllRoundersCount(team));
		System.out.println("Batsmen: " + PlayerDetails.getBatsmenCount(team));
		System.out.println("Bowlers: " + PlayerDetails.getBowlersCount(team));
		
		System.out.println("Captain Exists: " + PlayerDetails.checkCaptainExists(team));
		System.out.println("Wicket Keeper Exists: " + PlayerDetails.checkWicketKeeperExists(team));
		
		System.out.println("Fast Bowlers count: " + PlayerDetails.getFastBowlersCount(team));
		System.out.println("Spinners Count: " + PlayerDetails.getSpinnersCount(team));
		
		System.out.println("Fifties Scored: " + PlayerDetails.getPlayerDetails(16).fiftiesScored);
		
		System.out.println("Team Fitness : " + teamFitness(team));
	}
}
