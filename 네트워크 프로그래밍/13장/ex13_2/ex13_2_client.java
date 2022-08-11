package chapter13;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

public class ex13_2_client extends Frame implements ActionListener, KeyListener {
	TextArea display;
	TextField wtext, ltext;
	Label mlbl, wlbl, loglbl;
	BufferedWriter output;
	BufferedReader input;
	Socket client;
	StringBuffer clientdata;
	String serverdata;
	String ID;
	
	Button logout;
	Panel ptotal, plabel;
	
	MulticastSocket msocket;
    DatagramPacket packet;
    InetAddress group;
    SocketAddress sadd;
	
    boolean join = false;
    boolean rejoin = true;
    
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	//private static final int REQ_WISPERSEND = 1022;
	private static final int REQ_LOGOUT = 9999;
	
    public ex13_2_client() {
    	super("Ŭ���̾�Ʈ");

    	mlbl = new Label("��Ƽĳ��Ʈ ä�� ������ ���� ��û�մϴ�!");
    	add(mlbl, BorderLayout.NORTH);

    	display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
    	display.setEditable(false);
    	add(display, BorderLayout.CENTER);

    	ptotal = new Panel(new BorderLayout());
 
    	Panel pword = new Panel(new BorderLayout());
    	wlbl = new Label("��ȭ��");
    	wtext = new TextField(30); // ������ �����͸� �Է��ϴ� �ʵ�
    	wtext.addKeyListener(this); // �Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
    	pword.add(wlbl, BorderLayout.WEST);
    	pword.add(wtext, BorderLayout.EAST);
    	ptotal.add(pword, BorderLayout.CENTER);

    	plabel = new Panel(new BorderLayout()); 	
    	loglbl = new Label("�α׿�");
    	ltext = new TextField(30); //������ �����͸� �Է��ϴ� �ʵ�
    	ltext.addActionListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
    	plabel.add(loglbl, BorderLayout.WEST);
    	plabel.add(ltext, BorderLayout.EAST);
    	ptotal.add(plabel, BorderLayout.SOUTH); // Button���� ����
    	
    	logout = new Button("�α׾ƿ�"); // �α׾ƿ� ��ư
    	logout.addActionListener(this); // �׼� �߰�
    	
    	add(ptotal, BorderLayout.SOUTH); // ��ġ �߰�

    	addWindowListener(new WinListener());
    	setSize(400, 250);
    	setVisible(true);
    }
	
    public void runClient() {
    	try {
    		client = new Socket(InetAddress.getLocalHost(), 5000); // ���� ����
	        mlbl.setText("����� �����̸� : " + client.getInetAddress().getHostName()); // ���� �� 
	        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	        clientdata = new StringBuffer(2048);
	        mlbl.setText("���� �Ϸ� ����� ���̵� �Է��ϼ���.");
	        
	        byte data[] = new byte[512]; //������ ��
	        
	        while(true) {
	        	if(!join) { // ó���� ���� �� ������
	        		serverdata = input.readLine();
	        		display.append(serverdata + "\r\n");
	    			group = InetAddress.getByName(serverdata.substring(serverdata.indexOf('/')+1,serverdata.indexOf(':')));
                    String port_str = serverdata.substring(serverdata.indexOf(':')+1,serverdata.indexOf("��"));
                    int port = Integer.parseInt(port_str); // ���ڿ��� ���ڷ� ����
                    // https://jamesdreaming.tistory.com/125 // ����
	    			msocket = new MulticastSocket(port);
                    msocket.setSoTimeout(50000);
                    sadd = new InetSocketAddress(group, port);
                    // http://cris.joongbu.ac.kr/course/java/api/java/net/InetSocketAddress.html // ����
                    msocket.joinGroup(sadd, msocket.getNetworkInterface());  
                    packet = new DatagramPacket(data, data.length, group, port);
                    
                    serverdata = input.readLine();
	        		display.append(serverdata + "\r\n");
	        		
                    join = true; // ���Ե�
                    // msocket.joinGroup(group); 			
	        	}
	        	else { // ���� ������ ��
	        		//packet.setData(new byte[512]);
	        		//packet.setLength(512);
	        		msocket.receive(packet);
                    String receive = new String(packet.getData(),0, packet.getLength());
                    display.append(receive + "\r\n");
	        	}
	        }  
    	} catch(IOException e) {
    		e.printStackTrace();
    	} 
    }
		
    public void actionPerformed(ActionEvent ae) {
    	if(ae.getSource() == logout) {
    		plabel.removeAll();
    		plabel.add(loglbl, BorderLayout.WEST);
			plabel.add(ltext, BorderLayout.EAST);
			ptotal.add(plabel, BorderLayout.SOUTH);
			ltext.setVisible(true);
			ptotal.validate();
			mlbl.setText("�α׾ƿ� �Ͽ����ϴ�.");
			try {
				clientdata.setLength(0);
	            clientdata.append(REQ_LOGOUT);
	            clientdata.append(SEPARATOR);
	            clientdata.append(ID);
	            output.write(clientdata.toString() + "\r\n");
	            output.flush();
				client.close();
				ID = null;
	
				msocket.leaveGroup(sadd,msocket.getNetworkInterface()); // ���� Ż��
				join = false; // ���� Ż��
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}		
    	}
    	else {
    		if(client == null) {
    			mlbl.setText("������ ������ �Ұ��մϴ�.");
    		}
	    	else {
	    		plabel.removeAll();
				plabel.add(logout, BorderLayout.SOUTH);
				add(ptotal, BorderLayout.SOUTH);
				ptotal.validate();
		    	try {
					client = new Socket(InetAddress.getLocalHost(), 5000);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
		    	ID = ltext.getText();
		    	mlbl.setText(ID + "(��)�� �α��� �Ͽ����ϴ�.");
		    	
				try {
		    		clientdata.setLength(0);
			        clientdata.append(REQ_LOGON);
			        clientdata.append(SEPARATOR);
			        clientdata.append(ID);
			        output.write(clientdata.toString()+"\r\n");
			        output.flush();
			        ltext.setVisible(false);
			        if(!rejoin) {
	                    msocket.joinGroup(sadd,msocket.getNetworkInterface());
	                    rejoin = true;
	                }
		    	} catch(Exception e) {
		    		e.printStackTrace();
		    	}
	    	}	
    	}
   }
	
   public static void main(String args[]) {
	   ex13_2_client c = new ex13_2_client();
	   c.runClient();
   }
		
   class WinListener extends WindowAdapter {
	   public void windowClosing(WindowEvent e) {
		   if(client.isClosed()) { // �߰�
			   try {
					client.close(); // ��������
				} catch(IOException ioe) {
					ioe.printStackTrace();
				} catch(NullPointerException ne) {
					System.exit(0); // ���α׷� ����
				}
		   }
		   else { // �߰�
			   try { // �α׾ƿ� ��ȣ
				    clientdata.setLength(0);
		            clientdata.append(REQ_LOGOUT);
		            clientdata.append(SEPARATOR);
		            clientdata.append(ID);
		            output.write(clientdata.toString()+"\r\n");
		            output.flush();
					client.close();
					ID = null;
				    client.close();
			   } catch(IOException ioe) {   
				   ioe.printStackTrace();
			   }
		   }
		   System.exit(0); // ���α׷� ����
	   }
   }

   public void keyPressed(KeyEvent ke) {
	   if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
		   //String message = wtext.getText();
		   String message = new String();
		   message = wtext.getText();
		   if (ID == null) {
			   mlbl.setText("�α��� �� �̿��ϼ���!!!");
			   wtext.setText("");
		   }
		   else {
			   try {	   
				   clientdata.setLength(0);
				   clientdata.append(REQ_SENDWORDS);
				   clientdata.append(SEPARATOR);
				   clientdata.append(ID);
				   clientdata.append(SEPARATOR);
				   clientdata.append(message);
				   output.write(clientdata.toString() + "\r\n");
				   output.flush();
				   wtext.setText("");
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		   }
	   }
   }
   public void keyReleased(KeyEvent ke) {}
   public void keyTyped(KeyEvent ke) {}
}