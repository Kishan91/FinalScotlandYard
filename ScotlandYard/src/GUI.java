import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;


public class GUI extends GameVisualiser {
	
	private Dimension desDimension;
	private Dimension buffDimension;
	public double ratio;

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
		JFrame window = new JFrame("Scotland Yard");
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
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel background = createImage(a);
		window.getContentPane().add(background);
		window.pack();
		ArrayList<Integer> mrXIdList = (ArrayList<Integer>) playerVisualisable.getMrXIdList();
		ArrayList<Integer> detectiveIdList = (ArrayList<Integer>) playerVisualisable.getDetectiveIdList();
		Integer node;
		for(Integer b : mrXIdList)
		{
			node = playerVisualisable.getNodeId(b);	
			drawNode(node, playerType.MrX);
		}
		for(Integer b : detectiveIdList)
		{
			node = playerVisualisable.getNodeId(b);
			drawNode(node, playerType.Detective);
		}
	}
	
	private void drawNode(Integer node, playerType type)
	{
		Coordinate toDrawUnscaled = new Coordinate(playerVisualisable.getLocationX(node), playerVisualisable.getLocationY(node));
		Test.printf("UNSCALED YEEEEE:");
		Test.printf(type);
		Test.printf("X coordinate" + toDrawUnscaled.x);
		Test.printf("Y coordinate" + toDrawUnscaled.y);
		Coordinate toDrawScaled = scaleCoordinate(toDrawUnscaled);
		drawPlayer(toDrawScaled.x, toDrawScaled.y, type);
	}
	
	private void drawPlayer(Integer X, Integer Y, playerType type)
	{
		Test.printf("SCALED YEEE:");
		Test.printf(type);
		Test.printf("X coordinate" + X);
		Test.printf("Y coordinate" + Y);
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