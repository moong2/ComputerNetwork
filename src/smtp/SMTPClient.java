package smtp;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import socket.NaverSocket;
import socket.SocketHelper;

public class SMTPClient {
    public static void main(String[] args) {
        try  {
            SocketHelper socket = new NaverSocket();
            socket.FromSTARTTLS();
            System.out.println("socket.readResponse() = " + socket.readResponse());

            socket.sendRequest("EHLO naver.com");
            List<String> responses = new ArrayList<>();
            for (int i = 0; i < 7; i ++) {
                String response = socket.readResponse();
                System.out.println("response = " + response);
                responses.add(response);
            }

            boolean isTLS = false;
//            for (String response : responses) {
//                if (response.contains("250-STARTTLS")) {
//                    System.out.println("here!");
//                    socket.sendRequest("STARTTLS");
//                    response = socket.readResponse();
//                    System.out.println("response = " + response);
//                    socket.upgradeToSSL();
//
//                    isTLS = true;
//                    break;
//                }
//            }

            if (!isTLS) {
                socket.close();
                socket.FromSSL();
                socket.upgradeToSSL();
                System.out.println("socket.readResponse() = " + socket.readResponse());
            }

            System.out.println("AUTH LOGIN");
            socket.sendRequest("AUTH LOGIN");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            System.out.println("USER NAME");
            String userName = "boeun0904";
            socket.sendRequest(Base64.getEncoder().encodeToString(userName.getBytes()));
            System.out.println(socket.readResponse());
            System.out.println("===========");

            System.out.println("PASSWORD");
            String password = "MWG229CQ5ESR";
            socket.sendRequest(Base64.getEncoder().encodeToString(password.getBytes()));
            System.out.println(socket.readResponse());
            System.out.println("===========");

            System.out.println("MAIL FROM:<boeun0904@naver.com>");
            socket.sendRequest("MAIL FROM:<boeun0904@naver.com>");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            System.out.println("RCPT TO:<h.boeunn@gmail.com>");
            socket.sendRequest("RCPT TO:<h.boeunn@gmail.com>");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            System.out.println("DATA");
            socket.sendRequest("DATA");
            System.out.println(socket.readResponse());
            System.out.println("===========");

            socket.sendRequest("From: Boeun <boeun0904@naver.com>");
            socket.sendRequest("Subject: 넌 이름이 뭐야");
            socket.sendRequest("Content-Type: text/plain; charset=utf-8");
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
