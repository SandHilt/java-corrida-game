
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Enemy extends Element {

	private Color color;
	private BufferedImage img;

	public Enemy(Point point) {
		this(point, new Dimension(50, 50));
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
		g.fillRect(getPoint().x, getPoint().y, getDimension().width, getDimension().height);
	}

}
