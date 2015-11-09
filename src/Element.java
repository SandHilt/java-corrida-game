
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element extends Rectangle {

	private static int vel = 0;
	public static final int MAX_VEL = 15;
	private Color color;

	public Element() {
		super();
		color = Color.WHITE;
	}

	public Element(Rectangle rectangle) {
		super(rectangle);
		color = Color.WHITE;
	}

	public Element(Rectangle rectangle, Color color) {
		this(rectangle);
		this.color = color;
	}

	public Element(Point point) {
		this(new Rectangle(point));
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
		Point point = getLocation();

		point.translate(0 , vel);

		if (road.contains(point)) {
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
