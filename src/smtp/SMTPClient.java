package smtp;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import socket.NaverSocket;
import socket.SocketHelper;
public class SMTPClient {
    String userName;
    String password ;
    String FromEmail;
    String[] ToEmail ;
    String Subject ;

    String attachmentFilePath ;
    String[] Contents;

    public SMTPClient( String _FromEmail, String _password, String _ToEmail, String _Subject,String _attachmentFilePath, String[] _Contents){
        FromEmail=_FromEmail;
        password=_password;
        ToEmail=_ToEmail.split(",");
        Subject= _Subject;
        Contents=_Contents;
        attachmentFilePath = _attachmentFilePath;

        if(_FromEmail.contains("naver")){
            userName= _FromEmail.split("@")[0];
        }
        else if(_FromEmail.contains("gmail")){
            userName=_FromEmail;
        }
    }
    public void isSuccess (SocketHelper socket){
        try{
            String TempRes = socket.readResponse();
            String StatusCode = TempRes.split("\\s")[0];

            if(StatusCode.equals("334")) {
                System.out.println(TempRes);
                System.out.println(StatusCode);
            }
            else if(StatusCode.equals("354")) {
                System.out.println(TempRes);
                System.out.println(StatusCode);
            }
            else if(StatusCode.equals("250")) {
                System.out.println(TempRes);
                System.out.println(StatusCode);
            }
            else if(StatusCode.equals("221")) {
                System.out.println(TempRes);
                System.out.println(StatusCode);
            }
            else if(StatusCode.equals("235")) {
                System.out.println(TempRes);
                System.out.println(StatusCode);
            }
            else throw new Error("Response Error");
    } catch (UnknownHostException ex) {

        System.out.println("Server not found: " + ex.getMessage());
    } catch (IOException ex) {

        System.out.println("I/O error: " + ex.getMessage());
    }
    }

    public boolean SMTPFunc() {
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
            isSuccess(socket);
            System.out.println("===========");

            System.out.println("USER NAME");
            socket.sendRequest(Base64.getEncoder().encodeToString(userName.getBytes()));
            isSuccess(socket);
            System.out.println("===========");

            System.out.println("PASSWORD");
            socket.sendRequest(Base64.getEncoder().encodeToString(password.getBytes()));
            isSuccess(socket);
            System.out.println("===========");

            System.out.println("MAIL FROM:<"+ FromEmail +">");
            socket.sendRequest("MAIL FROM:<"+ FromEmail +">");
            isSuccess(socket);
            System.out.println("===========");

            for(int i=0;i<ToEmail.length;i++) {
                System.out.println("RCPT TO:<" + ToEmail[i] + ">");
                socket.sendRequest("RCPT TO:<" + ToEmail[i] + ">");
                isSuccess(socket);
                System.out.println("===========");
            }

            System.out.println("DATA");
            socket.sendRequest("DATA");
            isSuccess(socket);
            System.out.println("===========");

            String boundary = "abcdxyz";

            socket.sendRequest("From: "+userName+" <"+FromEmail+">");
            socket.sendRequest("Subject: "+Subject);
            socket.sendRequest("MIME-Version: 1.0");
            socket.sendRequest("Content-Type: multipart/mixed; boundary=\"" + boundary + "\"");
            socket.sendRequest("");

            socket.sendRequest("--" + boundary);
            socket.sendRequest("Content-Type: text/plain; charset=utf-8");
            socket.sendRequest("Content-Transfer-Encoding: 7bit");
            socket.sendRequest("");
            for(int i=0;i<Contents.length;i++){
                socket.sendRequest(Contents[i]);
            }

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

            isSuccess(socket);
            System.out.println("===========");

            socket.sendRequest("QUIT");
            isSuccess(socket);
            return true;
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());
            return false;
        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
            return false;
        }
    }
}



//    public static void main(String[] args) {
//        try  {
//            SocketHelper socket = new NaverSocket();
//            socket.FromSTARTTLS();
//            System.out.println("socket.readResponse() = " + socket.readResponse());
//
//            socket.sendRequest("EHLO naver.com");
//            List<String> responses = new ArrayList<>();
//            for (int i = 0; i < 7; i ++) {
//                String response = socket.readResponse();
//                System.out.println("response = " + response);
//                responses.add(response);
//            }
//
//            boolean isTLS = false;
////            for (String response : responses) {
////                if (response.contains("250-STARTTLS")) {
////                    System.out.println("here!");
////                    socket.sendRequest("STARTTLS");
////                    response = socket.readResponse();
////                    System.out.println("response = " + response);
////                    socket.upgradeToSSL();
////
////                    isTLS = true;
////                    break;
////                }
////            }
//
//            if (!isTLS) {
//                socket.close();
//                socket.FromSSL();
//                socket.upgradeToSSL();
//                System.out.println("socket.readResponse() = " + socket.readResponse());
//            }
//
//            System.out.println("AUTH LOGIN");
//            socket.sendRequest("AUTH LOGIN");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            System.out.println("USER NAME");
//            socket.sendRequest(Base64.getEncoder().encodeToString(userName.getBytes()));
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            System.out.println("PASSWORD");
//            socket.sendRequest(Base64.getEncoder().encodeToString(password.getBytes()));
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            System.out.println("MAIL FROM:<"+ FromEmail +">");
//            socket.sendRequest("MAIL FROM:<"+ FromEmail +">");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            System.out.println("RCPT TO:<"+ ToEmail +">");
//            socket.sendRequest("RCPT TO:<"+ ToEmail +">");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            System.out.println("DATA");
//            socket.sendRequest("DATA");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            socket.sendRequest("From: "+userName+" <"+FromEmail+">");
//            socket.sendRequest("Subject: "+Subject);
//            socket.sendRequest("Content-Type: text/plain; charset=utf-8");
//            for(int i=0;i<Contents.length;i++){
//                socket.sendRequest(Contents[i]);
//            }
//            socket.sendRequest(".");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//            socket.sendRequest("QUIT");
//            System.out.println(socket.readResponse());
//            System.out.println("===========");
//
//        } catch (UnknownHostException ex) {
//
//            System.out.println("Server not found: " + ex.getMessage());
//
//        } catch (IOException ex) {
//
//            System.out.println("I/O error: " + ex.getMessage());
//        }
//    }
//}
