package jogocorrida;

import javax.swing.JFrame;

public class JogoCorrida {
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		JFrame janela = new JFrame("Quando a janela abre...");
		Painel meuPainel = new Painel();

		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.add(meuPainel);
		janela.setSize(600, 400);
		janela.setVisible(true);
	}

}
