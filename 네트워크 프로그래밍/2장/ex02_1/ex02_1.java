package chapter2;
import java.io.*;
// ���� 2.1 + ���� 2.3
public class ex02_1 {
   static FileOutputStream fout;
   static DataOutputStream dos;
   static FileInputStream fin;
   static DataInputStream dis;
   public static void main(String args[]) {
      boolean bdata;
      double ddata;
      int number;
      try {
         fout = new FileOutputStream("numberdata.txt");
         dos = new DataOutputStream(fout); // ���ϰ� ����� ���� ��Ʈ�� ����
         dos.writeBoolean(true); // 1���� ���Ͽ� �����Ѵ�.
         dos.writeDouble(989.27); // �Ǽ��� ���Ͽ� ����ȴ�.
         for(int i = 1; i <= 500; i++) 
            dos.writeInt(i); // 1���� 500������ ������ ���Ͽ� �����Ѵ�.
         fin = new FileInputStream("numberdata.txt");
         dis = new DataInputStream(fin);
         bdata = dis.readBoolean(); // �ο� ���� �д´�.
         System.out.println(bdata); // �ο� ���� ����Ѵ�.
         ddata = dis.readDouble(); // �Ǽ� ���� �д´�.
         System.out.println(ddata); // �Ǽ� ���� ����Ѵ�.
         while(true) {
            number = dis.readInt();
            System.out.println(number+" "); // ���� ���� �а� ȭ�鿡 ����Ѵ�.
         }
      } catch(EOFException e) {
         System.out.println("�����͸� ��� �о����ϴ�."); // ��������
      } catch(IOException e) { // ������ ����
         System.err.println(e);
      } finally {
         try {
            if(dos != null) dos.close();
         } catch(IOException e) {}
      }
      
   }
}