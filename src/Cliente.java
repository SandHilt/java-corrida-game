
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
	private String[] imageEnemies;
	private Sound sounds;
	private Timer timerSplash;
	private static final int TIMER_SPLASH_DELAY = 1000;
	private static final int TIMER_SPLASH_FLICK = 300;
	private int id;
	private ArrayList<Enemy> enemies;
	private boolean ready;
	private Cenario cenario;

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
		ready = false;
		id = 0;

		try {
			cena = (IJogo) Naming.lookup("Cenario");
		} catch (NotBoundException | MalformedURLException | RemoteException ex) {
			System.out.println("Nao consegui achar o cenario");
		}

		try {
			id = cena.getIDPlayer();
		} catch (RemoteException ex) {
			System.err.println("Nao consegui saber quantos jogadores estao conectados.");
		}

		try {
			switch (id) {
				case 1:
					player = (IPlayer) Naming.lookup("Player1");
					break;
				case 2:
					player = (IPlayer) Naming.lookup("Player2");
					break;
				default:
					throw new Exception("Server lotado.");
			}
		} catch (RemoteException e) {
			System.out.println("Nao consigo achar o jogador dentro do registro");
		} catch (NotBoundException | MalformedURLException ex) {
			System.out.println("Desculpe, esta lotado. Tente novamente mais tarde.");
		} catch (Exception ex) {
			ex.printStackTrace();
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

		cenario = null;

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
		timerSplash = new Timer(TIMER_SPLASH_DELAY, new ActionListener() {
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
		try {
			canvas.setBackground(cena.corTempo());
		} catch (RemoteException ex) {
			System.err.println("Nao consegui pegar a cor do cenario");
		}
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

					try {
						ready = cena.isReady();
					} catch (RemoteException ex) {
						System.out.println("Nao consegui saber o estado do players");
					}

					if (splash || !ready) {

						g.drawImage(getImg(relativePath + "splash.jpg"), 0, 0, null);

						g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 72));
						g.setColor(Color.WHITE);

						if (splashPressEnter) {
							g.drawString("PRESS ENTER", 150, 500);
						} else {
							/**
							 * Adicionando um temporizador para piscar o Press Enter
							 */
							timer(TIMER_SPLASH_DELAY + TIMER_SPLASH_FLICK, new ActionListener() {
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

						try {
							road = cena.getRoad();
						} catch (RemoteException ex) {
							System.err.println("Nao consegui pegar a rua do server.");
						}

						if (road != null) {
							try {
								road.render(g, player.getVel(), cena.getCrossovers());
							} catch (RemoteException ex) {
								System.out.println("Nao consegui renderizar a rua.");
							}
						}

						try {
							cenario = cena.getCenario();
						} catch (RemoteException ex) {
						}

						if (cenario != null) {
							cenario.render(g);
						}

						try {
							player.render(g);
						} catch (RemoteException ex) {
						}

						/**
						 * Tentativa de imprimir os inimigos na tela
						 */
//						if (enemies == null) {
//							try {
//								enemies = cena.getEnemies();
//							} catch (RemoteException ex) {
//							}
//						} else {
//							for (int i = 0; i < enemies.size(); i++) {
//								Enemy e = enemies.get(i);
//								if (e != null) {
//									e.render(g);
//								}
//							}
//						}

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
								sounds.stopSoundTrack();
							}
						} catch (Exception e) {
							e.printStackTrace();
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
