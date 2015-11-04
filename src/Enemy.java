
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Enemy extends Element {

	private Color color;
	private BufferedImage img;

	public Enemy(Point point, String locationImg) {
		super(point);
		img = LoadImage.getImg(locationImg);
		setDimension(new Dimension(img.getWidth(), img.getHeight()));
	}

	public Enemy(Point point) {
		this(point, new Dimension(50, 50));
		this.img = null;
	}

	public Enemy(Point point, Dimension dimension) {
		super(point, dimension);
		this.img = null;

		Random r = new Random();
		color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));

	}

	public Enemy(Point point, Dimension dimension, Color color) {
		super(point, dimension);
		this.img = null;
		this.color = color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void move(int limitY) {
		int delta = Crossover.getDelta();
		getPoint().y += delta;

		if (getPoint().y + getDimension().height > limitY) {
			getPoint().y = 0;
		}
	}

	@Override
	public void render(Graphics g) {
		if (img == null) {
			g.setColor(color);
			g.fillRect(getPoint().x, getPoint().y, getDimension().width, getDimension().height);
		} else {
			g.drawImage(img, getPoint().x, getPoint().y, null);
		}
	}

}
