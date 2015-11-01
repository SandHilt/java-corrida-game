
import java.awt.*;
import java.awt.image.*;

/**
 * refere-se ao jogador usando o carro
 *
 */
class Player {

	private int pos_x;
	private int pos_y;

	private int width;
	private int height;

	private BufferedImage img;

	public Player(int pos_x, int pos_y) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;

		img = LoadImage.getImg("./src/car.png");

		width = img.getWidth();
		height = img.getHeight();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void moveTop() {
		pos_y--;
	}

	public void moveRight() {
		pos_x++;
	}

	public void moveDown() {
		pos_y++;
	}

	public void moveLeft() {
		pos_x--;
	}

	public void render(Graphics g) {
		g.drawImage(img, pos_x, pos_y, null);
	}

}
