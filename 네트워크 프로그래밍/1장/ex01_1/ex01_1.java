package chapter1;
import java.io.*;
import java.io.FileInputStream;
import java.util.Scanner;

public class ex01_1 {
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		
		int bytesRead;
		byte[] buffer = new byte[256];
		FileInputStream fin = null;
		for(int i = 0; i < 2; i++) {
			System.out.print("파일 이름을 입력하세요 : ");
			String name = in.next();
			
			try {
				System.out.println("파일 이름 : " + name);
				System.out.print("파일 내용 : ");
				fin = new FileInputStream(name);
				while((bytesRead = fin.read(buffer)) >= 0) {
					System.out.write(buffer, 0, bytesRead);
					System.out.println("\n======");
				}
			} catch(IOException e) {
				System.err.println("스트림으로부터 데이터를 읽을 수 없습니다.");
				System.out.print("======");
			} finally {
				try {
					if(fin != null) fin.close();
				} catch(IOException e) {}
			}
		}
	}
}