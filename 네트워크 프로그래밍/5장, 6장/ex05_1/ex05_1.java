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
				System.out.println("호스트 이름 및 IP 주소를 입력하세요.");
				if((hostname = br.readLine()) != null)
					printRemoteAddress(hostname);
			} while(hostname != null);
			System.out.println("프로그램을 종료합니다.");
		} catch(IOException ex) {
			System.out.println("입력 에러!");
		}
	}
	private static void printLocalAddress() {
		try {
			InetAddress myself = InetAddress.getLocalHost();
			System.out.println("로컬 호스트 이름 : " + myself.getHostName());
			System.out.println("로컬 IP 주소 : " + myself.getHostAddress());
			System.out.println("로컬 호스트 class : " + ipClass(myself.getAddress()));
			System.out.println("로컬 호스트 InetAddress : " + myself.toString()); 
			System.out.println("로컬 호스트 루프백 주소 : " + InetAddress.getLoopbackAddress()); // 루프백 주소 출력
		} catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	static void printRemoteAddress(String hostname) {
		try {
			System.out.println("호스트를 찾고 있습니다. " + hostname + "....");
			InetAddress machine1 = InetAddress.getByName(hostname);
			
			System.out.println("원격 호스트 이름 : " + machine1.getHostName());
			System.out.println("원격 호스트 IP : " + machine1.getHostAddress());
			System.out.println("원격 호스트 class : " + ipClass(machine1.getAddress()));
			System.out.println("원격 호스트 InetAddress : " + machine1.toString());
			
			System.out.println("-----------------------------------------");
			InetAddress[] iad = InetAddress.getAllByName(hostname);
			for(int i = 0; i < iad.length; i++) { // iad 배열 크기만큼 주소를 출력
				System.out.println("원격 호스트 주소 : " + iad[i].toString());
			}
			System.out.println("-----------------------------------------");
			
			byte[] ipAddr = machine1.getAddress(); // InetAddress getByAddress 메소드 사용하기 위해 배열 선언
			InetAddress machine2 = InetAddress.getByAddress(ipAddr);
			
			if(machine1.equals(machine2) == true)
				System.out.println("같다.");
			else
				System.out.println("다르다.");
				
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