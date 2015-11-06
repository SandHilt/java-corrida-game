
import java.awt.*;
import java.awt.image.*;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element implements IPlayer {

	private BufferedImage img;

	/**
	 * Numero de pixels que a imagem eh movida
	 */
	private int delta;
	private Direction direction;
	private int imageIndex;
	private byte life;
	private volatile boolean gameOver;

	public enum Direction {

		FOWARD,
		LEFT,
		RIGHT
	};

	public Player(Point point, int imageIndex) {
		super(point);
		try {
			if (imageIndex < 1 || imageIndex > 2) {
				throw new Exception("Erro no indice da imagem do Player.");
			}

			this.imageIndex = imageIndex;
			loadImg("");
			setDimension(new Dimension(img.getWidth(), img.getHeight()));
			life = 3;
			delta = 5;
			direction = Direction.FOWARD;
			gameOver = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public boolean getGameOver() {
		return gameOver;
	}

	public boolean haveLife() {
		return life > 0;
	}

	/**
	 * Renderiza o game over
	 *
	 * @param g
	 * @param p
	 */
	public void gameOver(Graphics g, Point p) {
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 72));
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", p.x, p.y);
	}

	/**
	 * Carega uma imagem que pode sido rodada
	 *
	 * @param imageIndex sufixo da imagem
	 * @param rotation sufixo da rotacao
	 */
	public void loadImg(String rotation) {
		if (!"".equals(rotation)) {
			rotation = "_" + rotation;
		}
		img = JogoCorrida.getImg(JogoCorrida.relativePath + "car_" + imageIndex + rotation + ".png");
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
	 * Se os elementos colidiram, entao para o carro
	 *
	 * @param element
	 */
	public void isColision(Enemy enemy) {
		if (this.colision(enemy) && enemy.isObstacle()) {
			Crossover.stopDelta();
			life--;
			enemy.setObstacle();
		}
	}

	@Override
	public void moveRight() {
		getPoint().x += delta;
	}

	@Override
	public void moveLeft() {
		getPoint().x -= delta;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, getPoint().x, getPoint().y, null);
	}

}
