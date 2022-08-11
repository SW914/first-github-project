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
    	super("클라이언트");

    	mlbl = new Label("멀티캐스트 채팅 서버에 가입 요청합니다!");
    	add(mlbl, BorderLayout.NORTH);

    	display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
    	display.setEditable(false);
    	add(display, BorderLayout.CENTER);

    	ptotal = new Panel(new BorderLayout());
 
    	Panel pword = new Panel(new BorderLayout());
    	wlbl = new Label("대화말");
    	wtext = new TextField(30); // 전송할 데이터를 입력하는 필드
    	wtext.addKeyListener(this); // 입력된 데이터를 송신하기 위한 이벤트 연결
    	pword.add(wlbl, BorderLayout.WEST);
    	pword.add(wtext, BorderLayout.EAST);
    	ptotal.add(pword, BorderLayout.CENTER);

    	plabel = new Panel(new BorderLayout()); 	
    	loglbl = new Label("로그온");
    	ltext = new TextField(30); //전송할 데이터를 입력하는 필드
    	ltext.addActionListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
    	plabel.add(loglbl, BorderLayout.WEST);
    	plabel.add(ltext, BorderLayout.EAST);
    	ptotal.add(plabel, BorderLayout.SOUTH); // Button으로 변경
    	
    	logout = new Button("로그아웃"); // 로그아웃 버튼
    	logout.addActionListener(this); // 액션 추가
    	
    	add(ptotal, BorderLayout.SOUTH); // 위치 추가

    	addWindowListener(new WinListener());
    	setSize(400, 250);
    	setVisible(true);
    }
	
    public void runClient() {
    	try {
    		client = new Socket(InetAddress.getLocalHost(), 5000); // 연결 소켓
	        mlbl.setText("연결된 서버이름 : " + client.getInetAddress().getHostName()); // 연결 시 
	        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	        clientdata = new StringBuffer(2048);
	        mlbl.setText("접속 완료 사용할 아이디를 입력하세요.");
	        
	        byte data[] = new byte[512]; //데이터 통
	        
	        while(true) {
	        	if(!join) { // 처음엔 가입 안 되있음
	        		serverdata = input.readLine();
	        		display.append(serverdata + "\r\n");
	    			group = InetAddress.getByName(serverdata.substring(serverdata.indexOf('/')+1,serverdata.indexOf(':')));
                    String port_str = serverdata.substring(serverdata.indexOf(':')+1,serverdata.indexOf("입"));
                    int port = Integer.parseInt(port_str); // 문자열을 숫자로 변경
                    // https://jamesdreaming.tistory.com/125 // 참고
	    			msocket = new MulticastSocket(port);
                    msocket.setSoTimeout(50000);
                    sadd = new InetSocketAddress(group, port);
                    // http://cris.joongbu.ac.kr/course/java/api/java/net/InetSocketAddress.html // 참고
                    msocket.joinGroup(sadd, msocket.getNetworkInterface());  
                    packet = new DatagramPacket(data, data.length, group, port);
                    
                    serverdata = input.readLine();
	        		display.append(serverdata + "\r\n");
	        		
                    join = true; // 가입됨
                    // msocket.joinGroup(group); 			
	        	}
	        	else { // 가입 되있을 때
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
			mlbl.setText("로그아웃 하였습니다.");
			try {
				clientdata.setLength(0);
	            clientdata.append(REQ_LOGOUT);
	            clientdata.append(SEPARATOR);
	            clientdata.append(ID);
	            output.write(clientdata.toString() + "\r\n");
	            output.flush();
				client.close();
				ID = null;
	
				msocket.leaveGroup(sadd,msocket.getNetworkInterface()); // 가입 탈퇴
				join = false; // 가입 탈퇴
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}		
    	}
    	else {
    		if(client == null) {
    			mlbl.setText("서버와 연결이 불가합니다.");
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
		    	mlbl.setText(ID + "(으)로 로그인 하였습니다.");
		    	
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
		   if(client.isClosed()) { // 추가
			   try {
					client.close(); // 서버끄기
				} catch(IOException ioe) {
					ioe.printStackTrace();
				} catch(NullPointerException ne) {
					System.exit(0); // 프로그램 종료
				}
		   }
		   else { // 추가
			   try { // 로그아웃 신호
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
		   System.exit(0); // 프로그램 종료
	   }
   }

   public void keyPressed(KeyEvent ke) {
	   if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
		   //String message = wtext.getText();
		   String message = new String();
		   message = wtext.getText();
		   if (ID == null) {
			   mlbl.setText("로그인 후 이용하세요!!!");
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