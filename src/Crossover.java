
import java.awt.*;
import java.util.*;

/**
 * Classe das faixas de transito
 */
public class Crossover extends Element {

	private Color color;

	public static ArrayList<Crossover> crossovers;
	private static int delta;
	public static final int MAX_VEL = 15;

	public Crossover(Rectangle r, Color color) {
		super(r);
		this.color = color;

		if (crossovers == null) {
			crossovers = new ArrayList<Crossover>();
			delta = 0;
		}
	}

	public Crossover(Point point, Color color) {
		this(new Rectangle(point, new Dimension(3, 15)), color);
	}

	/**
	 * Setar a cor da faixa
	 *
	 * @param color cor da faixa
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	public static void setDelta(int delta) {
		int sum = Crossover.delta + delta;

		if (sum >= 0 && sum <= MAX_VEL) {
			Crossover.delta += delta;
		}
	}

	public static int getDelta() {
		return delta;
	}

	public static void stopDelta() {
		delta = 0;
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
	public void move(int limitY) {
		y += delta;

		if (y - height > limitY) {
			y = 0;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

}
