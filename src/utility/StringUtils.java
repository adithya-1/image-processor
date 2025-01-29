package utility;

/**
 * Utility class for String operations.
 */
public class StringUtils {

  public static final String EMPTY_STRING = "";

  /**
   * Private constructor to prevent instantiation.
   */
  private StringUtils() {
  }

  /**
   * Checks if any of the strings is null or empty.
   *
   * @param strings the strings to check
   * @return true if any of the strings is null or empty, false otherwise
   */
  public static boolean isNullOrEmpty(String... strings) {
    for (String string : strings) {
      if (isNullOrEmpty(string)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the string is null or empty.
   *
   * @param string the string to check
   * @return true if the string is null or empty, false otherwise
   */
  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }

  /**
   * Checks if any of the strings is not null or empty.
   *
   * @param strings the strings to check
   * @return true if any of the strings is not null or empty, false otherwise
   */
  public static boolean isNotNullOrEmpty(String... strings) {
    return !isNullOrEmpty(strings);
  }
}
