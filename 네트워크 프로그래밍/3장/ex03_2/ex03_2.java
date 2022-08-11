package chapter3;
import java.io.*;

public class ex03_2 {
	static FileInputStream fileone;
	static FileInputStream filetwo;
	public static void main(String[] args) throws java.io.IOException {
		String first = null, second = null;
		byte[] buffer = new byte[80];
		@SuppressWarnings("unused")
		int numberRead;
				
		System.out.print("ù ��° ���� �̸��� �Է��ϼ��� : ");
		numberRead = System.in.read(buffer);
		first = new String(buffer);
		first = first.trim();
		
		System.out.print("�� ��° ���� �̸��� �Է��ϼ��� : ");
		numberRead = System.in.read(buffer);
		second = new String(buffer);
		second = second.trim();
		
		System.out.println(first + "�� " + second + "�� ���մϴ�.");
		
		try {
			fileone = new FileInputStream(first);
			filetwo = new FileInputStream(second);
			
			if(compareFile(fileone, filetwo))
				System.out.println("������ �����ϴ�.");
			else
				System.out.println("������ �ٸ��ϴ�.");
			
			fileone.close();
			filetwo.close();
		} catch(IOException e) {
			System.out.println("����� ������ �߻��߽��ϴ�.");
		}
	}
	
	private static boolean compareFile(FileInputStream one, FileInputStream two) throws IOException {
		byte[] buf1 = new byte[1024]; // ����1
		byte[] buf2 = new byte[1024]; // ����2
		int count1 = 0, count2;
		
		while(true) {
			count1 = one.read(buf1, 0, buf1.length); // ���� ũ�⸸ŭ �о
			count2 = two.read(buf2, 0, buf2.length);
			if(count1 != count2) return false; // ũ�Ⱑ �ٸ��� ������ �ٸ�
			if(count1 == -1) break; // ���� ����
			for(int i = 0; i < count1; i++) { // ���� ��
				if(buf1[i] != buf2[i]) return false;
			}
		}
		return true;
	}
}