package smtp;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import socket.NaverSocket;
import socket.SocketHelper;
import java.util.UUID;

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
            for (String response : responses) {
                if (response.contains("250-STARTTLS")) {
                    System.out.println("here!");
                    socket.sendRequest("STARTTLS");
                    response = socket.readResponse();
                    System.out.println("response = " + response);
                    socket.upgradeToSSL();

                    isTLS = true;
                    break;
                }
            }

//            if (!isTLS) {
//                socket.close();
//                socket.FromSSL();
//                socket.upgradeToSSL();
//                System.out.println("socket.readResponse() = " + socket.readResponse());
//            }

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

            String boundary = "abcdxyz";
            String attachmentFilePath = "C:\\Users\\Boeun\\Downloads\\cat.jpg";

            socket.sendRequest("From: Boeun <boeun0904@naver.com>");
            socket.sendRequest("To: <h.boeunn@gmail.com>");
            socket.sendRequest("Subject: 제목입니다");
            socket.sendRequest("MIME-Version: 1.0");
            socket.sendRequest("Content-Type: multipart/mixed; boundary=\"" + boundary + "\"");
            socket.sendRequest("");

            socket.sendRequest("--" + boundary);
            socket.sendRequest("Content-Type: text/plain; charset=utf-8");
            socket.sendRequest("Content-Transfer-Encoding: 7bit");
            socket.sendRequest("");
            socket.sendRequest("hello");
            socket.sendRequest("this is SMTP test");
            socket.sendRequest("can you hear me?");
            socket.sendRequest("from Boeun");

            socket.sendRequest("--" + boundary);
            socket.sendRequest("Content-Type: application/octet-stream; name=\"" + new File(attachmentFilePath).getName() + "\"");
            socket.sendRequest("Content-Disposition: attachment; filename=\"" + new File(attachmentFilePath).getName() + "\"");
            socket.sendRequest("Content-Transfer-Encoding: base64");
            socket.sendRequest("");

            byte[] attachmentBytes = Files.readAllBytes(new File(attachmentFilePath).toPath());
            String attachmentBase64 = Base64.getEncoder().encodeToString(attachmentBytes);
            socket.sendRequest(attachmentBase64);
            socket.sendRequest("");

            socket.sendRequest("--" + boundary + "--");
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