package chapter2;
import java.io.*;
// ���� 2.2 + ���� 2.4
public class ex02_2 {
   static DataOutputStream dos;
   static FileInputStream fin;
   static DataInputStream dis;
   public static void main(String args[]) {
      try {
         char ch;
         String data, sdata1, sdata2;
         FileOutputStream fout = new FileOutputStream("chardata.txt");
         dos = new DataOutputStream(fout); // ���ϰ� ������
         dos.writeChar(65); // �빮�� 'A'�� ������
         dos.writeUTF("�ݰ����ϴ�.");
         dos.writeUTF("�ڹ� ä�� ���α׷��� ����");
         // �μ��� �־��� ���ڿ��� UTF������� ���ڵ��ؼ� ���Ͽ� �����Ѵ�.
         fin = new FileInputStream("chardata.txt");
         dis = new DataInputStream(fin);
         ch = dis.readChar(); // writeChar()�� �����޼ҵ�
         sdata1 = dis.readUTF(); //writeUTF()�� �����޼ҵ�
         sdata2 = dis.readUTF();
         System.out.println(ch); // ���� 'A'�� ���
         System.out.println(sdata1); // "�ݰ����ϴ�"�� ���
         System.out.println(sdata2); // "�ڹ� ä�����α׷��� ����"�� ���
      } catch(EOFException e) {
         System.err.println(e);
      } catch(IOException e) {
         System.err.println(e);
      } finally {
         try {
            if(dos != null) dos.close();
            if(dos != null) dos.close();
         } catch(IOException e) {
            System.err.println(e);
         }
      }
   }
}
   