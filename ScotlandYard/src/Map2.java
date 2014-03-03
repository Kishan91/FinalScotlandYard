import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.image.*;
import javax.imageio.*;

public class Map2 extends JFrame implements Runnable {

	public static void main(String[] args) 
	{
		Map2 map = new Map2();
		map.run();
	}

	private void run()
	{
		JFrame window = new JFrame("Scotland Yard");
		setTitle("Scotland Yard");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		int bottom = scnMax.bottom;
		int left = scnMax.left;
		int right = scnMax.right;
		int top = scnMax.top;
		int screenWidth = (int) screenSize.getWidth() - left - right - getWidth();
		int screenHeight = (int) screenSize.getHeight() - bottom - top - getHeight();
		Dimension a = new Dimension(screenWidth,screenHeight);
		window.setPreferredSize(a);
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		URL london = this.getClass().getResource("map.jpg");
		BufferedImage image = null;
		try{
			image = ImageIO.read(london);
		} catch (Exception e){
			System.exit(1);
		}
		int width = (int) window.getWidth();
		int height = (int) window.getHeight();
		BufferedImage buffered = image;
		int buffHeight= (int) buffered.getHeight();
		int buffWidth = (int) buffered.getWidth();
		Dimension buffDimension = new Dimension(buffWidth,buffHeight);
		Dimension desDimension =  new Dimension(width,height);
		desDimension = aspectRatio(buffDimension,desDimension);
		BufferedImage resized = resize(buffered, desDimension);
		JLabel background  = new JLabel(new ImageIcon(resized));
		window.setResizable(false);
		window.getContentPane().add(background);
		window.pack();
	}

	private void printf(Object o)
	{
		System.out.print(o);
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