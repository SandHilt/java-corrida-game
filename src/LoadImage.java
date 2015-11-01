
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class LoadImage {

	public static BufferedImage getImg(String location) {
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(new File(location));
		} catch (IOException e) {
			buffer = null;
		}
		return buffer;
	}

}
