package chapter7;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ex07_4_server extends Frame implements ActionListener {
	private TextField accountField, nameField, balanceField;
	private Button prt, clearbutton, deletebutton;
	private Record data;
	
	public static String host;
	protected InputStream is;
	protected BufferedReader reader;
	
	public final static int port = 6060;
	protected ServerSocket theServer;
	protected Socket theSocket = null;
	protected BufferedWriter writer;
	private RandomAccessFile output;
	public ex07_4_server() {
		super("고객관리");
		
		try {
			theServer = new ServerSocket(port);
			// 13번 포트에서 클라이언트의 접속 요청을 기다리는 서버소켓 객체를 생성한다.
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
		try {
			output = new RandomAccessFile("customer.txt", "rw");
		} catch(IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
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
	public void prtRecord() {
		int accountNo = Integer.parseInt(accountField.getText());
		
		if(!accountField.getText().equals("")) {
			try {
				if(accountNo > 0 && accountNo <= 100) {
					output.seek((long)(accountNo-1)*Record.size());
					data.read(output);
					accountField.setText("" + data.getAccount());
					nameField.setText(data.getName());
					balanceField.setText("" + data.getBalance());
				}
			} catch(NumberFormatException nfe) {
				System.err.println("숫자를 입력하세요");
			} catch(IOException io) {
				nameField.setText("");
				balanceField.setText("");
				System.err.println("파일읽기 에러\n" + io.toString());
			}
		}
	}
	public void delete() {
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
		if(e.getSource() == prt) {
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
		new ex07_4_server();
		
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