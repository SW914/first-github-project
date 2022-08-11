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
		super("서버");
		
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		Panel pword = new Panel(new BorderLayout());
		lword = new Label("대화말");
		
		text = new TextField(30); // 전송할 데이터를 입력하는 필드
		text.addActionListener(this); // 입력된 데이터를 송신하기 위한 이벤트 연결
		
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
			while(true) { // 종료 후 재연결 요청
				connection = server.accept();
				if(connect) { // 
					connection.close();
					continue;
				}
				connect = true;
				
				ServerThread re = new ServerThread(this, connection); // 인스턴스 자기 자신
			  	re.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException ne) {
			display.append("접속 종료\n");
		}
	}
	
	class ServerThread extends Thread {
		ex09_1_server otos; // 서버
		
		InputStream is;
		InputStreamReader isr;
		
		OutputStream os;
		OutputStreamWriter osw;
		
		BufferedReader input;
		BufferedWriter output;
		
		public ServerThread(ex09_1_server s, Socket connection) { // 생성자 값 지정
			try {
				otos = s;
				
				is = connection.getInputStream();
				isr = new InputStreamReader(is);
				input = new BufferedReader(isr); // 서버가 전송한 대화말을 수신
				
				os = connection.getOutputStream();
				osw = new OutputStreamWriter(os);
				output = new BufferedWriter(osw); // 클라이언트에 대화말을 전송
				
				otos.output = this.output;
				otos.input = this.input;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			display.append("클라이언트 " + connection.getInetAddress().getHostAddress() + "가 연결되었습니다.\n");
			try {
			     while(true) {
			    	 clientdata = input.readLine();
			         if(clientdata.equals("quit")) {
			        	 display.append("\n클라이언트와의 접속이 중단되었습니다.\n");
			        	 connect = false;
			        	 break;
			         }
			         else {
			        	 display.append("\n클라이언트 메세지 : " + clientdata);
			         }
			     }
			} catch(NullPointerException npe) {
				display.append("\n클라이언트와의 접속이 중단되었습니다.");
			} catch(IOException e){
				text.setText("");
				display.append("\n연결된 클라이언트가 없습니다.");
				e.printStackTrace();
			}
			try {
				connection.close(); // 소켓닫기
			} catch(IOException ea){
				ea.printStackTrace();
			} 
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		serverdata = text.getText();
		try {
			if(connect) { // 클라이언트와의 연결 상태 true
				display.append("\n서버 : " + serverdata);
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
				connection.close(); // 서버끄기
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(NullPointerException ne) {
				System.exit(0); // 프로그램 종료
			}
			System.exit(0); // 프로그램 종료
		}
	}	
}