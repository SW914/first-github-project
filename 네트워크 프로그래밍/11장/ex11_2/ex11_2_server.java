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
		String o = new String("�����Դϴ�. ����Ͻðڽ��ϱ�?(y/n)");
		String x = new String("�����Դϴ�. ����Ͻðڽ��ϱ�?(y/n)");
		
		if(a.trim().equals(b)) {
			try {
				byte[] buffer = new byte[BUFFER_SIZE];
				
				DatagramPacket outcoming = new DatagramPacket(o.getBytes(), o.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT); // ������ ���
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length); 
				ds.send(outcoming); // �� ������
				data = new String(outcoming.getData()); // byte ���� String���� ����
				System.out.println("server : " + data); // ������ data ���
				
				Arrays.fill(buffer, (byte)0); // �迭 �ʱ�ȭ
				ds.receive(incoming); // �� �ޱ�
				data = new String(incoming.getData()); // byte ���� String���� ����
				System.out.println("server : " + data); // ������ data ���
			} catch(UnknownHostException ex) {
				System.out.println(ex);
			} catch(IOException e) {
				System.out.println(e);	
			}
		}
		else {
			try {
				byte[] buffer = new byte[BUFFER_SIZE]; 
				
				DatagramPacket outcoming = new DatagramPacket(x.getBytes(), x.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT); // Ʋ���� ���
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length); 
				ds.send(outcoming);
				data = new String(outcoming.getData());
				System.out.println("server : " + data); // �����Դϴ�. ����Ͻðڽ��ϱ�?(y/n)
				
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
		String quizstart  = "��� �����մϴ�(y/n)";	
		String quizquit   = "quit";		
		String[] quiz = {"�������� ���� ȸ���?", "�����ø� ���� ȸ���?", "ȫ�̸� ���� ȸ���?"};
		String[] answer = {"apple", "samsung", "xiaomi"};
		int i = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			DatagramPacket outcoming = new DatagramPacket(quizstart.getBytes(), quizstart.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
			ds.send(outcoming);
			data = new String(outcoming.getData()); // ��� �����մϴ�(y/n)
			System.out.println("server : " + data);
				
			Arrays.fill(buffer, (byte)0);
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			ds.receive(incoming);
			data = new String(incoming.getData());
			System.out.println("server : " + data); // y, n �� �Է� ����
			do {
				if(data.trim().equals("y")) {
					outcoming = new DatagramPacket(quiz[i].getBytes(), quiz[i].getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
					ds.send(outcoming);
					data = new String(outcoming.getData()); // ���� ���� ����
					System.out.println("server : " + data);
					
					Arrays.fill(buffer, (byte)0);
					incoming = new DatagramPacket(buffer, buffer.length);
					ds.receive(incoming);
					data = new String(incoming.getData());
					System.out.println("server : " + data); // �� �ް�
					
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
					System.out.println("server : y �Ǵ� n�� �Է��� ���� �ʾƼ� ���α׷��� ����˴ϴ�."); // quit
					break;
				}	
				if(i==3) {
					outcoming = new DatagramPacket(quizquit.getBytes(), quizquit.getBytes().length, InetAddress.getLocalHost(), CLIENTPORT);
					ds.send(outcoming);
					data = new String(outcoming.getData());
					System.out.println("server : ������ �ٴ��� �����ϴ�.");
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