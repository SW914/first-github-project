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
			theSocket = new Socket(host, 7); // echo ������ �����Ѵ�.
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
			System.out.println("������ ������ �Է��Ͻʽÿ�.");
			while(true) {
				theLine = userInput.readLine(); // �����͸� �Է��Ѵ�.
				
				if(theLine.equals("quit")) break;
				
				writer.write(theLine + '\r' + '\n');
				writer.flush(); // ������ ������ ����
				System.out.println(reader.readLine());
			}
		} catch(UnknownHostException e) {
			System.out.println(host + " ȣ��Ʈ�� ã�� �� �����ϴ�.");
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
			host = args[0]; // ���� ȣ��Ʈ�� �Է¹���
		}
		else {
			host = "localhost"; // �ΰ� ȣ��Ʈ�� ���� ȣ��Ʈ�� ���
		}
		ex07_2_client client = new ex07_2_client();
		client.start();
		
	}
}