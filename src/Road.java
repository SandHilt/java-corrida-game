
import java.awt.*;

public class Road extends Element {

	private Color roadColor;

	public Road(Point point, Dimension dimension, Color roadColor, Color crossoverColor) {
		super(point, dimension);
		this.roadColor = roadColor;

		for (int i = 0; i < 10; i++) {
			Crossover crossover = new Crossover(new Point(0, 0), crossoverColor);
			crossover.setColor(crossoverColor);
			crossover.nextCrossover(i);
			Crossover.crossovers.add(crossover);
		}
	}

	public Road(Point point, Dimension dimension) {
		this(point, dimension, new Color(51, 51, 51), Color.WHITE);
	}

	public Road(Point point) {
		super(point);
	}

	public void setRoadColor(Color roadColor) {
		this.roadColor = roadColor;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(roadColor);

		g.fillRect(getPoint().x, getPoint().y, getDimension().width, getDimension().height);

		for (Crossover crossover : Crossover.crossovers) {
			crossover.getPoint().x = getPoint().x + (getDimension().width / 2);
			crossover.incrementCrossoverPos_y(getDimension().height);
			crossover.render(g);
		}

	}
}
