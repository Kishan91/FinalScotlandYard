
public class Game {

	static boolean newGame = false;
	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
	
	
	public void run()
	{
		GameState state = new GameState();
		GUI gui = new GUI();
		gui.registerMapVisualisable(state);
		gui.registerPlayerVisualisable(state);
		gui.registerInitialisable(state);
		gui.registerVisualisable(state);
		state.initialiseGame(3);
		gui.run();
	}
}
