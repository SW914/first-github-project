package chapter5;

import java.io.*;
import java.net.*;

public class ex05_1 {
	public static void main(String args[]) {
		String hostname;
		BufferedReader br;
		printLocalAddress();
		br = new BufferedReader(new InputStreamReader(System.in));
		try {
			do {
				System.out.println("ȣ��Ʈ �̸� �� IP �ּҸ� �Է��ϼ���.");
				if((hostname = br.readLine()) != null)
					printRemoteAddress(hostname);
			} while(hostname != null);
			System.out.println("���α׷��� �����մϴ�.");
		} catch(IOException ex) {
			System.out.println("�Է� ����!");
		}
	}
	private static void printLocalAddress() {
		try {
			InetAddress myself = InetAddress.getLocalHost();
			System.out.println("���� ȣ��Ʈ �̸� : " + myself.getHostName());
			System.out.println("���� IP �ּ� : " + myself.getHostAddress());
			System.out.println("���� ȣ��Ʈ class : " + ipClass(myself.getAddress()));
			System.out.println("���� ȣ��Ʈ InetAddress : " + myself.toString()); 
			System.out.println("���� ȣ��Ʈ ������ �ּ� : " + InetAddress.getLoopbackAddress()); // ������ �ּ� ���
		} catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	static void printRemoteAddress(String hostname) {
		try {
			System.out.println("ȣ��Ʈ�� ã�� �ֽ��ϴ�. " + hostname + "....");
			InetAddress machine1 = InetAddress.getByName(hostname);
			
			System.out.println("���� ȣ��Ʈ �̸� : " + machine1.getHostName());
			System.out.println("���� ȣ��Ʈ IP : " + machine1.getHostAddress());
			System.out.println("���� ȣ��Ʈ class : " + ipClass(machine1.getAddress()));
			System.out.println("���� ȣ��Ʈ InetAddress : " + machine1.toString());
			
			System.out.println("-----------------------------------------");
			InetAddress[] iad = InetAddress.getAllByName(hostname);
			for(int i = 0; i < iad.length; i++) { // iad �迭 ũ�⸸ŭ �ּҸ� ���
				System.out.println("���� ȣ��Ʈ �ּ� : " + iad[i].toString());
			}
			System.out.println("-----------------------------------------");
			
			byte[] ipAddr = machine1.getAddress(); // InetAddress getByAddress �޼ҵ� ����ϱ� ���� �迭 ����
			InetAddress machine2 = InetAddress.getByAddress(ipAddr);
			
			if(machine1.equals(machine2) == true)
				System.out.println("����.");
			else
				System.out.println("�ٸ���.");
				
		} catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	
	static char ipClass(byte[] ip) {
		int highByte = 0xff & ip[0];
		return(highByte < 128) ? 'A' : (highByte < 192)
				? 'B' : (highByte < 224)
				? 'C' : (highByte < 240)
				? 'D' : 'E';
	}
}
// boolean	equals(Object obj)
// getAllByName(String host)