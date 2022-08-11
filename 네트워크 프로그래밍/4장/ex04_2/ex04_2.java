package charter4;
import java.io.*;

public class ex04_2 {
	public static void main(String args[]) throws IOException {
		String text1, text2;
		
		InputStreamReader ipstr = new InputStreamReader(System.in);
		BufferedReader bfr1 = new BufferedReader(ipstr);
		FileOutputStream fopst = new FileOutputStream("PrintWriter.txt");
		OutputStreamWriter opstw = new OutputStreamWriter(fopst);
		BufferedWriter bfw = new BufferedWriter(opstw);
		
		while((text1 = bfr1.readLine()) != null) {
			bfw.write(text1 + "\r\n");
			bfw.flush();
		}
		bfw.close();
		
		FileReader fr = new FileReader("PrintWriter.txt");
		BufferedReader bfr2 = new BufferedReader(fr);
		PrintWriter pw = new PrintWriter(System.out);
		
		while((text2 = bfr2.readLine()) != null) {
			pw.print(text2 + "\r\n");
		}
		pw.close();
		fr.close();
	}
}