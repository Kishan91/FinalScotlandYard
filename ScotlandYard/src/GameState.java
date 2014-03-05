import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, Initialisable{
	
	private class Coordinate
	{
		int x;
		int y;
		String type;
		
		public Coordinate(int xvalue, int yvalue)
		{
			x = xvalue;
			y = yvalue;
		}
	}
	
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


	private Coordinate scaleCoordinates(Coordinate originalCo)
	{
		Coordinate scaledCo = scale(originalCo,scaleFactor);
		return scaledCo;
	} 


	public void showPlayer(Dimension player)
	{
		
	}

	public Coordinate scale (Coordinate a, double scale)
	{
		double height = a.y;
		double width = a.x;
		// scale is a decimalised percentage 
		double adjWidth = width*scale;
		double ratio = width/height;
		double adjHeight = adjWidth/ratio;
		a = new Coordinate((int)adjWidth,(int)adjHeight);
		return a;
	}

	@Override
	public Boolean initialiseGame(Integer numberOfDetectives) {
		// TODO Auto-generated method stub
		int idxD = -1;
		ArrayList<Integer> idxArray = new ArrayList<Integer>(numberOfDetectives);
		for(int i = 0; i < numberOfDetectives; i++)
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
			listDetectives.add(temp);
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
		showPosition(mrX.Position, "MrX");
		for(Detective a : listDetectives)
		{
			showPosition(a.Position, "Detective");
		}
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
	
	private void showPosition(int position, String type)
	{
		String[] coordinates = fileLines1[position].split("\\s+");	
		int x = Integer.parseInt(coordinates[1]);
		int y = Integer.parseInt(coordinates[2]);
		Coordinate xy = new Coordinate(x, y);
		xy.type = type;
		Test.printf("\n" + "X co-ordinate" + xy.x);
		Test.printf("\n" + "Y co-ordinate" + xy.y); 
		Coordinate scaledxy = scaleCoordinates(xy);
		Test.printf("\n" + "Adjusted X co-ordinate" + scaledxy.x);
		Test.printf("\n" + "Adjusted Y co-ordinate" + scaledxy.y); 
		//need to display scaled stuff on screen
		//made a coordinate class so that i can also store the type of player which you will need when displaying the players on the screen
		
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
	
}
