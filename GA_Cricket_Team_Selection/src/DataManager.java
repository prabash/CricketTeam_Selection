import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class DataManager {
	
	public static List<List<HSSFCell>> readExcel() throws IOException{
		
	    // An excel file name. You can create a file name with a full path
	    // information.
	    String filename = "C:/Users/Prabash/Documents/GitHub/GA_Cricket_Team_Selection/stats.xls";
	
	    // Create an ArrayList to store the data read from excel sheet.
	    List<List<HSSFCell>> sheetData = new ArrayList<List<HSSFCell>> ();
	
	    FileInputStream fis = null;
	    HSSFWorkbook workbook = null;
	    
	    try {
	    	
	        // Create a FileInputStream that will be use to read the excel file.
	        fis = new FileInputStream(filename);
	
	        // Create an excel workbook from the file system.
	        workbook = new HSSFWorkbook(fis);
	        // Get the first sheet on the workbook.
	        HSSFSheet sheet = workbook.getSheetAt(0);
	
	        // When we have a sheet object in hand we can iterator on each
	        // sheet's rows and on each row's cells. We store the data read
	        // on an ArrayList so that we can printed the content of the excel
	        // to the console.
	        Iterator<Row> rows = sheet.rowIterator();
	        while (rows.hasNext()) {
	            HSSFRow row = (HSSFRow) rows.next();
	            Iterator<Cell> cells = row.cellIterator();
	
	            List<HSSFCell> data = new ArrayList<HSSFCell>();
	            while (cells.hasNext()) {
	                HSSFCell cell = (HSSFCell) cells.next();
	                data.add(cell);
	            }
	
	            sheetData.add(data);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (fis != null) {
	            fis.close();
	        }
	        if (workbook != null){
	        	workbook.close();
	        }
	    }
    
	    // Iterates the data and print it out to the console.
		return sheetData;
	}
	
    public static PlayerStatistics showExcelRowData(List<List<HSSFCell>> sheetData, int row) {	    
    	
    	PlayerStatistics stat = new PlayerStatistics();
    	List<HSSFCell>  list = sheetData.get(row);
       
        DataFormatter formatter = new DataFormatter();
        
        String id = formatter.formatCellValue((HSSFCell) list.get(0));
        if (!id.equals(null)){
	        stat.playerId = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(0)));
	        stat.playerName = formatter.formatCellValue((HSSFCell) list.get(1));         
	        stat.playerType = PlayerType.valueOf(formatter.formatCellValue((HSSFCell) list.get(2)));
	        
	        String otherTypes = formatter.formatCellValue((HSSFCell) list.get(3));
	        List<OtherPlayerTypes> otherPlayerTypes = new ArrayList<OtherPlayerTypes>();
	        String [] otherTypesList = null;
	        if (!otherTypes.equals(null) && otherTypes != ""){
	        	if (otherTypes.contains("|")){
	        		otherTypesList = otherTypes.split("\\|");
	        	}
	        	else{
	        		otherTypesList = new String[1];
	        		otherTypesList[0] = otherTypes;
	        	}
	
	            for (int i = 0; i < otherTypesList.length; i++) {
	            	
	            	otherPlayerTypes.add(OtherPlayerTypes.valueOf(otherTypesList[i]));
	    		}
	        }
	        stat.otherPlayerTypes = otherPlayerTypes;
	        
	        stat.matchesPlayed = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(4)));
	        stat.totalRuns = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(5)));
	        stat.battingAvg = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(6)));
	        stat.battingStrikeRate = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(7)));
	        stat.hundredScored = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(8)));
	        stat.fiftiesScored = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(9)));
	        
	        stat.bowlingInnings = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(10)));
	        stat.oversBowled = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(11)));
	        stat.wicketsTaken = Integer.parseInt(formatter.formatCellValue((HSSFCell) list.get(12)));
	        stat.bowlingAvg = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(13)));
	        stat.bowlingEconomyRate = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(14)));
	        stat.bowlingStrikeRate = Double.parseDouble(formatter.formatCellValue((HSSFCell) list.get(15)));
        }
        return stat;
    }
 
}
