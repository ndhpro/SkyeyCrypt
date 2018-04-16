/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skyey;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ndhpro
 */
public class Sound {
    private String source;
    
    public void setPath(String source){
        this.source = source;
    }
    
    public void playSound() {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(source).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(IOException | LineUnavailableException | UnsupportedAudioFileException e) {}
    }
}
