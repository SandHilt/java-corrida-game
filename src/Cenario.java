
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Cenario extends Element {

	public static ArrayList<Cenario> objects;
	private BufferedImage img;

	public Cenario(Point point, BufferedImage img) {
		super(point, new Vector2f(0, Crossover.getDelta()));
		this.img = img;
	}

	/**
	 *
	 * @return
	 */
	public static Cenario nextImg() {
		try {
			if (objects != null) {
				Random r = new Random(System.currentTimeMillis());
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
	 */
//	public void move(Road road) {
//		int sizeRoadY = road.height;
//
//		int delta = Crossover.getDelta();
//		super.translate(0, delta);
//
//		/**
//		 * Caso o cenario tenha saido da tela ele vai chamar outro elemento
//		 */
//		if (y + height > sizeRoadY) {
//
//		}
//	}

	/**
	 *
	 * @param road
	 * @param Janela
	 */
	public static void loadImg(Road road, int Janela) {
		if (objects == null) {
			objects = new ArrayList<Cenario>();

			int pos = 0;

			for (int i = 0; i < 16; i++) {
				BufferedImage img = JogoCorrida.getImg(JogoCorrida.relativePath + "sprites/" + i + ".png");
				Random r = new Random(System.currentTimeMillis() + i);

				try {
					int left = r.nextInt(road.x - img.getWidth() / 2);
					int right = road.x + road.width + left;

					if (pos == 0) {
						pos = left;
					} else {
						pos = right;
					}

					pos = ++pos % 2;

					Cenario cenario = new Cenario(new Point(pos, 0), img);
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
		g.drawImage(img, x, y, null);
	}

}
