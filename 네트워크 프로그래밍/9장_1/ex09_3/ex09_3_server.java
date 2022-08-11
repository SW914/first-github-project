package chapter9;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class ex09_3_server extends Frame {
	TextArea display;
	Label info;
	List<ServerThread> list;
	
	public ServerThread SThread;
	
	public ex09_3_server() {
		super("서버");
	    info = new Label();
	    add(info, BorderLayout.CENTER);
	    display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
 	    display.setEditable(false);
  	    add(display, BorderLayout.SOUTH);
	    addWindowListener(new WinListener());
	    setSize(400, 200);
	    setVisible(true);
	}
	
	public void runServer() {
		ServerSocket server;
	    Socket sock;
	    ServerThread SThread;
	    try {
	    	list = new ArrayList<ServerThread>();
	    	server = new ServerSocket(5000, 100);
	    	try {
	    		while(true) {
	    			sock = server.accept();
	    			SThread = new ServerThread(this, sock, display, info);
	    			SThread.start();
	    			info.setText(sock.getInetAddress().getHostAddress() + " 서버는 클라이언트와 연결됨");
	    		}
	    	} catch(IOException ioe) {
	    		server.close();
	    		ioe.printStackTrace();
	    	}
	    } catch(IOException ioe) {
	    	ioe.printStackTrace();
	    }
	}
		
	public static void main(String args[]) {
		ex09_3_server s = new ex09_3_server();
		s.runServer();
	}
			
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}

class ServerThread extends Thread {
	Socket sock;
	BufferedWriter output;
	BufferedReader input;
	TextArea display;
	Label info;
	TextField text;
	String clientdata;
	String serverdata = "";
	ex09_3_server cs;
	
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	private static final int REQ_LOGOUT = 9999;
	
	public ServerThread(ex09_3_server c, Socket s, TextArea ta, Label l) {
		sock = s;
		display = ta;
		info = l;
		cs = c;
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public void run() {
		try {
			//cs.list.add(this); // 로그온 전에 리스트 등록
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				int cnt = cs.list.size();
				switch(command) {
					case REQ_LOGON : { // “1001|아이디”를 수신한 경우
						String ID = st.nextToken();
						display.append("클라이언트가 " + ID + "(으)로 로그인 하였습니다.\r\n");
						for(int i = 0; i < cnt; i++) { // 모든 클라이언트에 전송
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write("클라이언트가 " + ID + "(으)로 로그인 하였습니다.\r\n");
							SThread.output.flush();
						}
						cs.list.add(this);// 로그온 이후 리스트 등록
						break;
					}
					case REQ_SENDWORDS : { // “1021|아이디|대화말”를 수신
						String ID = st.nextToken();
						String message = st.nextToken();
						display.append(ID + " : " + message + "\r\n");
						for(int i = 0; i < cnt; i++) { // 모든 클라이언트에 전송
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write(ID + " : " + message + "\r\n");
							SThread.output.flush();
						}
						break;
					}
					case REQ_LOGOUT : {
						String ID = st.nextToken();
						display.append("클라이언트가 " + ID + " 이(가) 로그아웃 하였습니다." +  "\r\n");
						for(int i = 0; i < cnt; i++) { // 모든 클라이언트에 전송
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write("클라이언트가 " + ID + " 이(가) 로그아웃 하였습니다." + "\r\n");
							SThread.output.flush();
						}
						cs.list.remove(this);
						break;
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		cs.list.remove(this);
		try {
			sock.close();
		} catch(IOException ea){
			ea.printStackTrace();
		}
	}
}	