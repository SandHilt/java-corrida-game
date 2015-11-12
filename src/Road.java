
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Bruno O
 */
public class Road extends Element {

	/**
	 *
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public Road(int x, int y, int w, int h) {
		super(new Rectangle(x, y, w, h));
	}

	/**
	 *
	 * @param rectangle
	 * @param roadColor
	 * @param crossoverColor
	 */
	public Road(Rectangle rectangle, Color roadColor, Color crossoverColor) {
		super(rectangle, roadColor);

		/**
		 * Criando cada faixa de transito
		 */
		for (int i = 0; i < 10; i++) {
			Crossover crossover = new Crossover(crossoverColor);
			crossover.setColor(crossoverColor);
			crossover.nextCrossover(i);
			Crossover.crossovers.add(crossover);
		}
	}

	/**
	 *
	 * @param rectangle
	 * @param roadColor
	 */
	public Road(Rectangle rectangle, Color roadColor) {
		this(rectangle, roadColor, Color.WHITE);
	}

	/**
	 *
	 * @param rectangle
	 */
	public Road(Rectangle rectangle) {
		this(rectangle, new Color(51, 51, 51));
	}

	/**
	 *
	 * @param g
	 * @param vel
	 */
	public void render(Graphics g, int vel) {
		this.render(g, vel, Crossover.crossovers);
	}

	/**
	 *
	 * @param g
	 * @param vel
	 * @param crossovers
	 */
	public void render(Graphics g, int vel, ArrayList<Crossover> crossovers) {
		g.fillRect(x, y, width, height);

		/**
		 * Renderizando cada faixa, que faz parte da rua
		 */
		for (Crossover crossover : crossovers) {
			crossover.x = x + (width / 2);

			if (!crossover.move(this, vel)) {
				crossover.y = 0;
			}

			crossover.render(g);
		}
	}

	/**
	 *
	 * @param g
	 */
	@Override
	public void render(Graphics g) {
		try {
			throw new Exception("Metodo nao implementado, utilize o outro");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
