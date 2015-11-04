
import java.util.*;

public class HighScore {

	private final int MAX_SCORE;
	private static HighScore instance;
	private static Comparable<Score> comp;
	private ArrayList<Score> highScore;

	/**
	 * Adiciona o score padrao na tabela
	 */
	private HighScore() {
		this.MAX_SCORE = 10;

		if (highScore == null) {
			highScore = new ArrayList<Score>();
		}

		/**
		 * Exemplo de score basico
		 */
		addItem(new Score("Carlos", 8001));
		addItem(new Score("Bruno", 6666));
		addItem(new Score("Matheus", 323232));
		addItem(new Score("Luiz", 98765));

		for (Score score : highScore) {
			System.out.println(score);
		}
	}

	public void addItem(Score score) {
		highScore.add(score);

		highScore.sort(new Comparator<Score>() {

			@Override
			public int compare(Score o1, Score o2) {
				if (o1.getScore() < o2.getScore()) {
					return 1;
				} else if (o1.getScore() > o2.getScore()) {
					return -1;
				}
				return 0;
			}
		});

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
