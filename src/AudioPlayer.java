
import java.io.*;
import javax.sound.sampled.*;

/**
 * An utility class for playing back audio files using Java Sound API.
 *
 * @author www.codejava.net
 *
 */
public class AudioPlayer implements LineListener {

	/**
	 * this flag indicates whether the playback completes or not.
	 */
	private boolean playCompleted;

	/**
	 * this flag indicates whether the playback is stopped or not.
	 */
	private boolean isStopped;

	private boolean isPaused;

	private Clip audioClip;

	/**
	 * Load audio file before playing back
	 *
	 * @param audioFilePath Path of the audio file.
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public void load(String audioFilePath)
					throws UnsupportedAudioFileException, IOException,
					LineUnavailableException {
		File audioFile = new File(audioFilePath);

		AudioInputStream audioStream = AudioSystem
						.getAudioInputStream(audioFile);

		AudioFormat format = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(Clip.class, format);

		audioClip = (Clip) AudioSystem.getLine(info);

		audioClip.addLineListener(this);

		audioClip.open(audioStream);
	}

	/**
	 * Play a given audio file.
	 *
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	void play() throws IOException {

		audioClip.start();

		playCompleted = false;
		isStopped = false;

		while (!playCompleted) {
			// wait for the playback completes
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				if (isStopped) {
					audioClip.stop();
					break;
				}
				if (isPaused) {
					audioClip.stop();
				} else {
//					System.out.println("!!!!");
					audioClip.start();
				}
			}
		}

		audioClip.close();
	}

	/**
	 * Stop playing back.
	 */
	public void stop() {
		isStopped = true;
	}

	/**
	 * Pause music
	 */
	public void pause() {
		isPaused = true;
	}

	/**
	 * Back to the music
	 */
	public void resume() {
		isPaused = false;
	}

	/**
	 * Listens to the audio line events to know when the playback completes.
	 */
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
//			System.out.println("STOP EVENT");
			if (isStopped || !isPaused) {
				playCompleted = true;
			}
		}
	}

}
