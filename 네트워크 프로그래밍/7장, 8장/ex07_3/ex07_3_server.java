package chapter7;

import java.io.*;
import java.net.*;
import java.util.*;

public class ex07_3_server {
	public static void main(String args[]) {
		int b, port;
		byte[] data = null;
		
		String encoding = "ASCII";
		String contenttype = "text/plain";
		
		try {
			port = 5050;
			
			ServerSocket server = new ServerSocket(port);
			
			if(args.length > 2) encoding = args[2];
			
 			while(true) {
 				Socket connection = null;
 				FileDownload client = null;
 				
 				try {
 	 				connection = server.accept(); // 클라이언트의 접속을 기다린다.
 	 				client = new FileDownload(connection, data, encoding, contenttype, port);
 	 				client.start();
 	 			} catch(IOException e) {
 	 				System.out.println(e);
 	 			}	
 			}	
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class FileDownload extends Thread {
	private byte[] content;
	private byte[] header;
	private String MIMEType;
	Socket connection;
	BufferedOutputStream out;
	BufferedInputStream in;
	
	FileOutputStream fout;
	FileInputStream fin;
	OutputStreamWriter osw;
	InputStreamReader isr;
	BufferedReader br;
	File file;
	
	int start, end, b;
	String requestFile;
	public FileDownload(Socket connection, byte[] data, String encoding, String MIMEType, int port) throws UnsupportedEncodingException {
		this.connection = connection;
		this.content = data;
		this.MIMEType = MIMEType;
	}
	public void run() {
		try {
			out = new BufferedOutputStream(connection.getOutputStream());
			in = new BufferedInputStream(connection.getInputStream());
			StringBuffer request = new StringBuffer(80);
			
			while (true) {
				int c = in.read();
				if (c == '\r' || c == '\n' || c == -1)
					break;
				request.append((char)c);
			}
			
			start = request.toString().indexOf("/");
			end = request.toString().indexOf(" HTTP/");
			requestFile = request.substring(start + 1, end);
			System.out.println(request.toString());
			System.out.println(requestFile);
			file = new File(requestFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			
			fin = new FileInputStream(requestFile);
				
			while ((b = fin.read()) != -1)
				baos.write(b);
			this.content = baos.toByteArray();
 
			String header = "HTTP 1.0 200 OK\r\n" + "Server: OneFile 1.0\r\n" + "Content-length:" + /*String.valueOf(message).getBytes()*/this.content.length + "\r\n" + "Content-type: " + this.MIMEType + "\r\n\r\n";
			this.header = header.getBytes("ASCII");
					
			if (request.toString().indexOf("HTTP/") != -1) {
				out.write(this.header);
			}
			out.write(this.content);
			out.flush();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}	
