package charter4;
import java.io.*;

public class ex04_1 {
	public static void main(String args[]) {
		String buf;
		//FileInputStream fin = null;	
		//FileOutputStream fout = null;
		FileReader fin = null;
		FileWriter fout = null;
		LineNumberReader reader = null;
		if(args.length != 2) {
			System.out.println("�ҽ����� �� ��������� �����Ͻʽÿ�.");
			System.exit(1);
		}
		try {
			//fin = new FileInputStream(args[0]); // �ҽ� ���ϰ� ����� �Է� ���� ��Ʈ��
			//fout = new FileOutputStream(args[1]); // ��� ���ϰ� ����� �Է� ���� ��Ʈ��
			//fin = new FileReader(args[0]);
			reader = new LineNumberReader(new FileReader(args[0]));
			fout = new FileWriter(args[1]);
		} catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		//BufferedReader read = new BufferedReader(new InputStreamReader(fin));
		BufferedReader read = new BufferedReader(reader);
		PrintWriter write = new PrintWriter(fout);
		
		int num = 1;
		while(true) {
			try {
				buf = reader.readLine();
				if(buf==null) break;
			} catch(IOException e) {
				System.out.println(e);
				break;
			}
			buf = num + " : " + buf;
			write.println(buf);
			num++;
		}
		try {
			reader.close();
			fout.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
}
