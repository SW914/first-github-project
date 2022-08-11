package chapter9;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

public class ex09_3_client extends Frame implements ActionListener, KeyListener {
	TextArea display;
	TextField wtext, ltext;
	Label mlbl, wlbl, loglbl;
	Button logout;
	Panel ptotal, plabel;
	BufferedWriter output;
	BufferedReader input;
	Socket client = null;
	StringBuffer clientdata;
	String serverdata;
	String ID;
	
    private static final String SEPARATOR = "|";
    private static final int REQ_LOGON = 1001;
    private static final int REQ_SENDWORDS = 1021;
	private static final int REQ_LOGOUT = 9999;
	
    public ex09_3_client() {
    	super("클라이언트");

    	mlbl = new Label("채팅 상태를 보여줍니다.");
    	add(mlbl, BorderLayout.NORTH);

    	display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
    	display.setEditable(false);
    	add(display, BorderLayout.CENTER);

    	ptotal = new Panel(new BorderLayout());
 
    	Panel pword = new Panel(new BorderLayout());
    	wlbl = new Label("대화말");
    	wtext = new TextField(30); //전송할 데이터를 입력하는 필드
    	wtext.addKeyListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
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
    	logout = new Button("로그아웃");
    	logout.addActionListener(this);
    	add(ptotal, BorderLayout.SOUTH);

    	addWindowListener(new WinListener());
    	setSize(400, 200);
    	setVisible(true);
    }
	
    public void runClient() {
    	try {
    		client = new Socket(InetAddress.getLocalHost(), 5000);
	        mlbl.setText("연결된 서버이름 : " + client.getInetAddress().getHostName());
	        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	        clientdata = new StringBuffer(2048);
	        mlbl.setText("접속 완료 사용할 아이디를 입력하세요.");
	        while(true) {
	        	serverdata = input.readLine();
	        	display.append(serverdata+"\r\n");
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
	            output.write(clientdata.toString()+"\r\n");
	            output.flush();
				client.close();
				ID = null;
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}		
    	}
    	else {
    		if(client == null) mlbl.setText("서버와 연결이 불가합니다.");
	    	else {
		    	if(ID == null) {
		    		try {
						client = new Socket(InetAddress.getLocalHost(), 5000);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
		    		ID = ltext.getText();
		    		mlbl.setText(ID + "(으)로 로그인 하였습니다.");
		    		plabel.removeAll();
					plabel.add(logout, BorderLayout.SOUTH);
					add(ptotal, BorderLayout.SOUTH);
					ptotal.validate();
		    		try {
		    			clientdata.setLength(0);
			            clientdata.append(REQ_LOGON);
			            clientdata.append(SEPARATOR);
			            clientdata.append(ID);
			            output.write(clientdata.toString()+"\r\n");
			            output.flush();
			            ltext.setVisible(false);
		    		} catch(Exception e) {
		    			e.printStackTrace();
		    		}
		    	}
	    	}
    	}
   }
	
   public static void main(String args[]) {
	   ex09_3_client c = new ex09_3_client();
	   c.runClient();
   }
		
   class WinListener extends WindowAdapter {
	   public void windowClosing(WindowEvent e) {
		   if(client.isClosed()) {

		   }
		   else {
			   try {
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
		   System.exit(0);
	   }
   }

   public void keyPressed(KeyEvent ke) {
	   if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
		   String message = new String();
		   message = wtext.getText();
		   if (ID == null) {
			   mlbl.setText("다시 로그인 하세요!!!");
			   wtext.setText("");
		   } else {
			   try {
	               clientdata.setLength(0);
	               clientdata.append(REQ_SENDWORDS);
	               clientdata.append(SEPARATOR);
	               clientdata.append(ID);
	               clientdata.append(SEPARATOR);
	               clientdata.append(message);
	               output.write(clientdata.toString()+"\r\n");
	               output.flush();
	               wtext.setText("");
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		   }
	   }
   }

   public void keyReleased(KeyEvent ke) {
   }

   public void keyTyped(KeyEvent ke) {
   }
}