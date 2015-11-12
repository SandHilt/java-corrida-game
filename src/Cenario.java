
import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Classe para mostrar elemento de cenario na tela
 *
 */
public class Cenario extends Element {

	private static ArrayList<Cenario> objects;
	private BufferedImage img;
	private Direction direction;

	/**
	 * Enumeravel para direcao que vai aparecer o elemento
	 */
	public enum Direction {

		/**
		 * Aparece na esquerda da tela
		 */
		LEFT,
		/**
		 * Aparece na direita da tela
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
	 * Pega a direcao que aparece
	 *
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Seta a direcao que aparece o elemento.
	 *
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Pega uma imagem arbitraria
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
	 * Carregando em uma lista estatica todas as imagens de cenario
	 * Colocando uma posica e direcao em cada elemento
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
	 * Redenrizando o cenario acima da tela
	 * @param g
	 */
	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y - img.getHeight(), null);
	}

}
