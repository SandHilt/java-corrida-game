
import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 *
 * @author Bruno O
 */
public class Cenario extends Element {

	private static ArrayList<Cenario> objects;
	private BufferedImage img;
	private Direction direction;

	/**
	 *
	 */
	public enum Direction {

		/**
		 *
		 */
		LEFT,

		/**
		 *
		 */
		RIGHT
	};

	/**
	 *
	 * @param p
	 * @param img
	 */
	public Cenario(Point p, BufferedImage img) {
		super(new Rectangle(p.x, p.y, img.getWidth(), img.getHeight()));
		this.img = img;
	}

	/**
	 *
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 *
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 *
	 * @return
	 */
	public static Cenario nextImg() {
		try {
			if (objects != null) {
				Random r = new Random();
				return objects.get(r.nextInt(objects.size()));
			} else {
				throw new Exception("Imagens ainda nao foram carregadas.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param road
	 * @param window
	 */
	public static void loadImg(Road road, int window) {
		if (objects == null) {
			objects = new ArrayList<Cenario>();

			Direction nextDirection = Direction.LEFT;

			for (int i = 0; i < 16; i++) {
				BufferedImage img = JogoCorrida.getImg(JogoCorrida.relativePath + "sprites/" + i + ".png");

				Random r = new Random(System.currentTimeMillis());

				try {
					int left = r.nextInt(road.x - img.getWidth() / 2);
					int right = road.x + road.width + left;
					int pos_x;

					Direction actualDirection = nextDirection;

					if (nextDirection == Direction.LEFT) {
						pos_x = left;
						nextDirection = Direction.RIGHT;
					} else {
						pos_x = right;
						nextDirection = Direction.LEFT;
					}

					Cenario cenario = new Cenario(new Point(pos_x, 0), img);
					cenario.setDirection(actualDirection);

					objects.add(cenario);
				} catch (Exception e) {
					System.out.println("Imagem grande.");
				}
			}
		}
	}

	/**
	 *
	 * @param g
	 */
	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y - img.getHeight(), null);
	}

}
