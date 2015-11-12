
import java.awt.*;
import java.awt.image.*;
import java.rmi.*;
import javax.swing.*;

/**
 * refere-se ao jogador usando o carro
 */
class Player extends Element implements IPlayer {

	private BufferedImage img;
	public int vel = 0;

	/**
	 * Velocidade maxima do carrinho
	 */
	public static final int MAX_VEL = 100;

	/* vida do personagem */
	private byte life;
	public static final byte MAX_LIFE = 3;
	private Direction direction;
	private int imageIndex;
	private Timer timer;
	private boolean connected;

	public enum Direction {
		FOWARD,
		LEFT,
		RIGHT
	};

	public Player(Point point, int imageIndex) {
		super(point);
		this.connected = false;
		try {
			if (imageIndex < 1 || imageIndex > 2) {
				throw new Exception("Erro no indice da imagem do Player.");
			}

			this.imageIndex = imageIndex;

			loadImg("");
			super.setSize(img.getWidth(), img.getHeight());

			life = MAX_LIFE;
			direction = Direction.FOWARD;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Pega a velocidade no instante
	 *
	 * @return
	 */
	@Override
	public int getVel() {
		return vel;
	}

	/**
	 * Incrementa a velocidade no jogo
	 *
	 * @param vel
	 */
	@Override
	public void setVel(int vel) {
		int dx = this.vel + vel;
		if (dx <= Player.MAX_VEL && dx >= 0) {
			this.vel = dx;
		}
	}

	/**
	 * Para todos os elementos do jogo
	 */
	public void stopVel() {
		if (vel != 0) {
			vel = 0;
		}
	}

	public byte getLife() {
		return life;
	}

	@Override
	public boolean haveLife() {
		return life > 0;
	}

	public void winner(Graphics g, Point p, String s) throws RemoteException {
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 60));
		g.setColor(Color.WHITE);
		g.drawString("Player" + s +"WIN", p.x, p.y);
	}

	/**
	 * Renderiza o game over
	 *
	 * @param g
	 * @param p
	 */
	public void gameOver(Graphics g, Point p) throws RemoteException {
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 60));
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", p.x, p.y);
	}

	/**
	 * Carega uma imagem que pode sido rodada
	 *
	 * @param imageIndex sufixo da imagem
	 * @param rotation sufixo da rotacao que pode ser left ou right
	 */
	private void loadImg(String rotation) {
		if (!"".equals(rotation)) {
			rotation = "_" + rotation;
		}
		img = JogoCorrida.getImg(JogoCorrida.relativePath + "car/car_" + imageIndex + rotation + ".png");
	}

	@Override
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
	public boolean isColision(Enemy enemy, Sound crash) {
		if (super.intersects(enemy) && enemy.isObstacle()) {
			stopVel();
			life--;
			enemy.setObstacle();
			crash.playCrash();
			return true;
		}

		return false;
	}

	@Override
	public void moveRight(Road road) {
		if (road.contains(x + vel, y, width, height)) {
			x += getVel() + 15;
			changeDirection(Direction.RIGHT);
		} else {
			x = road.x + road.width - width;
		}
	}

	@Override
	public void moveLeft(Road road) {
		if (road.contains(x - vel, y, width, height)) {
			x -= getVel() + 15;
			changeDirection(Direction.LEFT);
		} else {
			x = road.x;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
	}

}
