import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, Initialisable, PlayerVisualisable{
	
	
	
	ArrayList<Detective> listDetectives = new ArrayList<Detective>();
	ArrayList<MrX> listMrX = new ArrayList<MrX>();
	private final Integer[] DstartPos = new Integer[]{13,26,29,34,50,53,91,94,103,112,117,132,138,141,155,174,197,198};
    private final Integer[] MrXstartPos = new Integer[]{51, 146, 45, 132, 106, 78, 127, 71, 172, 170, 166, 35, 104};
    String[] fileLines1;
    ArrayList<Integer> mrXIdList;
    ArrayList<Integer> detectiveIdList;
    
	/**
	 * Variable that will hold the filename for the map
	 */
	private String mapFilename = "map.jpg";
	
	
	/**
	 * Concrete implementation of the MapVisualisable getMapFilename function
	 * @return The map filename
	 */
	
	public String getMapFilename()
	{	
		return mapFilename;
	}


	@Override
	public Boolean initialiseGame(Integer numberOfDetectives) {
		// TODO Auto-generated method stub
		int idxD = -1;
		mrXIdList = new ArrayList<Integer>();
		detectiveIdList = new ArrayList<Integer>();
		ArrayList<Integer> idxArray = new ArrayList<Integer>(numberOfDetectives);
		int i;
		for(i = 0; i < numberOfDetectives; i++)
		{
			idxD = -1;
			Detective temp = null;
			while(idxD == -1)
			{
				idxD = new Random().nextInt(DstartPos.length);

				if(!idxArray.contains(idxD))
				{
					temp = new Detective();
					temp.Position = DstartPos[idxD];
					idxArray.add(idxD);
				}
				else idxD = -1;
			}
			temp.ID = i;
			listDetectives.add(temp);
			detectiveIdList.add(i);
		}
		try {
			splitLines();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MrX mrX = new MrX();
		int idxX = new Random().nextInt(MrXstartPos.length);
		mrX.setPosition(MrXstartPos[idxX]);
		mrX.ID = i;
		listMrX.add(mrX);
		mrXIdList.add(i);
		return null;
	}
	
	private void splitLines() throws IOException
	{
		String fileContents = readFileAsString("pos.txt");
		fileContents = fileContents.substring(5);
		fileLines1 = fileContents.split("\n");
	}
	
	private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
	
	public String readFile(String filepath)
	{
	   String content = null;
	   try {
		   File file = new File(filepath);
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}

	@Override
	public Integer getLocationX(Integer nodeId) {
		String[] coordinates = fileLines1[nodeId].split("\\s+");	
		return Integer.parseInt(coordinates[1]);
	}

	@Override
	public Integer getLocationY(Integer nodeId) {
		String[] coordinates = fileLines1[nodeId].split("\\s+");	
		return Integer.parseInt(coordinates[2]);
	}

	@Override
	public List<Integer> getDetectiveIdList() {
		return detectiveIdList;
	}

	@Override
	public List<Integer> getMrXIdList() {
		return mrXIdList;
	}

	@Override
	public Integer getNodeId(Integer playerId) {
		Integer Position = null;
		for(Detective a : listDetectives)
		{
			if(a.ID.equals(playerId))
			{
				Position = a.Position;
			}
		}
		for(MrX a : listMrX)
		{
			if(a.ID.equals(playerId))
			{
				Position =  a.Position;
			}
		}
		return Position;
	}
	
}
