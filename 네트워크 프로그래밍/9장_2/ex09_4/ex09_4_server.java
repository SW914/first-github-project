package chapter9_2;

import java.io.*;
import java.net.*;
import java.util.Date;

public class ex09_4_server extends Thread {
	public final static int daytimeport=13;	
	protected ServerSocket theServer;
	protected Socket theSocket = null;
	
	protected InputStream is;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	
	public ex09_4_server() {
		try {
			theServer = new ServerSocket(daytimeport, 100);
			// 13번 포트에서 클라이언트의 접속 요청을 기다리는 서버소켓 객체를 생성한다.
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	public void run() {
		while(true) {
			try {
				theSocket = theServer.accept();
				// 클라이언트의 접속요청을 기다리고
				// 클라이언트의 소켓과 연결된 서버 측의 소켓(theSocket)을 생성한다.
				is = theSocket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is));
				OutputStream os = theSocket.getOutputStream();
				// 클라이언트에 데이터를 전송할 OutputStream 객체를 생성한다.	
				writer = new BufferedWriter(new OutputStreamWriter(os));
				// 클라이언트에 데이터를 전송하는 BufferedWriter 객체를 생성한다.
					
				Date now = new Date(); // 날짜를 구한다.
				
				writer.write(now.toString() + "\r\n"); // 날짜를 전송한다.
				writer.flush();
				
				String theThankYou = reader.readLine(); // 날짜를 읽는다
				System.out.println(theThankYou + "\r\n");
			
				theSocket.close();
			} catch(IOException e) {
				System.out.println(e);
			} finally {
				try {
					if(theSocket != null) theSocket.close();
				} catch(IOException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	public static void main(String args[]) {
		ex09_4_server server = new ex09_4_server();
		server.start();
	}
}
