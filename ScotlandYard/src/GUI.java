import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.imageio.*;


import java.awt.event.MouseMotionListener;

public class GUI extends GameVisualiser implements ActionListener, MouseListener, MouseMotionListener  {
	
	//stores resized image dimensions
	private Dimension resizedImageDimensions;
	//stores old image dimensions
	private Dimension oldImageDimensions;
	//stores window which contains the JLabel for the map background
	private JFrame window;
	//layered pane which contains all the JComponents
	private JLayeredPane layeredPane;
	//stores currentPlayerID - mimics the one in GameState
	private int currentPlayerID = 0;
	//tabbedPane for Mr X's and Detectives
	private JTabbedPane tabbedPane;
	//extra height at top of screen which is used in mouseMoved and mouseClicked functions
	private int extraH;
	//custom mouse cursor
	private BufferedImage mousecursor;
	boolean flag = false;
	//enum playerType to distinguish between player types
	int currentRound = 1;
	enum playerType { Detective, MrX }
	
	protected customVisualisable customVisualiser;
	public void registerCustomVisualiser(customVisualisable a)
	{
		flag = true;
		customVisualiser = a;
	}
	
	//run function
	public void run()
	{
		//displays map
		displayMap();
		//displays the buttons on the window
		displayButtons();
	}
	
	//displays map on screen
	private void displayMap()
	{
		//set window title
		window = new JFrame("Scotland Yard");
		window.setTitle("Scotland Yard");
		//gets screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//used to the size of the taskbar stuff above and below the window
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
		int bottom = scnMax.bottom;
		int left = scnMax.left;
		int right = scnMax.right;
		int top = scnMax.top;
		//works out the effective screen width and screen height
		int screenWidth = (int) screenSize.getWidth() - left - right - window.getWidth();
		int screenHeight = (int) screenSize.getHeight() - bottom - top - window.getHeight();
		//stores the effective screen width and screen height in a dimension
		Dimension effectiveScreenRes = new Dimension(screenWidth,screenHeight);
		window.setPreferredSize(effectiveScreenRes);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//makes a layeredPane which is the effective screen size
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(effectiveScreenRes);
		//adds the layeredpane to the window jframe
		window.add(layeredPane);
		
		//returns a JLabel with the map size set
		JLabel background = createImage(effectiveScreenRes);
		
		//adds a mouse listener to the window jframe - which is used for the mouseClicked method
		window.addMouseListener(this);
		//adds a mouse motion listener to the window jframe - which is used for the mouseMoved method
		window.addMouseMotionListener(this); 
		
		//adds the background jlabel to the layeredPane with component ID 0 - automatically added to layer 0
		layeredPane.add(background, 0);
		window.setVisible(true);
		window.pack();
		//draws cursor label
		drawCursorLabel();
		//displays players
		displayPlayers();
		//this gives height of the program title bar and the taskbar above the window if one exists - used later in the mouseMoved and mouseClicked methods
		extraH = screenSize.height - window.getContentPane().getHeight() - bottom;
	}
	
	//creates a JLabel for the map with the given dimensions + scaled
	private JLabel createImage(Dimension a)
	{
		//creates a URL for the map
		BufferedImage image = null;
		URL london = this.getClass().getResource(mapVisualisable.getMapFilename());
		//tries to read the map
		try{
			image = ImageIO.read(london);
		} catch (Exception e){
			System.exit(1);
		}
		//gets the image height and image width and stores them in the oldImageDimensions dimension
		int buffHeight= (int) image.getHeight();
		int buffWidth = (int) image.getWidth();
		oldImageDimensions = new Dimension(buffWidth,buffHeight);
		
		//scales the image dimensions by 0.8
		resizedImageDimensions =  scaleImageDimensions(a,0.80);
		//makes sure the dimensions to make sure aspect ratio is maintained
		resizedImageDimensions = aspectRatioImageDimensions(oldImageDimensions,resizedImageDimensions);
		//resizes the actual image to the resized dimensions
		BufferedImage resized = resize(image, resizedImageDimensions);
		
		//stores the resized image dimensions in resizedImageDimensions
		resizedImageDimensions = new Dimension(resized.getWidth(), resized.getHeight());
		
		//makes a new JLabel with the new resized image
		JLabel background  = new JLabel(new ImageIcon(resized));
		//sets the size of the JLabel to the resizedImageDimensions
		background.setSize((int) resizedImageDimensions.getWidth(), (int) resizedImageDimensions.getHeight());
		return background;
	}
	
	//takes an image dimension [contains the width and height] and the scale factor and works out the adjusted width and height
	public Dimension scaleImageDimensions (Dimension a, double scale)
	{
		double height = a.getHeight();
		double width = a.getWidth();
		double adjWidth = width*scale;
		double ratio = width/height;
		double adjHeight = adjWidth/ratio;
		a = new Dimension((int)adjWidth,(int)adjHeight);
		return a;
	}
	
	//scales width and height to maintain the aspect ratio
	private Dimension aspectRatioImageDimensions(Dimension imgSize, Dimension boundary) 
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
	
	//scales an image to the required image dimensions
	public BufferedImage resize(BufferedImage image, Dimension requiredImage) 
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
	
	//displays players on the screen
	private void displayPlayers()
	{
		//gets Mr X ID list and Detective ID list using playerVisualisable interface
		ArrayList<Integer> mrXIdList = (ArrayList<Integer>) playerVisualisable.getMrXIdList();
		ArrayList<Integer> detectiveIdList = (ArrayList<Integer>) playerVisualisable.getDetectiveIdList();
		
		//stores the node each player is on during the for loops
		Integer node;
		
		//use i here because each component needs a unique ID when adding it to the layeredPane
		int i = 1;
		
		//loops through Mr X ID's
		for(Integer ID : mrXIdList)
		{
			//gets node for the current player
			node = playerVisualisable.getNodeId(ID);
	
			//makes a MrX JLabel
			JLabel MrX = drawNode(node, playerType.MrX);
			//sets size of jlabel to be 30px by 30px
			MrX.setSize(30,30);
			//adds label to layered pane with ID i
			layeredPane.add(MrX, i);
			//adds the label to layer 1 on the layered pane
			layeredPane.setLayer(MrX, 1);
			i++;
		}
		for(Integer ID : detectiveIdList)
		{
			//gets node for the current player
			node = playerVisualisable.getNodeId(ID);
			//makes a Detective JLabel
			JLabel Detective = drawNode(node, playerType.Detective);
			//sets size of jlabel to be 30px by 30px
			Detective.setSize(30,30);
			//adds label to layered pane with ID i
			layeredPane.add(Detective, i);
			//adds the label to layer 1 on the layered pane
			layeredPane.setLayer(Detective, 1);
			i++;
		}
		//displays current player label
		currentPlayerLabel();
		//displays next player label
		nextPlayerLabel();
		//displays mrX panel and detective panels in tabbed pane
		currentRound();
		int index = mrXPanel();
		detectivePanels(index);
		//displays Mr X move log
		mrXMoveLog();
		if(flag == true)
		{
			
			//draws highlighting around the moves that can be moved to from the current node by the current player
			drawHighlights();
		}
	}
	
	//returns a JLabel for the Detective/MrX player
	private JLabel drawNode(Integer node, playerType type)
	{
		//gets X and Y coordinate for the given node
		Point toDrawUnscaled = new Point(playerVisualisable.getLocationX(node), playerVisualisable.getLocationY(node));
		//scales the coordinates given map scaling
		Point toDrawScaled = scalePoint(toDrawUnscaled);
		//creates JLabel using drawPlayer function
		JLabel toDraw = drawPlayer(toDrawScaled.x, toDrawScaled.y, type);
		//returns label
		return toDraw;
	}

	//scales the coordinate given by the map scale factor
	private Point scalePoint(Point toDrawUnscaled)
	{
		//gets scale factor that the map is scaled by
		double scale = imageScale();
		//adjusts width and height by multiplying by scale factor
		double adjWidth = toDrawUnscaled.x*scale;
		double adjHeight = toDrawUnscaled.y*scale;
		//stores them in a coordinate
		Point toDrawScaled = new Point((int)adjWidth,(int)adjHeight);
		//returns the coordinate
		return toDrawScaled;
	}
	
	//creates a JLabel given the x and y coordinates to draw them and the player type
	private JLabel drawPlayer(Integer X, Integer Y, playerType type)
	{
		//multiplies size 30 by scale factor to account for different screen resolutions
		int size = (int) (30 * imageScale());
		
		//if player type = mr x
		if(type == playerType.MrX)
		{
			//gets mr x url and reads the image at the url
			URL mrX = this.getClass().getResource("MrX.jpg");
			ImageIcon mrXImage = null;
			BufferedImage mrXBuffered = null;
			try {
				mrXBuffered = ImageIO.read(mrX);
				//scales the image to be size * size
				mrXBuffered = scale(mrXBuffered, BufferedImage.TYPE_INT_RGB, size);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//creates an imageicon out of the buffered image
			mrXImage = new ImageIcon(mrXBuffered);
			//creates a jlabel with the image
			JLabel mrXLabel = new JLabel(mrXImage);
			//sets the location of the label to be the given x and y coordinate but adjusted so it displays in the center
			mrXLabel.setLocation(X - size/2, Y - size/2);
			//returns the label
			return mrXLabel;
		//otherwise if player type = detective
		} else {
			//gets detective url and reads the image
			URL detective = this.getClass().getResource("Detective.jpg");
			ImageIcon detectiveImage = null;
			BufferedImage detectiveBuffered = null;
			try {
				detectiveBuffered = ImageIO.read(detective);
				//scales image
				detectiveBuffered = scale(detectiveBuffered, BufferedImage.TYPE_INT_RGB, size);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//same as before
			detectiveImage = new ImageIcon(detectiveBuffered);
			JLabel detectiveLabel = new JLabel(detectiveImage);
			detectiveLabel.setLocation(X - 15, Y - 15);
			return detectiveLabel;
		}
	}
	
	//scales image to the required size * size and the required image type
	private BufferedImage scale(BufferedImage previous, int imageType, int size) {
	    BufferedImage scaledImage = null;
	    if(previous != null) {
	        scaledImage = new BufferedImage(size, size, imageType);
	        Graphics2D g2D = scaledImage.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(imageScale(), imageScale());
	        g2D.drawRenderedImage(previous, at);
	    }
	    return scaledImage;
	}
	
	//uses old map dimensions and new map dimensions and works out the scale factor
	private double imageScale()
	{
		double oldHeight = oldImageDimensions.getHeight();
		double newHeight = resizedImageDimensions.getHeight();
		double ratio = newHeight/oldHeight;
		return ratio;
	}
	
	//label for current player
	private void currentPlayerLabel()
	{
		JLabel currentPlayerLabel;
		//if the current ID is 0, then the current player is Mr X
		if(currentPlayerID == 0) currentPlayerLabel = new JLabel("Current Player: " + "Mr X");
		//otherwise the current player is a Detective of number current player ID
		else currentPlayerLabel = new JLabel("Current Player: " + "Detective " + currentPlayerID);		
		//sets the location of the label in relation to the map
		currentPlayerLabel.setLocation((int) (resizedImageDimensions.getWidth() * 0.05), (int) (resizedImageDimensions.getHeight() + 5));
		//sets size of the label
		currentPlayerLabel.setSize((int) (470 * imageScale()),60);
		//creates a border around the label and sets the font
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		currentPlayerLabel.setBorder(border);
		
		currentPlayerLabel.setFont(new Font("Impact", Font.PLAIN, (int) (40 * imageScale())));
		//adds the label to the layered pane at layer 1
		layeredPane.add(currentPlayerLabel, 1);
		layeredPane.setLayer(currentPlayerLabel, 1);
	}
	
	//label for next player
	private void nextPlayerLabel()
	{
		JLabel nextPlayerLabel;
		//if the next player ID is 0, then the next player is Mr X
		if(visualisable.getNextPlayerToMove() == 0) nextPlayerLabel = new JLabel("Next Player: Mr X");
		//otherwise the next player is a detective of number returned by getNextPlayerToMove
		else nextPlayerLabel = new JLabel("Next Player: Detective " + visualisable.getNextPlayerToMove());
		//sets location of the next player label in relation to the map
		nextPlayerLabel.setLocation((int) (resizedImageDimensions.getWidth() * 0.05), (int) (resizedImageDimensions.getHeight() + 65));
		//sets size of the label
		nextPlayerLabel.setSize((int) (470 * imageScale()),60);
		//creates a border around the label and sets the font
		Border border = BorderFactory.createLineBorder(Color.RED, 5);
		nextPlayerLabel.setBorder(border);
		
		nextPlayerLabel.setFont(new Font("Impact", Font.PLAIN, (int) (40 * imageScale())));
		//ads the label to the layered pane at layer 1
		layeredPane.add(nextPlayerLabel, 1);
		layeredPane.setLayer(nextPlayerLabel, 1);
	}
	
	private void currentRound()
	{
		int usedMovesTotal = 0;
		for(int ID : playerVisualisable.getMrXIdList())
		{
			usedMovesTotal = usedMovesTotal + visualisable.getMoveList(ID).size();
		}
		for(int ID : playerVisualisable.getDetectiveIdList())
		{
			usedMovesTotal = usedMovesTotal + visualisable.getMoveList(ID).size();
		}
		currentRound = 1;
		if(usedMovesTotal == 0) currentRound = 1;
		else{
			currentRound = usedMovesTotal / (playerVisualisable.getMrXIdList().size() + playerVisualisable.getDetectiveIdList().size()) + 1;
		}
		JLabel currentRoundLabel = new JLabel("Current Round: " + currentRound);
		currentRoundLabel.setLocation((int) (resizedImageDimensions.getWidth() * 0.55), (int) resizedImageDimensions.getHeight() + 40);
		currentRoundLabel.setSize((int) (390 * imageScale()),60);
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 5);
		currentRoundLabel.setBorder(border);
		currentRoundLabel.setFont(new Font("Impact", Font.PLAIN, (int) (40 * imageScale())));
		layeredPane.add(currentRoundLabel, 1);
		layeredPane.setLayer(currentRoundLabel, 1);
	}
	
	//creates a mrX panel for the tabbed pane
	private int mrXPanel()
	{
		//makes a new tabbed pane
		tabbedPane = new JTabbedPane();
		//sets the location to be 10 pixels to the right of the map - in relation to how the map is scaled
		tabbedPane.setLocation((int) (resizedImageDimensions.getWidth() + 10), 70);
		//sets size of tabbed pane
		tabbedPane.setSize(430, 400);
		int i;
		//for the number of Mr X's in the Mr X ID List
		for(i = 0; i < playerVisualisable.getMrXIdList().size(); i++)
		{
			//creates a mr x jpanel
			JPanel mrX = new JPanel();
			//sets layout to null
			mrX.setLayout(null);
			//sets size of the Mr X Jpanel
			mrX.setSize(430, 300);
			//gets the number of tickets for the given Mr X player, the player position and the station type
			JLabel noBusTickets = new JLabel("Number of bus tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, 0));
			JLabel noTaxiTickets = new JLabel("Number of taxi tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, 0));
			JLabel noTubeTickets = new JLabel("Number of tube tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, 0));
			JLabel noSdoubleTickets = new JLabel("Number of double move tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, 0));
			JLabel noSpecialTickets = new JLabel("Number of special tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.SecretMove, 0));
			JLabel playerPosition = new JLabel("Player position: " + visualisable.getNodeId(0));
			if(flag == true)
			{
				JLabel playerStationType = new JLabel("Current station type: " + customVisualiser.getStationType(0));
				playerStationType.setLocation(0, 190);
				playerStationType.setSize(400, 20);
				mrX.add(playerStationType);
			}
			//sets the location of the JLabels and the sizes
			noBusTickets.setLocation(0, 10);
			noTaxiTickets.setLocation(0, 40);
			noTubeTickets.setLocation(0, 70);
			noSdoubleTickets.setLocation(0, 100);
			noSpecialTickets.setLocation(0, 130);
			playerPosition.setLocation(0, 160);
			noBusTickets.setSize(400, 20);
			noTaxiTickets.setSize(400, 20);
			noTubeTickets.setSize(400, 20);
			noSdoubleTickets.setSize(400, 20);
			noSpecialTickets.setSize(400, 20);
			playerPosition.setSize(400, 20);
			//adds all the labels to the Mr X JPanel
			mrX.add(noBusTickets);
			mrX.add(noTaxiTickets);
			mrX.add(noTubeTickets);
			mrX.add(noSdoubleTickets);
			mrX.add(noSpecialTickets);
			mrX.add(playerPosition);
			//makes the panel visible and adds it to the tabbed pane
			mrX.setVisible(true);
			tabbedPane.add("Mr X", mrX);
		}
		//returns i, so it can be used in the Detective JPanel creation
		return i;
	}
	
	//creates a detective panel for the tabbed pane
	private void detectivePanels(int index)
	{
		//for the number of Detectives in the Detective ID list - starting at 1 in this case
		for(int i = index; i < playerVisualisable.getDetectiveIdList().size() + 1; i++)
		{
			//creates a detective1 JPanel
			JPanel detective1 = new JPanel();
			//sets layout to null
			detective1.setLayout(null);
			//sets size of detective1 jpanel
			detective1.setSize(430, 300);
			//gets the number of tickets for the given Mr X player, the player position and the station type
			JLabel noBusTickets = new JLabel("Number of bus tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, i));
			JLabel noTaxiTickets = new JLabel("Number of taxi tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, i));
			JLabel noTubeTickets = new JLabel("Number of tube tickets: " + visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, i));
			JLabel playerPosition = new JLabel("Player position: " + visualisable.getNodeId(i));
			if(flag == true)
			{
				JLabel playerStationType = new JLabel("Current station type: " + customVisualiser.getStationType(i));
				playerStationType.setLocation(0, 130);
				playerStationType.setSize(400, 20);
				detective1.add(playerStationType);
			}
			//sets the location of the JLabels and the sizes
			noBusTickets.setLocation(0, 10);
			noTaxiTickets.setLocation(0, 40);
			noTubeTickets.setLocation(0, 70);
			playerPosition.setLocation(0, 100);
			noBusTickets.setSize(400, 20);
			noTaxiTickets.setSize(400, 20);
			noTubeTickets.setSize(400, 20);
			playerPosition.setSize(400, 20);
			//adds all the labels to the Detective JPanel
			detective1.add(noBusTickets);
			detective1.add(noTaxiTickets);
			detective1.add(noTubeTickets);
			detective1.add(playerPosition);
			//makes the panel visible and adds it to the tabbed pane
			detective1.setVisible(true);
			tabbedPane.add("Detective " + String.valueOf(i), detective1);
		}
		//sets the shown tab to tbe current player ID
		tabbedPane.setSelectedIndex(currentPlayerID);
		//sets the tabbed pane to be visible
		tabbedPane.setVisible(true);
		//adds the tabbed pane to the layered pane
		layeredPane.add(tabbedPane);
		//adds the tabbed pane to layer 1
		layeredPane.setLayer(tabbedPane, 1);
	}
	
	//makes a Mr X move log
	private void mrXMoveLog()
	{
		//makes a tabbed pane for the Mr X move log
		JTabbedPane moveLog = new JTabbedPane();
		//sets location of the Mr X location in relation to the map and sets the size
		moveLog.setLocation((int) (resizedImageDimensions.getWidth() + 10), 460);
		moveLog.setSize(430, (int) (350 * imageScale()));
		//creates a movesUsed JPanel and sets the size
		JPanel movesUsed = new JPanel();
		movesUsed.setLayout(null);
		movesUsed.setSize(600, 300);
		//gets the list of used moves
		ArrayList<Initialisable.TicketType> usedMoves = (ArrayList<Initialisable.TicketType>) visualisable.getMoveList(0);
		for(int i = 0; i < usedMoves.size(); i++)
		{
			//goes through the list of used moves and adds a new label for each one to the JPanel
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
		//sets the JPanel to be visible
		movesUsed.setVisible(true);
		//adds the JPanel to the tabbed pane and sets it to be visible
		moveLog.add("Mr X Move Log", movesUsed);
		moveLog.setVisible(true);
		//add the tabbed pane to the layered pane and sets the layer to 1
		layeredPane.add(moveLog);
		layeredPane.setLayer(moveLog, 1);
	}
	
	//draws a border around all the possible places the current player can move to
	private void drawHighlights()
	{
		int size = (int) (30 * imageScale());
        int i = 100;
        //gets all the possible nodes the current player can possibly move to
        ArrayList<String> currentNodeNeighbours = (ArrayList<String>) customVisualiser.getNodeNeighbours(currentPlayerID);
        //goes through all the possible nodes
        for(String node : currentNodeNeighbours){
        	//gets the x and y coordinates of the node
        	int x = visualisable.getLocationX(Integer.valueOf(node));
        	int y = visualisable.getLocationY(Integer.valueOf(node));
        	//scales the coordinates
        	Point scaled = scalePoint(new Point(x, y));
        	//gest the highlightURL for this png and reads it into a bufferedImage
        	URL highlightURL = this.getClass().getResource("highlight.png");
    		ImageIcon highlightImage = null;
    		BufferedImage highlightBuffered = null;
    		try {
    			highlightBuffered = ImageIO.read(highlightURL);
    			//scales the bufferedimage to be size*size which is 30 * the scale factor the map was scaled by
    			highlightBuffered = scale(highlightBuffered, BufferedImage.TYPE_INT_ARGB, size);
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    		highlightImage = new ImageIcon(highlightBuffered);
    		//creates a new JLabel with the image
        	JLabel highlightLabel = new JLabel(highlightImage);
        	//sets the location so that the highlighting surrounds the node -- otherwise label set location coordinates would be from the top left corner
    		highlightLabel.setLocation(scaled.x - size/2, scaled.y - size/2);
    		//sets the size of the label
    		highlightLabel.setSize(size,size);
    		
    		highlightLabel.setVisible(true);
    		//adds the hightlightLabel to the layeredPane at layer 1 with ID i
    		layeredPane.add(highlightLabel, i);
    		layeredPane.setLayer(highlightLabel, 1);
    		//increments i
    		i++;
       	}
	}
	
	//creates a cursor object with the mouse cursor image
	private void drawCursorLabel()
	{
		//sets the size to be 50 * the map scale factor
		int size = (int) (50 * imageScale());
		URL cursorURL = this.getClass().getResource("mouse cursor.png");
		mousecursor = null;
		try {
			mousecursor = ImageIO.read(cursorURL);
			//mouse cursor is set to size (size * size)
			mousecursor = scale(mousecursor, BufferedImage.TYPE_INT_ARGB, size);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//displays the new, load and save game buttons
	private void displayButtons()
	{
		newGameButton();
		loadGameButton();
		saveGameButton();
	}

	//creates and displays the new game button
	private void newGameButton()
	{	
		//reads the new game button image
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("new.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//resizes the image
		img = resize(img, new Dimension(130, 30));
		//creates a new JButton with the image
		JButton newGame = new JButton(new ImageIcon(img));
		//sets the location in relation to the map and sets the size
		newGame.setLocation((int) (resizedImageDimensions.getWidth() + 20), 30);
		newGame.setSize(130, 30);
		//adds the button to the layeredPane at layer 0
		layeredPane.add(newGame, 0);
		//when the button is pressed, this method in this listener is called
		newGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	//gets all the components in layer 1
	        	Component[] listComponents = layeredPane.getComponentsInLayer(1);
	        	//sets window to be invisible
	        	window.setVisible(false);
	        	//asks the user for the number of detectives
	        	JFrame detectiveInput = new JFrame("Number of detectives?");
	        	detectiveInput.setTitle("Scotland Yard");
	     		int noDetectives = 0;
	     		Object[] possibilities = {"3", "4", "5","6", "7", "8", "9", "10"};
	     		String s = (String)JOptionPane.showInputDialog(detectiveInput,"Pick the number of detectives: ", "Detective selection",
	     	                JOptionPane.PLAIN_MESSAGE, null , possibilities, "3");
	     		//stores the number of detectives
	     		if(s == null) System.exit(0);
	     		else noDetectives = Integer.parseInt(s);
	     		//reinitialises the game
	        	initialisable.initialiseGame(noDetectives);
	        	 //removes all the components in layer 1 from the layered pane
	        	 for(Component b : listComponents)
	        	 {
	        		 layeredPane.remove(b);
	        	 }
	        	 //repaints the layeredpane
        		 layeredPane.repaint();
        		//sets the currentPlayerID to 0
	        	 currentPlayerID = 0;
        		 //displays all the players
	        	 displayPlayers();
	        	 //shows the Mr X tab
	        	 tabbedPane.setSelectedIndex(0);
	        	 
	        	 window.setVisible(true);
	         }          
		});
	}
	
	//creates and displays the load game button
	private void loadGameButton()
	{	
		//reads the load game button image
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("load.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//resizes the image
		img = resize(img, new Dimension(130, 30));
		//creates a new JButton with the image
		JButton loadGame = new JButton(new ImageIcon(img));
		//sets the location in relation to the map and sets the size
		loadGame.setLocation((int) (resizedImageDimensions.getWidth() + 160), 30);
		loadGame.setSize(130, 30);
		//adds the button to the layeredPane at layer 0
		layeredPane.add(loadGame, 0);
		//load game listener
		loadGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        
	         }          
		});
	}
	
	//creates and displays the save game button
	private void saveGameButton()
	{	
		//reads the save game button image
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("save.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//resizes the image
		img = resize(img, new Dimension(130, 30));
		//creates a new JButton with the image
		JButton saveGame = new JButton(new ImageIcon(img));
		//sets the location in relation to the map and sets the size
		saveGame.setLocation((int) (resizedImageDimensions.getWidth() + 300), 30);
		saveGame.setSize(130, 30);
		//adds the button to the layeredPane at layer 0
		layeredPane.add(saveGame, 0);
		//save game listener
		saveGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 
	         }          
		});
	}
	
	//mouse moved listener
	@Override
	public void mouseMoved(MouseEvent event) 
	{
		if(!visualisable.isGameOver())
		{
			//gets the current x and y position of the mouse
			int xPos = event.getXOnScreen();
	        int yPos = event.getYOnScreen();
	        //gets the mouse position is in the map
	        if(xPos <= resizedImageDimensions.getWidth() - 20 && yPos <= resizedImageDimensions.getHeight() + extraH){
	        	//if it is, change the cursor to the custom cursor
	    		Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
	    		    mousecursor, new Point(25, 25), "mouse cursor");
	    		window.setCursor(customCursor);
	    		//otherwise set the cursor to the default one
	        } else {
	        	window.setCursor(Cursor.getDefaultCursor());
	        }
	        
		}
	}
		
	//mouse clicked listener
	public void mouseClicked (MouseEvent event)
	{
		if(!visualisable.isGameOver())
		{
			//sets movePlayer to false initially
			boolean movePlayer = false;
			//gets the mouse click x and y coordinate and scales them
			int xPos = (int) (event.getXOnScreen() / imageScale());
			int yPos = (int) ((event.getYOnScreen() - extraH) / imageScale());
			//gets the nearest node from this x,y location using all the possible nodes the player can move to
			Integer newNode = controllable.getNodeIdFromLocation(xPos, yPos);
			//sets ticket type initially to null
			Initialisable.TicketType ticketType = null;
			int tempPlayerID = 0;
			//if newNode returns a valid node
			if(newNode != -1 && newNode != -2)
			{
				//give the use the option to select the transport type
				ticketType = transportSelection(ticketType, newNode);
				if(ticketType != null)
				{
					//get the next player to move and stores it in a temp variable
					tempPlayerID = visualisable.getNextPlayerToMove();
					//moves the player - returns true if it is successful, otherwise returns false
					movePlayer = controllable.movePlayer(currentPlayerID, newNode, ticketType);
					//if true
					if(movePlayer == true)
					{
						repaintPlayersLabels(tempPlayerID);
			  		 	if(visualisable.isGameOver())
			   		 	{   		 	
			  		 		int winnerPlayerID = visualisable.getWinningPlayerId();
				   		 	String winner = null;
				   		 	if(winnerPlayerID == 0) showMrXWinner(winner);
				   		 	else showDetectiveWinner(winner, winnerPlayerID);
				   		}
					} else JOptionPane.showMessageDialog(null, "Invalid move" ,"Player move", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			//if newNode is -2, there are no possible moves to move so skip the players go
			else if (newNode == -2){
				//get the next player to move and stores it in a temp variable
				tempPlayerID = visualisable.getNextPlayerToMove();
				//moves the player - returns true if it is successful, otherwise returns false
				movePlayer = controllable.movePlayer(currentPlayerID, newNode, ticketType);
				currentPlayerID = tempPlayerID;
				displayPlayers();
			}
		}
	}
	
	/*
	if(visualisable.isVisible(tempPlayerID) && playerVisualisable.getMrXIdList().contains(tempPlayerID))
	{
		MrX.setVisible(true);
	} else {
		MrX.setVisible(false);
	}
	*/
	
	private Initialisable.TicketType transportSelection(Initialisable.TicketType ticketType, Integer newNode)
	{
		
		Object[] possibilities = new Object[5];
		possibilities[0] = "Taxi";
		possibilities[1] = "Bus";
		possibilities[2] = "Underground";
		if(playerVisualisable.getMrXIdList().contains(currentPlayerID))
		{
			possibilities[3] = "Secret move";
			possibilities[4] = "Double move";
		}
		
		String s = (String)JOptionPane.showInputDialog(window,"Pick a transport method - " + newNode, "Transport selection",
	            JOptionPane.PLAIN_MESSAGE, null , possibilities, "Taxi");
		//gets the answer from the dialog box and sets the ticket type accordingly
		if(s == "Train") ticketType = Initialisable.TicketType.Underground;
		else if (s == "Bus") ticketType = Initialisable.TicketType.Bus;
		else if (s == "Taxi") ticketType = Initialisable.TicketType.Taxi;
		else if (s == "Secret move") ticketType = Initialisable.TicketType.SecretMove;
		else if (s == "Double move") ticketType = Initialisable.TicketType.DoubleMove;
		return ticketType;
	}
	
	private void showDetectiveWinner(String winner, Integer winnerPlayerID)
	{
		URL detectiveWin = this.getClass().getResource("DetectiveWin.png");
		BufferedImage detectiveWinBuffered = null;
		try {
			detectiveWinBuffered = ImageIO.read(detectiveWin);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	 		winner = "Detective " + winnerPlayerID;
	 		JOptionPane.showMessageDialog(window,"Congratulations for winning. " + winner, "Winner!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(detectiveWinBuffered));
	}
	
	private void showMrXWinner(String winner)
	{
		URL MrXWin = this.getClass().getResource("MrXWin.png");
		BufferedImage MrXWinBuffered = null;
		try {
			MrXWinBuffered = ImageIO.read(MrXWin);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	 		winner = "Mr X";
	 		JOptionPane.showMessageDialog(window,"Congratulations for winning. " + winner, "Winner!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(MrXWinBuffered));
	}
	
	private void repaintPlayersLabels(Integer tempPlayerID)
	{
		//remove all layer 1 components
		final Component[] listComponents = layeredPane.getComponentsInLayer(1);
		for(Component b : listComponents)
   	 	{
			layeredPane.remove(b);
       	}
		//repaint the layered pane
		layeredPane.repaint();
   	 	//assigns currentPlayerID to tempPlayerID
   	 	currentPlayerID = tempPlayerID;
   	 	//calls displayPlayers()
 	 	displayPlayers();
	}
	
	//not implemented
	public void mouseDragged(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void actionPerformed(ActionEvent arg0) {}

}