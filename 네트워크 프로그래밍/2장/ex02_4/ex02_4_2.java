package chapter2;
import java.io.*;

public class ex02_4_2 extends FilterOutputStream {
	protected OutputStream out;
	protected FilterOutputStream out2;
	
	public ex02_4_2(OutputStream out, OutputStream out1) { 
		super(out);
        out2 = new FilterOutputStream(out1);
    } 

	public void write(int arg0) throws IOException {
		super.write(arg0);
	}

	public void close() throws IOException {
		try {
			super.close();
			out2.close();
		} catch(IOException e) {
			System.err.println(e.toString());
		}
	}

	public void flush() throws IOException {
		try {
			super.flush();
			out2.flush();
		}
		catch(IOException e) {
			System.err.println(e.toString());
		}
	}

	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		super.write(arg0, arg1, arg2);
		out2.write(arg0, arg1, arg2);
	}

	public void write(byte[] arg0) throws IOException {
		super.write(arg0);
		out2.write(arg0);
	}
	
	public static void copy(InputStream in, ex02_4_2 out) throws IOException {
		int byteRead;
		byte[] buffer = new byte[256];
		
		synchronized (in) {
			synchronized (out) {
				while((byteRead = in.read(buffer)) >= 0) {
					out.write(buffer, 0, byteRead);
				}
				out.flush();
			}
		}
	}
}