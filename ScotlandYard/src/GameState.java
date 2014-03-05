import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    String[] fileLines1;
    
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


	private Dimension scaleCoordinates(Dimension originalCo)
	{
		Dimension scaledCo = scale(originalCo,scaleFactor);
		return scaledCo;
	} 


	public void showPlayer(Dimension player)
	{
		
	}

	public Dimension scale (Dimension a, double scale)
	{
		double height = a.getHeight();
		double width = a.getWidth();
		// scale is a decimalised percentage 
		double adjWidth = width*scale;
		double ratio = width/height;
		double adjHeight = adjWidth/ratio;
		a = new Dimension((int)adjWidth,(int)adjHeight);
		return a;
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
					Test.printf("Detective Position" + DstartPos[idxD]);
					idxArray.add(idxD);
				}
				else idxD = -1;
			}
			listDetectives.add(temp);
		}
		splitLines();
		MrX mrX = new MrX();
		int idxX = new Random().nextInt(MrXstartPos.length);
		Test.printf("Mr X Position" + idxX);
		mrX.setPosition(MrXstartPos[idxX]);
		for(Detective a: listDetectives)
		{
			showPosition(a.getPosition());
		}
		showPosition(mrX.getPosition());
		
		//test
		return null;
	}
	
	private void splitLines()
	{
		URL positions = this.getClass().getResource("pos.txt");
		String fileContents = readFile(positions.toString());
		fileContents = fileContents.substring(5);
		fileLines1 = fileContents.split("\n");
	}
	
	private void showPosition(int position)
	{
		String[] coordinates = fileLines1[position].split("\\s+");
		int x = Integer.parseInt(coordinates[1]);
		int y = Integer.parseInt(coordinates[2]);
		Dimension xy = new Dimension(x, y);
		Test.printf("X co-ordinate" + xy.getWidth());
		Test.printf("Y co-ordinate" + xy.getHeight());
		
		
		
		
		
	}
	
	public static String readFile(String filepath)
	{
	   String content = null;
	   File file = new File(filepath); //for ex foo.txt
	   try {
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
	
}
