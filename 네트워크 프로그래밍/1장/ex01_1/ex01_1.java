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
			System.out.print("���� �̸��� �Է��ϼ��� : ");
			String name = in.next();
			
			try {
				System.out.println("���� �̸� : " + name);
				System.out.print("���� ���� : ");
				fin = new FileInputStream(name);
				while((bytesRead = fin.read(buffer)) >= 0) {
					System.out.write(buffer, 0, bytesRead);
					System.out.println("\n======");
				}
			} catch(IOException e) {
				System.err.println("��Ʈ�����κ��� �����͸� ���� �� �����ϴ�.");
				System.out.print("======");
			} finally {
				try {
					if(fin != null) fin.close();
				} catch(IOException e) {}
			}
		}
	}
}