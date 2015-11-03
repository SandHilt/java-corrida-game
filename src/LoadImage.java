
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class LoadImage {

	public static BufferedImage getImg(String file) {
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(new File(file));
		} catch (IOException e) {
			buffer = null;
			System.out.println("Erro no carregamento da imagem.");
		}
		return buffer;
	}

}
