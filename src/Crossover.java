
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

	/**
	 *
	 * @param rectangle
	 * @param color
	 */
	public Crossover(Rectangle rectangle, Color color) {
		super(rectangle, new Vector2f(0, 1));
		this.color = color;

		if (crossovers == null) {
			crossovers = new ArrayList<Crossover>();
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

	public void nextCrossover(int i) {
		y += height * 5 * i;
	}

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
