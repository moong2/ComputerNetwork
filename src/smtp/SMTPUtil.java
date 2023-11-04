package smtp;

public class SMTPUtil {
  public static boolean isUserNameExpected(String _FromEmail, String expected) {
    return _FromEmail.contains(expected);
  }

  public static void log(String title, String... responses) {
    System.out.println("<<<" + title + ">>>");
    for (String response : responses) {
      System.out.println(response);
    }
    System.out.println();
  }
}
