package charter4;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class ex04_3 extends Frame implements ActionListener {
	private TextField fileinput1, fileinput2;
	TextArea output;
	private Button compare;
	public ex04_3() {
		super(" File Ŭ���� ��" );
		setSize(400, 300);
		setLayout(new FlowLayout());
		
		add(new Label("ù��° ���� �Է�"));
		fileinput1 = new TextField(20);
		add(fileinput1);
		
		add(new Label("�ι�° ���� �Է�"));
		fileinput2 = new TextField(20);
		add(fileinput2);
		
		output = new TextArea(10,40);
		add(output);
		
		compare = new Button("��");
		compare.addActionListener(this);
		add(compare);
		
		
		addWindowListener(new WinListener());
		setVisible(true);
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == compare)
			compareFile();
		
	}
	
	public void compareFile() {
		boolean flag = true;
		String buf1 = null, buf2 = null;
		FileReader fin1 = null;
		FileReader fin2 = null;
		File file1 = null;
		File file2 = null;
		
		try {
			fin1 = new FileReader(fileinput1.getText());
			fin2 = new FileReader(fileinput2.getText());
			
			LineNumberReader read1 = new LineNumberReader(fin1);
			LineNumberReader read2 = new LineNumberReader(fin2);
			
			try {
				buf1 = read1.readLine();
				buf2 = read2.readLine();
				
			} catch(IOException e1) {
				System.out.println(e1);
			}
			if(buf1.equals(buf2) == false) {
				flag = false;
			}
			
			try {
				fin1.close();
				fin2.close();
			} catch(IOException e2) {
				System.out.println(e2);
			}
	      
			file1 = new File(fileinput1.getText());
			file2 = new File(fileinput2.getText());
	      
			if(flag == true) {
				Long lastModified1 = file1.lastModified();
				Long lastModified2 = file2.lastModified();
				Date date1 = new Date(lastModified1);
				Date date2 = new Date(lastModified2);
				output.setText(("�� ���� ������ �����ϴ�.") +
						("\n" + fileinput1.getText() + " ���� ���� �ð� : " + date1) +
						("\n" + fileinput2.getText() + " ���� ���� �ð� : " + date2) );
			}
			else {
				if (file1.exists() && file2.exists()) {
					long L1 = file1.length();
					long L2 = file2.length();
					output.setText( ("�� ���� ������ ���� �ʽ��ϴ�.") +
							("\n" + fileinput1.getText() + " ������ ���� : "+ L1 + " bytes ") +
							("\n" + fileinput2.getText() + " ������ ���� : "+ L2 + " bytes ") );
				}
				else if(file1.exists() && !file2.exists()) {
					long L1 = file2.length();
					output.setText( ("�� ���� ������ ���� �ʽ��ϴ�.") +
							("\n" + fileinput1.getText() + " ������ ���� : "+ L1 + " bytes ") +
							("\n" + fileinput2.getText() + "������ �����ϴ�.") );
				}
				else if(!file1.exists() && file2.exists()) {
					long L2 = file2.length();
					output.setText( ("�� ���� ������ ���� �ʽ��ϴ�.") +
							("\n" + fileinput1.getText() + "������ �����ϴ�.") +
							("\n" + fileinput2.getText() + " ������ ���� : "+ L2 + " bytes ") );
				}
				else {
					output.setText( ("�� ���� ������ ���� �ʽ��ϴ�.") +
							("\n" + fileinput1.getText() + "������ �����ϴ�.") +
							("\n" + fileinput2.getText() + "������ �����ϴ�.") );
				}
			}
		} catch(FileNotFoundException e3) {
			System.err.println("�ùٸ� ���ϸ��� �Է����ּ���.");
			System.err.println(e3.toString());
			output.setText("");
		}
	}		
	public static void main(String args[]) {
		ex04_3 f = new ex04_3();
	}
}