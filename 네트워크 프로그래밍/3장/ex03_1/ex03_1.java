package chapter3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ex03_1 extends Frame implements ActionListener {
	private TextField enter;
	private TextArea output;
	private TextArea directoryoutput;
	public ex03_1() {
		super("File Ŭ���� �׽�Ʈ");
		enter = new TextField("���� �� ���丮���� �Է��ϼ���");
		enter.addActionListener( this );
		output = new TextArea();
		directoryoutput = new TextArea();
		add(enter, BorderLayout.NORTH); // ���� �̸� �Է�
		add(output, BorderLayout.CENTER); // ���Ͽ� ���� ���� ���� ���
		add(directoryoutput, BorderLayout.SOUTH); // ���丮 �� ���� ���� ���
		addWindowListener(new WinListener());
		setSize(400, 400);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		File name = new File(e.getActionCommand()); // �ؽ�Ʈ �ʵ��� �����̸��� ����
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy�� MM�� dd�� (E����) hh�� mm��", Locale.KOREA); // �ð�
		Long lastModified = name.lastModified(); // �ð�
		String fileDate = sdf.format(lastModified); // �ð�
		if(name.exists()) {
			output.setText( name.getName() + "�� �����Ѵ�.\n" + 
					(name.isFile() ? "�����̴�.\n" : "������ �ƴϴ�.\n" ) +
					(name.isDirectory() ? "���丮�̴�.\n" : "���丮�� �ƴϴ�.\n") +
					(name.isAbsolute() ? "�������̴�.\n" : "�����ΰ� �ƴϴ�.\n" ) +
					"������ ������¥�� : " + fileDate +"\n������ ���̴� : " + name.length() );
					/*
					"������ ������¥�� : " + name.lastModified() +
					"\n������ ���̴� : " + name.length() +
					"\n������ ��δ� : " + name.getPath() +
					"\n�����δ� : " + name.getAbsoluteFile() + name.getCanonicalPath() +
					"\n���� �巺�丮�� : " + name.getParent() );
					*/
					try {
						directoryoutput.setText("������ ��δ� : " + name.getPath() +
								"\n�����δ� : " + name.getAbsolutePath() +
								"\n���԰�δ� : " + name.getCanonicalPath() +
								"\n���� ���丮�� : " + name.getParent() ); 
					} catch (IOException e3) {
						System.err.println(e3.toString());
					}
			if(name.isFile()) {
				try {
					RandomAccessFile r = new RandomAccessFile(name, "r");
					StringBuffer buf = new StringBuffer();
					String text;
					directoryoutput.append( "\n\n" );
					while( ( text = r.readLine()) != null)
						buf.append( text + "\n" );
					directoryoutput.append( buf.toString() );
				} catch(IOException e2) {}
				
			}
			else if(name.isDirectory()) {
				String directory[] = name.list();
				directoryoutput.append("\n\n���丮�� ������ :\n");
				for(int i = 0; i < directory.length; i++) 
					directoryoutput.append(directory[i] + "\n");
			}
		}
		else {
			output.setText( e.getActionCommand() + " �� �������� �ʴ´�.\n");
		}
	}

	public static void main(String args[]) {
		ex03_1 f = new ex03_1();
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}
