package chapter9_2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ex09_6_client extends Frame implements ActionListener {
	private TextField accountField, nameField, balanceField;
	private Button enter, prt, done, clearbutton, deletebutton;
	
	private Record data;
	
	Socket client = null;
	
	public final static int port = 6060;
	
	private static final String SEPARATOR = "|";
	private static final int REQ_INPUT = 1001;
	private static final int REQ_SEARCH = 1002;
	private static final int REQ_DELETE = 1003;
	private static final int REQ_QUIT = 1004;
	
	public static String host;
	protected Socket theSocket;
	protected InputStream is;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	
	StringBuffer clientdata;
	String serverdata;
	
	BufferedWriter output;
	BufferedReader input;
	
	public ex09_6_client() {
		super("고객인터페이스");
		
		data = new Record();
		
		setSize(300, 150);
		
		setLayout(new GridLayout(5, 2));
		
		add(new Label("계좌번호"));
		accountField = new TextField();
		add(accountField);
		
		add(new Label("이름"));
		nameField = new TextField(20);
		add(nameField);
		
		add(new Label("잔고"));
		balanceField = new TextField(20);
		add(balanceField);
		
		enter=new Button("입력");
		enter.addActionListener(this);
		add(enter);
		
		prt=new Button("조회");
		prt.addActionListener(this);
		add(prt);
		
		deletebutton = new Button("삭제");
		deletebutton.addActionListener(this);
		add(deletebutton);
		
		clearbutton = new Button("clear");
		clearbutton.addActionListener(this);
		add(clearbutton);
		
		addWindowListener(new WinListener());
		setVisible(true);
	}
	public void run() {
		try {
			try {
				client = new Socket(InetAddress.getLocalHost(), 5000); // 연결 소켓
		        //mlbl.setText("연결된 서버이름 : " + client.getInetAddress().getHostName()); // 연결 시
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		        output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			} catch(UnknownHostException e) {
				System.out.println(host + " 호스트를 찾을 수 없습니다.");
			} catch(IOException e) {
				System.out.println(e);
				System.exit(0);
			}
			while(true) {
				serverdata = input.readLine(); // 조회
				// display.append(serverdata + "\r\n");
			}
		} catch(IOException e) {
    		e.printStackTrace();
    	}
	}
	public void addRecord() {		
		int accountNo = 0;
		Double money;
		String name;
		if(! accountField.getText().equals("")) {
			try {
				accountNo = Integer.parseInt(accountField.getText());
				
				if(accountNo > 0 && accountNo <= 100) {				
					name = nameField.getText();
					money = new Double(balanceField.getText());
					
					data.setAccount(accountNo);
					data.setName(nameField.getText());
					data.setBalance(money.doubleValue());
					
					//output.seek((long)(accountNo-1)*Record.size());
					
					try {
						clientdata.setLength(0);
			            clientdata.append(REQ_INPUT);
			            clientdata.append(SEPARATOR);
			            clientdata.append(name);
			            clientdata.append(accountNo);
			            clientdata.append(money);   
			            output.write(clientdata.toString() + "\r\n");
			            output.flush();
						client.close();
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
				else System.out.println("1부터 100까지 입력 가능합니다.");
				
				accountField.setText("");
				nameField.setText("");
				balanceField.setText("");
				
			} catch(NumberFormatException nfe) {
				System.err.println("숫자를 입력하세요");
			}
		}
	}
	public void prtRecord() {
		int accountNo = Integer.parseInt(accountField.getText());
		
		if(!accountField.getText().equals("")) {
			try {
				if(accountNo > 0 && accountNo <= 100) {
					
					accountField.setText("" + data.getAccount());
					nameField.setText(data.getName());
					balanceField.setText("" + data.getBalance());
				}
			} catch(NumberFormatException nfe) {
				System.err.println("숫자를 입력하세요");
			}
		}
	}
	
	public void delete() {
		/*
		int accountNo = Integer.parseInt( accountField.getText() );
	    try {
	    	output.seek((long) (accountNo-1) * Record.size() );
	        // accountNo = 0;
	    	data.setAccount(accountNo);
	    	data.setName("");
	        data.setBalance(0.0);
	        data.write( output );
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    */
	    accountField.setText("");
		nameField.setText("");
		balanceField.setText("");
	}
	
	private void clear() {
		accountField.setText("");
		nameField.setText("");
		balanceField.setText("");
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == enter) {
			addRecord();
		}
		else if(e.getSource() == prt) {
			prtRecord();
		}
		else if(e.getSource() == deletebutton ) {
			delete();
		}
		else if(e.getSource() == clearbutton ) {
			clear();
		}
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	public static void main(String[] args) {
		ex09_6_client c = new ex09_6_client();
		
	}
}

class Record {
	private int account;
	private String name;
	private double balance;
	
	public void read(RandomAccessFile file) throws IOException {
		char namearray[] = new char[15];
		account = file.readInt();
		
		for(int i = 0; i < namearray.length; i++) {
			namearray[i] = file.readChar();
		}
		
		name = new String(namearray);
		balance = file.readDouble();
	}
	
	public void write(RandomAccessFile file) throws IOException {
		StringBuffer buf;
		file.writeInt(account);
		
		if(name != null)
			buf = new StringBuffer(name); 
		else
			buf = new StringBuffer(15);
		
		buf.setLength(15);
		file.writeChars(buf.toString());
		file.writeDouble(balance);
	}
	public void setAccount(int a) { account = a;}
	public int getAccount() {return account;}
	public void setName(String f) {name = f;}
	public String getName() {return name;}
	public void setBalance(double b) {balance = b;}
	public double getBalance() {return balance;}
	public static int size() {return 42;}
}