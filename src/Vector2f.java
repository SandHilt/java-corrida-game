

/**
 *
 * @author Bruno O
 */
public class Vector2f {

    /**
     *
     */
    public float x;

    /**
     *
     */
    public float y;

    /**
     *
     */
    public float w;

    /**
     *
     */
    public Vector2f() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.w = 1.0f;
	}

    /**
     *
     * @param v
     */
    public Vector2f(Vector2f v) {
		this(v.x, v.y, v.w);
	}

    /**
     *
     * @param x
     * @param y
     */
    public Vector2f(float x, float y) {
		this(x, y, 1.0f);
	}

    /**
     *
     * @param x
     * @param y
     * @param w
     */
    public Vector2f(float x, float y, float w) {
		this.x = x;
		this.y = y;
		this.w = w;
	}

    /**
     *
     * @param tx
     * @param ty
     */
    public void translate(float tx, float ty) {
		x += tx;
		y += ty;
	}

    /**
     *
     * @param sx
     * @param sy
     */
    public void scale(float sx, float sy) {
		x *= sx;
		y *= sy;
	}

    /**
     *
     * @param rad
     */
    public void rotate(float rad) {
		float tmp = (float) (x * Math.cos(rad) - y * Math.sin(rad));
		y = (float) (x * Math.sin(rad) + y * Math.cos(rad));
		x = tmp;
	}

    /**
     *
     * @param sx
     * @param sy
     */
    public void shear(float sx, float sy) {
		float tmp = x + sx * y;
		y = y + sy * x;
		x = tmp;
	}
}
