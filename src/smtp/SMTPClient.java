package smtp;

import static smtp.SMTPUtil.isUserNameExpected;
import static smtp.SMTPUtil.log;
import static util.CodeContent.GMAIL;
import static util.CodeContent.NAVER;
import static util.CodeContent.boundary;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import socket.GoogleSocket;
import socket.NaverSocket;
import socket.SocketHelper;

public class SMTPClient {
    static SocketHelper socket;

    String userName;
    String password ;
    String FromEmail;
    String ToEmail ;
    String Subject ;

    String attachmentFilePath ;
    String[] Contents;

    public SMTPClient( String _FromEmail, String _password, String _ToEmail, String _Subject, String _attachmentFilePath, String[] _Contents){
        FromEmail=_FromEmail;
        password=_password;
        ToEmail=_ToEmail;
        Subject= _Subject;
        Contents=_Contents;
        attachmentFilePath = _attachmentFilePath;
        this.ofUserName(_FromEmail);
    }

    private void ofUserName(String _FromEmail) {
        if (isUserNameExpected(_FromEmail, GMAIL)) {
            userName = _FromEmail;
            socket = new GoogleSocket();
        }
        else if (isUserNameExpected(_FromEmail, NAVER)) {
            userName = _FromEmail.split("@")[0];
            socket = new NaverSocket();
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 이메일 형식입니다. gmail, naver만 이용해주세요.");
        }
    }

    public void SMTPFunc() throws IOException {
        socket.FromSTARTTLS();
        log("STARTTLS", socket.readResponse());

        List<String> responses = getEHLO(socket);

        boolean isTLS = checkSTARTTLS(responses);

        if (!isTLS) makeSSL();

        getAuth();

        sendMail();
        sendTo();
        sendData();

        sendMailHeader();
        sendBodyText();
        sendBodyFile();
        sendMailEnd();
    }

    private List<String> getEHLO(SocketHelper socket) throws IOException {
        socket.sendRequest("EHLO gmail.com");
        List<String> responses = new ArrayList<>();
        for (int i = 0; i < 7; i ++) {
            String response = socket.readResponse();
            responses.add(response);
        }
        log("EHLO", responses.toArray(new String[responses.size()]));

        return responses;
    }

    private boolean checkSTARTTLS(List<String> responses) throws IOException {
        for (String response : responses) {
            if (response.contains("250-STARTTLS")) {
                socket.sendRequest("STARTTLS");
                log("STARTTLS", socket.readResponse());
                socket.upgradeToSSL();

                return true;
            }
        }
        return false;
    }

    private void makeSSL() throws IOException {
        socket.close();
        socket.FromSSL();
        socket.upgradeToSSL();
        log("SSL", socket.readResponse());
    }

    private void getAuth() throws IOException {
        socket.sendRequest("AUTH LOGIN");
        log("AUTH LOGIN", socket.readResponse());

        socket.sendRequest(Base64.getEncoder().encodeToString(userName.getBytes()));
        log("USER NAME", socket.readResponse());

        socket.sendRequest(Base64.getEncoder().encodeToString(password.getBytes()));
        log("PASSWORD", socket.readResponse());
    }

    private void sendMail() throws IOException {
        socket.sendRequest("MAIL FROM:<"+ FromEmail +">");
        log("MAIL FROM", socket.readResponse());
    }

    private void sendTo() throws IOException {
        socket.sendRequest("RCPT TO:<"+ ToEmail +">");
        log("RCPT TO", socket.readResponse());
    }

    private void sendData() throws IOException {
        socket.sendRequest("DATA");
        log("DATA", socket.readResponse());
    }

    private void sendMailHeader() {
        System.out.println("<<<MAIL SEND>>>");
        socket.sendRequest("From: "+userName+" <"+FromEmail+">");
        socket.sendRequest("To: <"+ToEmail+">");
        socket.sendRequest("Subject: "+Subject);
        socket.sendRequest("MIME-Version: 1.0");
        socket.sendRequest("Content-Type: multipart/mixed; boundary=\"" + boundary + "\"");
        socket.sendRequest("");
        socket.sendRequest("--" + boundary);
    }

    private void sendBodyText() {
        socket.sendRequest("Content-Type: text/plain; charset=utf-8");
        socket.sendRequest("Content-Transfer-Encoding: 7bit");
        socket.sendRequest("");
        for(int i=0;i<Contents.length;i++){
            socket.sendRequest(Contents[i]);
        }
    }

    private void sendBodyFile() throws IOException {
        if (!attachmentFilePath.isBlank()) {
            socket.sendRequest("--" + boundary);
            socket.sendRequest("Content-Type: application/octet-stream; name=\"" + new File(attachmentFilePath).getName() + "\"");
            socket.sendRequest("Content-Disposition: attachment; filename=\"" + new File(attachmentFilePath).getName() + "\"");
            socket.sendRequest("Content-Transfer-Encoding: base64");
            socket.sendRequest("");

            byte[] attachmentBytes = Files.readAllBytes(new File(attachmentFilePath).toPath());
            String attachmentBase64 = Base64.getEncoder().encodeToString(attachmentBytes);
            socket.sendRequest(attachmentBase64);
            socket.sendRequest("");
        }
    }

    private void sendMailEnd() throws IOException {
        socket.sendRequest("--" + boundary + "--");
        socket.sendRequest(".");
        log("SEND .", socket.readResponse());

        socket.sendRequest("QUIT");
        log("SEND QUIT", socket.readResponse());
    }
}
