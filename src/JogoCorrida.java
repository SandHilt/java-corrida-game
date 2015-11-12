
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

/**
 * Classe principal
 */
public class JogoCorrida extends JFrame implements Runnable, IJogo {

//	private FrameRate fr;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private volatile Road road;
	private int connectedPlayers;
	private Player p1;
	private Player p2;
	private ArrayList<Enemy> enemies;
	private int numberCenario;
	private volatile Cenario cenario;

	/**
	 * Define se esta pronto se os dois jogadores estao prontos para comecar
	 */
	private volatile boolean ready;

	private static Map<String, BufferedImage> bufferImages;

	/**
	 * Pasta relativa para pegar os recursos
	 */
	public static String relativePath = "./src/";

	/**
	 * Pegando o canvas para pinta-lo posteriormente
	 */
	private Canvas canvas;

	@Override
	public boolean isReady() throws RemoteException {
		return ready;
	}

	@Override
	public int getIDPlayer() throws RemoteException {
		return ++connectedPlayers;
	}

	@Override
	public Cenario getCenario() throws RemoteException {
		return cenario;
	}

	@Override
	public ArrayList<Enemy> getEnemies() throws RemoteException {
		return enemies;
	}

	@Override
	public Road getRoad() throws RemoteException {
		return road;
	}

	@Override
	public Color corTempo() {
		return canvas.getBackground();
	}

	@Override
	public ArrayList<Crossover> getCrossovers() throws RemoteException {
		return Crossover.crossovers;
	}

	/**
	 * Enumeravel para pintar o cenario
	 */
	public enum Tempo {

		/**
		 * Snow
		 */
		NEVE(new Color(254, 254, 250)),
		/**
		 * Florest
		 */
		FLORESTA(new Color(1, 68, 33)),
		/**
		 * Big City
		 */
		CIDADE(new Color(51, 51, 51)),
		/**
		 * Waterless
		 */
		DESERTO(new Color(237, 201, 175));

		final Color cor;

		Tempo(Color cor) {
			this.cor = cor;
		}

	};

//	private Timer timerVel;
	private String[] imageEnemies;

	/**
	 * Para o RMI
	 */
	Registry reg = null;

	/**
	 * Sons
	 */
	Sound sounds;

	/**
	 * Classe principal do jogo
	 *
	 */
	public JogoCorrida() {
		numberCenario = 0;

//		fr = new FrameRate();
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
		 * Instanciando o jogador
		 */
		p1 = new Player(new Point(250, 500), 1);
		p2 = new Player(new Point(550, 500), 2);

		/**
		 * Array com localizacao da imagem dos inimigos
		 */
		imageEnemies = new String[]{JogoCorrida.relativePath + "tree_obst.png", JogoCorrida.relativePath + "stone_obst.png"};
		/**
		 * Criando os arquivos de sons e preparando para rodar.
		 */
		sounds = new Sound(JogoCorrida.relativePath + "sound/miami.mp3", JogoCorrida.relativePath + "sound/crash.wav");

		/**
		 * Temporazidor para velocidade
		 */
		p1.setTimer(timerVel(p1));
		p2.setTimer(timerVel(p2));

		rmi();
	}

	/**
	 * RMI
	 */
	private void rmi() {
		try {
			reg = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			System.err.println("Java RMI registry ja exite");
		}

		try {
			IPlayer stubPlayer1 = (IPlayer) UnicastRemoteObject.exportObject(p1, 6789);
			IPlayer stubPlayer2 = (IPlayer) UnicastRemoteObject.exportObject(p2, 6790);
			IJogo stubJogo = (IJogo) UnicastRemoteObject.exportObject(this, 6791);

			try {
				reg.bind("Player1", stubPlayer1);
				reg.bind("Player2", stubPlayer2);
				reg.bind("Cenario", stubJogo);
			} catch (RemoteException | AlreadyBoundException e) {
				System.err.println("Nao consigo bindar o Player ao registro");
			}

		} catch (RemoteException e) {
			System.err.println("Nao consigo exportar o objeto Player ou Cenario");
		}

		System.out.println("Servidor RMI pronto");
	}

	/**
	 * Adicionando um escutador de eventos para quando a janela fechar e chamando
	 * createAndShowGui quando o jogo puder ser chamado.
	 *
	 * @param args
	 */
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
	 * Criando o canvas
	 */
	public void createAndShowGui() {
		canvas = new Canvas();
		canvas.setSize(800, 600);

		Random r = new Random();
		Tempo[] tempo = Tempo.values();

		canvas.setBackground(tempo[r.nextInt(tempo.length)].cor);
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

		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Rodando o jogo
	 */
	@Override
	public void run() {
		running = true;

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

					ready = p1.isConnected() && p2.isConnected();

					if (!ready) {
						g.drawImage(getImg(relativePath + "splash.jpg"), 0, 0, null);
					} else {
						p1.getTimer().start();
						p2.getTimer().start();

						/**
						 * Renderizando a rua que comeca a 10% do inicio da janela e tem 80%
						 * de tamanho em relacao a janela
						 */
						if (road == null) {
							road = new Road(new Rectangle((int) (getWidth() * .1), 0, (int) (getWidth() * .8), getHeight()));
						}

						road.render(g, p1.getVel());

						if (numberCenario++ < 1) {
							Cenario.loadImg(road, getWidth());
							cenario = Cenario.nextImg();
						}

						cenario.render(g);

						if (!cenario.move(new Road(0, 0, getWidth(), getHeight()), p1.getVel())) {
							numberCenario = 0;
							cenario.y = 0;
						}

//						render(g);
						p1.render(g);
						p2.render(g);

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
							if (enemy.move(road, p1.vel)) {
								enemies.clear();
								enemies = null;
								break;
							} else if (p1.isColision(enemy, sounds) || p2.isColision(enemy, sounds)) {
								p1.stopVel();
								p2.stopVel();
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

							if (i < p2.getLife()) {
								g.drawImage(life, getWidth() / 2 + (life.getWidth() + 15) * i, 50, null);
							} else {
								g.drawImage(lifeless, getWidth() / 2 + (lifeless.getWidth() + 15) * i, 50, null);
							}

						}
						if (!p1.haveLife() || !p2.haveLife()) {
							try {
								if (p1.haveLife()) {
									p1.winner(g, new Point(0, getHeight() / 2), "1");
									p2.gameOver(g, new Point(getWidth() / 2, getHeight() / 2));
								} else if (p2.haveLife()) {
									p1.gameOver(g, new Point(0, getHeight() / 2));
									p2.winner(g, new Point(getWidth() / 2, getHeight() / 2), "2");
								}

							} catch (RemoteException e) {
								e.printStackTrace();
							}

							p1.getTimer().stop();
							p2.getTimer().stop();
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

	private Timer timerVel(Player p) {
		return new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (p.haveLife()) {
					p.setVel(1);
				} else {
					sounds.stopSoundTrack();
				}
			}
		});
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
	 * Renderizam para debug
	 *
	 * @param g
	 */
//	private void render(Graphics g) {
//		fr.calculate();
//		g.setColor(Color.red);
//		g.drawString(fr.getFrameRate(), 300, 20);
//		g.setColor(Color.YELLOW);
//		g.drawString("janela:" + toString(), 500, 180);
//		g.drawString("carro_pos:" + p1.getLocation().toString(), 500, 200);
//		g.drawString("carro_tam:" + p1.getSize().toString(), 500, 220);
//		g.drawString("carro_vel:" + Player.getVel(), 500, 240);
//		g.drawString("crossover_vel:" + Element.getVel() + "/" + Crossover.MAX_VEL, 500, 260);
//	}
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
