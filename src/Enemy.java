
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Enemy extends Element {

	private BufferedImage img;

	public Enemy(String locationImg) {
		super(new Point());
		img = JogoCorrida.getImg(locationImg);
		setDimension(new Dimension(img.getWidth(), img.getHeight()));
	}

	/**
	 * Gera uma posicao randomica em relacao ao eixo X
	 *
	 * @param road
	 * @return uma posicao arbitraria
	 */
	public int randomPos(Road road) {
		return randomPos(road, 1);
	}

	/**
	 *
	 * @param road
	 * @param modificador
	 * @return
	 */
	public int randomPos(Road road, double modificador) {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random r = new Random();

		int pos_x = road.getPoint().x;
		int width = (int) (road.getDimension().width * modificador);

		return pos_x + r.nextInt(width) - getDimension().width;
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
		 * Caso o inimigo tenha saido da tela ele vai reaparecer em uma nova posicao
		 */
		if (getPoint().y + getDimension().height > sizeRoadY) {
			int pos_x = randomPos(road);
			getPoint().setLocation(pos_x, 0);
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, getPoint().x, getPoint().y, null);
	}

}
