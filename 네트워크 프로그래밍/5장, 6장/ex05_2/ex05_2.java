package chapter5;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class ex05_2 extends Frame implements ActionListener {
	TextField hostname; //ȣ��Ʈ �̸��� �Է¹޴� �ʵ�
	Button getinfor; //�Էµ� ȸ��Ʈ�� ���� IP������ �д� �ʵ�
	TextArea display, ipdisplay; //������ IP�� ���� ������ ����ϴ� �ʵ�
	
	public static void main(String args[]) {
		ex05_2 host = new ex05_2("InetAddress Ŭ����");
		host.setVisible(true);
	}

	public ex05_2(String str) {
		super(str);
		addWindowListener(new WinListener());
		setLayout(new BorderLayout());
		
		Panel inputpanel = new Panel(); //ù��° �г�
		inputpanel.setLayout(new BorderLayout());
		inputpanel.add("North", new Label("ȣ��Ʈ �̸�: "));
		hostname = new TextField("",30);
		getinfor = new Button("ȣ��Ʈ ���� ���");
		
		inputpanel.add("Center", hostname);
		inputpanel.add("South", getinfor);
		getinfor.addActionListener(this);//�̺�Ʈ ���
		add("North", inputpanel); //�г��� �����ӿ� ����
			
		Panel outputpanel = new Panel(); //�� ��° �г�
		outputpanel.setLayout(new BorderLayout());
		display = new TextArea("", 10, 40);
		display.setEditable(false);
		outputpanel.add("North", new Label("���ͳ� �ּ�"));
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
		String name = hostname.getText(); // �Էµ� ȣ��Ʈ �̸��� ���Ѵ�.
		try {
			InetAddress inet[] = InetAddress.getAllByName(name); // InetAddress ��ü ����
			String ip = null;
			display.setText("");
			ipdisplay.setText("");
			for(int i = 0; i < inet.length; i++) {
				ip = inet[i].getCanonicalHostName() + "\n"; // �������� ip �ּҸ� �����´�
				display.append(ip);
			}
			ip = String.valueOf(ipClass(inet[0].getAddress())); // ip �ּ��� Ŭ���� ���� �Լ� ȣ��
			ipdisplay.append(ip); // 2��° �гο� ��ǥ Ŭ���� ���� ���
		} catch(UnknownHostException ue) {
			String ip = name +": �ش� ȣ��Ʈ�� �����ϴ�.\n";
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