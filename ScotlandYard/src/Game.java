import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Game {

	static boolean newGame = false;
	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
	
	
	public void run()
	{
		JFrame detectiveInput = new JFrame("Number of detectives?");
		detectiveInput.setTitle("Scotland Yard");
		int noDetectives = 0;
		Object[] possibilities = {"3", "4", "5","6", "7", "8", "9", "10"};
		String s = (String)JOptionPane.showInputDialog(detectiveInput,"Pick the number of detectives: ", "Detective selection",
	                JOptionPane.PLAIN_MESSAGE, null , possibilities, "3");
			if(s == null) System.exit(0);
			else noDetectives = Integer.parseInt(s);
		GameState state = new GameState();
		GUI gui = new GUI();
		gui.registerMapVisualisable(state);
		gui.registerPlayerVisualisable(state);
		gui.registerInitialisable(state);
		gui.registerVisualisable(state);
		gui.registerControllable(state);
		gui.registerCustomVisualiser(state);
		state.initialiseGame(noDetectives);
		gui.run();
	}
}
