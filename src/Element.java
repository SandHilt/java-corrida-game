
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element extends Rectangle {


	/**
	 * Cor do elemento
	 * Apesar de algumas elementos terem imagens
	 * todos tem este campo por padrao
	 */
	private Color color;

	/**
	 *
	 */
	public Element() {
		super();
		color = Color.WHITE;
	}

	/**
	 *
	 * @param rectangle
	 */
	public Element(Rectangle rectangle) {
		super(rectangle);
		color = Color.WHITE;
	}

	/**
	 * Elemento com retangulo e um cor especifica
	 *
	 * @param rectangle
	 * @param color
	 */
	public Element(Rectangle rectangle, Color color) {
		this(rectangle);
		this.color = color;
	}

	/**
	 * Criando um elemento sem tamanho, apenas com um ponto Posteriormente vai
	 * receber um tamanho
	 *
	 * @param point
	 */
	public Element(Point point) {
		this(new Rectangle(point));
	}

	/**
	 *
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Faz um elemento andar em relacao a Road
	 *
	 * @param road
	 * @param p
	 * @return Se o elemento nao esta mais nas coordenadas da rua
	 */
	public boolean move(Road road, Player p) {
		Point point = getLocation();

		point.translate(0, p.vel);

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
