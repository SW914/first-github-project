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
		super("����");
		enter = new TextField(" �޽����� �Է��ϼ���" );
		enter.addActionListener(this); // �Էµ� �����͸� �����ϱ� ���� �̺�Ʈ
		add( enter, BorderLayout.NORTH);
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		addWindowListener(new WinListener());
		setSize(400, 300);
		setVisible(true);
		try {
			socket = new DatagramSocket(5000); // �������� ���Ǵ� ��Ʈ��ȣ(5000)
		} catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	public void waitForPackets() {
		while(true) {
			try { // ���ſ� ��Ŷ�� �����.
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket( data, data.length );
				socket.receive( receivePacket ); // ��Ŷ�� ������ ��ٸ���.
				display.append("\n ���ŵ� ��Ŷ : " +
						"\nŬ���̾�Ʈ �ּ� : " + receivePacket.getAddress() +
						"\nŬ���̾�Ʈ ��Ʈ��ȣ : " + receivePacket.getPort() +
						"\n�޽��� ���� : " + receivePacket.getLength() +
						"\n�޽��� : " + new String( receivePacket.getData()));
				display.append("\n");
				// ���Ź��� ��Ŷ�� �ٽ� Ŭ���̾�Ʈ�� �����Ѵ�.
				//display.append("\n\nŬ���̾�Ʈ�� �ٽ� ����(Echo data)...)");
				//sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), receivePacket.getPort() );
				//socket.send( sendPacket ); // �̿��� ������� ��Ŷ�� �����Ѵ�.
				//display.append("��Ŷ ���� �Ϸ�\n");
			} catch( IOException exception) {
				display.append( exception.toString() + "\n" );
				exception.printStackTrace();
			}
		}
	}
	public void actionPerformed( ActionEvent e) {
		try {
			display.append("\n�۽� �޽��� : " + e.getActionCommand() + "\n");
			String s = e.getActionCommand(); // ������ ���� �����͸� ���Ѵ�.(�ؽ�Ʈ�ʵ�)
			byte data[] = s.getBytes(); // ���ڿ��� ����Ʈ �迭�� ��ȯ�Ѵ�.
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 4000);
			socket.send( sendPacket );
			display.append("��Ŷ ���� �Ϸ�\n");
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