package chapter11;
import java.net.*;
import java.util.*;
import java.io.*;
// extends 상속
public class ex11_3_server extends Thread {
	public static final int CLIENTPORT = 1500;
	public static final int SERVERPORT = 1600;
	public static final int BUFFER_SIZE = 256;
	protected DatagramSocket ds;
	
	public ex11_3_server() {
		try {
			ds = new DatagramSocket(SERVERPORT);
		} catch(SocketException se){
			se.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		String msg = "영어단어를 입력해주세요! 나가려면 exit를 쳐주세요!";
		byte[] buffer = new byte[BUFFER_SIZE];
		String[] str = null;
		String line = null;
		int i = 0;
		
		try {
			while(true) {
				FileInputStream fin = new FileInputStream("text.txt");
				InputStreamReader isr = new InputStreamReader(fin, "UTF-8");
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(isr);
				
				DatagramPacket outcoming = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
				ds.send(outcoming);
				String data = new String(outcoming.getData());
				System.out.println("server : " + data); // 서버에서 출력
				
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), CLIENTPORT);
				ds.receive(incoming);
				data = new String(incoming.getData());
				System.out.println("server : " + data); // 서버에서 출력
				Arrays.fill(buffer, (byte)0); // y // ios // yos
				
				while((line = br.readLine()) != null) { // 나무 wood
					str = line.split(" ");
					if(data.trim().equals(str[i])) { 
						outcoming = new DatagramPacket(str[i+1].getBytes(), str[i+1].getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
						ds.send(outcoming);
						data = new String(outcoming.getData());
						System.out.println("server : " + data + "\n"); // 서버에서 출력
					}
					else if(data.trim().equals(str[i+1])){
						outcoming = new DatagramPacket(str[i].getBytes(), str[i].getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
						ds.send(outcoming);
						data = new String(outcoming.getData());
						System.out.println("server : " + data + "\n"); // 서버에서 출력
					}
				}
				if(data.trim().equals("exit")) {
					break;
				}
			}
		} catch(FileNotFoundException fnfe) {
			System.out.println(fnfe);
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	public static void main(String args[]) {
		ex11_3_server server = new ex11_3_server();
		server.start();
	}
}