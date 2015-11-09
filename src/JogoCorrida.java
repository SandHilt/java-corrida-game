
import javax.swing.*;
import javax.imageio.*;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class JogoCorrida extends JFrame implements Runnable, KeyListener {

	private FrameRate fr;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;

	private Road road;
	private Player p1;
	private Player p2;
	private ArrayList<Enemy> enemies;
	private int numberCenario;
	private Cenario cenario;

	private static Map<String, BufferedImage> bufferImages;

	public static String relativePath = "./src/";
	private Canvas canvas;

	public enum Tempo {

		NEVE(new Color(254, 254, 250)),
		FLORESTA(new Color(1, 68, 33)),
		CIDADE(new Color(51, 51, 51)),
		DESERTO(new Color(237, 201, 175));

		private final Color cor;

		Tempo(Color cor) {
			this.cor = cor;
		}

	};

	private volatile boolean splash;
	private Timer timerSplash;
	private int timerSplashDelay = 1000;
	private int timerSplashFlick = 300;
	private volatile boolean splashPressEnter;
	private Road janela;

	private long quilometros = 50;

	private Timer timerVel;

	private String[] imageEnemies;

	Registry reg = null;

	AudioPlayer ap;
	Sound sounds;

	/**
	 * Classe principal do jogo
	 */
	public JogoCorrida() {
		numberCenario = 0;

		fr = new FrameRate();

		bufferImages = new HashMap<String, BufferedImage>();

		road = null;
		p1 = new Player(new Point(250, 500), 1);
//		p2 = new Player(new Point(550, 500), 2);

		splash = true;
		splashPressEnter = true;
		imageEnemies = new String[]{JogoCorrida.relativePath + "tree_obst.png", JogoCorrida.relativePath + "stone_obst.png"};
		sounds = new Sound(JogoCorrida.relativePath + "sound/miami.mp3", JogoCorrida.relativePath + "sound/crash.wav");

		timerSplash = new Timer(timerSplashDelay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splashPressEnter = false;
			}
		});

		timerVel = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (p1.haveLife()) {
					Element.setVel(1);
				} else {
					sounds.stopSoundTrack();
				}
			}
		});

		try {
			reg = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			System.err.println("Java RMI registry ja exite");
		}

		try {
			IPlayer stub = (IPlayer) UnicastRemoteObject.exportObject(p1, 6789);

			try {
				reg.bind("Player1", stub);
			} catch (RemoteException | AlreadyBoundException e) {
				System.err.println("Nao consigo bindar Player1 ao registro");
			}

		} catch (RemoteException e) {
			System.err.println("Nao consigo exportar o objeto Player1");
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

	/**
	 *
	 */
	public void createAndShowGui() {
		canvas = new Canvas();
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

		sounds.playSoundTrackLoop();

		janela = new Road(new Rectangle(0, 0, getWidth(), getHeight()));

		while (running) {
			gameLoop();
			sleep(15);
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

					if (splash) {

						g.drawImage(getImg(relativePath + "splash.jpg"), 0, 0, null);

						g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 72));
						g.setColor(Color.WHITE);

						if (splashPressEnter) {
							g.drawString("PRESS ENTER", 150, 500);
						} else {
							timer(timerSplashDelay + timerSplashFlick, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									splashPressEnter = true;
								}
							});
						}

						timerSplash.start();
					} else {

						if (timerSplash.isRunning()) {
							timerSplash.stop();
							timerVel.start();
						}

						/**
						 * Renderizando a rua que comeca a 10% do inicio da janela e tem 80%
						 * de tamanho em relacao a janela
						 */
						if (road == null) {
							road = new Road(new Rectangle((int) (getWidth() * .1), 0, (int) (getWidth() * .8), getHeight()));
						}

						road.render(g);

						if (numberCenario++ < 1) {
							Cenario.loadImg(road, getWidth());
							cenario = Cenario.nextImg();
						}

//						System.out.println(cenario.toString());

						cenario.render(g);

						if (!cenario.move(janela)) {
							numberCenario = 0;
						}

//						render(g);

						p1.render(g);
//						p2.render(g);

						/**
						 * Gerando uma posicao randomica para os inimigos
						 */
						if (enemies == null) {
							enemies = new ArrayList<Enemy>();
							for (int i = 0; i < 5; i++) {
								Enemy enemy = new Enemy(imageEnemies[new Random().nextInt(2)]);
								enemy.x = enemy.randomPos(road);
								enemies.add(enemy);
							}
						}

						/**
						 * Renderizando cada inimigo
						 */
						for (int i = 0; i < enemies.size(); i++) {
							Enemy enemy = enemies.get(i);
							enemy.render(g);
							if (enemy.move(road)) {
								enemies.clear();
								enemies = null;
								break;
							} else {
								p1.isColision(enemy, sounds);
							}
						}

						/**
						 * Renderizando a vida
						 */
						for (int i = 0; i < Player.MAX_LIFE; i++) {
							BufferedImage life = getImg(relativePath + "life.png");
							BufferedImage lifeless = getImg(relativePath + "lifeless.png");

							if (i < p1.getLife()) {
								g.drawImage(life, 50 + (life.getWidth() + 15) * i, 50, null);
							} else {
								g.drawImage(lifeless, 50 + (lifeless.getWidth() + 15) * i, 50, null);
							}
						}
						if (!p1.haveLife()) {
							p1.gameOver(g, new Point(getWidth() / 2, getHeight() / 2));
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

	public static void timer(int delay, ActionListener al) {
		Timer timer = new Timer(delay, al);
		timer.setRepeats(false);
		timer.start();
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (p1.haveLife()) {
			switch (e.getKeyCode()) {
				case (KeyEvent.VK_RIGHT):
					if (road.contains(p1.x + Player.getVel(), p1.y, p1.width, p1.height)) {
						p1.moveRight();
					} else {
						p1.x = road.x + road.width - p1.width;
					}
					break;
				case (KeyEvent.VK_LEFT):
					if (road.contains(p1.x - Player.getVel(), p1.y, p1.width, p1.height)) {
						p1.moveLeft();
					} else {
						p1.x = road.x;
					}
					break;
				case (KeyEvent.VK_ENTER):
					if (splash) {
						splash = false;
					}
					break;
				case (KeyEvent.VK_UP):
					Element.setVel(5);
					break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		p1.changeDirection(Player.Direction.FOWARD);
	}

	public static BufferedImage getImg(String file) {
		BufferedImage buffer;

		buffer = bufferImages.get(file);

		if (buffer != null) {
			return buffer;
		}

		try {
			buffer = ImageIO.read(new File(file));
			bufferImages.put(file, buffer);
		} catch (IOException e) {
			buffer = null;
			System.out.println("Erro no carregamento da imagem.");
		}

		return buffer;
	}

	private void render(Graphics g) {
		fr.calculate();
		g.setColor(Color.red);
		g.drawString(fr.getFrameRate(), 300, 20);
		g.setColor(Color.YELLOW);
		g.drawString("janela:" + toString(), 500, 180);
		g.drawString("carro_pos:" + p1.getLocation().toString(), 500, 200);
		g.drawString("carro_tam:" + p1.getSize().toString(), 500, 220);
		g.drawString("carro_vel:" + Player.getVel(), 500, 240);
		g.drawString("crossover_vel:" + Element.getVel() + "/" + Crossover.MAX_VEL, 500, 260);
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
