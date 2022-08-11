package chapter7;

import java.io.*;
import java.net.*;

public class ex07_2_server extends Thread {
	public static String theLine;
	protected ServerSocket theServer;
	protected Socket theSocket = null;
	protected InputStream is;
	protected BufferedReader reader;
	protected OutputStream os;
	protected BufferedWriter writer;
	
	
	public ex07_2_server() {
		try {
			theServer = new ServerSocket(7);
			// 7�� ��Ʈ���� Ŭ���̾�Ʈ�� ���� ��û�� ��ٸ��� �������� ��ü�� �����Ѵ�.
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void run() {
		try {	
			theSocket = theServer.accept(); // Ŭ���̾�Ʈ�� ���ӿ�û��
			// ��ٸ��� Ŭ���̾�Ʈ�� ���ϰ� ����� ���� ���� ������ �����Ѵ�.
			
			is = theSocket.getInputStream();
			// Ŭ���̾�Ʈ�� ������ �����͸� ���� InputStream ��ü�� �����Ѵ�.
			
			reader = new BufferedReader(new InputStreamReader(is));
			// Ŭ���̾�Ʈ�� ������ �����͸� ���� BufferedReader ��ü�� �����Ѵ�.
			
			os = theSocket.getOutputStream();
			// Ŭ���̾�Ʈ�� �����͸� ������ OutputStream ��ü�� �����Ѵ�.
			
			writer = new BufferedWriter(new OutputStreamWriter(os));
			// Ŭ���̾�Ʈ�� �����͸� �����ϴ� BufferedWriter ��ü�� �����Ѵ�.
			
			while((theLine = reader.readLine()) != null ) { // Ŭ���̾�Ʈ�� �����͸� ����
				System.out.println(theLine); // ���� �����͸� ȭ�鿡 ����Ѵ�.
				writer.write(theLine+'\r'+'\n'); // Ŭ���̾�Ʈ�� �����͸� ������
				writer.flush(); // Ŭ���̾�Ʈ�� �����͸� ������
			}
		} catch(UnknownHostException e) {
			System.err.println(e);
		} catch(IOException e) {
			System.err.println(e);
		} finally {
			if(theSocket != null) {
				try {
					theSocket.close();
				} catch(IOException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	public static void main(String args[]) {
		ex07_2_server server = new ex07_2_server();
		server.start();
	}
}