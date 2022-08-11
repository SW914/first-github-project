package chapter9_2;

import java.io.*;
import java.net.*;

public class ex09_4_client extends Thread {
	public static String host;
	protected Socket theSocket;
	
	protected InputStream is;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	
	public ex09_4_client() { // ������
		try {
			//theSocket = new Socket(host, 13);
			// daytime ������ �����Ѵ�.
			theSocket = new Socket(InetAddress.getLocalHost(), 13);
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
		} catch(UnknownHostException e) {
			System.out.println(host + " ȣ��Ʈ�� ã�� �� �����ϴ�.");
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	public void run() {
		try {
			String theTime = reader.readLine(); // ��¥�� �д´�
			System.out.println("ȣ��Ʈ�� �ð��� "+ theTime + "�̴�");
			
			OutputStream os = theSocket.getOutputStream();
			// Ŭ���̾�Ʈ�� �����͸� ������ OutputStream ��ü�� �����Ѵ�.	
			writer = new BufferedWriter(new OutputStreamWriter(os));
			// Ŭ���̾�Ʈ�� �����͸� �����ϴ� BufferedWriter ��ü�� �����Ѵ�.
			
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