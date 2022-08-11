package chapter2;
import java.io.*;
public class ex02_4_1 {
	public static void main(String[] args) {
		ex02_4_2 over = null;
		FileInputStream inf = null;
		
		try {
			inf = new FileInputStream(args[0]);
			over = new ex02_4_2(new FileOutputStream("copier1.txt"), new FileOutputStream("copier2.txt"));
			
			ex02_4_2.copy(inf, over);
		}
		catch(FileNotFoundException e) {
			System.err.println(e.toString());
		} catch(IOException e) {
			System.err.println(e.toString());
		} finally {
			try {
				if (inf != null)
					inf.close();
				if (over != null)
					over.close();
			} catch(IOException e) {}
		}
	}
}