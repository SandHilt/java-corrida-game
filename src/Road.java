
import java.awt.*;

public class Road extends Element {

	private Color roadColor;

	public Road(int pos_x, int pos_y) {
		super(pos_x, pos_y);
		this.roadColor = new Color(51, 51, 51);

		for (int i = 0; i < 10; i++) {
			Crossover c = new Crossover(0, 0);
			c.incrementCrossoverPos_y(c.getHeight() * i * 2);
			Crossover.crossovers.add(c);
		}

	}

	public void setRoadColor(Color roadColor) {
		this.roadColor = roadColor;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(roadColor);
		g.fillRect(pos_x, pos_y, this.getWidth(), this.getHeight());

		for (Crossover c : Crossover.crossovers) {
			c.pos_x = pos_x + (this.getWidth() / 2);
			c.incrementCrossoverPos_y(2, this.getHeight());
			c.render(g);
		}
	}
}
