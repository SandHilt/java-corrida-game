
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
	private byte life;

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
		life = 3;
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
	 * Colisao com um elemento
	 *
	 * @param el elemento a ser testado
	 */
	public void colision(Element el) {
		int dx = getPoint().x - el.getPoint().x;
		int dy = getPoint().y - el.getPoint().y;

		String s = dx + "x" + dy;

		if (dx <= el.getDimension().width) {
			s += "Mesma linha.";
			if (dy >= 0 && dy <= el.getDimension().height) {
				Crossover.stopDelta();
			}
		}

		System.out.println(s);
	}

	/**
	 * Testa as colisoes da pista
	 *
	 * @param limitA intervalo A na coordernada X
	 * @param limitB intervalo B na coordenada X
	 */
	public void colision(int limitA, int limitB) {

		if (getPoint().x <= limitA) {
			getPoint().x = limitA;
		} else if (getPoint().x + getDimension().width >= limitB) {
			getPoint().x = limitB - getDimension().width;
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
}
