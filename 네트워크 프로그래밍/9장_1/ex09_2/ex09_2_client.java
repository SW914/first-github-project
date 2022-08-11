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
		super("클라이언트");
		
		Panel idpword = new Panel(new BorderLayout());
		idlword = new Label("사용자 이름");
		
		idtext = new TextField(30);
		idtext.addActionListener(this);
		
		idpword.add(idlword, BorderLayout.WEST); // 왼쪽
		idpword.add(idtext, BorderLayout.CENTER); // 오른쪽
		
		add(idpword, BorderLayout.NORTH); // 아래 한번에 등록
		
		
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
	 
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("대화말");
		
		text = new TextField(); //전송할 데이터를 입력하는 필드
		text.addActionListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
		
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
	                display.append("나의 대화말 : " + clientdata + "\r\n");
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
					//display.append("나의 대화말 : " + clientdata + "\n");
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
			display.append("사용자 이름을 입력 후 사용하세요!\n");
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