
import javafx.collections.transformation.SortedList;

public class HighScore {

	private final int MAX_SCORE;
	private static HighScore instance;
	private SortedList<Score> highScore;

	/**
	 * Adiciona o score padrao na tabela
	 */
	private HighScore() {
		this.MAX_SCORE = 2;
		/**
		 * Exemplo de score basico
		 */
		addItem("Carlos", 8001);
		addItem("Bruno", 6666);
		addItem("Matheus", 323232);
		addItem("Luiz", 98765);

		for (Score score : highScore) {
			System.out.println(score);
		}
	}

	public void addItem(String name, int score) {
		highScore.add(new Score(name, score));

		if (highScore.size() >= MAX_SCORE) {
			int lastElement = highScore.size();
			highScore.remove(lastElement);
		}
	}

	public static HighScore getInstance() {
		if (instance == null) {
			instance = new HighScore();
		}
		return instance;
	}

}
