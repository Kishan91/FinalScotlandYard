import java.awt.Frame;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Game {
	
	public static void main(String[] args) {
		Game game = new Game();
		game.run(args);
	}
	
	
	public void run(String[] args)
	{ 
		GameState state = new GameState();
		GUI gui = new GUI();
		gui.registerMapVisualisable(state);
		gui.registerPlayerVisualisable(state);
		gui.registerInitialisable(state);
		gui.registerVisualisable(state);
		gui.registerControllable(state);
		if(Arrays.asList(args).contains("-i")) gui.registerCustomVisualiser(state);
		state.initialiseGame(DetectiveSelection());
		gui.run();
	}
	
	private int DetectiveSelection()
	{
		JFrame window = new JFrame();
		window.setVisible(true);
		window.setSize(300,300);
		
		Test.printf(Frame.getFrames().length);
		int noDetectives = 0;
		Object[] possibilities = {"3", "4", "5","6", "7", "8", "9", "10"};
		String s = (String)JOptionPane.showInternalInputDialog(window.getContentPane(),"Pick the number of detectives: ", "Detective selection",
	                JOptionPane.PLAIN_MESSAGE, null , possibilities, "3");
		if(s == null) System.exit(0);
		else noDetectives = Integer.parseInt(s);
		return noDetectives;
	}
	
}
