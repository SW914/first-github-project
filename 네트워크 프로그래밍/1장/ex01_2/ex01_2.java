package chapter1;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ex01_2 extends Frame implements ActionListener {
	static Label lfile;
	static Label ldata;
	TextField tfile, tdata;
	TextArea txarea;
	Button cpy; //prt
	String filename1, filename2;
	byte buffer[] = new byte[80];
	
	public ex01_2(String str, Label scenter) {
		super(str);
		this.setSize(300, 500);
		this.setLayout(new FlowLayout());
		
		lfile = new Label("입력파일");
		add(lfile);
		
		tfile = new TextField(20);
		add(tfile);
		
		ldata = new Label("출력파일");
		add(ldata);
		
		tdata = new TextField(20);
		add(tdata);
		
        /*
		prt = new Button("출력"); // 출력
		prt.addActionListener(this);
		add(prt);
		*/
		
		scenter = new Label("파일내용");
		add(scenter);
		
		cpy = new Button("확인"); // 복사
		cpy.addActionListener(this);
		add(cpy);
		
		txarea = new TextArea(5, 30);
		add(txarea);
        
		addWindowListener(new WinListener()); // x버튼 이벤트
	}
	
	public static void main(String[] args) {
		test text = new test("파일 복사 / 출력", ldata);
		text.setSize(300, 220); // 고정된 창 크기
		text.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		filename1 = tfile.getText(); // 복사할 파일 이름
		filename2 = tdata.getText(); // 복사한 파일 이름
		
		FileInputStream in = null;
		FileOutputStream out = null;
        
		try {
			int byteRead;
			byte[] buffer = new byte[256];
			
			if(ae.getSource() == cpy) { // 복사
				in = new FileInputStream(filename1);
				out = new FileOutputStream(filename2);
				
				while((byteRead = in.read(buffer)) >= 0)
					out.write(buffer, 0, byteRead);

				txarea.setText("복사하셨습니다."); // textArea에 복사 성공 출력
				in = new FileInputStream(filename2); // 두 번째 파일 읽어오기
                in.read(buffer);
                String data = new String(buffer);
                txarea.setText("복사한 파일 내용\n"+data+"\n");
			}
            /*
			if(ae.getSource() == prt) { // 출력
			// } 복사한 한 파일 내용 출력
            */
        } catch(FileNotFoundException e) {// 파일을 찾을 수 없을 때 오류메세지
        	txarea.setText("파일을 찾을 수 없습니다");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
        finally {
        	try {
        		if (in != null) in.close();
        		if (out != null) out.close();
        	} catch(IOException e) {}
        }
	}
    
    class WinListener extends WindowAdapter { // x버튼 이벤트
    	public void windowClosing(WindowEvent we) {
    		System.exit(0);
    	}
    }
}