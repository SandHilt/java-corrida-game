
import java.awt.Graphics;

public abstract class Element {

	protected int pos_x;
	protected int pos_y;

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
