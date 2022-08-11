package chapter11;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ex11_1_server  extends Frame implements ActionListener {
	private TextField enter;
	private TextArea display;
	private DatagramPacket sendPacket, receivePacket;;
	private DatagramSocket socket;
	
	public ex11_1_server() {
		super("서버");
		enter = new TextField(" 메시지를 입력하세요" );
		enter.addActionListener(this); // 입력된 데이터를 전송하기 위한 이벤트
		add( enter, BorderLayout.NORTH);
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		addWindowListener(new WinListener());
		setSize(400, 300);
		setVisible(true);
		try {
			socket = new DatagramSocket(5000); // 서버에서 사용되는 포트번호(5000)
		} catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	public void waitForPackets() {
		while(true) {
			try { // 수신용 패킷을 만든다.
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket( data, data.length );
				socket.receive( receivePacket ); // 패킷의 수신을 기다린다.
				display.append("\n 수신된 패킷 : " +
						"\n클라이언트 주소 : " + receivePacket.getAddress() +
						"\n클라이언트 포트번호 : " + receivePacket.getPort() +
						"\n메시지 길이 : " + receivePacket.getLength() +
						"\n메시지 : " + new String( receivePacket.getData()));
				display.append("\n");
				// 수신받은 패킷을 다시 클라이언트에 전송한다.
				//display.append("\n\n클라이언트로 다시 전송(Echo data)...)");
				//sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), receivePacket.getPort() );
				//socket.send( sendPacket ); // 이에서 만들어진 패킷을 전송한다.
				//display.append("패킷 전송 완료\n");
			} catch( IOException exception) {
				display.append( exception.toString() + "\n" );
				exception.printStackTrace();
			}
		}
	}
	public void actionPerformed( ActionEvent e) {
		try {
			display.append("\n송신 메시지 : " + e.getActionCommand() + "\n");
			String s = e.getActionCommand(); // 서버에 보낼 데이터를 구한다.(텍스트필드)
			byte data[] = s.getBytes(); // 문자열을 바이트 배열로 변환한다.
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 4000);
			socket.send( sendPacket );
			display.append("패킷 전송 완료\n");
			enter.setText(" ");
		} catch(IOException exception) {
			display.append( exception.toString() + "\n");
			exception.printStackTrace();
		}
	}
	public static void main(String args[]) {
		ex11_1_server s = new ex11_1_server();
		s.waitForPackets();
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}