
import javax.imageio.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import javax.sound.sampled.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JogoCorrida extends JFrame implements Runnable, KeyListener {

	private FrameRate fr;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;

	private Road road;
	private Player p1;
	private Player p2;
	private ArrayList<Enemy> enemies;

	public static String relativePath = "./src/";

	private volatile boolean splash;

	private String[] locations;

	Registry reg = null;

	public JogoCorrida() {
		fr = new FrameRate();

		splash = true;

		road = null;

		p1 = new Player(new Point(250, 500), 1);
		p1.setDelta(10);

		p2 = new Player(new Point(550, 500), 2);

		locations = new String[]{JogoCorrida.relativePath + "tree_obst.png", JogoCorrida.relativePath + "stone_obst.png"};

		try {;
			reg = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			System.out.println("Java RMI registry ja exite");
		}

		try {
			IPlayer stub = (IPlayer) UnicastRemoteObject.exportObject(p1, 6789);

			try {
				reg.bind("Player1", stub);
			} catch (Exception e) {
				System.out.println("Nao consigo bindar Player1 ao registro");
			}

		} catch (RemoteException e) {
			System.out.println("Nao consigo exportar o objeto Player1");
		}

		System.out.println("Servidor RMI pronto");
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
		canvas.setBackground(new Color(1, 68, 33));
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

//					if (splash) {
//						BufferedImage img = JogoCorrida.getImg(JogoCorrida.relativePath + "splash_lg.jpg");
//						g.drawImage(img, 0, 0, null);
//						splash = false;
//					}
					/**
					 * Renderizando a rua que comeca a 10% do inicio da janela e tem 80%
					 * de tamanho
					 */
					if (road == null) {
						road = new Road(new Rectangle((int) (getWidth() * .1), 0, (int) (getWidth() * .8), getHeight()));
					}

					road.render(g);

//					Cenario.loadImg(road, getWidth());
//
//					Cenario cenario = Cenario.nextImg();
//					cenario.render(g);

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

					if (p1.getGameOver()) {
						p1.gameOver(g, new Point(getWidth() / 2, getHeight() / 2));
					} else {

						p1.render(g);
						p2.render(g);

						/**
						 * Gerando uma posicao randomica para os inimigos
						 */
						if (enemies == null) {
							enemies = new ArrayList<Enemy>();
							for (int i = 0; i < 5; i++) {
								try {
									Thread.sleep(1);
								} catch (InterruptedException e) {
								}
								Enemy enemy = new Enemy(locations[new Random().nextInt(2)]);
								enemy.getPoint().x = enemy.randomPos(road);
								enemies.add(enemy);
							}
						}

						/**
						 * Renderizando cada inimigo
						 */
						for (int i = 0; i < enemies.size(); i++) {
							Enemy enemy = enemies.get(i);
							enemy.render(g);
							enemy.move(road);
							p1.isColision(enemy);
						}
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
		if(p1.haveLife()){
			switch (e.getKeyCode()) {
				case (KeyEvent.VK_RIGHT):
					if (p1.inside(road)) {
							p1.moveRight();
							p1.changeDirection(Player.Direction.RIGHT);
					} else {
						p1.getPoint().x = road.getPoint().x + road.getDimension().width - p1.getDimension().width;
					}
					break;
				case (KeyEvent.VK_LEFT):
					if (p1.inside(road)) {
							p1.moveLeft();
							p1.changeDirection(Player.Direction.LEFT);
					} else {
						p1.getPoint().x = road.getPoint().x;
					}
					break;
				case (KeyEvent.VK_UP):
						Crossover.setDelta(5);
					break;
				case (KeyEvent.VK_DOWN):
						Crossover.setDelta(-5);
					break;
			}
		}
	}

	public static BufferedImage getImg(String file) {
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(new File(file));
		} catch (IOException e) {
			buffer = null;
			System.out.println("Erro no carregamento da imagem.");
		}
		return buffer;
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
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}

}
