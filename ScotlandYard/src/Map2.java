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

	public void run()
	{
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		width = width*0.8;
		height = height*0.8;*/
		printf("iman is watching you terminal");
		JFrame window = new JFrame("Scotland Yard");
		setTitle("Scotland Yard");
		Dimension a = Toolkit.getDefaultToolkit().getScreenSize();
		printf(a.getWidth());
		printf(a.getHeight());
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setResizable(true);
		window.setVisible(true);
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
		/*printf("\n");
		printf(width);
		printf(height);*/
		BufferedImage buffered = image;
		int buffHeight= (int) buffered.getHeight();
		int buffWidth = (int) buffered.getWidth();
		printf(buffWidth);

		printf(buffHeight);
		/*int buffHeight = 1018;
		int buffWidth = 809;
		printf("\n");*/
		Dimension buffDimension = new Dimension(buffWidth,buffHeight);
		Dimension desDimension =  new Dimension(width,height);
		desDimension = aspectRatio(buffDimension,desDimension);
		printf("\n");
		printf (desDimension.getWidth());
		printf("\n");
		printf (desDimension.getHeight());


		/*printf(desDimension.getWidth());
		printf(desDimension.getHeight());*/
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

	/*private Dimension aspectRatio (Dimension image, Dimension requiredImage)
	{
		int original_width = (int) image.getWidth();
		int original_height = (int) image.getHeight();
		int designer_width = (int) requiredImage.getWidth();
		int designer_height = (int) requiredImage.getHeight();
		int original_ratio = (int) original_width / original_height;
		int designer_ratio = (int) designer_width / designer_height;
		if 	(original_ratio > designer_ratio)
		{
    		designer_height =  (int) designer_width / original_ratio;
		}
		else designer_width =  (int) designer_height * original_ratio;
		image = new Dimension(designer_width, designer_height);
		return image;*/
		private Dimension aspectRatio(Dimension imgSize, Dimension boundary) {

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