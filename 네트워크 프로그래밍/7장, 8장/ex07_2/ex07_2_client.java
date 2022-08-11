package chapter7;

import java.io.*;
import java.net.*;

public class ex07_2_client extends Thread {
	protected Socket theSocket = null;
	protected InputStream is;
	protected BufferedReader reader, userInput;
	protected OutputStream os;
	protected BufferedWriter writer;
	public static String theLine;
	public static String host;
	
	public ex07_2_client() {
		try {
			theSocket = new Socket(host, 7); // echo 서버에 접속한다.
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void run() {
		try {	
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			userInput = new BufferedReader(new InputStreamReader(System.in));
			os = theSocket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));
			System.out.println("전송할 문장을 입력하십시오.");
			while(true) {
				theLine = userInput.readLine(); // 데이터를 입력한다.
				
				if(theLine.equals("quit")) break;
				
				writer.write(theLine + '\r' + '\n');
				writer.flush(); // 서버에 데이터 전송
				System.out.println(reader.readLine());
			}
		} catch(UnknownHostException e) {
			System.out.println(host + " 호스트를 찾을 수 없습니다.");
		} catch(IOException e) {
			System.out.println(e);
		} finally {
			try {
				theSocket.close();
			} catch(IOException e) {
				System.out.println(e);
			}
		}		
	}
 	public static void main(String args[]) {
		if(args.length > 0) {
			host = args[0]; // 원격 호스트를 입력받음
		}
		else {
			host = "localhost"; // 로결 호스트를 원격 호스트로 사용
		}
		ex07_2_client client = new ex07_2_client();
		client.start();
		
	}
}