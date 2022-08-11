package chapter13;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class ex13_2_server extends Frame {
	TextArea display;
	Label info;
	
	List<ServerThread> list;
	public ServerThread SThread;
	
	HashMap hash;
	MulticastSocket msocket;
	
	public ex13_2_server() {
		super("서버");
	    info = new Label();
	    add(info, BorderLayout.CENTER);
	    display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
 	    display.setEditable(false);
  	    add(display, BorderLayout.SOUTH);
	    addWindowListener(new WinListener());
	    setSize(400, 250);
	    setVisible(true);
	}
	
	public void runServer() {
		ServerSocket server;
	    Socket sock;
	    ServerThread SThread;
	    
	    int port = 5265;
	    
	    try {	
	    	server = new ServerSocket(5000, 100);
	    	
	    	msocket = new MulticastSocket(port);
	    	msocket.setSoTimeout(10000);
	    	msocket.setTimeToLive(1);
	    	
	    	hash = new HashMap<>();
	    	list = new ArrayList<ServerThread>();
	    	try {	
	    		InetAddress group = InetAddress.getByName("239.255.10.10");
	    		while(true) {
	    			sock = server.accept();
	    			SThread = new ServerThread(this, sock, display, info, msocket); // 인자값 전달
	    			SThread.start();
	    			info.setText("멀티캐스트 채팅 그룹 주소 " + group);
	    			// 연결되면 윗부분 info 값 변경
	    			//info.setText(sock.getInetAddress().getHostAddress() + " 서버는 클라이언트와 연결됨");
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
		ex13_2_server s = new ex13_2_server();
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
	
	ex13_2_server cs;
	MulticastSocket m;
    //DatagramPacket packet;
    InetAddress group;
    int port;
	
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	//private static final int REQ_WISPERSEND = 1022;
	private static final int REQ_LOGOUT = 9999;
	
	public ServerThread(ex13_2_server c, Socket s, TextArea ta, Label l, MulticastSocket msocket) {
		sock = s;
		display = ta;
		info = l;
		cs = c;
		m = msocket;
		port = 1473;
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			group = InetAddress.getByName("239.255.10.10");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				int Lcnt = cs.list.size();
				
				switch(command) {
					case REQ_LOGON : { // “1001|아이디”를 수신한 경우
						String ID = st.nextToken();
						display.append("클라이언트가 " + ID + "(으)로 로그인 하였습니다.\r\n");
						
						this.output.write("멀티캐스트 채팅 그룹 주소는 /239.255.10.10:1473입니다." + "\r\n");						
						this.output.flush();
						
						this.output.write(cs.hash.keySet().toString() + "\r\n");
						this.output.flush();
						
						
						for(int i = 0; i < Lcnt; i++) { // 모든 클라이언트에 전송							
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write("클라이언트가 " + ID + "(으)로 로그인 하였습니다.\r\n");
							SThread.output.flush();
						}
                        
						
						cs.hash.put(ID, this); // 해쉬 테이블에 아이디와 스레드를 저장한다
						cs.list.add(this); // 로그온 이후 리스트 등록

						break;
					}
					case REQ_SENDWORDS : { // “1021|아이디|대화말”를 수신
						String ID = st.nextToken();
						String message = st.nextToken();
						display.append(ID + " : " + message + "\r\n");
						
						String sending = ID + " : " + message;	
						byte data[] = sending.getBytes();
	                    DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
						m.send(packet);
						
						/*
						for(int i = 0; i < Lcnt; i++) { // 모든 클라이언트에 전송
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write(ID + " : " + message + "\r\n");
							SThread.output.flush();
						}
	                    */
						
						break;
					}
					/*
					case REQ_WISPERSEND : {
						String ID = st.nextToken();
						String WID = st.nextToken();
						String message = st.nextToken();
						
						
						if(cs.hash.containsKey(WID)) {
							display.append(ID + " -> " + WID + " : " + message + "\r\n");
							ServerThread SThread = (ServerThread)cs.hash.get(ID);
							// 해쉬테이블에서 귓속말 메세지를 전송한 클라이언트의 스레드를 구함
							SThread.output.write(ID + " -> " + WID + " : " + message + "\r\n");
							// 귓속말 메세지를 전송한 클라이언트에 전송함
							SThread.output.flush();
							SThread = (ServerThread)cs.hash.get(WID);
							// 해쉬테이블에서 귓속말 메시지를 수신할 클라이언트의 스레드를 구함
							SThread.output.write(ID + " : " + message + "\r\n");
							// 귓속말 메시지를 수신할 클라이언트에 전송함
							SThread.output.flush();
							break;
						}
						else {
							display.append("대상자 " + WID + " 이(가) 없습니다." +  "\r\n");
							ServerThread SThread = (ServerThread)cs.hash.get(ID);
							// 해쉬테이블에서 귓속말 메세지를 전송한 클라이언트의 스레드를 구함
							SThread.output.write(ID + " -> " + WID + " 이(가) 없습니다." +  "\r\n");
							// 귓속말 메세지를 전송한 클라이언트에 전송함
							SThread.output.flush();
						}
					}
					*/
					case REQ_LOGOUT : {
						String ID = st.nextToken();
						display.append("클라이언트가 " + ID + " 이(가) 로그아웃 하였습니다." +  "\r\n");
						/*
						for(int i = 0; i < Lcnt; i++) { // 모든 클라이언트에 전송
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write("클라이언트가 " + ID + " 이(가) 로그아웃 하였습니다." + "\r\n");
							SThread.output.flush();
						}
						*/
						cs.list.remove(this);
						cs.hash.remove(ID);
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