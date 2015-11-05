
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Cenario extends Element {

	public static ArrayList<Cenario> objects;
	private BufferedImage img;

	public Cenario(Point point, BufferedImage img) {
		super(point);
		this.img = img;
	}

	public static void loadImg() {
		if (objects == null) {
			objects = new ArrayList<Cenario>();
			for (int i = 0; i < 16; i++) {
				BufferedImage img = JogoCorrida.getImg(JogoCorrida.relativePath + "sprites/" + i + ".png");
				Cenario cenario = new Cenario(new Point(), img);
				objects.add(cenario);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, getPoint().x, getPoint().y, null);
	}

}
