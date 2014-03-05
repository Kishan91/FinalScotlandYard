
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
		Test.printf("SCALE" + gui.ratio);
		state.scaleFactor(gui.ratio);
		state.initialiseGame(3);
	}
}
