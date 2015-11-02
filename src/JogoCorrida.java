
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

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

	public JogoCorrida() {
		fr = new FrameRate();

		road = new Road(0, 0);
		p1 = new Player(250, 500);
		p1.setVel(5);

		p2 = new Player(550, 500);
		p2.setVel(50);

		en = new ArrayList<>();

		en.add(new Enemy(50, 50));
		en.add(new Enemy(100, 50));
		en.add(new Enemy(50, 100));

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
				Thread.sleep(30);
			} catch (InterruptedException ex) {
			}
		}
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
					road.setHeight(getHeight());
					road.setWidth((int) (getWidth() * .8));
					road.pos_x = ((int) (getWidth() * .1));

					road.render(g);

					render(g);

					/**
					 * Debug janela WxH carro: posicao tamanho velocidade
					 */
					g.setColor(Color.YELLOW);
					g.drawString("janela:" + getWidth() + "x" + getHeight(), 600, 180);
					g.drawString("carro_pos:" + p1.pos_x + "x" + p1.pos_y, 600, 200);
					g.drawString("carro_tam:" + p1.getWidth() + "x" + p1.getHeight(), 600, 220);
					g.drawString("carro_vel:" + p1.getVel(), 600, 240);

					/**
					 * Teste de colisao na tela
					 */
					p1.colision(getWidth(), getHeight());

					p1.render(g);
					p2.render(g);

					for (int i = 0; i < en.size(); i++) {
						en.get(i).render(g);
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
				break;
			case (KeyEvent.VK_LEFT):
				p1.moveLeft();
				break;
			case (KeyEvent.VK_UP):
				Crossover.setDelta(1);
//				p1.moveTop();
				break;
			case (KeyEvent.VK_DOWN):
//				p1.moveDown();
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
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
