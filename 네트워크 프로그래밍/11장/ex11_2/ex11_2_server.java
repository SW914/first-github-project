package chapter11;
import java.net.*;
import java.util.Arrays;
import java.io.*;

public class ex11_2_server extends Thread {
	public static final int CLIENTPORT = 10;
	public static final int SERVERPORT = 7;
	public static final int BUFFER_SIZE = 256;
	protected DatagramSocket ds;
	String data = null;
	
	public ex11_2_server() {
		try {
			ds = new DatagramSocket(SERVERPORT);	
		} catch(SocketException se){
			se.printStackTrace();
			System.exit(0);
		}
	}
	
	public void comparison(String a, String b) {
		String o = new String("정답입니다. 계속하시겠습니까?(y/n)");
		String x = new String("오답입니다. 계속하시겠습니까?(y/n)");
		
		if(a.trim().equals(b)) {
			try {
				byte[] buffer = new byte[BUFFER_SIZE];
				
				DatagramPacket outcoming = new DatagramPacket(o.getBytes(), o.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT); // 맞으면 출력
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length); 
				ds.send(outcoming); // 값 보내기
				data = new String(outcoming.getData()); // byte 문자 String으로 변경
				System.out.println("server : " + data); // 서버에 data 출력
				
				Arrays.fill(buffer, (byte)0); // 배열 초기화
				ds.receive(incoming); // 값 받기
				data = new String(incoming.getData()); // byte 문자 String으로 변경
				System.out.println("server : " + data); // 서버에 data 출력
			} catch(UnknownHostException ex) {
				System.out.println(ex);
			} catch(IOException e) {
				System.out.println(e);	
			}
		}
		else {
			try {
				byte[] buffer = new byte[BUFFER_SIZE]; 
				
				DatagramPacket outcoming = new DatagramPacket(x.getBytes(), x.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT); // 틀리면 출력
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length); 
				ds.send(outcoming);
				data = new String(outcoming.getData());
				System.out.println("server : " + data); // 정답입니다. 계속하시겠습니까?(y/n)
				
				Arrays.fill(buffer, (byte)0);
				ds.receive(incoming);
				data = new String(incoming.getData());
				System.out.println("server : " + data); // y // ios
			} catch(UnknownHostException ex) {
				System.out.println(ex);
			} catch(IOException e) {
				System.out.println(e);	
			}
		}
	}
	public void run() {
		String quizstart  = "퀴즈를 시작합니다(y/n)";	
		String quizquit   = "quit";		
		String[] quiz = {"아이폰을 만든 회사는?", "갤럭시를 만든 회사는?", "홍미를 만든 회사는?"};
		String[] answer = {"apple", "samsung", "xiaomi"};
		int i = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			DatagramPacket outcoming = new DatagramPacket(quizstart.getBytes(), quizstart.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
			ds.send(outcoming);
			data = new String(outcoming.getData()); // 퀴즈를 시작합니다(y/n)
			System.out.println("server : " + data);
				
			Arrays.fill(buffer, (byte)0);
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			ds.receive(incoming);
			data = new String(incoming.getData());
			System.out.println("server : " + data); // y, n 값 입력 구문
			do {
				if(data.trim().equals("y")) {
					outcoming = new DatagramPacket(quiz[i].getBytes(), quiz[i].getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
					ds.send(outcoming);
					data = new String(outcoming.getData()); // 퀴즈 문제 제시
					System.out.println("server : " + data);
					
					Arrays.fill(buffer, (byte)0);
					incoming = new DatagramPacket(buffer, buffer.length);
					ds.receive(incoming);
					data = new String(incoming.getData());
					System.out.println("server : " + data); // 값 받고
					
					comparison(data, answer[i]);
					++i;
				}
				else if(data.trim().equals("n")) {
					outcoming = new DatagramPacket(quizquit.getBytes(), quizquit.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
					ds.send(outcoming);
					data = new String(outcoming.getData());
					
					break;
				}
				else {
					System.out.println("server : y 또는 n을 입력을 하지 않아서 프로그램이 종료됩니다."); // quit
					break;
				}	
				if(i==3) {
					outcoming = new DatagramPacket(quizquit.getBytes(), quizquit.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
					ds.send(outcoming);
					data = new String(outcoming.getData());
					System.out.println("server : 문제가 바닥이 났습니다.");
					System.out.println("server : " + data); // quit
					break;
				}
			} while(true);
		} catch(UnknownHostException ex) {
			System.out.println(ex);
		} catch(IOException e) {
			System.out.println(e);	
		}
	}
	public static void main(String args[]) {
		ex11_2_server server = new ex11_2_server();
		server.start();
	}
}	