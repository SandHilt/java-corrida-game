
import java.awt.*;
import java.awt.image.*;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element {

	private final BufferedImage img;

	private int vel;

	public Player(int pos_x, int pos_y) {
		super(pos_x, pos_y);

		img = LoadImage.getImg("./src/car.png");

		this.setWidth(img.getWidth());
		this.setHeight(img.getHeight());
	}

	public int getVel() {
		return vel;
	}

	public void setVel(int vel) {
		this.vel = vel;
	}

	public void colision(int limitX, int limitY) {
		if (pos_x < 0) {
			pos_x = 0;
		} else if (pos_x + getWidth() >= limitX) {
			pos_x = limitX - getWidth();
		}

		if (pos_y < 0) {
			pos_y = 0;
		} else if (pos_y + getHeight() >= limitY) {
			pos_y = limitY - getHeight();
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, pos_x, pos_y, null);
	}

	void moveRight() {
		pos_x += vel;
	}

	void moveLeft() {
		pos_x -= vel;
	}

	void moveTop() {
		pos_y -= vel;
	}

	void moveDown() {
		pos_y += vel;
	}

}
