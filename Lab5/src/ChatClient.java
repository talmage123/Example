
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
* The ChatClient class handles the initialization of the
* main chat client frame. 
* @author  Marshall Cockerell, Shawn Fahimi, Ben Ruelas and Antonio Sanchez
* @version 1.1
* @since   2020-11-14
*/
public class ChatClient extends JFrame implements WindowListener{  
	
	public final boolean verbose = true;
	JPanel names = new JPanel();
	JPanel options = new JPanel();
	JPanel message = new JPanel();
	JButton login = new JButton("Login");
	JButton logout = new JButton("Logout");
	JLabel password = new JLabel("", JLabel.RIGHT);
	JLabel dummy1 = new JLabel(""); 
	JLabel dummy2 = new JLabel("");
	JLabel dummy3 = new JLabel("");
	JPasswordField pwField = new JPasswordField();
    JButton sendButton = new JButton("Send");
    JLabel mL = new JLabel("Message to send ", JLabel.RIGHT);
    JLabel fL = new JLabel("Username:", JLabel.RIGHT);
    JLabel tL = new JLabel("To:", JLabel.RIGHT);
    JTextField name = new JTextField(15);
    JTextField to = new JTextField(15);
    JCheckBox encrypt = new JCheckBox("Encrypt");
    JCheckBox fancy = new JCheckBox("Fancy");
    JTextArea incoming;
    JScrollPane qScroller;
    JTextField outgoing;
    JLabel ip = new JLabel("Server IP address: ", JLabel.RIGHT);
    JFrame fancyFrame = new JFrame();
    JTextField ipField = new JTextField("192.168.1.87");
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String todayS, timeS, minS, secS;
    Calendar now; int year,month,day,hour,min,sec;
    
    /**
	 * Initializes all display features of the Chat Client
	 * <p>
	 */
    public ChatClient() {
       
    	setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
	    Random rn = new Random(sec);
	    int number =  rn.nextInt((50 - 1) + 1) + 0;
        setTitle("Simple Chat Client " + number); 
        Color c=Color.GRAY;
        if( number%4 == 0  ) {
        	c = Color.blue;
        }
        else if(number%4 == 1 ) {
        	c = Color.magenta;
        }
        else if( number%4 == 2 ) {
        	c = Color.yellow;
        }
        else if( number%4 == 3 ) {
        	c = Color.red;
        }
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        
        qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(36);
        
        names.setLayout(new GridLayout(3,2));
        names.add(fL); names.add(name); 
        
        names.add(tL); names.add(to);
        names.add(ip); names.add(ipField); 
        
        ipField.setForeground(Color.green);
		ipField.setBackground(Color.black);
		ipField.setEditable(false);
        
        options.setLayout(new FlowLayout());
        options.add(encrypt); options.add(fancy);
        
        message.setLayout(new FlowLayout());
        message.add(mL); message.add(outgoing); message.add(sendButton);
        
        add(names);
        add(options);
        add(message);
        add(qScroller);
       
       
        setBounds(400+5*number,8*number,650,320);
        setVisible(true);
        
    }
 
    
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/** 
	   An internal helper class used to handle the display of "Fancy" messages
    */
	class BackgroundPanel extends JPanel{
	  Image image;
	  
	  /**
	   * Constructs a BackgroundPanel with the necessary image information to display a message in Fancy form
	   * 
	   */
	  public BackgroundPanel(){
	    try{
	      image = ImageIO.read(new URL(getClass().getResource("Horned Frog.png"), "Horned Frog.png"));
	    }
	    catch (Exception e) { 
	    	/*handled in paintComponent()*/ 
	    }
	  }
	 
	  @Override
	  /**
	   * Helper method that draws an image to be used when displaying a message with Fancy display selected
	   * @param g a graphics context onto which the method draws
	   */
	  protected void paintComponent(Graphics g){
	    super.paintComponent(g); 
	    if (image != null) {
	      g.drawImage(image, 0,0,this.getWidth(),this.getHeight(),this);
	    }
	  }
	}

	
	
}