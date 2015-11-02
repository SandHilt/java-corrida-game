
import java.awt.*;
import java.util.*;

/**
 * Classe das faixas de transito
 */
public class Crossover extends Element {

	private Color color;
	public static ArrayList<Crossover> crossovers;
	private static int delta;

	public Crossover(int pos_x, int pos_y) {
		super(pos_x, pos_y);

		if (crossovers == null) {
			crossovers = new ArrayList<>();
			delta = 0;
		}

		this.color = Color.WHITE;
		this.setWidth(3);
		this.setHeight(15);
	}

	/**
	 * Setar a cor da faixa
	 *
	 * @param color cor da faixa
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	public static void setDelta(double delta) {
		if (delta > 0) {
			Crossover.delta += delta;
		}
	}

	public void nextCrossover(int i) {
		pos_y += getHeight() * 5 * i;
	}

	public void incrementCrossoverPos_y(int limitY) {
		pos_y += delta;

		if (pos_y - getHeight() > limitY) {
			pos_y = 0;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(pos_x, pos_y, this.getWidth(), this.getHeight());
	}

}
