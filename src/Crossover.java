
import java.awt.*;
import java.util.*;

public class Crossover extends Element {

	private Color color;
	public static ArrayList<Crossover> crossovers;

	public Crossover(int pos_x, int pos_y) {
		super(pos_x, pos_y);

		if (crossovers == null) {
			crossovers = new ArrayList<>();
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

	/**
	 * Incrementa a pos y
	 *
	 * @param delta valor a ser acrescido
	 */
	void incrementCrossoverPos_y(int delta) {
		pos_y += delta;
	}

	public void incrementCrossoverPos_y(int delta, int height) {
		this.incrementCrossoverPos_y(delta);

		if (pos_y - getHeight() > height) {
			pos_y = 0;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(pos_x, pos_y, this.getWidth(), this.getHeight());
	}

}
