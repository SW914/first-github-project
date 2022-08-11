package chapter9;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ex09_1_client extends Frame implements ActionListener {
	TextArea display;
	TextField text;
	
	Label lword;
	
	BufferedWriter output;
	BufferedReader input;
	
	Socket client = null;
	
	String clientdata = "";
	String serverdata = "";
	
	Button reconnection; // �翬�� ��ư
	Boolean connect = false; // ������� true, false
	
	public ex09_1_client() { //GUI
		super("Ŭ���̾�Ʈ");
		display=new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
	
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("��ȭ��");
	   
		text = new TextField(30); //������ �����͸� �Է��ϴ� �ʵ�
		text.addActionListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
	   
		reconnection = new Button("�����"); // ��ư
		reconnection.addActionListener(this); // ��ư �̺�Ʈ ���
	   
		pword.add(lword, BorderLayout.WEST); // ����
		pword.add(text, BorderLayout.CENTER); // ���
		pword.add(reconnection, BorderLayout.EAST); // ������
	   
		add(pword, BorderLayout.SOUTH); // �Ʒ� �ѹ��� ���
	
		addWindowListener(new WinListener()); // X��ư �̺�Ʈ ���
		setSize(400, 200); // GUI ũ��
		setVisible(true);
	}	
		
	public void runClient() {
		try {
			client = new Socket(InetAddress.getLocalHost(), 5000); // ���� ����
			display.append("Ŭ���̾�Ʈ " + client.getInetAddress().getHostAddress() + "�� ����Ǿ����ϴ�.\n");
			connect = true; // ���� ���� ���� ON
			
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			while(true) {
				String serverdata = input.readLine(); // �� ������ serverdata�� ����
				if(!connect) {
					//System.exit(0);
					//display.append("\n�������� ������ �ߴܵǾ����ϴ�.");
					connect = false;
					break;
				}
				else if(serverdata.equals("exit")) {
					System.exit(0);
				}
				else {
					display.append("\n���� �޼��� : " + serverdata);
				}
			}
			client.close();
	   } catch(IOException e) {
		   e.printStackTrace();
	   } catch(NullPointerException ne) { 
		   display.append("�������� ������ �ߴܵǾ����ϴ�.\n");
		   try {
			   client.close();
		   } catch(IOException e) {
			   e.printStackTrace();
		   }
	   }
	}
			
	public void actionPerformed(ActionEvent ae){
		clientdata = text.getText(); // TextField �� �޾ƿ���
		if(reconnection.equals(ae.getSource())) {
			if(!connect) {	
				Reconnect re = new Reconnect(); // ������ ����
				re.start(); // �翬��
			}
			else if(!client.isBound()) {
				display.append("\n������ �� �ִ� ������ �����ϴ�.");
			}
			else if(client.isConnected()) {
				display.append("\n");
			}
		}
		else {
			try {
				if(!connect) {
					text.setText("");
					return;
				}
				else {
					text.setText("");
					display.append("\nŬ���̾�Ʈ : " + clientdata);
					output.write(clientdata + "\r\n");
					output.flush();	
					if(clientdata.equals("quit")) {
						display.append("\n�������� ������ �ߴܵǾ����ϴ�.");
						connect = false;
						//display.append("\n");
						client.close();
					}
					else if(serverdata.equals("exit")) {
						System.exit(0); //������ ������ ���α׷� ����
					}		
				}
				text.setText("");
			} catch(IOException e){
				text.setText("");
				display.append("\n����� ������ �����ϴ�.");
				e.printStackTrace();
			}
		}
	}
	
	class Reconnect extends Thread {
		public void run() {
			try {	
				client = new Socket(InetAddress.getLocalHost(), 5000);
				display.append("\nŬ���̾�Ʈ " + client.getInetAddress().getHostAddress() + "�� ����Ǿ����ϴ�.\n");
				connect = true;
				
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				
				while(true) {
					String serverdata = input.readLine();
					if(serverdata == null) {
						//display.append("\n�������� ������ �ߴܵǾ����ϴ�.");
						connect = false;
						break;
				    }
					else {
						display.append("\n���� �޼���: " + serverdata);
					}
				}
				client.close();
			} catch(IOException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		ex09_1_client c = new ex09_1_client();
		c.runClient();
	}
			
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				client.close(); // ��������
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(NullPointerException ne) {
				System.exit(0); // ���α׷� ����
			}
			System.exit(0); // ���α׷� ����
		}
	}			
}