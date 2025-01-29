package exception;

/**
 * A generic runtime exception that is
 * used across the image processing application.
 */
public class ImageProcessingRunTimeException extends RuntimeException {

  /**
   * Constructs a new ImageProcessingRunTimeException with the given message.
   *
   * @param message the message of this ImageProcessingRunTimeException
   */
  public ImageProcessingRunTimeException(String message) {
    super(message);
  }

  /**
   * Constructs a new ImageProcessingRunTimeException with the given message
   * and the cause of this exception.
   *
   * @param message the message of this ImageProcessingRunTimeException
   * @param cause   the cause of this ImageProcessingRunTimeException
   */
  public ImageProcessingRunTimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * An exception that is thrown when there is an error displaying an image.
   */
  public static class DisplayException extends ImageProcessingRunTimeException {

    /**
     * Constructs a new DisplayException with the given message
     * and the cause of this exception.
     *
     * @param message the message of this DisplayException
     * @param cause   the cause of this DisplayException
     */
    public DisplayException(String message, Throwable cause) {
      super(message, cause);
    }

    /**
     * Constructs a new DisplayException with the given message.
     *
     * @param message the message of this DisplayException
     */
    public DisplayException(String message) {
      super(message);
    }

  }


  /**
   * An exception that is thrown when the user wants to quit the program.
   */
  public static class QuitException extends ImageProcessingRunTimeException {

    /**
     * Constructs a new QuitException with the given message.
     *
     * @param message the message of this QuitException
     */
    public QuitException(String message) {
      super(message);
    }
  }
}
