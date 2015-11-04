
import java.awt.*;

/**
 * Classe abstrata para tratar todos os elementos da tela
 */
public abstract class Element implements IElement {

	private Rectangle rectangle;

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
/**
 * Pegando o valor atual da dimensao como uma copia
 * @return copia da dimensao
 */
	public Dimension getDimension() {
		return new Dimension(dimension);
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

	/**
	 * Cria o objeto retangulo ou atualiza Se ele estiver criado porem com os
	 * valores desatualizados
	 */
	public void setRectangle() {
		if (rectangle == null) {
			rectangle = new Rectangle(point, dimension);
		} else {
			if (rectangle.getLocation() != point) {
				rectangle.setLocation(point);
			}
			if (rectangle.getSize() != dimension) {
				rectangle.setSize(dimension);
			}
		}
	}

	public Rectangle getRectangle() {
		setRectangle();
		return rectangle;
	}

	/**
	 * Verfica se os elementos se tocaram
	 *
	 * @param element
	 * @return
	 */
	public boolean colision(Element element) {
		setRectangle();
		return rectangle.intersects(element.getRectangle());
	}

	/**
	 * Verfica se um retangulo esta dentro do outro
	 *
	 * @param element
	 * @return
	 */
	public boolean inside(Element element) {
		setRectangle();
		return element.getRectangle().contains(rectangle);
	}

	public Point getPoint() {
		return point;
	}

	public abstract void render(Graphics g);
}
