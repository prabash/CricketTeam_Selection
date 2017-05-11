package objects;

import java.util.List;

import enums.OtherPlayerTypes;
import enums.PlayerType;

public class PlayerStatistics {

	public int playerId;
	public String playerName;
	
	public PlayerType playerType;
	public List<OtherPlayerTypes> otherPlayerTypes;
	public int matchesPlayed;
	
	//Batting stats
	public int totalRuns;
	public Double battingAvg;
	public Double battingStrikeRate;
	public int hundredScored;
	public int fiftiesScored;
	
	//Bowling stats
	public Double bowlingAvg;
	public Double bowlingStrikeRate;
	public Double bowlingEconomyRate;
	public int wicketsTaken;
	public Double oversBowled;
	public int bowlingInnings;
}
