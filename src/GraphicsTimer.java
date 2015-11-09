
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Bruno O
 */
public class GraphicsTimer implements ActionListener {

	private final Graphics g;

	/**
	 *
	 * @param g
	 */
	public GraphicsTimer(final Graphics g) {
		this.g = g;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		g.drawString("Ola", 0, 0);
	}
}
