import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, Initialisable{
	ArrayList<Detective> listDetectives = new ArrayList<Detective>();
	private final Integer[] DstartPos = new Integer[]{13,26,29,34,50,53,91,94,103,112,117,132,138,141,155,174,197,198};
    private final Integer[] MrXstartPos = new Integer[]{51, 146, 45, 132, 106, 78, 127, 71, 172, 170, 166, 35, 104};
    double scaleFactor;
    
	/**
	 * Variable that will hold the filename for the map
	 */
	private String mapFilename = "map.jpg";
	
	
	/**
	 * Concrete implementation of the MapVisualisable getMapFilename function
	 * @return The map filename
	 */
	
	public void scaleFactor(double guiScaleFactor)
	{
		scaleFactor = guiScaleFactor;
	}
	
	public String getMapFilename()
	{
		
		return mapFilename;
	}


	@Override
	public Boolean initialiseGame(Integer numberOfDetectives) {
		// TODO Auto-generated method stub
		int idxD = -1;
		ArrayList<Integer> idxArray = new ArrayList<Integer>(numberOfDetectives);
		for(int i = 0; i < numberOfDetectives; i++)
		{
			Detective temp = new Detective();
			while(idxD == -1)
			{
				idxD = new Random().nextInt(DstartPos.length);
				if(idxArray.contains(idxD))
				{
					temp.setPosition(DstartPos[idxD]); 
					idxArray.add(idxD);
				}
				else idxD = -1;
			}
			listDetectives.add(temp);
		}
		
		MrX mrX = new MrX();
		int idxX = new Random().nextInt(MrXstartPos.length);
		mrX.setPosition(MrXstartPos[idxX]);
		for(Detetive a: )
		showPositions()
		return null;
	}
	
	private String[] getFileContents()
	{
		String[] fileContents = null;
		return fileContents;
	}
	
}
