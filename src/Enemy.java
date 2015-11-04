
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Enemy extends Element {

	private Color color;
	private BufferedImage img;

	public Enemy(Point point, String locationImg) {
		super(point);
		img = JogoCorrida.getImg(locationImg);
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

	/**
	 * Gera uma posicao randomica em relacao ao eixo X
	 *
	 * @param road
	 * @return uma posicao arbitraria
	 */
	public static int randomPos(Road road) {
		return randomPos(road, 1);
	}

	public static int randomPos(Road road, double modificador) {
		Random r = new Random();

		int pos_x = road.getPoint().x;
		int width = (int) (road.getDimension().width * modificador);

		return pos_x + r.nextInt(width);
	}

	/**
	 * Responsavel por mover o inimigo
	 *
	 * @param road Passando a rua como paramero
	 */
	public void move(Road road) {
		int sizeRoadY = road.getDimension().height;

		int delta = Crossover.getDelta();
		getPoint().translate(0, delta);

		/**
		 * Caso o inimigo tenha saido da tela Ele vai reaparecer em uma nova posicao
		 */
		if (getPoint().y + getDimension().height > sizeRoadY) {
			int pos_x = randomPos(road, .5);
			getPoint().setLocation(pos_x, 0);
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
