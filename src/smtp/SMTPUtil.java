package smtp;

import socket.SocketHelper;

public class SMTPUtil {
  public static boolean isUserNameExpected(String _FromEmail, String expected) {
    return _FromEmail.contains(expected);
  }

  public static void log(String title, String toEmail, String... responses) {
    System.out.println("<<<"+ title + ">>>");
    for (String response : responses) {
      System.out.println(response);
      checkResponseCode(toEmail, response);
    }
    System.out.println();
  }

  private static void checkResponseCode(String toEmail, String response) {
    if (!response.contains("220") && !response.contains("334") && !response.contains("354") && !response.contains("250")
        && !response.contains("221") && !response.contains("235")) {
      throw new RuntimeException(toEmail + " : 이메일 전송이 실패했습니다.");
    }
  }
}
