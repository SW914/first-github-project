package chapter5;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class ex05_2 extends Frame implements ActionListener {
	TextField hostname; //호스트 이름을 입력받는 필드
	Button getinfor; //입력된 회스트에 관한 IP정보를 읽는 필드
	TextArea display, ipdisplay; //구해진 IP에 관한 정보를 출력하는 필드
	
	public static void main(String args[]) {
		ex05_2 host = new ex05_2("InetAddress 클래스");
		host.setVisible(true);
	}

	public ex05_2(String str) {
		super(str);
		addWindowListener(new WinListener());
		setLayout(new BorderLayout());
		
		Panel inputpanel = new Panel(); //첫번째 패널
		inputpanel.setLayout(new BorderLayout());
		inputpanel.add("North", new Label("호스트 이름: "));
		hostname = new TextField("",30);
		getinfor = new Button("호스트 정보 얻기");
		
		inputpanel.add("Center", hostname);
		inputpanel.add("South", getinfor);
		getinfor.addActionListener(this);//이벤트 등록
		add("North", inputpanel); //패널을 프레임에 부착
			
		Panel outputpanel = new Panel(); //두 번째 패널
		outputpanel.setLayout(new BorderLayout());
		display = new TextArea("", 10, 40);
		display.setEditable(false);
		outputpanel.add("North", new Label("인터넷 주소"));
		outputpanel.add("Center", display);
		Panel ipclasspanel = new Panel();
		ipclasspanel.setLayout(new BorderLayout());
		ipdisplay = new TextArea("", 10, 10);
		ipdisplay.setEditable(false);
		ipclasspanel.add("North", new Label("IP CLASS"));		    
		ipclasspanel.add("Center", ipdisplay);
		add("Center", outputpanel);
		add("South", ipclasspanel);
		    
		setSize(270, 500);
	}
		
	public void actionPerformed(ActionEvent e) {
		String name = hostname.getText(); // 입력된 호스트 이름을 구한다.
		try {
			InetAddress inet[] = InetAddress.getAllByName(name); // InetAddress 객체 생성
			String ip = null;
			display.setText("");
			ipdisplay.setText("");
			for(int i = 0; i < inet.length; i++) {
				ip = inet[i].getCanonicalHostName() + "\n"; // 도메인의 ip 주소를 가져온다
				display.append(ip);
			}
			ip = String.valueOf(ipClass(inet[0].getAddress())); // ip 주소의 클래스 유형 함수 호출
			ipdisplay.append(ip); // 2번째 패널에 대표 클래스 유형 출력
		} catch(UnknownHostException ue) {
			String ip = name +": 해당 호스트가 없습니다.\n";
			display.setText(ip);
		}
	}
	static char ipClass(byte[] ip) {
		int highByte = 0xff & ip[0];
		return(highByte < 128) ? 'A' : (highByte < 192)
				? 'B' : (highByte < 224)
				? 'C' : (highByte < 240)
				? 'D' : 'E';
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}