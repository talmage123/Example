
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * The ChatServer class handles the initialization of the main server frame and
 * all networking components necessary to broadcast and process messages from
 * numerous chat clients.
 * 
 * @author Marshall Cockerell, Shawn Fahimi, Ben Ruelas and Antonio Sanchez
 * @version 1.1
 * @since 2020-11-14
 */
public class ChatServer extends JFrame implements WindowListener, ActionListener {

	static ArrayList<PrintWriter> clientOutputStreams;
	BufferedReader reader;
	// Socket sock;
	public final boolean verbose = true;
	public boolean portEntered;
	public JTextArea result = new JTextArea(20, 30);
	public JScrollPane jsp = new JScrollPane(result);
	public Font f = new Font("Helvetica", Font.BOLD, 16);
	JLabel timeLabel = new JLabel("Date and Time ", JLabel.RIGHT);
	JTextField timeField = new JTextField("");
	JPanel displayTime = new JPanel(new GridLayout(1, 2));
	JButton port = new JButton("Enter Server Port:");
	JTextField portField = new JTextField("");
	JPanel p = new JPanel(new BorderLayout());
	int year, month, day, hour, min, sec;
	String todayS, timeS, minS, secS;
	Calendar now;
	Color TCUColors = new Color(77, 25, 121);
	JButton save = new JButton("Save");

	/**
	 * Constructs and initializes all display and networking features for a Chat
	 * Server, and continuously listens for connections afterwards
	 */
	public ChatServer() {
		setBounds(100, 100, 500, 500);
		setSize(500, 550);
		displayTime.add(timeLabel);
		displayTime.add(timeField);
		p.add(displayTime, BorderLayout.NORTH);
		p.add(jsp, BorderLayout.CENTER);
		p.setBackground(TCUColors);
		timeLabel.setFont(f);
		result.setFont(f);
		timeLabel.setForeground(Color.WHITE);
		displayTime.setBackground(TCUColors);
		setLayout(new BorderLayout());
		setTitle("Chat Server");
		add(p, BorderLayout.CENTER);
		add(save, BorderLayout.SOUTH);
		save.addActionListener(this);
		save.requestFocus();
		addWindowListener(this);
		setVisible(true);
		clientOutputStreams = new ArrayList<PrintWriter>();
		handlePort(portEntered);

	}
	public static void main(String args[]) {
		new ChatServer();
	}

	protected void handlePort(boolean b) {

		try {
			ServerSocket serverSock = new ServerSocket(5001);
			if (verbose) {
				System.out.println(" Server IP 192.168.1.87. ready at port " + 5001);
			}
			if (verbose) {
				System.out.println(" ___________________" + "________________________________________________");
			}
			result.append("Server started on " + processTime(2) + "\n");
			if (verbose) {
				System.out.println("\nStart Server on " + processTime(2));
				timeField.setText(processTime(2));
			}
			while (true) {

				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start(); // to execute the run() method of ClientHandler
				if (verbose) {
					System.out.println("got a connection");
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		}

		return null; // should not get here

	}

	/**
	 * Helper method that writes a message sent from a client to all client output
	 * streams
	 * 
	 * @param message the message sent from a chat client
	 */
	public void tellEveryone(String message) {

		Iterator<PrintWriter> it = clientOutputStreams.iterator();

		StringTokenizer st = new StringTokenizer(message, "/");
		String msg = st.nextToken().trim();
		String rec = st.nextToken().trim();
		String from = st.nextToken().trim();
		String encry = st.nextToken().toLowerCase();
		String fancy = st.nextToken().toLowerCase();

		// result.append(message + " broadcasted on " + processTime(2)+ "\n");
		result.append("FROM " + from + " TO " + rec + " ENC " + encry + " FANCY " + fancy + " MESS " + msg
				+ " broadcasted on " + processTime(2) + "\n");

		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				System.out.println("coded message" + message);
				writer.print(message + "\r\n");
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Internal class to handle the Client Handler The benefit of using an internal
	 * class is that we have access to the various objects of the external class
	 */
	public class ClientHandler implements Runnable {

		/**
		 * Constructor of the inner class
		 * 
		 * @param clientSocket
		 */
		Socket sock;

		public ClientHandler(Socket clientSocket) {

			try {

				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
				if (verbose) {
					System.out.println("new client ");
				}
				if (verbose) {
					System.out.println(
							" new client at address " + sock.getLocalAddress() + " at port " + sock.getLocalPort());
				}
				if (verbose) {
					System.out.println(" Client at address " + sock.getInetAddress() + " at port " + sock.getPort());
				}
				if (verbose) {
					System.out.println(" ___________________" + "________________________________________________");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		/**
		 * Method RUN to be executed when thread.start() is executed
		 */
		public void run() {

			String message;

			try {
				while ((message = reader.readLine()) != null) {
					if (verbose) {
						System.out.println("coded " + message);
					}

					StringTokenizer st = new StringTokenizer(message, "/");
					System.out.println(message);
					String msg = st.nextToken();
					String rec = st.nextToken();
					String from = st.nextToken();
					String encry = st.nextToken().toLowerCase();
					String fancy = st.nextToken().toLowerCase();

					if (encry.equals("true")) {
						String encrypted = "";
						for (int i = 0; i < msg.length(); i++) {
							encrypted += (char) (encrypt(msg.charAt(i), 3));
						}

						msg = encrypted;
						message = msg + "/" + rec + "/" + from + "/" + encry + "/" + fancy;
					}
					result.append("message received " + msg + " on " + processTime(2) + "\n");
					tellEveryone(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// writer.close();

		}
	}

	/**
	 * Encrypts a char by adding the key provided to the char provided
	 * 
	 * @param b   the char to encrypt
	 * @param key the encryption key
	 * @return the encrypted char
	 */
	public char encrypt(char b, int key) {
		return (char) (b + key);
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		System.exit(1);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Handles user input
	 */
	public void actionPerformed(ActionEvent e) {
		String ui = e.getActionCommand();
		if(ui.equals("Save")) 
			procSaveAs("Log");
			System.out.println("Save Test");
		
	}
	/**
	 * Opens a file dialog to allow the user to save a server log file. 
	 * @param fileName
	 */
	public void procSaveAs(String fileName) {
		FileDialog myFD;
		myFD = new FileDialog(this, fileName, FileDialog.SAVE);
		myFD.setVisible(true);
		String name = myFD.getFile();
		String dir = myFD.getDirectory();
		
		if (name == null) {
			
		}
		try {

			File file = new File(dir + name);
			OutputStream outputStream = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			String code = new String(result.getText());
			byte[] bytesArray = code.getBytes();
			outputStream.write(bytesArray);
			outputStream.flush();
			

		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		return;
	}

}
