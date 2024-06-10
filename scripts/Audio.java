import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private Clip clip;
    Audio(String filePath,float volume){
        try {
            // Open an audio input stream.
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioStream);
            setVolume(volume);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }



    // Play the loaded audio
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Stop the audio
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Loop the audio
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}