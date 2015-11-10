
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

class Cliente extends JFrame implements Runnable, KeyListener {

	private IPlayer player;
	private IJogo cena;
	private static Map<String, BufferedImage> bufferImages;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;

	private Road road;
	private boolean splash;
	private boolean splashPressEnter;
	private final String[] imageEnemies;
	private final Sound sounds;
	private final Timer timerSplash;
	private static int timerSplashDelay = 1000;
	private static int timerSplashFlick = 300;
	private ArrayList<Enemy> enemies;
	private static int vel;

	public static String relativePath = "./src/";

	public static void main(String[] args) {
		final Cliente cliente = new Cliente();

		cliente.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cliente.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cliente.createAndShowGui();
			}
		});
	}

	public Cliente() {
		try {
			player = (IPlayer) Naming.lookup("Player1");
		} catch (RemoteException e) {
			System.out.println("Nao consigo achar o jogador dentro do registro");
			try {
				player = (IPlayer) Naming.lookup("Player2");
			} catch (NotBoundException | MalformedURLException | RemoteException ex) {
				System.out.println("Desculpe, esta lotado. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			System.out.println("Nao consigo achar o jogador dentro do registro");
		}

		try {
			cena = (IJogo) Naming.lookup("Cenario");
		} catch (NotBoundException | MalformedURLException | RemoteException ex) {
			System.out.println("Nao consegui achar o cenario");
		}

		/**
		 * Para otimizar as imagens Usando a string com o caminho das imagens E o
		 * buffer delas
		 */
		bufferImages = new HashMap<String, BufferedImage>();

		/**
		 * Iniciando a rua
		 */
		road = null;

		/**
		 * Habilitando a Splash Screen
		 */
		splash = true;
		/**
		 * Iniciando com a String Press Enter ligado
		 */
		splashPressEnter = true;
		/**
		 * Array com localizacao dos inimigos
		 */
		imageEnemies = new String[]{JogoCorrida.relativePath + "tree_obst.png", JogoCorrida.relativePath + "stone_obst.png"};
		/**
		 * Criando os arquivos de sons e preparando para rodar.
		 */
		sounds = new Sound(JogoCorrida.relativePath + "sound/miami.mp3", JogoCorrida.relativePath + "sound/crash.wav");

		/**
		 * Temporizador para a splash apagar o Press Enter
		 */
		timerSplash = new Timer(timerSplashDelay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splashPressEnter = false;
			}
		});

		try {
			enemies = cena.getEnemies();
		} catch (RemoteException ex) {
			System.out.println("Nao consegui pegar o array de inimigos");
		}
	}

	public void createAndShowGui() {
		Canvas canvas = new Canvas();
		canvas.setSize(800, 600);
		canvas.setBackground(JogoCorrida.Tempo.FLORESTA.cor);
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
//		fr.init();

		sounds.playSoundTrackLoop();

		while (running) {
			gameLoop();
			sleep(15);
		}
	}

	/**
	 * Loop do jogo
	 */
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
							/**
							 * Adicionando um temporizador para piscar o Press Enter
							 */
							timer(timerSplashDelay + timerSplashFlick, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									splashPressEnter = true;
								}
							});
						}
						timerSplash.start();
					} else {

						/**
						 * Verficando se o Press Enter esta rodando se rodar, para de rodar.
						 */
						if (timerSplash.isRunning()) {
							timerSplash.stop();
						}

						/**
						 * Renderizando a rua que comeca a 10% do inicio da janela e tem 80%
						 * de tamanho em relacao a janela
						 */
						if (road == null) {
							road = new Road(new Rectangle((int) (getWidth() * .1), 0, (int) (getWidth() * .8), getHeight()));
						}

						road.render(g);

						try {
							cena.getCenario().render(g);
						} catch (RemoteException ex) {
							System.out.println("Nao consegui renderizar o cenario");
						}

						try {
							player.render(g);
						} catch (RemoteException ex) {
							System.out.println("Nao consegui renderizar o player");
						}

						for (int i = 0; i < enemies.size(); i++) {
							Enemy e = enemies.get(i);
							e.render(g);
						}

						try {
							/**
							 * Renderizando a vida
							 */
							for (int i = 0; i < Player.MAX_LIFE; i++) {
								BufferedImage life = getImg(relativePath + "life.png");
								BufferedImage lifeless = getImg(relativePath + "lifeless.png");

								if (i < player.getLife()) {
									g.drawImage(life, 50 + (life.getWidth() + 15) * i, 50, null);
								} else {
									g.drawImage(lifeless, 50 + (lifeless.getWidth() + 15) * i, 50, null);
								}
							}
							if (!player.haveLife()) {
								player.gameOver(g, new Point(getWidth() / 2, getHeight() / 2));
							}
						} catch (Exception e) {

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
		try {
			if (player.haveLife()) {
				switch (e.getKeyCode()) {
					case (KeyEvent.VK_RIGHT):
						player.moveRight(road);
						break;
					case (KeyEvent.VK_LEFT):
						player.moveLeft(road);
						break;
					case (KeyEvent.VK_ENTER):
						if (splash) {
							System.out.println("Conectado");
							splash = false;
							player.setConnected(true);
						}
						break;
				}
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * Quando a tecla e solta
	 *
	 * @param e
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			player.changeDirection(Player.Direction.FOWARD);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Pega uma imagem a partir de um caminho
	 *
	 * @param file caminho do arquivo
	 * @return um buffer de imagem para plotar no jogo
	 */
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

	/**
	 * Colocando uma Thread para dormir por l segundo
	 *
	 * @param l
	 */
	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
		}
	}

	/**
	 *
	 * @param delay
	 * @param al
	 */
	public static void timer(int delay, ActionListener al) {
		Timer timer = new Timer(delay, al);
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Fechando a Thread do jogo
	 */
	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}

}
