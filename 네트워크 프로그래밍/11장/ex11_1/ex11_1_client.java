package chapter11;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ex11_1_client extends Frame implements ActionListener {
	private TextField enter;
	private TextArea display;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	
	public ex11_1_client() {
		super( "Ŭ���̾�Ʈ" );
		enter = new TextField(" �޽����� �Է��ϼ���" );
		enter.addActionListener(this); // �Էµ� �����͸� �����ϱ� ���� �̺�Ʈ
		add( enter, BorderLayout.NORTH);
		display = new TextArea();
		add( display, BorderLayout.CENTER);
		addWindowListener(new WinListener());
		setSize(400, 300);
		setVisible( true );
		try {
			socket = new DatagramSocket(4000); // Ŭ���̾�Ʈ�� ����ϴ� ��Ʈ��ȣ(4000)
		} catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	public void waitForPackets() {
		while(true) {
			try { // ���ŵ� ��Ŷ�� �����.
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket( data, data.length );
				socket.receive(receivePacket); // ��Ŷ�� ��ٸ���.
				display.append("\n ���ŵ� ��Ŷ : " +
						"\nŬ���̾�Ʈ �ּ� : " + receivePacket.getAddress() +
						"\nŬ���̾�Ʈ ��Ʈ��ȣ : " + receivePacket.getPort() +
						"\n�޽��� ���� : " + receivePacket.getLength() +
						"\n�޽��� : " + new String( receivePacket.getData()));
				display.append("\n");
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
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);
			socket.send( sendPacket );
			display.append("��Ŷ ���� �Ϸ�\n");
			enter.setText(" ");
		} catch(IOException exception) {
			display.append( exception.toString() + "\n");
			exception.printStackTrace();
		}
	}
	public static void main(String args[]) {
		ex11_1_client c = new ex11_1_client();
		c.waitForPackets();
	}
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}