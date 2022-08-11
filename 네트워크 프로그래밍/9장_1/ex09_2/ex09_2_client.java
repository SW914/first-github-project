package chapter9;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ex09_2_client extends Frame implements ActionListener {
	TextArea display;
	TextField text, idtext;
	TextField id;
	Label lword, idlword;
	
	BufferedWriter output;
	BufferedReader input;
	Socket connection = null;
	String connectionid = "";
	String clientdata = "";
	String serverdata = "";
	
	public ex09_2_client() {
		super("Ŭ���̾�Ʈ");
		
		Panel idpword = new Panel(new BorderLayout());
		idlword = new Label("����� �̸�");
		
		idtext = new TextField(30);
		idtext.addActionListener(this);
		
		idpword.add(idlword, BorderLayout.WEST); // ����
		idpword.add(idtext, BorderLayout.CENTER); // ������
		
		add(idpword, BorderLayout.NORTH); // �Ʒ� �ѹ��� ���
		
		
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
	 
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("��ȭ��");
		
		text = new TextField(); //������ �����͸� �Է��ϴ� �ʵ�
		text.addActionListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
		
		pword.add(lword, BorderLayout.WEST);
		pword.add(text, BorderLayout.CENTER);
		
		add(pword, BorderLayout.SOUTH);
	
		addWindowListener(new WinListener());
		setSize(400, 200);
		setVisible(true);
	}
		
	public void runconnection() {
		try {
			connection = new Socket(InetAddress.getLocalHost(), 5000);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			while(true) {
	            String[] str = null;
	             String serverdata = input.readLine();
	             str = serverdata.split(" ");
	             if(str[0].equals(connectionid)) {
	                display.append("���� ��ȭ�� : " + clientdata + "\r\n");
	             }
	             else {
	                display.append(serverdata + "\r\n");
	             }
	            
	         }
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
			
	public void actionPerformed(ActionEvent ae) {
		clientdata = text.getText();
		connectionid = idtext.getText();
		if(connectionid.trim().length() >= 1) {
			if (!clientdata.equals("quit")) {
				try {
					//display.append("���� ��ȭ�� : " + clientdata + "\n");
					output.write(connectionid + " : " + clientdata + "\r\n");
					output.flush();
					text.setText("");
				} catch(IOException e){
					e.printStackTrace();
				}
			}
			else {
				try {
					output.write("quit" + "\r\n");
					output.flush();
					connection.close(); 
					System.exit(0);
				} catch(IOException e) {
					e.printStackTrace();
				}  
			}
		}
		else {
			display.append("����� �̸��� �Է� �� ����ϼ���!\n");
		}
		
	}			
	public static void main(String args[]) {
		ex09_2_client c = new ex09_2_client();
		c.runconnection();
	}
			
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {	  
			try {
				if(connection != null) {
					connection.close();
			   }	      
		   } catch(IOException ioe) {
			   ioe.printStackTrace();
		   }
		   System.exit(0);
	   }
	}			
}