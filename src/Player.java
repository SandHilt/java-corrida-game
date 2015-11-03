
import java.awt.*;
import java.awt.image.*;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element {

	private BufferedImage img;
	private static Player player;

	/**
	 * Numero de pixels que a imagem eh movida
	 */
	private int delta;
	private Direction direction;
	private int imageIndex;

	public enum Direction {

		FOWARD,
		LEFT,
		RIGHT
	};

	public Player(Point point, int imageIndex) {
		super(point);
		this.imageIndex = imageIndex;
		loadImg("");
		setDimension(new Dimension(img.getWidth(), img.getHeight()));
		direction = Direction.FOWARD;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	/**
	 * Carega uma image que pode sido rodada
	 *
	 * @param imageIndex sufixo da imagem
	 * @param rotation sufixo da rotacao
	 */
	public void loadImg(String rotation) {
		if (!"".equals(rotation)) {
			rotation = "_" + rotation;
		}
		img = LoadImage.getImg("./src/car_" + imageIndex + rotation + ".png");
	}

	public void changeDirection(Direction direction) {
		this.direction = direction;
		if (!direction.equals(Direction.FOWARD)) {
			loadImg(direction.name().toLowerCase());
		} else {
			loadImg("");
		}

	}

	/**
	 * Testa as colisoes da pista e de outros inimigos
	 *
	 * @param limitX
	 * @param limitY
	 */
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
