package ga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;

import enums.OtherPlayerTypes;
import enums.PlayerType;
import objects.PlayerStatistics;

public class PlayerDetails {
	
	private static List<PlayerStatistics> playerStats = new ArrayList<PlayerStatistics>();
	
	// this method has to be called at the beginning
	// will read the data from the excel sheet
	public static void ReadStatistics(){
		
		List<List<HSSFCell>> stats = null;
		try {
			stats = DataManager.readExcel();
			System.out.println("Size : " + stats.size());
			for (int i = 1; i <= 26; i++) {
				playerStats.add(DataManager.showExcelRowData(stats, i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			for (PlayerStatistics stat : playerStats) {
				System.out.println(stat.playerId + " : " + stat.playerName);
			}
		}
	}
	
	public static PlayerStatistics getPlayerDetails(int playerId)
	{
		return playerStats.stream()
			   .filter(item -> item.playerId == playerId)
			   .collect(Collectors.toList()).get(0);
	}
	
	public static int getBatsmenCount(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
			   .filter(item -> item.playerType == PlayerType.Batsman)
			   .collect(Collectors.toList()).size();
	}
	
	public static int getBowlersCount(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.playerType == PlayerType.Bowler)
				   .collect(Collectors.toList()).size();
	}
	
	public static int getAllRoundersCount(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.playerType == PlayerType.AllRounder)
				   .collect(Collectors.toList()).size();
		
	}
	
	public static Boolean checkCaptainExists(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.otherPlayerTypes.contains(OtherPlayerTypes.Captain))
				   .collect(Collectors.toList()).size() > 0;
	}
	
	public static Boolean checkWicketKeeperExists(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.otherPlayerTypes.contains(OtherPlayerTypes.WicketKeeper))
				   .collect(Collectors.toList()).size() > 0;
	}
	
	public static int getFastBowlersCount(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.otherPlayerTypes.contains(OtherPlayerTypes.FastBowler))
				   .collect(Collectors.toList()).size();
	}
	
	public static int getSpinnersCount(int[] team){
		List<PlayerStatistics> teamDetails = getTeamDetails(team);
		
		return teamDetails.stream()
				   .filter(item -> item.otherPlayerTypes.contains(OtherPlayerTypes.Spinner))
				   .collect(Collectors.toList()).size();
	}
	
	public static List<PlayerStatistics> getTeamDetails(int [] team){
		
		List<Integer> teamList = new ArrayList<Integer>();
		for (int i = 0; i < team.length; i++) {
			teamList.add(team[i]);
		}
		
		teamList.contains(1);
		
		List<PlayerStatistics> teamDetails = playerStats.stream()
                .filter(e -> teamList.stream()
                        .filter(d -> d == e.playerId)
                        .count() > 0)
                        .collect(Collectors.toList());
		return teamDetails;
	}
	
	public static List<PlayerStatistics> getEntireSquad(){
		return playerStats;
	}
}

