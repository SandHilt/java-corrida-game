
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.*;
import java.util.Random;

public class Enemy extends Element {

	private Color color;
	private BufferedImage img;
	private boolean obstaculo;

	public Enemy(Point point) {
		this(point, new Dimension(50, 50));
	}

	public Enemy(Point point, String locationImg) {
		super(point);
		img = LoadImage.getImg(locationImg);
		setDimension(new Dimension(img.getWidth(), img.getHeight()));
	}

	public Enemy(Point point, Dimension dimension) {
		super(point, dimension);
		Random r = new Random();
		color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
	}

	public Enemy(Point point, Dimension dimension, Color color) {
		super(point, dimension);
		this.color = color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(getPoint().x, getPoint().y, 50, 50);
	}

}
