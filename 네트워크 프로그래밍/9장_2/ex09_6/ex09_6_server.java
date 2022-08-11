package chapter9_2;

import java.io.*;
import java.net.*;
import java.util.*;

public class ex09_6_server {
	private static final String SEPARATOR = "|";
	private static final int REQ_INPUT = 1001;
	private static final int REQ_SEARCH = 1002;
	private static final int REQ_DELETE = 1003;
	private static final int REQ_QUIT = 1004;
	
	private Record data;
	
	ServerSocket server = null;
    Socket sock = null;
    
    BufferedWriter output = null;
	BufferedReader input = null;
	
	RandomAccessFile raf;

    String clientdata;
	String serverdata = "";
	
	public void run() {
		try {	
			server = new ServerSocket(5000, 100);	    	
			try {
				while(true) {
					sock = server.accept();
				}
			} catch(IOException ioe) {
				server.close();
				ioe.printStackTrace();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		try {
			raf = new RandomAccessFile("customer.txt", "rw");
		} catch(IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
		
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		try {
			int accountNo = 0;
			String name = "";
			double money = 0;
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
					
				switch(command) {
					case REQ_INPUT : { // 입력
						accountNo = Integer.parseInt(st.nextToken());
						data.setAccount(accountNo);
						
						name = st.nextToken();
						data.setName(name);
						
						money = new Double(st.nextToken());
						data.setBalance(money);
						
						raf.seek((long)(accountNo-1)*Record.size());
						data.write(raf);
						break;
					}
					case REQ_SEARCH : { // 검색
						accountNo = Integer.parseInt(st.nextToken());
						raf.seek((long)(accountNo-1)*Record.size());
						data.read(raf);
						break;
					}
					case REQ_DELETE : { // 삭제
						
						
						break;
					}
					case REQ_QUIT : { // 나가기
						server.close();
						break;
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ex09_6_server s = new ex09_6_server();
		s.run();
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