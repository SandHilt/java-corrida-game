
import java.awt.*;


/**
 * refere-se ao jogador usando o carro
 *
 */
class Player {

	private int pos_x;
	private int pos_y;

	public Player(int pos_x, int pos_y) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
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

	}

}
