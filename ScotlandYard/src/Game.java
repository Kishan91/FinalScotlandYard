
public class Game {

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
	
	
	public void run()
	{
		GameState state = new GameState();
		
		GUI gui = new GUI();
		gui.registerMapVisualisable(state);
		gui.run();
		state.scaleFactor(gui.scaleFactor());
		
		//initialise then start your GUI
	}
}
