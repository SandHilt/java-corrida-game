
import java.awt.image.*;
import javax.imageio.*;
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
