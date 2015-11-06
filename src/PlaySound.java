import javax.sound.sampled.*;
import java.io.*;

class PlaySound implements LineListener{
	private volatile boolean open = false;
	private volatile boolean started = false;

	/**
 * Leitura dos bytes de um arquivo
 * @param  in local do arquivo
 * @return    array de bytes daquele arquivo
 */
public byte[] readByte(InputStream in) {
	try{
			BufferedInputStream buf = new BufferedInputStream (in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			while((read = buf.read()) != -1){
				out.write(read);
			}
			in.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
}

public void sound() throws Exception {
	Clip clip = AudioSystem.getClip();
	clip.addLineListener(this);
//	InputStream resource = ResourceLoader.load();
}

	@Override
	public void update(LineEvent event) {
	}


}
