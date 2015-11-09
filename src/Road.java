
import java.awt.*;

/**
 *
 * @author Bruno O
 */
public class Road extends Element {

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
	 */
	@Override
	public void render(Graphics g) {
		g.fillRect(x, y, width, height);

		/**
		 * Renderizando cada faixa, que faz parte da rua
		 */
		for (Crossover crossover : Crossover.crossovers) {
			crossover.x = x + (width / 2);

			if (!crossover.move(this)) {
				crossover.y = 0;
			}

			crossover.render(g);
		}

	}
}
