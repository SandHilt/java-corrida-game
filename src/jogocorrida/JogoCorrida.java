import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class JogoCorrida extends JFrame {

	private boolean estaRodando = true;
	private static int fps = 30;

	/* Resolucao da tela do jogo */
	private static int windowWidth = 640;
	private static int windowHeight = 320;

	public static void main(String[] args) {
		JogoCorrida jogo = new JogoCorrida();
		jogo.rodar();
		System.exit(0);
	}

	public void rodar() {
		inicializar();

		while(estaRodando){
			long tempo = System.currentTimeMillis();

			atualizar();
			desenhar();

			tempo = ( 1000 / fps ) - (System.currentTimeMillis() - tempo);

			if(tempo > 0){
				try{
					Thread.sleep(tempo);
				} catch(Exception e) { }
			}

		}

		setVisible(false);

	}
	public void inicializar(){
		setTitle("Corrida ~le");
		setSize(windowWidth, windowHeight);
		setResizable(false);
		/* apenas para previnir que o programa continue
		rodando mesmo depois de fechar */
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public void atualizar(){
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {

		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {

		}
	}
	public void desenhar(){

	}

}
