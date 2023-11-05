package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import util.CodeContent;

public class NaverSocket implements SocketHelper{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    @Override
    public void FromSSL() throws IOException {
        socket = new Socket(CodeContent.NAVER_SMTP, CodeContent.PORT_SSL);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public void FromSTARTTLS() throws IOException {
        socket = new Socket(CodeContent.NAVER_SMTP, CodeContent.PORT_STARTTLS);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public String readResponse() throws IOException {
        return reader.readLine();
    }

    @Override
    public void sendRequest(String request) {
        writer.println(request);
    }

    @Override
    public void upgradeToSSL() throws IOException {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = sslSocketFactory.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
