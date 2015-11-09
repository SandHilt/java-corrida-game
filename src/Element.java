
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element extends Rectangle {

	private static int vel = 0;
	public static final int MAX_VEL = 15;
	private Vector2f vector;
	private Color color;

	public Element() {
		super();
		vector = new Vector2f();
		color = Color.WHITE;
	}

	public Element(Rectangle rectangle) {
		super(rectangle);
		vector = new Vector2f();
		color = Color.WHITE;
	}

	public Element(Rectangle rectangle, Vector2f vector) {
		this(rectangle);
		this.vector = vector;
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

	public Element(Point point) {
		this(new Rectangle(point));
	}

	public Element(Point point, Vector2f vector) {
		this(point);
		this.vector = vector;
	}

	public static int getVel() {
		return vel;
	}

	public static void setVel(int vel) {
		int dx = Element.vel + vel;
		if (dx <= Element.MAX_VEL && dx >= 0) {
			Element.vel = dx;
		}
	}

	public static void stopVel() {
		if (Element.vel != 0) {
			Element.vel = 0;
		}
	}

	public Vector2f getVector() {
		return vector;
	}

	public int getVectorX() {
		return (int) (vector.x * vector.w);
	}

	public int getVectorY() {
		return (int) (vector.y * vector.w);
	}

	public Color getColor() {
		return color;
	}

	/**
	 * Faz um elemento andar com seu vector2f em relacao a Road
	 *
	 * @param rectangle
	 * @return Se o elemento nao esta mais nas coordenadas da rua
	 */
	public boolean move(Rectangle rectangle) {
		Point point = getLocation();

		point.translate((int) (vector.x * vector.w), (int) (vector.y * vector.w * vel));

		if (rectangle.contains(point)) {
			this.setLocation(point);
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
