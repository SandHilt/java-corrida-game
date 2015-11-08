

/**
 *
 * @author Bruno O
 */
public class FrameRate {

	private String frameRate;
	private long lastTime;
	private long delta;
	private int frameCount;

    /**
     *
     */
    public void init() {
		lastTime = System.currentTimeMillis();
		frameRate = "FPS 0";
	}

    /**
     *
     * @return
     */
    public boolean is1Sec() {
		return delta > 1000;
	}

    /**
     *
     */
    public void calculate() {
		long current = System.currentTimeMillis();
		delta += current - lastTime;
		lastTime = current;
		frameCount++;

		if (is1Sec()) {
			delta -= 1000;
			frameRate = String.format("FPS %s", frameCount);
			frameCount = 0;
		}
	}

    /**
     *
     * @return
     */
    public String getFrameRate() {
		return frameRate;
	}
}
