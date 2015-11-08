
import java.awt.*;
import java.util.*;

/**
 * Classe das faixas de transito
 */
public class Crossover extends Element {

	private Color color;

	/**
	 *
	 */
	public static ArrayList<Crossover> crossovers;
	private static Vector2f delta;

	/**
	 *
	 */
	public static final int MAX_VEL = 15;

	/**
	 *
	 * @param rectangle
	 * @param color
	 */
	public Crossover(Rectangle rectangle, Color color) {
		super(rectangle);
		this.color = color;

		if (crossovers == null) {
			crossovers = new ArrayList<Crossover>();
			delta = new Vector2f();
		}
	}

	/**
	 *
	 * @param point
	 * @param color
	 */
	public Crossover(Point point, Color color) {
		this(new Rectangle(point, new Dimension(3, 15)), color);
	}

	public Crossover(Color color) {
		this(new Point(), color);
	}

	/**
	 * Setar a cor da faixa
	 *
	 * @param color cor da faixa
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 *
	 * @param delta
	 */
//	public void setDelta(int delta) {
//		delta.translate(0, delta);
//
//		if (sum >= 0 && sum <= MAX_VEL) {
//			Crossover.delta += delta;
//		}
//	}
	/**
	 *
	 * @return
	 */
	public static Vector2f getDelta() {
		return delta;
	}

	/**
	 *
	 */
	public static void stopDelta() {
		delta.y = 0;
	}

	/**
	 * Margem entre cada faixa
	 *
	 * @param i
	 */
	public void nextCrossover(int i) {
		y += height * 5 * i;
	}

	/**
	 * Responsavel por fazer a animacao das faixas
	 *
	 * @param limitY qual o limite da rua
	 */
//	public void move(int limitY) {
//		y += delta.y;
//
//		if (y - height > limitY) {
//			y = 0;
//		}
//	}
	/**
	 *
	 * @param g
	 */
	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

}
