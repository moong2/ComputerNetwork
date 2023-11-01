import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

public class SMTPClient {
    public static void main(String[] args) {
        try  {
            NaverSocket socket = new NaverSocket();
            socket.FromSSL();
            socket.upgradeToSSL();
            socket.sendRequest("EHLO");
            for(int i=0;i<7;i++) System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("AUTH LOGIN");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            String userName = "boeun0904";
            socket.sendRequest(Base64.getEncoder().encodeToString(userName.getBytes()));
            System.out.println(socket.readResponse());
            System.out.println("===========");

            String password = "password";
            socket.sendRequest(Base64.getEncoder().encodeToString(password.getBytes()));
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("MAIL FROM:<boeun0904@naver.com>");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("RCPT TO:<h.boeunn@gmail.com>");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("DATA");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("From: Boeun <boeun0904@naver.com>");
            socket.sendRequest("hello");
            socket.sendRequest("this is SMTP test");
            socket.sendRequest("can you hear me?");
            socket.sendRequest("from Boeun");
            socket.sendRequest(".");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("QUIT");
            System.out.println(socket.readResponse());
            System.out.println("===========");

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
