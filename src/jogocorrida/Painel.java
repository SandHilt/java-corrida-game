package jogocorrida;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Painel extends JPanel{
	private BufferedImage image;

	public Painel(){
		try{
			this.image = ImageIO.read(new File("jogocorrida/splash.gif"));
		} catch(IOException e){
			System.out.println("Ops..");
		}
	}

	public void paintComponent( Graphics g ){
		super.paintComponent(g);

		g.drawImage(image, 0, 0, null);
	}

}
