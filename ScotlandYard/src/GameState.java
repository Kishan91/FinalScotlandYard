/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable {
	
	/**
	 * Vairable that will hold the filename for the map
	 */
	private String mapFilename;
	
	
	/**
	 * Concrete implementation of the MapVisualisable getMapFilename function
	 * @return The map filename
	 */
	public String getMapFilename()
	{
		return mapFilename;
	}

}
