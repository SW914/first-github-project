package chapter9;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class ex09_1_server extends Frame implements ActionListener {
	TextArea display;
	TextField text;
	Label lword;
	Socket connection;
	
	BufferedWriter output;
	BufferedReader input;
	
	String clientdata = "";
	String serverdata = "";
	
	Boolean connect = false;
	
	public ex09_1_server() {
		super("����");
		
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("��ȭ��");
		
		text = new TextField(30); // ������ �����͸� �Է��ϴ� �ʵ�
		text.addActionListener(this); // �Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
		
		pword.add(lword, BorderLayout.WEST);
		pword.add(text, BorderLayout.EAST);
		add(pword, BorderLayout.SOUTH);
	
		addWindowListener(new WinListener());
		setSize(400, 200);
		setVisible(true);
	}
	
	public void runServer() {  
		ServerSocket server;
		try {
			server = new ServerSocket(5000, 100);
			while(true) { // ���� �� �翬�� ��û
				connection = server.accept();
				if(connect) { // 
					connection.close();
					continue;
				}
				connect = true;
				
				ServerThread re = new ServerThread(this, connection); // �ν��Ͻ� �ڱ� �ڽ�
			  	re.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException ne) {
			display.append("���� ����\n");
		}
	}
	
	class ServerThread extends Thread {
		ex09_1_server otos; // ����
		
		InputStream is;
		InputStreamReader isr;
		
		OutputStream os;
		OutputStreamWriter osw;
		
		BufferedReader input;
		BufferedWriter output;
		
		public ServerThread(ex09_1_server s, Socket connection) { // ������ �� ����
			try {
				otos = s;
				
				is = connection.getInputStream();
				isr = new InputStreamReader(is);
				input = new BufferedReader(isr); // ������ ������ ��ȭ���� ����
				
				os = connection.getOutputStream();
				osw = new OutputStreamWriter(os);
				output = new BufferedWriter(osw); // Ŭ���̾�Ʈ�� ��ȭ���� ����
				
				otos.output = this.output;
				otos.input = this.input;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			display.append("Ŭ���̾�Ʈ " + connection.getInetAddress().getHostAddress() + "�� ����Ǿ����ϴ�.\n");
			try {
			     while(true) {
			    	 clientdata = input.readLine();
			         if(clientdata.equals("quit")) {
			        	 display.append("\nŬ���̾�Ʈ���� ������ �ߴܵǾ����ϴ�.\n");
			        	 connect = false;
			        	 break;
			         }
			         else {
			        	 display.append("\nŬ���̾�Ʈ �޼��� : " + clientdata);
			         }
			     }
			} catch(NullPointerException npe) {
				display.append("\nŬ���̾�Ʈ���� ������ �ߴܵǾ����ϴ�.");
			} catch(IOException e){
				text.setText("");
				display.append("\n����� Ŭ���̾�Ʈ�� �����ϴ�.");
				e.printStackTrace();
			}
			try {
				connection.close(); // ���ϴݱ�
			} catch(IOException ea){
				ea.printStackTrace();
			} 
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		serverdata = text.getText();
		try {
			if(connect) { // Ŭ���̾�Ʈ���� ���� ���� true
				display.append("\n���� : " + serverdata);
				output.write(serverdata + "\r\n");
				output.flush();
				text.setText("");
				if(serverdata.equals("quit")){
					connection.close();
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		ex09_1_server s = new ex09_1_server();
		s.runServer();
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				connection.close(); // ��������
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(NullPointerException ne) {
				System.exit(0); // ���α׷� ����
			}
			System.exit(0); // ���α׷� ����
		}
	}	
}