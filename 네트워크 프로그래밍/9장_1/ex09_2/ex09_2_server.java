package chapter9;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class ex09_2_server extends Frame {
	TextArea display;
	Label info;
	String clientdata = "";
	String serverdata = "";
	List<ServerThread> list;
	public ServerThread SThread;
	
	public ex09_2_server() {
		super("서버");
		info = new Label();
	    add(info, BorderLayout.NORTH);
	    
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display);
		
		addWindowListener(new WinListener());
		setSize(400,200);
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
					SThread = new ServerThread(this, sock, display, serverdata);
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
		ex09_2_server s = new ex09_2_server();
		s.runServer();
	}
		
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	class ServerThread extends Thread {
		Socket sock;
		InputStream is;
		InputStreamReader isr;
		BufferedReader input;
		OutputStream os;
		OutputStreamWriter osw;
		BufferedWriter output;
		TextArea display;
		Label info;
		TextField text;
		String serverdata = "";
		ex09_2_server cs;
		
		public ServerThread(ex09_2_server c, Socket s, TextArea ta, String data) {
			sock = s;
			display = ta;
			serverdata = data;
			cs = c;
			try {
				is = sock.getInputStream();
				isr = new InputStreamReader(is);       
				input = new BufferedReader(isr);
		      
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				output = new BufferedWriter(osw);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		public void run() {
			cs.list.add(this);
			String clientdata;
			try {
				while((clientdata = input.readLine()) != null) {
					display.append(clientdata + "\r\n");
					int cnt = cs.list.size();
					
					for(int i=0; i<cnt; i++) { //모든 클라이언트에 데이터를 전송한다.
						ServerThread SThread = (ServerThread)cs.list.get(i);
						SThread.output.write(clientdata + "\r\n");
						SThread.output.flush();
					}
					
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			cs.list.remove(this); //리스트에서 close된 클라이언트를 지운다.
			try {
				sock.close();   //소켓닫기
			} catch(IOException ea){
				ea.printStackTrace();
			}
		}
	}
}
	
