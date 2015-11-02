
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Enemy extends Element {

	private Color color;

	public Enemy(int pos_x, int pos_y) {
		super(pos_x, pos_y);
		Random r = new Random();
		color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(pos_x, pos_y, 50, 50);
	}

}
