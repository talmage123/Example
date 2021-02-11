import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
/**
* TCU Cosc 20203 Fall 2020
* The ChatClientModel class handles the sounds and encrytpion/decryption for the chat client.  
* @author  Marshall Cockerell, Shawn Fahimi, Ben Ruelas and Antonio Sanchez
* @version 1.1
* @since   2020-11-14
*/
public class ChatClientModel   {
	ChatClientControl c;
	/**
	 * 
	 * @param fromC refers to the argument inside the model constructor which is
	 *              instantiated in the control class. Therefore "this" is talking
	 *              about the control object. We are passing our control object into
	 *              the constructor of our model class, where we create an instance
	 *              variable of the control object.
	 */
	public ChatClientModel(ChatClientControl fromC) {
		 c = fromC;
		 
	        
	        
	}
	/**
	 *Gets the sound resource and plays when the method is called.
	 *Plays once connection has been attempted to the server. 
	 */
	    public void playSound() {
	    	 try {
	             // Open an audio input stream.
	             URL url = this.getClass().getClassLoader().getResource("chnlopen.wav");
	    		 //URL url = new URL("https://www.thesoundarchive.com/play-wav-files.asp?sound=email/youGotmail.mp3");
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	             // Get a sound clip resource.
	             Clip clip = AudioSystem.getClip();
	             // Open audio clip and load samples from the audio input stream.
	             clip.open(audioIn);
	             clip.start();
	          } catch (UnsupportedAudioFileException e) {
	             e.printStackTrace();
	          } catch (IOException e) {
	             e.printStackTrace();
	          } catch (LineUnavailableException e) {
	             e.printStackTrace();
	          }
	    	 System.out.println("Audio Test");
	       }
	    /**
	     * Gets a sound resource and plays when the method is called. 
	     * Plays when a fancy message has been received by the client. 
	     */
	    public void playSound2() {
	    	 try {
	             // Open an audio input stream.
	             URL url = this.getClass().getClassLoader().getResource("sentnc10.wav");
	    		 //URL url = new URL("https://www.thesoundarchive.com/play-wav-files.asp?sound=email/youGotmail.mp3");
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	             // Get a sound clip resource.
	             Clip clip = AudioSystem.getClip();
	             // Open audio clip and load samples from the audio input stream.
	             clip.open(audioIn);
	             clip.start();
	          } catch (UnsupportedAudioFileException e) {
	             e.printStackTrace();
	          } catch (IOException e) {
	             e.printStackTrace();
	          } catch (LineUnavailableException e) {
	             e.printStackTrace();
	          }
	    	 System.out.println("Audio Test");
	       }
	    
	    
	    
	    /**
		  Encrypts a char by adding the key provided to the char provided
	      @param b the char to encrypt
	      @param key the encryption key
	      @return the encrypted char
	    */
	    public char encrypt(char b, int key) {
	    	return (char) (b + key);
	    }
	    
	    /**
	 	  Decrypts a char by subtracting the key provided from the char provided
	      @param b the char to decrypt
	      @param key the encryption key
	      @return the decrypted char
	    */
	    public char decrypt(char b, int key)
	    {
	    	return(char) (b - key);
	    }
}
