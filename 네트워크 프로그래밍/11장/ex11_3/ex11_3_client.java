package chapter11;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class ex11_3_client extends Thread{
	public static final int CLIENTPORT = 1500;
	public static final int SERVERPORT = 1600;
	public static final int BUFFER_SIZE = 256;
	protected DatagramSocket socket;
	BufferedReader reader;
	
	public ex11_3_client() {
		try {
			socket = new DatagramSocket(CLIENTPORT); // ���� ���� socket ����
		}
		catch(SocketException se){
			se.printStackTrace();
			System.exit(0);
		}
	}
	public void run() {
		String line;
		 try {	  
			 while(true) {
				 byte[] buffer = new byte[BUFFER_SIZE];
				 Arrays.fill(buffer, (byte)0);
				 DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
					
				 socket.receive(incoming); // �޼��� ����
				 String msg = new String(buffer, 0, incoming.getLength()); // ��Ʈ������ ����
				 System.out.println("client : " + msg); // ���� ���
				 
				 reader = new BufferedReader(new InputStreamReader(System.in)); // �� �ޱ�
				 line = reader.readLine(); // line�� �� �ֱ�
				 byte[] databyte = line.getBytes(); // ����Ʈ �����ͷ� ��ȯ�Ѵ�.
				 DatagramPacket outcoming = new DatagramPacket(databyte, databyte.length, InetAddress.getLocalHost(), SERVERPORT); // ������ ���� packet ����
				 socket.send(outcoming); // ���� �� ������
				 String data = new String(outcoming.getData()); // �� ��Ʈ������ ����
				 System.out.println("client : " + data); // Ŭ���̾�Ʈ���� ���
				 Arrays.fill(databyte, (byte)0);
				 
				 socket.receive(incoming); // �޼��� ����
				 msg = new String(buffer, 0, incoming.getLength()); // ��Ʈ������ ����
				 System.out.println("client : " + msg + "\n"); // ���� ���
			 }
		 } catch(IOException e) {
				System.out.println(e);
		 }
	} 
	public static void main(String[] args) {
		ex11_3_client client = new ex11_3_client();
		client.start();
	}
}
