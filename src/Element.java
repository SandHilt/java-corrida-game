
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element extends Rectangle {

	private Vector2f vector;
	private Color color;

	public Element() {
		super(new Rectangle());
		vector = new Vector2f();
		color = Color.WHITE;
	}

	public Element(Rectangle rectangle) {
		this();
		setBounds(rectangle);
	}

	public Element(Rectangle rectangle, Vector2f vector, Color color) {
		this(rectangle);
		this.vector = vector;
		this.color = color;
	}

	public Element(Rectangle rectangle, Color color) {
		this(rectangle);
		this.color = color;
	}

	public Element(Point p) {
		this(new Rectangle(p));
	}

	public Vector2f getVector() {
		return vector;
	}

	public Color getColor() {
		return color;
	}

	/**
	 * Faz um elemento andar com seu vector2f em relacao a Road
	 *
	 * @param road
	 * @return Se o elemento nao esta mais nas coordenadas da rua
	 */
	public boolean move(Road road) {
		super.translate((int) (vector.x * vector.w), (int) (vector.y * vector.w));

		if (road.contains(getLocation())) {
			return true;
		}

		return false;
	}

	/**
	 * Cada elemento implementa como se renderiza
	 *
	 * @param g
	 */
	public abstract void render(Graphics g);
}
