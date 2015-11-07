
import java.awt.*;

public class Road extends Element {

	private Color roadColor;

	public Road(Rectangle r, Color roadColor, Color crossoverColor) {
		super(r);
		this.roadColor = roadColor;

		for (int i = 0; i < 10; i++) {
			Crossover crossover = new Crossover(new Point(), crossoverColor);
			crossover.setColor(crossoverColor);
			crossover.nextCrossover(i);
			Crossover.crossovers.add(crossover);
		}
	}

	public Road(Rectangle r) {
		this(r, new Color(51, 51, 51), Color.WHITE);
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

		g.fillRect(x, y, width, height);

		for (Crossover crossover : Crossover.crossovers) {
			crossover.x = x + (width / 2);
			crossover.move(height);
			crossover.render(g);
		}

	}
}
