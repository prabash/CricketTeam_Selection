import java.util.HashSet;
import java.util.Set;

public class FitnessCalculator {
	// total fitness of the generated team
	public static double calculateTeamFitness(int [] team,
										      int reqBatsmenCount,
											  int reqBowlersCount,
											  int reqAllRoundersCount,
											  PitchType pitchType){
		double totalFitness = 0.0;
		
		if (!checkDuplicates(team))
		{
			// out of 100
			double playerFitness = calculatePlayerFitness(team);
			// out of 100
			double compFitness = calculateCompFitness(team, reqBowlersCount, reqBowlersCount, reqAllRoundersCount);
			// out of 20
			double matchFitness = calculateMatchFitness(team, pitchType);
			
			totalFitness = ((playerFitness + compFitness + matchFitness)/220 * 100);
		}
		
		return totalFitness;
	}

	//will calculate the fitness of all the players of the team using their statistics
	//Value will be returned out of 100
	private static double calculatePlayerFitness(int[] team) {
		double totalPlayerFitness = 0.0;
		
		for (int i = 0; i < team.length; i++) {
			PlayerStatistics playerStats = PlayerDetails.getPlayerDetails(team[i]);
			if (playerStats.playerType == PlayerType.Batsman){
				totalPlayerFitness += calculateBatsmanFitness(playerStats);
			}
			else if (playerStats.playerType == PlayerType.Bowler){
				totalPlayerFitness += calculateBowlerFitness(playerStats);
			}
			else if (playerStats.playerType == PlayerType.AllRounder){
				totalPlayerFitness += calculateAllRounderFitness(playerStats);
			}
		}
		
		// the max total player fitness is 1100
		// should return the value out of 100
		return (totalPlayerFitness/1100 * 100);
	}

	private static boolean checkDuplicates(int [] team){
		 Set<Integer> lump = new HashSet<Integer>();
		 for (int i : team)
		 {
		   if (lump.contains(i)) return true;
		   lump.add(i);
		 }
		 return false;
	}
	
	//calculate the fitness of a given batsman using stats 
	//Value will be returned out of 100
	private static double calculateBatsmanFitness(PlayerStatistics playerStats) {
		double battingFitness = 0.0;
		// each criteria will score marks out of maximum 20
		int batAvgFitness = 0;
		int batSRFitness = 0;
		int batRunsFitness = 0;
		int bat100sFitness = 0;
		int bat50sFitness = 0;
		
		//batting avg fitness criteria
		if (playerStats.battingAvg > 50) batAvgFitness = 20;
		else if (playerStats.battingAvg <= 50 && playerStats.battingAvg > 45) batAvgFitness = 18; 	
		else if (playerStats.battingAvg <= 45 && playerStats.battingAvg > 40) batAvgFitness = 16;
		else if (playerStats.battingAvg <= 40 && playerStats.battingAvg > 35) batAvgFitness = 14;
		else if (playerStats.battingAvg <= 35 && playerStats.battingAvg > 30) batAvgFitness = 12;
		else if (playerStats.battingAvg <= 30 && playerStats.battingAvg > 25) batAvgFitness = 10;
		else if (playerStats.battingAvg <= 25 && playerStats.battingAvg > 20) batAvgFitness = 8;
		else if (playerStats.battingAvg <= 20 && playerStats.battingAvg > 15) batAvgFitness = 6;
		else if (playerStats.battingAvg <= 15 && playerStats.battingAvg > 10) batAvgFitness = 4;
		else if (playerStats.battingAvg <= 10) batAvgFitness = 2;
		
		//batting SR fitness criteria
		if (playerStats.battingStrikeRate > 100) batSRFitness = 20; 	
		else if (playerStats.battingStrikeRate <= 100 && playerStats.battingStrikeRate > 95) batSRFitness = 18;
		else if (playerStats.battingStrikeRate <= 95 && playerStats.battingStrikeRate > 90) batSRFitness = 16;
		else if (playerStats.battingStrikeRate <= 90 && playerStats.battingStrikeRate > 85) batSRFitness = 14;
		else if (playerStats.battingStrikeRate <= 85 && playerStats.battingStrikeRate > 80) batSRFitness = 12;
		else if (playerStats.battingStrikeRate <= 80 && playerStats.battingStrikeRate > 75) batSRFitness = 10;
		else if (playerStats.battingStrikeRate <= 75 && playerStats.battingStrikeRate > 70) batSRFitness = 8;
		else if (playerStats.battingStrikeRate <= 70 && playerStats.battingStrikeRate > 65) batSRFitness = 6;
		else if (playerStats.battingStrikeRate <= 65 && playerStats.battingStrikeRate > 60) batSRFitness = 4;
		else if (playerStats.battingStrikeRate <= 60) batSRFitness = 2;
		
		//batting Runs fitness criteria
		if (playerStats.totalRuns > 1000) batRunsFitness = 20; 	
		else if (playerStats.totalRuns <= 1000 && playerStats.totalRuns > 900) batRunsFitness = 18;
		else if (playerStats.totalRuns <= 900 && playerStats.totalRuns > 800) batRunsFitness = 16;
		else if (playerStats.totalRuns <= 800 && playerStats.totalRuns > 700) batRunsFitness = 14;
		else if (playerStats.totalRuns <= 700 && playerStats.totalRuns > 600) batRunsFitness = 12;
		else if (playerStats.totalRuns <= 600 && playerStats.totalRuns > 500) batRunsFitness = 10;
		else if (playerStats.totalRuns <= 500 && playerStats.totalRuns > 400) batRunsFitness = 8;
		else if (playerStats.totalRuns <= 400 && playerStats.totalRuns > 300) batRunsFitness = 6;
		else if (playerStats.totalRuns <= 300 && playerStats.totalRuns > 200) batRunsFitness = 4;
		else if (playerStats.totalRuns <= 200) batRunsFitness = 2;
		
		//batting Runs fitness criteria
		if (playerStats.hundredScored > 6) bat100sFitness = 20; 	
		else if (playerStats.hundredScored <= 6 && playerStats.hundredScored > 4) bat100sFitness = 15;
		else if (playerStats.hundredScored <= 4 && playerStats.hundredScored > 2) bat100sFitness = 10;
		else if (playerStats.hundredScored <= 2 && playerStats.hundredScored > 0) bat100sFitness = 5;
		
		//batting Runs fitness criteria
		if (playerStats.fiftiesScored > 10) bat50sFitness = 20 * 6/6; 	
		else if (playerStats.fiftiesScored <= 10 && playerStats.fiftiesScored > 8) bat50sFitness = 20 * 5/6;
		else if (playerStats.fiftiesScored <= 8 && playerStats.fiftiesScored > 6) bat50sFitness = 20 * 4/6;
		else if (playerStats.fiftiesScored <= 6 && playerStats.fiftiesScored > 4) bat50sFitness = 20 * 3/6;
		else if (playerStats.fiftiesScored <= 4 && playerStats.fiftiesScored > 2) bat50sFitness = 20 * 2/6;
		else if (playerStats.fiftiesScored <= 2 && playerStats.fiftiesScored > 0) bat50sFitness = 20 * 1/6;
		
		battingFitness = batAvgFitness + batSRFitness + batRunsFitness + bat100sFitness + bat50sFitness;
		return battingFitness;
	}
	
	//calculate the fitness of a given bowler using stats 
	//Value will be returned out of 100
	private static double calculateBowlerFitness(PlayerStatistics playerStats) {
		double bowlingFitness = 0.0;
		// each criteria will score marks out of maximum 20
		int bowlAvgFitness = 0;
		int bowlSRFitness = 0;
		int bowlEconFitness = 0;
		int bowlWicketsFitness = 0;
		int oversBowledFitness = 0;
		
		//bowling avg (runs conceded per wicket) fitness criteria
		if (playerStats.bowlingAvg > 50) bowlAvgFitness = 20 * 1/7; 	
		else if (playerStats.bowlingAvg <= 50 && playerStats.bowlingAvg > 45) bowlAvgFitness = 20 * 2/7;
		else if (playerStats.bowlingAvg <= 45 && playerStats.bowlingAvg > 40) bowlAvgFitness = 20 * 3/7;
		else if (playerStats.bowlingAvg <= 40 && playerStats.bowlingAvg > 35) bowlAvgFitness = 20 * 4/7;
		else if (playerStats.bowlingAvg <= 35 && playerStats.bowlingAvg > 30) bowlAvgFitness = 20 * 5/7;
		else if (playerStats.bowlingAvg <= 30 && playerStats.bowlingAvg > 25) bowlAvgFitness = 20 * 6/7;
		else if (playerStats.bowlingAvg <= 25 && playerStats.bowlingAvg > 0) bowlAvgFitness = 20 * 7/7;
		
		//bowling strike rate (balls bowled per wicket) fitness criteria
		if (playerStats.bowlingStrikeRate > 50) bowlSRFitness = 20 * 1/7; 	
		else if (playerStats.bowlingStrikeRate <= 50 && playerStats.bowlingStrikeRate > 45) bowlSRFitness = 20 * 2/7;
		else if (playerStats.bowlingStrikeRate <= 45 && playerStats.bowlingStrikeRate > 40) bowlSRFitness = 20 * 3/7;
		else if (playerStats.bowlingStrikeRate <= 40 && playerStats.bowlingStrikeRate > 35) bowlSRFitness = 20 * 4/7;
		else if (playerStats.bowlingStrikeRate <= 35 && playerStats.bowlingStrikeRate > 30) bowlSRFitness = 20 * 5/7;
		else if (playerStats.bowlingStrikeRate <= 30 && playerStats.bowlingStrikeRate > 25) bowlSRFitness = 20 * 6/7;
		else if (playerStats.bowlingStrikeRate <= 25 && playerStats.bowlingStrikeRate > 0) bowlSRFitness = 20 * 7/7;
		
		//bowling economy rate (runs conceded per over) fitness criteria
		if (playerStats.bowlingEconomyRate > 8) bowlEconFitness = 20 * 1/6; 	
		else if (playerStats.bowlingEconomyRate <= 8 && playerStats.bowlingEconomyRate > 7) bowlEconFitness = 20 * 2/6; 	
		else if (playerStats.bowlingEconomyRate <= 7 && playerStats.bowlingEconomyRate > 6) bowlEconFitness = 20 * 3/6; 	
		else if (playerStats.bowlingEconomyRate <= 6 && playerStats.bowlingEconomyRate > 5) bowlEconFitness = 20 * 4/6; 	
		else if (playerStats.bowlingEconomyRate <= 5 && playerStats.bowlingEconomyRate > 4) bowlEconFitness = 20 * 5/6; 	
		else if (playerStats.bowlingEconomyRate <= 4 && playerStats.bowlingEconomyRate > 0) bowlEconFitness = 20 * 6/6; 	

		//wickets fitness criteria
		if (playerStats.wicketsTaken > 15) bowlWicketsFitness = 20 * 6/6; 	
		else if (playerStats.wicketsTaken <= 15 && playerStats.wicketsTaken > 12) bowlWicketsFitness = 20 * 5/6; 	
		else if (playerStats.wicketsTaken <= 12 && playerStats.wicketsTaken > 9) bowlWicketsFitness = 20 * 3/6; 	
		else if (playerStats.wicketsTaken <= 9 && playerStats.wicketsTaken > 6) bowlWicketsFitness = 20 * 3/6; 	
		else if (playerStats.wicketsTaken <= 6 && playerStats.wicketsTaken > 3) bowlWicketsFitness = 20 * 2/6; 	
		else if (playerStats.wicketsTaken <= 3 && playerStats.wicketsTaken > 0) bowlWicketsFitness = 20 * 1/6;
		
		// overs bowled per an inning
		// this criteria would be high for a regular well performing bowler 
		double oversPerInning = playerStats.oversBowled / playerStats.bowlingInnings;
		 	
		if (oversPerInning <= 10 && oversPerInning > 8) oversBowledFitness = 20; 	
		else if (oversPerInning <= 8 && oversPerInning > 6) oversBowledFitness = 16; 	
		else if (oversPerInning <= 6 && oversPerInning > 4) oversBowledFitness = 12; 	
		else if (oversPerInning <= 4 && oversPerInning > 2) oversBowledFitness = 8; 	
		else if (oversPerInning <= 2 && oversPerInning > 0) oversBowledFitness = 4;
		
		bowlingFitness = bowlAvgFitness + bowlSRFitness + bowlEconFitness + bowlWicketsFitness + oversBowledFitness;
		return bowlingFitness;
	}
	
	//calculate the fitness of a given allrounder using stats 
	//Value will be returned out of 100
	private static double calculateAllRounderFitness(PlayerStatistics playerStats) {
		double battingFitness = calculateBatsmanFitness(playerStats);
		double bowlingFitness = calculateBowlerFitness(playerStats);
		
		double allRounderFitness = (battingFitness + bowlingFitness)/2;				
		return allRounderFitness;
	}

	//Calculate the fitness specific to the composition of the team
	//Value will be returned out of 100
	private static double calculateCompFitness(int[] team, 
											   int reqBatsmenCount,
											   int reqBowlersCount,
											   int reqAllRoundersCount) {
		double totalCompFitness = 0.0;
		
		double batsmenCompFitness = 0.0;
		double bowlersCompFitness = 0.0;
		double allRoundersCompFitness = 0.0;
		double captainCompFitness = 0.0;
		double wicketKeeperCompFitness = 0.0;
		
		int compScalar = 20;
		batsmenCompFitness = CalcCompFitnessByPlayerType(reqBatsmenCount, PlayerDetails.getBatsmenCount(team), compScalar);
		bowlersCompFitness = CalcCompFitnessByPlayerType(reqBowlersCount, PlayerDetails.getBowlersCount(team), compScalar);
		allRoundersCompFitness = CalcCompFitnessByPlayerType(reqAllRoundersCount, PlayerDetails.getAllRoundersCount(team), compScalar);
		
		captainCompFitness = PlayerDetails.checkCaptainExists(team) ? 20 : 0;
		wicketKeeperCompFitness = PlayerDetails.checkWicketKeeperExists(team) ? 20 : 0;
		
		totalCompFitness = batsmenCompFitness + bowlersCompFitness + allRoundersCompFitness + captainCompFitness + wicketKeeperCompFitness;
		return totalCompFitness;
	}
	
	// calculate the fitness depending on the pitch condition
	// current the will be is given out of 20 
	private static double calculateMatchFitness(int[] team, PitchType pitchType) {
		double totalMatchFitness = 0.0;
		int scalar = 20;
		
		if (pitchType == PitchType.bouncy){
			int minimumFastBowlers = 3;
			int currentFastBowlersCount = PlayerDetails.getFastBowlersCount(team);
			
			if (currentFastBowlersCount > minimumFastBowlers)
				totalMatchFitness = scalar;
			else
				totalMatchFitness = CalcCompFitnessByPlayerType(minimumFastBowlers, currentFastBowlersCount, scalar);
		}
		else if(pitchType == PitchType.spinning){
			int minimumSpinners = 2;
			int currentSpinnersCount = PlayerDetails.getSpinnersCount(team);
			
			if (currentSpinnersCount > minimumSpinners)
				totalMatchFitness = scalar;
			else
				totalMatchFitness = CalcCompFitnessByPlayerType(minimumSpinners, currentSpinnersCount, scalar);
		}
		
		return totalMatchFitness;
	}
		
	
	private static double CalcCompFitnessByPlayerType(int reqPlayersCount, int currentCount, int scaler){
		int playerDiff = Math.abs(reqPlayersCount - currentCount);
		return scaler * ((reqPlayersCount - playerDiff)/reqPlayersCount);
	}

	
}
