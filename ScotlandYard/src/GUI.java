import javax.swing.*;
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
	}
	
	private void newGameButton()
	{		
		JButton newGame = new JButton("New Game");
		Coordinate newGameXY = scaleCoordinate(new Coordinate(1100, 30));
		newGame.setLocation(newGameXY.x, newGameXY.y);
		Coordinate newGameSize = scaleCoordinate(new Coordinate(150, 30));
		newGame.setSize(newGameSize.x, newGameSize.y);
		layeredPane.add(newGame, 2);
		layeredPane.moveToBack(background);	
		newGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 Component[] listComponents = layeredPane.getComponentsInLayer(1);
	        	 initialisable.initialiseGame(3);
	        	 for(Component b : listComponents)
	        	 {
	        		 layeredPane.remove(layeredPane.getIndexOf(b));
	        	 }
	        	 displayPlayers();
	         }          
		});
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
		double scaleFactor = imageScale();
		for(Integer b : mrXIdList)
		{
			node = playerVisualisable.getNodeId(b);
			Test.printf("MR X LOCATION" + node);
			JLabel MrX = drawNode(node, playerType.MrX, scaleFactor);
			MrX.setSize(30,30);
			layeredPane.add(MrX, 1);
			layeredPane.setLayer(MrX, 1);
			
		}
		for(Integer b : detectiveIdList)
		{
			node = playerVisualisable.getNodeId(b);
			Test.printf("Detective LOCATION" + node);
			JLabel Detective = drawNode(node, playerType.Detective, scaleFactor);
			Detective.setSize(30,30);
			layeredPane.add(Detective, 1);
			layeredPane.setLayer(Detective, 1);
		}
		
	}
	
	private JLabel drawNode(Integer node, playerType type, double scaleFactor)
	{
		Coordinate toDrawUnscaled = new Coordinate(playerVisualisable.getLocationX(node), playerVisualisable.getLocationY(node));
		Coordinate toDrawScaled = scaleCoordinate(toDrawUnscaled);
		JLabel toDraw = drawPlayer(toDrawScaled.x, toDrawScaled.y, type, scaleFactor);
		return toDraw;
	}
	
	private BufferedImage scale(BufferedImage previous, int imageType, int size, double scaleFactor) {
	    BufferedImage scaledImage = null;
	    if(previous != null) {
	        scaledImage = new BufferedImage(size, size, imageType);
	        Graphics2D g2D = scaledImage.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
	        g2D.drawRenderedImage(previous, at);
	    }
	    return scaledImage;
	}
	
	private JLabel drawPlayer(Integer X, Integer Y, playerType type, double scaleFactor)
	{
		
		int size = (int) (30 * scaleFactor);
		if(type == playerType.MrX)
		{
			URL mrX = this.getClass().getResource("MrX.jpg");
			ImageIcon mrXImage = null;
			BufferedImage mrXBuffered = null;

			try {
				mrXBuffered = ImageIO.read(mrX);
				mrXBuffered = scale(mrXBuffered, BufferedImage.TYPE_INT_RGB, size, scaleFactor);
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
				detectiveBuffered = scale(detectiveBuffered, BufferedImage.TYPE_INT_RGB, size, scaleFactor);
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
		double ratio = width/height;
		double adjHeight = adjWidth/ratio;
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
		desDimension =  scale(a,0.90);
		desDimension = aspectRatio(buffDimension,desDimension);
		BufferedImage resized = resize(buffered, desDimension);
		JLabel background  = new JLabel(new ImageIcon(resized));
		background.setSize(resized.getWidth(), resized.getHeight());
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