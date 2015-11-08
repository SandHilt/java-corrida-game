
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Enemy extends Element {

	private BufferedImage img;
	private boolean obstacle;

    /**
     *
     * @param locationImg
     */
    public Enemy(String locationImg) {
		super();
		img = JogoCorrida.getImg(locationImg);
		super.setSize(img.getWidth(), img.getHeight());
		obstacle = true;
	}

    /**
     *
     */
    public void setObstacle() {
		obstacle = false;
	}

    /**
     *
     * @return
     */
    public boolean isObstacle() {
		return obstacle;
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

		int pos_x = road.x;
		int width = (int) (road.width * modificador);

		return pos_x + r.nextInt(width) - width;
	}

	/**
	 * Responsavel por mover o inimigo
	 *
	 * @param road Passando a rua como paramero
	 */
//	public void move(Road road) {
//		int sizeRoadY = road.height;
//
//		int delta = Crossover.getDelta();
//		super.translate(0, delta);
//
//		/**
//		 * Caso o inimigo tenha saido da tela ele vai reaparecer em uma nova posicao
//		 */
//		if (y + height > sizeRoadY) {
//			obstacle = true;
//			int pos_x = randomPos(road);
//			super.setLocation(pos_x, 0);
//		}
//	}

    /**
     *
     * @param g
     */
    @Override
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
	}

}
