
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element {

	private Rectangle rectangle;
	private Vector2f vector;

	public Element() {
		rectangle = new Rectangle();
		vector = new Vector2f();
	}

	public Element(Point point) {
		this();

		try {
			if (point.x >= 0 && point.y >= 0) {
				rectangle.setLocation(point);
			} else {
				throw new Exception("Erro no ponto.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element(Rectangle rectangle) {
		this(rectangle.getLocation());

		try {
			if (rectangle.x >= 0 && rectangle.x >= 0) {
				this.rectangle.setSize(rectangle.getSize());
			} else {
				throw new Exception("Erro na dimensao.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element(Rectangle rectangle, Vector2f vector) {
		this(rectangle);
		this.vector = vector;
	}

	/**
	 * Pegando o valor atual da dimensao como uma copia
	 *
	 * @return copia da dimensao
	 */
	public Dimension getDimension() {
		return rectangle.getSize();
	}

	public void setDimension(Dimension dimension) {
		try {
			if (rectangle.isEmpty()) {
				rectangle.setSize(dimension);
			} else {
				throw new Exception("A dimensao ja foi definida.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	/**
	 * Verfica se os elementos se tocaram
	 *
	 * @param element
	 * @return
	 */
	public boolean colision(Element element) {
		return rectangle.intersects(element.getRectangle());
	}

	/**
	 * Verfica se um retangulo esta dentro do outro
	 *
	 * @param element
	 * @return
	 */
	public boolean inside(Element element) {
		return element.getRectangle().contains(rectangle);
	}

	public Point getPoint() {
		return rectangle.getLocation();
	}

	public void setPoint(Point p) {
		rectangle.setLocation(p);
	}

	public abstract void render(Graphics g);
}
