
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element extends Rectangle {

	private Vector2f vector;

	public Element() {
		this(new Rectangle(), new Vector2f());
	}

	public Element(Point point) {
		this();
		setLocation(point);
	}

	public Element(Rectangle rectangle) {
		this(rectangle.getLocation());
		setSize(rectangle.getSize());
	}

	public Element(Rectangle rectangle, Vector2f vector) {
		super(rectangle);
		this.vector = vector;
	}

	/**
	 * Verfica se os elementos se tocaram
	 *
	 * @param element
	 * @return
	 */
//	public boolean colision(Element element) {
//		return rectangle.intersects(element.getRectangle());
//	}
	/**
	 * Verfica se um retangulo esta dentro do outro
	 *
	 * @param element
	 * @return
	 */
//	public boolean inside(Element element) {
//		return element.getRectangle().contains(rectangle);
//	}
	public abstract void render(Graphics g);
}
