
import java.awt.*;

public class Road extends Element {

	private Color roadColor;

	private Color crossover;
	private int crossoverWidth;
	private int crossoverHeight;

	public Road(int pos_x, int pos_y) {
		super(pos_x, pos_y);

		this.roadColor = new Color(51, 51, 51);
		this.crossover = Color.WHITE;
		this.crossoverWidth = 3;
		this.crossoverHeight = 15;
	}

	public void setRoadColor(Color roadColor) {
		this.roadColor = roadColor;
	}

	public void setCrossover(Color crossover) {
		this.crossover = crossover;
	}

	public void setCrossoverWidth(int crossoverWidth) {
		this.crossoverWidth = crossoverWidth;
	}

	public void setCrossoverHeight(int crossoverHeight) {
		this.crossoverHeight = crossoverHeight;
	}

	private void crossover(Graphics g) {
		g.setColor(crossover);

		int centro_x = (pos_x + this.getWidth()) / 2;
		int centro_y = (pos_y + this.getHeight()) / 2;

		g.fillRect(centro_x, centro_y, crossoverWidth, crossoverHeight);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(roadColor);
		g.fillRect(pos_x, pos_y, this.getWidth(), this.getHeight());
		crossover(g);
	}
}
