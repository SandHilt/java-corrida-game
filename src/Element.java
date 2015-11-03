
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element {

	private Dimension dimension;
	private Point point;

	public Element(Point point) {
		try {
			if (point.x >= 0 && point.y >= 0) {
				this.point = point;
			} else {
				throw new Exception("Erro no ponto.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element(Point point, Dimension dimension) {
		this(point);

		try {
			if (dimension.width >= 0 && dimension.height >= 0) {
				this.dimension = dimension;
			} else {
				throw new Exception("Erro na dimensao.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		try {
			if (this.dimension == null) {
				this.dimension = dimension;
			} else {
				throw new Exception("A dimensao ja foi definida");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Point getPoint() {
		return point;
	}

	public abstract void render(Graphics g);
}
