
import java.util.*;

public class HighScore {

	private static HighScore instance = null;
	private Map<String, Integer> names;

	/**
	 * Adiciona o score padrao na tabela
	 */
	private HighScore() {
		this.addItem("Carlos", 8001);
		this.addItem("Bruno", 6666);
		this.addItem("Matheus", 323232);
		this.addItem("Luiz", 98765);
	}

	public void addItem(String name, int score) {
		names.put(name, score);
	}

	public static HighScore getInstance() {
		if (instance == null) {
			instance = new HighScore();
		}
		return instance;
	}

}
