
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

	public static void setDelta(int delta) {
		int sum = Crossover.delta + delta;
		if (sum >= 0 && sum <= MAX_VEL) {
			Crossover.delta += delta;
		}
	}

	public static int getDelta() {
		return delta;
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
