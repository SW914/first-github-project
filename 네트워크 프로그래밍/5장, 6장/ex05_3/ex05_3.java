package chapter5;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ex05_3 extends Frame implements ActionListener {
	private TextField enter;
	private TextArea contents1, contents2;
	public ex05_3() {
		super("ȣ��Ʈ ���� �б�");
		setLayout(new BorderLayout());
		enter = new TextField("URL�� �Է��ϼ���!");
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
		String location = e.getActionCommand(); //�ؽ�Ʈ �ʵ忡 �Էµ� URL�� ����
		try {
			contents1.setText("");
			url = new URL(location);
			URLConnection connection = url.openConnection();
			connection.connect();
			String ipt = enter.getText();
			InetAddress ip = InetAddress.getByName(new URL(ipt).getHost()); // ���۸� ��������
			// Link : https://www.geeksforgeeks.org/finding-ip-address-of-a-url-in-java/
			contents1.append("���������� : " + url.getProtocol() + "\n");
			contents1.append("ȣ��Ʈ�� : " + url.getHost() + "\n");
			contents1.append("ip �ּҴ� : " + ip + "\n");
			contents1.append("��Ʈ��ȣ�� : " + url.getPort() + "\n"); 
			contents1.append("�����̸��� : " + url.getFile() + "\n");
			contents1.append("�ؽ��ڵ�� : " + url.hashCode() + "\n");
			
			Object o = url.getContent();
	    	if(connection.getContentType().contains("image")) {
				contents2.setText("�̹����Դϴ�.");
			} else if(connection.getContentType().contains("video")) {
				contents2.setText("�����Դϴ�.");
			} else if(connection.getContentType().contains("audio")) {
				contents2.setText("������Դϴ�.");
			} else if(o.getClass().getName().contains("InputStream")) {
				is = url.openStream(); // location(ȣ��Ʈ) �� �����Ű�� inputStream ��ü ����
				input = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				contents2.setText("������ �д� ���Դϴ�....");
				while((line = input.readLine()) != null) // ���� (�� �������� �д´�.)
					buffer.append(line).append('\n');
					contents2.setText(buffer.toString()); // ���� ������ �ؽ�Ʈ ����� ���
					input.close();
			}
		} catch(MalformedURLException mal) {
			contents2.setText("URL ������ �߸��Ǿ����ϴ�.");	
		} catch(IOException io) {
			contents2.setText(io.toString());
		} catch(Exception ex) {
			contents2.setText("ȣ��Ʈ ��ǻ���� ���ϸ��� �� �� �ֽ��ϴ�.");
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