package chapter2;
import java.io.*;
// 예제 2.2 + 예제 2.4
public class ex02_2 {
   static DataOutputStream dos;
   static FileInputStream fin;
   static DataInputStream dis;
   public static void main(String args[]) {
      try {
         char ch;
         String data, sdata1, sdata2;
         FileOutputStream fout = new FileOutputStream("chardata.txt");
         dos = new DataOutputStream(fout); // 파일과 연결함
         dos.writeChar(65); // 대문자 'A'를 전송함
         dos.writeUTF("반갑습니다.");
         dos.writeUTF("자바 채팅 프로그래밍 교재");
         // 인수로 주어진 문자열을 UTF방식으로 인코딩해서 파일에 전송한다.
         fin = new FileInputStream("chardata.txt");
         dis = new DataInputStream(fin);
         ch = dis.readChar(); // writeChar()의 대응메소드
         sdata1 = dis.readUTF(); //writeUTF()의 대응메소드
         sdata2 = dis.readUTF();
         System.out.println(ch); // 문자 'A'를 출력
         System.out.println(sdata1); // "반갑습니다"를 출력
         System.out.println(sdata2); // "자바 채팅프로그래밍 교재"를 출력
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
   