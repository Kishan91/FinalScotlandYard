import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.imageio.*;


public class GUI extends GameVisualiser {
	
	private Dimension desDimension;
	private Dimension buffDimension;
	public double ratio;
	JFrame window;
	JLayeredPane layeredPane;
	JLabel background;
	Dimension newSize;
	int currentPlayerID = 0;
	JTabbedPane tabbedPane;
	
	private class Coordinate
	{
		int x;
		int y;
		
		public Coordinate(int xvalue, int yvalue)
		{
			x = xvalue;
			y = yvalue;
		}
	}
	
	enum playerType 
	{
		Detective, MrX
	}
	
	
	private double imageScale()
	{
		double height = buffDimension.getHeight();
		double height2 = desDimension.getHeight();
		ratio = height2/height;
		return ratio;
	}
	

	public void run()
	{
		displayMap();
		newGameButton();
		loadGameButton();
		saveGameButton();
		
	}
	

	
	private void newGameButton()
	{		
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("test.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		img = resize(img, new Dimension(130, 30));
		JButton newGame = new JButton(new ImageIcon(img));
		newGame.setOpaque(false);
		newGame.setContentAreaFilled(false);
		newGame.setBorderPainted(false);
		newGame.setFocusPainted(false);
		//Coordinate newGameXY = scaleCoordinate(new Coordinate(1100, 30));
		newGame.setLocation((int) (desDimension.getWidth() + 20), 30);
		//Coordinate newGameSize = scaleCoordinate(new Coordinate(150, 30));
		newGame.setSize(130, 30);
		layeredPane.add(newGame, 0);
		//layeredPane.moveToBack(background);	
		newGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 Component[] listComponents = layeredPane.getComponentsInLayer(1);
	        	 initialisable.initialiseGame(3);
	        	 for(Component b : listComponents)
	        	 {
	        		 layeredPane.remove(b);
	        		 layeredPane.repaint();

	        	 }
	        	 displayPlayers();
	         }          
		});
	}
	
	private void loadGameButton()
	{		
		JButton loadGame = new JButton("Load Game");
		//Coordinate newGameXY = scaleCoordinate(new Coordinate(1100, 30));
		loadGame.setLocation((int) (desDimension.getWidth() + 160), 30);
		//Coordinate newGameSize = scaleCoordinate(new Coordinate(150, 30));
		loadGame.setSize(130, 30);
		layeredPane.add(loadGame, 0);
		//layeredPane.moveToBack(background);	
		loadGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        
	         }          
		});
	}
	
	private void saveGameButton()
	{		
		JButton saveGame = new JButton("Save Game");
		//Coordinate newGameXY = scaleCoordinate(new Coordinate(1100, 30));
		saveGame.setLocation((int) (desDimension.getWidth() + 300), 30);
		//Coordinate newGameSize = scaleCoordinate(new Coordinate(150, 30));
		saveGame.setSize(130, 30);
		layeredPane.add(saveGame, 0);
		//layeredPane.moveToBack(background);	
		saveGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 
	         }          
		});
	}
	
	private void currentPlayerLabel()
	{
		JLabel currentPlayerLabel ;
		if(currentPlayerID == 0) currentPlayerLabel = new JLabel("Current Player: " + "Mr X");
		else currentPlayerLabel = new JLabel("Current Player: " + "Detective - " + currentPlayerID);		
		currentPlayerLabel.setLocation((int) (desDimension.getWidth() * 0.05), (int) (desDimension.getHeight() + 60));
		currentPlayerLabel.setSize(340,60);
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		currentPlayerLabel.setBorder(border);
		currentPlayerLabel.setFont(new Font("Impact", Font.PLAIN, 40));
		layeredPane.add(currentPlayerLabel, 1);
		layeredPane.setLayer(currentPlayerLabel, 1);
		
	}
	
	private void nextPlayerLabel()
	{
		String type;
		if(visualisable.getNextPlayerToMove() == 0) type = "Mr X ";
		else type = "Detective ";
		JLabel nextPlayerLabel = new JLabel("Next Player: " + type + visualisable.getNextPlayerToMove());
		nextPlayerLabel.setLocation((int) (desDimension.getWidth() * 0.5), (int) (desDimension.getHeight() + 60));
		nextPlayerLabel.setSize(390,60);
		Border border = BorderFactory.createLineBorder(Color.RED, 5);
		nextPlayerLabel.setBorder(border);
		nextPlayerLabel.setFont(new Font("Impact", Font.PLAIN, 40));
		layeredPane.add(nextPlayerLabel, 1);
		layeredPane.setLayer(nextPlayerLabel, 1);
	}
	
	private void mrXMoveLog()
	{
		JTabbedPane moveLog = new JTabbedPane();
		moveLog.setLocation((int) (desDimension.getWidth() + 10), 490);
		moveLog.setSize(430, 300);
		JPanel movesUsed = new JPanel();
		movesUsed.setLayout(null);
		movesUsed.setSize(600, 300);
		ArrayList<Initialisable.TicketType> usedMoves = (ArrayList<Initialisable.TicketType>) visualisable.getMoveList(0);
		for(int i = 0; i < usedMoves.size(); i++)
		{
			String type = null;
			if(usedMoves.get(i) == Initialisable.TicketType.Bus) type = "Bus";
			else if (usedMoves.get(i) == Initialisable.TicketType.Taxi) type = "Taxi";
			else if (usedMoves.get(i) == Initialisable.TicketType.Underground) type = "Underground";
			else if (usedMoves.get(i) == Initialisable.TicketType.DoubleMove) type = "Double move";
			else if (usedMoves.get(i) == Initialisable.TicketType.SecretMove) type = "Secret move";
			JLabel move = new JLabel("Move " + String.valueOf(i + 1) + ": " + type);
			move.setLocation(0, 40 + (30 * i));
			move.setSize(400, 20);
			movesUsed.add(move);
		}
		movesUsed.setVisible(true);
		moveLog.add("Mr X Move Log", movesUsed);
		moveLog.setVisible(true);
		layeredPane.add(moveLog);
	}
	
	private void mrXLabel()
	{
		tabbedPane = new JTabbedPane();
		tabbedPane.setLocation((int) (desDimension.getWidth() + 10), 90);
		tabbedPane.setSize(430, 400);
		
		JPanel mrX = new JPanel();
		mrX.setLayout(null);
		mrX.setSize(430, 300);
		JLabel noBusTickets = new JLabel("Number of bus tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, 0));
		JLabel noTaxiTickets = new JLabel("Number of taxi tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, 0));
		JLabel noTubeTickets = new JLabel("Number of tube tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, 0));
		JLabel noSdoubleTickets = new JLabel("Number of double move tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, 0));
		JLabel noSpecialTickets = new JLabel("Number of special tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.SecretMove, 0));
		//noBusTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 40);
		noBusTickets.setLocation(0, 40);
		//noTaxiTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 70);
		noTaxiTickets.setLocation(0, 70);
		//noTubeTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 100);
		noTubeTickets.setLocation(0, 100);
		//noSdoubleTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 130);
		noSdoubleTickets.setLocation(0, 130);
		//noSpecialTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 160);
		noSpecialTickets.setLocation(0, 160);
		noBusTickets.setSize(400, 20);
		noTaxiTickets.setSize(400, 20);
		noTubeTickets.setSize(400, 20);
		noSdoubleTickets.setLocation(400, 20);
		noSpecialTickets.setLocation(400, 20);
		mrX.add(noBusTickets);
		mrX.add(noTaxiTickets);
		mrX.add(noTubeTickets);
		mrX.add(noSdoubleTickets);
		mrX.add(noSpecialTickets);
		mrX.setVisible(true);
		tabbedPane.add("Mr X", mrX);
		
	}
	
	private void detectiveLabels()
	{
		
		for(int i = 1; i < 4; i++)
		{
			JPanel detective1 = new JPanel();
			detective1.setLayout(null);
			detective1.setSize(430, 300);
			JLabel noBusTickets = new JLabel("Number of bus tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, i));
			JLabel noTaxiTickets = new JLabel("Number of taxi tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, i));
			JLabel noTubeTickets = new JLabel("Number of tube tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, i));
			//noBusTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 40);
			noBusTickets.setLocation(0, 40);
			//noTaxiTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 70);
			noTaxiTickets.setLocation(0, 70);
			//noTubeTickets.setBounds((int) (desDimension.getWidth() + 30), 90, 0, 100);
			noTubeTickets.setLocation(0, 100);
			noBusTickets.setSize(400, 20);
			noTaxiTickets.setSize(400, 20);
			noTubeTickets.setSize(400, 20);
			detective1.add(noBusTickets);
			detective1.add(noTaxiTickets);
			detective1.add(noTubeTickets);
			detective1.setVisible(true);
			tabbedPane.add("Detective " + String.valueOf(i), detective1);
		}
				
			tabbedPane.setVisible(true);
			layeredPane.add(tabbedPane);
	}
	
	
	
	private void displayMap()
	{
		window = new JFrame("Scotland Yard");
		window.setTitle("Scotland Yard");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
		int bottom = scnMax.bottom;
		int left = scnMax.left;
		int right = scnMax.right;
		int top = scnMax.top;
		int screenWidth = (int) screenSize.getWidth() - left - right - window.getWidth();
		int screenHeight = (int) screenSize.getHeight() - bottom - top - window.getHeight();
		Dimension a = new Dimension(screenWidth,screenHeight);
		window.setPreferredSize(a);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(a);
		window.add(layeredPane);
		background = createImage(a);
		layeredPane.add(background, 0);
		displayPlayers();
		window.setVisible(true);
		window.pack();
	}
	
	
	private void displayPlayers()
	{
		ArrayList<Integer> mrXIdList = (ArrayList<Integer>) playerVisualisable.getMrXIdList();
		ArrayList<Integer> detectiveIdList = (ArrayList<Integer>) playerVisualisable.getDetectiveIdList();
		Integer node;
		//double scaleFactor = imageScale();
		int i = 1;
		for(Integer b : mrXIdList)
		{
			node = playerVisualisable.getNodeId(b);
			Test.printf("MR X LOCATION" + node);
			JLabel MrX = drawNode(node, playerType.MrX);
			MrX.setSize(30,30);
			layeredPane.add(MrX, i);
			layeredPane.setLayer(MrX, 1);
			i++;
			
		}
		for(Integer b : detectiveIdList)
		{
			node = playerVisualisable.getNodeId(b);
			Test.printf("Detective LOCATION" + node);
			JLabel Detective = drawNode(node, playerType.Detective);
			Detective.setSize(30,30);
			layeredPane.add(Detective, i);
			layeredPane.setLayer(Detective, 1);
			i++;
		}
		currentPlayerLabel();
		nextPlayerLabel();
		mrXLabel();
		detectiveLabels();
		mrXMoveLog();
		
	}
	
	private JLabel drawNode(Integer node, playerType type)
	{
		Coordinate toDrawUnscaled = new Coordinate(playerVisualisable.getLocationX(node), playerVisualisable.getLocationY(node));
		Coordinate toDrawScaled = scaleCoordinate(toDrawUnscaled);
		JLabel toDraw = drawPlayer(toDrawScaled.x, toDrawScaled.y, type);
		return toDraw;
	}
	
	
	private BufferedImage scale(BufferedImage previous, int imageType, int size) {
	    BufferedImage scaledImage = null;
	    if(previous != null) {
	        scaledImage = new BufferedImage(size, size, imageType);
	        Graphics2D g2D = scaledImage.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(ratio, ratio);
	        g2D.drawRenderedImage(previous, at);
	    }
	    return scaledImage;
	}
	
	
	private JLabel drawPlayer(Integer X, Integer Y, playerType type)
	{
		
		int size = (int) (30 * ratio);
		if(type == playerType.MrX)
		{
			URL mrX = this.getClass().getResource("MrX.jpg");
			ImageIcon mrXImage = null;
			BufferedImage mrXBuffered = null;

			try {
				mrXBuffered = ImageIO.read(mrX);
				mrXBuffered = scale(mrXBuffered, BufferedImage.TYPE_INT_RGB, size);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				mrXImage = new ImageIcon(mrXBuffered);
			JLabel mrXLabel = new JLabel(mrXImage);
			mrXLabel.setLocation(X - 15, Y - 15);
			return mrXLabel;
			
		} else {
			URL detective = this.getClass().getResource("Detective.jpg");
			ImageIcon detectiveImage = null;
			BufferedImage detectiveBuffered = null;
			try {
				detectiveBuffered = ImageIO.read(detective);
				detectiveBuffered = scale(detectiveBuffered, BufferedImage.TYPE_INT_RGB, size);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			detectiveImage = new ImageIcon(detectiveBuffered);
			JLabel detectiveLabel = new JLabel(detectiveImage);
			detectiveLabel.setLocation(X - 15, Y - 15);
			return detectiveLabel;
		}
	
	}
	
	
	private Coordinate scaleCoordinate(Coordinate toDrawUnscaled)
	{
		double scale = imageScale();
		double height = toDrawUnscaled.y;
		double width = toDrawUnscaled.x;
		// scale is a decimalised percentage 
		double adjWidth = width*scale;
		double adjHeight = height*scale;
		Coordinate toDrawScaled = new Coordinate((int)adjWidth,(int)adjHeight);
		return toDrawScaled;
	}
	
	
	private JLabel createImage(Dimension a)
	{
		BufferedImage image = null;
		URL london = this.getClass().getResource(mapVisualisable.getMapFilename());
		try{
			image = ImageIO.read(london);
		} catch (Exception e){
			
			System.exit(1);
		}
		BufferedImage buffered = image;
		
		int buffHeight= (int) buffered.getHeight();
		int buffWidth = (int) buffered.getWidth();
		buffDimension = new Dimension(buffWidth,buffHeight);
		
		desDimension =  scale(a,0.80);
		desDimension = aspectRatio(buffDimension,desDimension);
		BufferedImage resized = resize(buffered, desDimension);
		Test.printf(resized.getWidth());
		Test.printf(resized.getHeight());
		desDimension = new Dimension(resized.getWidth(), resized.getHeight());
		JLabel background  = new JLabel(new ImageIcon(resized));
		background.setSize((int) desDimension.getWidth(), (int) desDimension.getHeight());
		return background;
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
	

	
	private Dimension aspectRatio(Dimension imgSize, Dimension boundary) 
	{
	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }
	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
    }
    return new Dimension(new_width, new_height);
	}
	

	
	public static BufferedImage resize(BufferedImage image, Dimension requiredImage) 
	{
		int height = (int) requiredImage.getHeight();
		int width = (int) requiredImage.getWidth();
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}

}