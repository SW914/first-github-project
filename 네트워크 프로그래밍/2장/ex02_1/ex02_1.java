package chapter2;
import java.io.*;
// 예제 2.1 + 예제 2.3
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
         dos = new DataOutputStream(fout); // 파일과 연결된 필터 스트림 생성
         dos.writeBoolean(true); // 1값을 파일에 저장한다.
         dos.writeDouble(989.27); // 실수를 파일에 저장된다.
         for(int i = 1; i <= 500; i++) 
            dos.writeInt(i); // 1부터 500까지의 정수를 파일에 저장한다.
         fin = new FileInputStream("numberdata.txt");
         dis = new DataInputStream(fin);
         bdata = dis.readBoolean(); // 부울 값을 읽는다.
         System.out.println(bdata); // 부울 값을 출력한다.
         ddata = dis.readDouble(); // 실수 값을 읽는다.
         System.out.println(ddata); // 실수 값을 출력한다.
         while(true) {
            number = dis.readInt();
            System.out.println(number+" "); // 정수 값을 읽고 화면에 출력한다.
         }
      } catch(EOFException e) {
         System.out.println("데이터를 모두 읽었습니다."); // 정상종료
      } catch(IOException e) { // 비정상 종료
         System.err.println(e);
      } finally {
         try {
            if(dos != null) dos.close();
         } catch(IOException e) {}
      }
      
   }
}