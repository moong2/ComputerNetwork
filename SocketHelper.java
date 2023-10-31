import java.io.IOException;

public interface SocketHelper {
    void FromSSL() throws IOException;
    void FromSTARTTLS() throws IOException;
    String readResponse() throws IOException;
    void sendRequest(String request);
    void upgradeToSSL() throws IOException;
    void close() throws IOException;
}
