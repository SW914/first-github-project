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
			// 13�� ��Ʈ���� Ŭ���̾�Ʈ�� ���� ��û�� ��ٸ��� �������� ��ü�� �����Ѵ�.
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	public void run() {
		while(true) {
			try {
				theSocket = theServer.accept();
				// Ŭ���̾�Ʈ�� ���ӿ�û�� ��ٸ���
				// Ŭ���̾�Ʈ�� ���ϰ� ����� ���� ���� ����(theSocket)�� �����Ѵ�.
				is = theSocket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is));
				OutputStream os = theSocket.getOutputStream();
				// Ŭ���̾�Ʈ�� �����͸� ������ OutputStream ��ü�� �����Ѵ�.	
				writer = new BufferedWriter(new OutputStreamWriter(os));
				// Ŭ���̾�Ʈ�� �����͸� �����ϴ� BufferedWriter ��ü�� �����Ѵ�.
					
				Date now = new Date(); // ��¥�� ���Ѵ�.
				
				writer.write(now.toString() + "\r\n"); // ��¥�� �����Ѵ�.
				writer.flush();
				
				String theThankYou = reader.readLine(); // ��¥�� �д´�
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
