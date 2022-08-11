package chapter5;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ex05_3 extends Frame implements ActionListener {
	private TextField enter;
	private TextArea contents1, contents2;
	public ex05_3() {
		super("호스트 파일 읽기");
		setLayout(new BorderLayout());
		enter = new TextField("URL를 입력하세요!");
		enter.addActionListener(this);
		add(enter, BorderLayout.NORTH);
		
		contents1 = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(contents1, BorderLayout.CENTER);
		
		contents2 = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(contents2, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		setSize(500, 500);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		URL url;
		InputStream is;
		BufferedReader input;
		String line;
		StringBuffer buffer = new StringBuffer();
		String location = e.getActionCommand(); //텍스트 필드에 입력된 URL을 구함
		try {
			contents1.setText("");
			url = new URL(location);
			URLConnection connection = url.openConnection();
			connection.connect();
			String ipt = enter.getText();
			InetAddress ip = InetAddress.getByName(new URL(ipt).getHost()); // 구글링 내용참조
			// Link : https://www.geeksforgeeks.org/finding-ip-address-of-a-url-in-java/
			contents1.append("프로토콜은 : " + url.getProtocol() + "\n");
			contents1.append("호스트는 : " + url.getHost() + "\n");
			contents1.append("ip 주소는 : " + ip + "\n");
			contents1.append("포트번호는 : " + url.getPort() + "\n"); 
			contents1.append("파일이름은 : " + url.getFile() + "\n");
			contents1.append("해쉬코드는 : " + url.hashCode() + "\n");
			
			Object o = url.getContent();
	    	if(connection.getContentType().contains("image")) {
				contents2.setText("이미지입니다.");
			} else if(connection.getContentType().contains("video")) {
				contents2.setText("비디오입니다.");
			} else if(connection.getContentType().contains("audio")) {
				contents2.setText("오디오입니다.");
			} else if(o.getClass().getName().contains("InputStream")) {
				is = url.openStream(); // location(호스트) 와 연결시키는 inputStream 객체 생성
				input = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				contents2.setText("파일을 읽는 중입니다....");
				while((line = input.readLine()) != null) // 파일 (웹 페이지를 읽는다.)
					buffer.append(line).append('\n');
					contents2.setText(buffer.toString()); // 읽은 파일을 텍스트 에리어에 출력
					input.close();
			}
		} catch(MalformedURLException mal) {
			contents2.setText("URL 형식이 잘못되었습니다.");	
		} catch(IOException io) {
			contents2.setText(io.toString());
		} catch(Exception ex) {
			contents2.setText("호스트 컴퓨터의 파일만을 열 수 있습니다.");
		}
	}
	public static void main(String args[]) {
		ex05_3 read = new ex05_3();
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}