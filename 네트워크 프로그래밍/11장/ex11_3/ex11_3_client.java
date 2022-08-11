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
			socket = new DatagramSocket(CLIENTPORT); // 서버 연결 socket 생성
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
					
				 socket.receive(incoming); // 메세지 전달
				 String msg = new String(buffer, 0, incoming.getLength()); // 스트링으로 변경
				 System.out.println("client : " + msg); // 서버 출력
				 
				 reader = new BufferedReader(new InputStreamReader(System.in)); // 값 받기
				 line = reader.readLine(); // line에 값 넣기
				 byte[] databyte = line.getBytes(); // 바이트 데이터로 변환한다.
				 DatagramPacket outcoming = new DatagramPacket(databyte, databyte.length, InetAddress.getLocalHost(), SERVERPORT); // 데이터 닮을 packet 생성
				 socket.send(outcoming); // 서버 값 보내기
				 String data = new String(outcoming.getData()); // 값 스트링으로 변경
				 System.out.println("client : " + data); // 클라이언트에서 출력
				 Arrays.fill(databyte, (byte)0);
				 
				 socket.receive(incoming); // 메세지 전달
				 msg = new String(buffer, 0, incoming.getLength()); // 스트링으로 변경
				 System.out.println("client : " + msg + "\n"); // 서버 출력
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
