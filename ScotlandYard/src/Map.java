import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.image.*;
import javax.imageio.*;

public class Map extends JFrame implements Runnable {

	public static void main(String[] args) 
	{
		Map map = new Map();
		map.run();
	}

	public void run()
	{
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		width = width*0.8;
		height = height*0.8;*/
		JFrame window = new JFrame("Scotland Yard");
		setTitle("Scotland Yard");
		window.setExtendedState(Frame.MAXIMIZED_BOTH);;
		URL london = this.getClass().getResource("map.jpg");
		Image image = null;
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			image = ImageIO.read(london);
		} catch (Exception e){
			System.exit(1);
		}
		int width = window.getWidth();
		int height = window.getHeight();
		BufferedImage buffered = (BufferedImage) image;
		double buffHeight = buffered.getHeight();
		double buffWidth = buffered.getWidth();
		BufferedImage resized = resize(buffered, width, height);
		JLabel background  = new JLabel(new ImageIcon(resized));
		window.getContentPane().add(background);
		window.pack();


	}

	/*private Dimension aspectRatio (BufferedImage image, int width, int height)
	{



	}*/

	public static BufferedImage resize(BufferedImage image, int width, int height) 
	{
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}

}