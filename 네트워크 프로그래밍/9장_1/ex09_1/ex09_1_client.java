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
	
	Button reconnection; // 재연결 버튼
	Boolean connect = false; // 연결상태 true, false
	
	public ex09_1_client() { //GUI
		super("클라이언트");
		display=new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
	
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("대화말");
	   
		text = new TextField(30); //전송할 데이터를 입력하는 필드
		text.addActionListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
	   
		reconnection = new Button("재시작"); // 버튼
		reconnection.addActionListener(this); // 버튼 이벤트 등록
	   
		pword.add(lword, BorderLayout.WEST); // 왼쪽
		pword.add(text, BorderLayout.CENTER); // 가운데
		pword.add(reconnection, BorderLayout.EAST); // 오른쪽
	   
		add(pword, BorderLayout.SOUTH); // 아래 한번에 등록
	
		addWindowListener(new WinListener()); // X버튼 이벤트 등록
		setSize(400, 200); // GUI 크기
		setVisible(true);
	}	
		
	public void runClient() {
		try {
			client = new Socket(InetAddress.getLocalHost(), 5000); // 소켓 생성
			display.append("클라이언트 " + client.getInetAddress().getHostAddress() + "와 연결되었습니다.\n");
			connect = true; // 서버 연결 상태 ON
			
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			while(true) {
				String serverdata = input.readLine(); // 값 받은거 serverdata에 저장
				if(!connect) {
					//System.exit(0);
					//display.append("\n서버와의 접속이 중단되었습니다.");
					connect = false;
					break;
				}
				else if(serverdata.equals("exit")) {
					System.exit(0);
				}
				else {
					display.append("\n서버 메세지 : " + serverdata);
				}
			}
			client.close();
	   } catch(IOException e) {
		   e.printStackTrace();
	   } catch(NullPointerException ne) { 
		   display.append("서버와의 접속이 중단되었습니다.\n");
		   try {
			   client.close();
		   } catch(IOException e) {
			   e.printStackTrace();
		   }
	   }
	}
			
	public void actionPerformed(ActionEvent ae){
		clientdata = text.getText(); // TextField 값 받아오기
		if(reconnection.equals(ae.getSource())) {
			if(!connect) {	
				Reconnect re = new Reconnect(); // 쓰레드 생성
				re.start(); // 재연결
			}
			else if(!client.isBound()) {
				display.append("\n연결할 수 있는 서버가 없습니다.");
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
					display.append("\n클라이언트 : " + clientdata);
					output.write(clientdata + "\r\n");
					output.flush();	
					if(clientdata.equals("quit")) {
						display.append("\n서버와의 접속이 중단되었습니다.");
						connect = false;
						//display.append("\n");
						client.close();
					}
					else if(serverdata.equals("exit")) {
						System.exit(0); //서버가 꺼지면 프로그램 종료
					}		
				}
				text.setText("");
			} catch(IOException e){
				text.setText("");
				display.append("\n연결된 서버가 없습니다.");
				e.printStackTrace();
			}
		}
	}
	
	class Reconnect extends Thread {
		public void run() {
			try {	
				client = new Socket(InetAddress.getLocalHost(), 5000);
				display.append("\n클라이언트 " + client.getInetAddress().getHostAddress() + "와 연결되었습니다.\n");
				connect = true;
				
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				
				while(true) {
					String serverdata = input.readLine();
					if(serverdata == null) {
						//display.append("\n서버와의 접속이 중단되었습니다.");
						connect = false;
						break;
				    }
					else {
						display.append("\n서버 메세지: " + serverdata);
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
				client.close(); // 서버끄기
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(NullPointerException ne) {
				System.exit(0); // 프로그램 종료
			}
			System.exit(0); // 프로그램 종료
		}
	}			
}