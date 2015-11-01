
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

	public int getPos_x() {
		return pos_x;
	}

	public int getPos_y() {
		return pos_y;
	}

	public void moveTop() {
		this.pos_y += -1;
	}

	public void moveRight() {
		this.pos_x += 1;
	}

	public void moveDown() {
		this.pos_y += 1;
	}

	public void moveLeft() {
		this.pos_x += -1;
	}

	public void render(Graphics g) {
		g.drawImage(img, pos_x, pos_y, null);
	}

}
