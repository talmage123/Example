import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
* TCU Cosc 20203 Fall 2020
* The ChatClientControl class handles the initialization of the
* buttons and main method for the program. It implements Action Listener and handles networking
* and the reader thread to listen for incoming traffic. 
* @author  Marshall Cockerell, Shawn Fahimi, Ben Ruelas and Antonio Sanchez
* @version 1.1
* @since   2020-11-14
*/
public class ChatClientControl extends ChatClient implements ActionListener {
	ChatClientModel m;
	JLabel pL1 = new JLabel();
	/**
	 * Initializes all user Interface and network features of the Chat Client
	 * <p>
	 */
	public ChatClientControl() {
		m = new ChatClientModel(this);
		incoming.setText("Client logged on " + processTime(2) + "\n");
		int sec = Integer.parseInt(processTime(3));
		setUpNetworking();
		addWindowListener(this);
		sendButton.addActionListener(this);
		login.addActionListener(this);
		logout.addActionListener(this);
		Thread readerThread = new Thread(new IncomingReader()); // thread to read messages
		readerThread.start();
		m.playSound();

		

	}
/**
 * Main method for the program.
 * @param args
 */
	public static void main(String args[]) {
		System.out.println("Simple Chat Client");
		new ChatClientControl();

	}

	/**
	 * Generates a string representing the date, time, or both (based on the current
	 * time of execution)
	 * 
	 * @param option an integer that specifies whether the user wants the date,
	 *               time, or both in String form
	 * @return the date, time, or both in String form
	 */
	public String processTime(int option) {
		now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		day = now.get(Calendar.DAY_OF_MONTH);
		hour = now.get(Calendar.HOUR);
		min = now.get(Calendar.MINUTE);
		sec = now.get(Calendar.SECOND);
		if (min < 10) {
			minS = "0" + min;
		} else {
			minS = "" + min;
		}
		if (sec < 10) {
			secS = "0" + sec;
		} else {
			secS = "" + sec;
		}
		todayS = month + " / " + day + " / " + year;
		timeS = hour + " : " + minS + " : " + secS;
		switch (option) {
		case (0):
			return todayS;
		case (1):
			return timeS;
		case (2):
			return todayS + " @ " + timeS;
		case (3):
			return secS;
		}
		return null; // should not get here
	}

	/**
	 * A helper method for the ChatClient constructor that initializes networking
	 * features for the client and reader/writers for the socket streams
	 * 
	 */
	protected void setUpNetworking() {
		try {
			if (verbose) {
				System.out.println("Opening socket at port 5001");
			}
			sock = new Socket("192.168.1.87", 5001);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking protocol established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Handles the processing of a message sent in a chat client
	 * 
	 * @param ev the "send" button click
	 *           <p>
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		String userInput = e.getActionCommand();
		if (userInput.equals("Send")) {

			try {
				if ((outgoing.getText().trim()).equals("") || (to.getText().trim()).equals("")
						|| (name.getText().trim()).equals("")) {
					// System.out.println("Completely fill out the chat form please!");
					JOptionPane.showMessageDialog(this, "Please fill out all text fields before sending a message!",
							"Incomplete form", JOptionPane.ERROR_MESSAGE);
				} else {
					String message = outgoing.getText() + "/" + to.getText().trim() + "/" + name.getText().trim() + "/"
							+ String.valueOf(encrypt.isSelected()) + "/" + String.valueOf(fancy.isSelected());
					if (verbose) {
						System.out.println("Sending coded message => " + message);
					}
					sock = new Socket("127.0.0.1", 5001);
					InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
					BufferedReader reader1 = new BufferedReader(streamReader);
					PrintWriter writer1 = new PrintWriter(sock.getOutputStream());
					writer1.print(message + "\r\n");
					writer1.flush();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (userInput.equals("Login")) {

			names.remove(password);
			names.remove(pwField);
			names.remove(dummy1);
			names.remove(login);
			ipField.setForeground(Color.green);
			ipField.setBackground(Color.black);
			ipField.setEditable(false);
			repaint();

		}
		if (userInput.equals("Logout")) {
			names.add(password);
			names.add(dummy3);
			names.add(login);
			ipField.setForeground(Color.black);
			ipField.setBackground(Color.white);
			ipField.setEditable(true);
			repaint();
		}
		outgoing.setText("");
		outgoing.requestFocus();
	}

	class IncomingReader implements Runnable {

		/**
		 * Method RUN to be executed when thread.start() is executed
		 */
		public void run() {

			String message;
			JLabel fancyLabel = new JLabel();
			try {
				while ((message = reader.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(message, "/");
					System.out.println(message);
					String msg = st.nextToken();
					String rec = st.nextToken();
					String from = st.nextToken();
					String encry = st.nextToken().toLowerCase();
					String fancy = st.nextToken().toLowerCase();

					if (verbose) {
						System.out.println("Encoded received =>  " + msg);
					}

					if ((rec.toLowerCase()).equals("all") || (name.getText().toLowerCase()).equals(rec.toLowerCase())) {

						if (encry.equals("false")) {
							if (fancy.equals("false")) {
								incoming.append("FROM " + from + " MESSAGE " + msg + "\n");

							} else {

								BackgroundPanel p1 = new BackgroundPanel();
								fancyLabel.setText(msg);
								fancyLabel.setHorizontalAlignment(JLabel.CENTER);
								fancyFrame.setBounds(300, 300, 300, 300);
								fancyFrame.setLayout(new BorderLayout());
								fancyFrame.add(p1, BorderLayout.CENTER);
								fancyFrame.add(fancyLabel, BorderLayout.PAGE_END);
								fancyFrame.setVisible(true);
								incoming.append("FROM " + from + " MESSAGE " + msg + "\n");
								m.playSound2();
								

							}
						} else {
							String decrypted = "";
							if (fancy.equals("false")) {
								for (int i = 0; i < msg.length(); i++) {
									decrypted += (char) (m.decrypt(msg.charAt(i), 3));
								}
								incoming.append("FROM " + from + " MESSAGE " + decrypted + "\n");
							} else {
								for (int i = 0; i < msg.length(); i++) {
									decrypted += (char) (m.decrypt(msg.charAt(i), 3));
								}
								BackgroundPanel p1 = new BackgroundPanel();
								fancyLabel.setText(decrypted);
								fancyLabel.setHorizontalAlignment(JLabel.CENTER);
								fancyFrame.setBounds(300, 300, 300, 300);
								fancyFrame.setLayout(new BorderLayout());
								fancyFrame.add(p1, BorderLayout.CENTER);
								fancyFrame.add(fancyLabel, BorderLayout.PAGE_END);
								fancyFrame.setVisible(true);
								incoming.append("FROM " + from + " MESSAGE " + decrypted + "\n");
								m.playSound2();
								
							}
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();

		}
	}

	/**
	 * Internal class to handle the IncomingReader Handler The benefit of using an
	 * internal class is that we have access to the various objects of the external
	 * class
	 */

}
