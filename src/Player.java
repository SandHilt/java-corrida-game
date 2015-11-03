
import java.awt.*;
import java.awt.image.*;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element {

	private BufferedImage img;

	/**
	 * Numero de pixels que a imagem eh movida
	 */
	private int delta;

	public Player(Point point, Dimension dimension) {
		super(point, dimension);
	}

	public Player(Point point) {
		super(point);
		img = LoadImage.getImg("./src/car_1.png");
		setDimension(new Dimension(img.getWidth(), img.getHeight()));
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public void colision(int limitX, int limitY) {

		if (getPoint().x < 0) {
			getPoint().x = 0;
		} else if (getPoint().x + getDimension().width >= limitX) {
			getPoint().x = limitX - getDimension().width;
		}

		if (getPoint().y < 0) {
			getPoint().y = 0;
		} else if (getPoint().y + getDimension().height >= limitY) {
			getPoint().y = limitY - getDimension().height;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, getPoint().x, getPoint().y, null);
	}

	void moveRight() {
		getPoint().x += delta;
	}

	void moveLeft() {
		getPoint().x -= delta;
	}

	void moveTop() {
		getPoint().y -= delta;
	}

	void moveDown() {
		getPoint().y += delta;
	}

}
