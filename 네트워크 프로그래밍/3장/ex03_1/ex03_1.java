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
		super("File 클래스 테스트");
		enter = new TextField("파일 및 디렉토리명을 입력하세요");
		enter.addActionListener( this );
		output = new TextArea();
		directoryoutput = new TextArea();
		add(enter, BorderLayout.NORTH); // 파일 이름 입력
		add(output, BorderLayout.CENTER); // 파일에 관한 정보 정보 출력
		add(directoryoutput, BorderLayout.SOUTH); // 디렉토리 및 파일 내용 출력
		addWindowListener(new WinListener());
		setSize(400, 400);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		File name = new File(e.getActionCommand()); // 텍스트 필드의 파일이름을 읽음
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 (E요일) hh시 mm분", Locale.KOREA); // 시간
		Long lastModified = name.lastModified(); // 시간
		String fileDate = sdf.format(lastModified); // 시간
		if(name.exists()) {
			output.setText( name.getName() + "이 존재한다.\n" + 
					(name.isFile() ? "파일이다.\n" : "파일이 아니다.\n" ) +
					(name.isDirectory() ? "디렉토리이다.\n" : "디렉토리가 아니다.\n") +
					(name.isAbsolute() ? "절대경로이다.\n" : "절대경로가 아니다.\n" ) +
					"마지막 수정날짜는 : " + fileDate +"\n파일의 길이는 : " + name.length() );
					/*
					"마지막 수정날짜는 : " + name.lastModified() +
					"\n파일의 길이는 : " + name.length() +
					"\n파일의 경로는 : " + name.getPath() +
					"\n절대경로는 : " + name.getAbsoluteFile() + name.getCanonicalPath() +
					"\n상위 드렉토리는 : " + name.getParent() );
					*/
					try {
						directoryoutput.setText("파일의 경로는 : " + name.getPath() +
								"\n절대경로는 : " + name.getAbsolutePath() +
								"\n정규경로는 : " + name.getCanonicalPath() +
								"\n상위 디렉토리는 : " + name.getParent() ); 
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
				directoryoutput.append("\n\n디렉토리의 내용은 :\n");
				for(int i = 0; i < directory.length; i++) 
					directoryoutput.append(directory[i] + "\n");
			}
		}
		else {
			output.setText( e.getActionCommand() + " 은 존재하지 않는다.\n");
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
