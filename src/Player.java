
import java.awt.*;
import java.awt.image.*;
import java.rmi.RemoteException;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element implements IPlayer {

	private BufferedImage img;

	/* vida do personagem */
	private byte life;

	private Direction direction;
	private int imageIndex;

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
			super.setSize(img.getWidth(), img.getHeight());

			life = 3;
			direction = Direction.FOWARD;
			gameOver = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * @param rotation sufixo da rotacao que pode ser left ou right
	 */
	public void loadImg(String rotation) {
		if (!"".equals(rotation)) {
			rotation = "_" + rotation;
		}
		img = JogoCorrida.getImg(JogoCorrida.relativePath + "car/car_" + imageIndex + rotation + ".png");
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
	 * @param enemy
	 */
	public void isColision(Enemy enemy) {
		if (super.intersects(enemy) && enemy.isObstacle()) {
			Element.stopVel();
			life--;
			enemy.setObstacle();
		}
	}

	@Override
	public void moveRight() {
		x += getVel();
	}

	@Override
	public void moveLeft() {
		x -= getVel();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	@Override
	public Point getPoint() throws RemoteException {
		return new Point(x, y);
	}

}
