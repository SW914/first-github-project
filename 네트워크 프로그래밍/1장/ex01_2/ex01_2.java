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
		
		lfile = new Label("�Է�����");
		add(lfile);
		
		tfile = new TextField(20);
		add(tfile);
		
		ldata = new Label("�������");
		add(ldata);
		
		tdata = new TextField(20);
		add(tdata);
		
        /*
		prt = new Button("���"); // ���
		prt.addActionListener(this);
		add(prt);
		*/
		
		scenter = new Label("���ϳ���");
		add(scenter);
		
		cpy = new Button("Ȯ��"); // ����
		cpy.addActionListener(this);
		add(cpy);
		
		txarea = new TextArea(5, 30);
		add(txarea);
        
		addWindowListener(new WinListener()); // x��ư �̺�Ʈ
	}
	
	public static void main(String[] args) {
		test text = new test("���� ���� / ���", ldata);
		text.setSize(300, 220); // ������ â ũ��
		text.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		filename1 = tfile.getText(); // ������ ���� �̸�
		filename2 = tdata.getText(); // ������ ���� �̸�
		
		FileInputStream in = null;
		FileOutputStream out = null;
        
		try {
			int byteRead;
			byte[] buffer = new byte[256];
			
			if(ae.getSource() == cpy) { // ����
				in = new FileInputStream(filename1);
				out = new FileOutputStream(filename2);
				
				while((byteRead = in.read(buffer)) >= 0)
					out.write(buffer, 0, byteRead);

				txarea.setText("�����ϼ̽��ϴ�."); // textArea�� ���� ���� ���
				in = new FileInputStream(filename2); // �� ��° ���� �о����
                in.read(buffer);
                String data = new String(buffer);
                txarea.setText("������ ���� ����\n"+data+"\n");
			}
            /*
			if(ae.getSource() == prt) { // ���
			// } ������ �� ���� ���� ���
            */
        } catch(FileNotFoundException e) {// ������ ã�� �� ���� �� �����޼���
        	txarea.setText("������ ã�� �� �����ϴ�");
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
    
    class WinListener extends WindowAdapter { // x��ư �̺�Ʈ
    	public void windowClosing(WindowEvent we) {
    		System.exit(0);
    	}
    }
}