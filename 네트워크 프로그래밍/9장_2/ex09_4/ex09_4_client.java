package chapter9_2;

import java.io.*;
import java.net.*;

public class ex09_4_client extends Thread {
	public static String host;
	protected Socket theSocket;
	
	protected InputStream is;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	
	public ex09_4_client() { // 생성자
		try {
			//theSocket = new Socket(host, 13);
			// daytime 서버에 접속한다.
			theSocket = new Socket(InetAddress.getLocalHost(), 13);
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
		} catch(UnknownHostException e) {
			System.out.println(host + " 호스트를 찾을 수 없습니다.");
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	public void run() {
		try {
			String theTime = reader.readLine(); // 날짜를 읽는다
			System.out.println("호스트의 시간은 "+ theTime + "이다");
			
			OutputStream os = theSocket.getOutputStream();
			// 클라이언트에 데이터를 전송할 OutputStream 객체를 생성한다.	
			writer = new BufferedWriter(new OutputStreamWriter(os));
			// 클라이언트에 데이터를 전송하는 BufferedWriter 객체를 생성한다.
			
			String theThankYou = "Thank You!";
			writer.write(theThankYou + "\r\n");
			writer.flush();
			
			theSocket.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public static void main(String args[]) {
		if(args.length > 0) {
			host = args[0];
		}
		else {
			host = "localhost";
		} 
		ex09_4_client client = new ex09_4_client();
		client.start();
	}
}