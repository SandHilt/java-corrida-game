
import java.io.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;

class Sound {

	private Player soundtrack;
	private AudioPlayer crash;
	private Thread loop;
	private boolean canRun;

	public Sound(String pathSoundTrack, String pathCrash) {
		this.canRun = true;
		File file = new File(pathSoundTrack);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Nao deu para achar o arquivo de audio.");
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		try {
			soundtrack = new Player(bis);
		} catch (JavaLayerException e) {
			System.err.println("Nao consegui instanciar seu arquivo o soundtrack.");
		}

		crash = new AudioPlayer();
		try {
			crash.load(pathCrash);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Nao consegui instanciar o som de crash.");
		}
	}

	private void playSoundTrack() {
		try {
			soundtrack.play();
		} catch (JavaLayerException e) {
			System.err.println("Nao consegui executar seu audio neste momento.");
		}
	}

	public void stopSoundTrack() {
		canRun = false;
		soundtrack.close();
	}

	public void playSoundTrackLoop() {
		loop = new Thread() {
			@Override
			public void run() {
				do {
					playSoundTrack();
				} while (canRun);
			}
		};

		loop.start();
	}

	public void playCrash() {
		Thread soundThread = new Thread() {
			@Override
			public void run() {
				do {
					try {
						crash.play();
					} catch (IOException e) {
						System.err.println("Erro na hora de dar play no crash");
					}
				} while (true);
			}
		};

		soundThread.start();

	}
}
