
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
//import javafx.scene.media.*;

//import java.rmi.*;
//import java.rmi.registry.*;
//import java.rmi.server.*;
public class JogoCorrida extends JFrame implements Runnable, KeyListener {

	private FrameRate fr;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;

	private Road road;
	private Player p1;
	private Player p2;
	private ArrayList<Enemy> en;

//	private MediaPlayer media;
	public JogoCorrida() {
		fr = new FrameRate();

		road = null;

		p1 = new Player(new Point(250, 500), 1);
		p1.setDelta(5);

		p2 = new Player(new Point(550, 500), 2);

		en = new ArrayList<>();

		String s = "./src/tree_obst.png";

		en.add(new Enemy(new Point(150, 50), s));

//		media = new MediaPlayer(new Media("./src/soundtrack.mp3"));
	}

	public static void main(String[] args) {
		final JogoCorrida jogo = new JogoCorrida();
		jogo.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jogo.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jogo.createAndShowGui();
			}
		});
	}

	public void createAndShowGui() {
		Canvas canvas = new Canvas();
		canvas.setSize(800, 600);
		canvas.setBackground(Color.GREEN);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("The Need Velocity Run");
		setIgnoreRepaint(true);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();

//		media.play();
		canvas.addKeyListener(this);

		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		running = true;
		fr.init();

		while (running) {
			gameLoop();
			try {
				Thread.sleep(15);
			} catch (InterruptedException ex) {
			}
		}
	}

	@Override
	public String toString() {
		return getWidth() + "x" + getHeight();
	}

	private void gameLoop() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());

					/**
					 * Renderizando a rua
					 */
					if (road == null) {
						road = new Road(new Point(((int) (getWidth() * .1)), 0), new Dimension((int) (getWidth() * .8), getHeight()));
					}

					road.render(g);

					render(g);

					/**
					 * Debug janela WxH carro: posicao tamanho velocidade
					 */
					g.setColor(Color.YELLOW);
					g.drawString("janela:" + toString(), 500, 180);
					g.drawString("carro_pos:" + p1.getPoint().toString(), 500, 200);
					g.drawString("carro_tam:" + p1.getDimension().toString(), 500, 220);
					g.drawString("carro_vel:" + p1.getDelta(), 500, 240);
					g.drawString("crossover_vel:" + Crossover.getDelta() + "/" + Crossover.MAX_VEL, 500, 260);

					/**
					 * Teste de colisao na tela
					 */
					//p1.colision(road.getPoint().x, road.getPoint().x + (road.getDimension().width / 2));
//					p1.colision(road.getPoint());

					p1.render(g);
					p2.render(g);

					/**
					 * Renderizando cada inimigo
					 */
					for (int i = 0; i < en.size(); i++) {
						Enemy enemy = en.get(i);
						enemy.render(g);
						enemy.move(getHeight());
						p1.colision(enemy);
						//p1.colision(enemy.getPoint().x, enemy.getPoint().y);
					}

				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

			case (KeyEvent.VK_RIGHT):
				p1.moveRight();
				p1.changeDirection(Player.Direction.RIGHT);
				break;
			case (KeyEvent.VK_LEFT):
				p1.moveLeft();
				p1.changeDirection(Player.Direction.LEFT);
				break;
			case (KeyEvent.VK_UP):
				Crossover.setDelta(5);
				break;
			case (KeyEvent.VK_DOWN):
				Crossover.setDelta(-5);
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		p1.changeDirection(Player.Direction.FOWARD);
	}

	private void render(Graphics g) {
		fr.calculate();
		g.setColor(Color.red);
		g.drawString(fr.getFrameRate(), 300, 20);
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();

//			media.stop();
//			media.dispose();
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}

}
