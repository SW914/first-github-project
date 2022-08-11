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
				
		System.out.print("첫 번째 파일 이름을 입력하세요 : ");
		numberRead = System.in.read(buffer);
		first = new String(buffer);
		first = first.trim();
		
		System.out.print("두 번째 파일 이름을 입력하세요 : ");
		numberRead = System.in.read(buffer);
		second = new String(buffer);
		second = second.trim();
		
		System.out.println(first + "과 " + second + "를 비교합니다.");
		
		try {
			fileone = new FileInputStream(first);
			filetwo = new FileInputStream(second);
			
			if(compareFile(fileone, filetwo))
				System.out.println("파일이 같습니다.");
			else
				System.out.println("파일이 다릅니다.");
			
			fileone.close();
			filetwo.close();
		} catch(IOException e) {
			System.out.println("입출력 오류가 발생했습니다.");
		}
	}
	
	private static boolean compareFile(FileInputStream one, FileInputStream two) throws IOException {
		byte[] buf1 = new byte[1024]; // 버퍼1
		byte[] buf2 = new byte[1024]; // 버퍼2
		int count1 = 0, count2;
		
		while(true) {
			count1 = one.read(buf1, 0, buf1.length); // 버퍼 크기만큼 읽어서
			count2 = two.read(buf2, 0, buf2.length);
			if(count1 != count2) return false; // 크기가 다르면 파일이 다름
			if(count1 == -1) break; // 끝에 도달
			for(int i = 0; i < count1; i++) { // 각각 비교
				if(buf1[i] != buf2[i]) return false;
			}
		}
		return true;
	}
}