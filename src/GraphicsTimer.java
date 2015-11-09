
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicsTimer implements ActionListener {

	private final Graphics g;

	public GraphicsTimer(final Graphics g) {
		this.g = g;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		g.drawString("Ola", 0, 0);
	}
}
