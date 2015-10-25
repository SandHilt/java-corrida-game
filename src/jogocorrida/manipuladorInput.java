import java.awt.Component;
import java.awt.event.*;

class manipuladorInput implements KeyListener {
	public manipuladorInput ( Component c ){
		c.addKeyListener(this);
	}
	/**
	 * [isKeyDown description]
	 * @param  keyCode tecla para checar
	 * @return         Qual tecla e pressionada
	 */
	public boolean isKeyDown(int keyCode){
		if (keyCode > 0 && keyCode < 256) {
			return keys[keyCode];
		}

		return false;
	}
	/**
	 * E invocado quando a tecla e pressionada
	 * enquanto o componente esta em foco
	 * @param e KeyEvent enviado pelo componente
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeycode() > 0 && e.getKeycode() < 256) {
			keys[e.getKeycode()] = true;
		}
	}
	/**
	 * E invocado quando a tecla e dropada
	 * e o componente esta em foco
	 * @param e KeyEvent enviado pelo componente
	 */
	public void keyReleased(KeyEvent e){
		if (e.getKeycode() > 0  && e.getKeycode() < 256) {
			keys[e.getKeycode()] = false;
		}
	}
	/**
	 * Nao usado
	 */
	public void keyTyped(KeyEvent e){}
}
