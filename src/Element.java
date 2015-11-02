
import java.awt.Graphics;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element {

	/**
	 * Coordenadas de um elemento
	 */
	protected int pos_x;
	protected int pos_y;

	/**
	 * Tamanhos desses elementos
	 */
	private int width;
	private int height;

	public Element(int pos_x, int pos_y) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width > 0) {
			this.width = width;
		} else {
			System.out.println("Valor invalido para largura");
		}
	}

	/**
	 * Pega a altura do elemento
	 *
	 * @return retorna a altura em inteiro
	 */
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height > 0) {
			this.height = height;
		} else {
			System.out.println("Valor invalido para altura");
		}
	}

	public abstract void render(Graphics g);
}
