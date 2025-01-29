package exception;

/**
 * A generic exception that is thrown across
 * the image processing application when there is an error processing an image.
 */
public class ImageProcessorException extends Exception {
  /**
   * Constructs a new ImageProcessorException with the given message.
   *
   * @param message the message of this ImageProcessorException
   */
  public ImageProcessorException(String message) {
    super(message);
  }

  /**
   * Constructs a new ImageProcessorException with the given message
   * and the cause of this exception.
   *
   * @param message the message of this ImageProcessorException
   * @param cause   the cause of this ImageProcessorException
   */
  public ImageProcessorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * An exception that is thrown when an image is not found.
   */
  public static class NotFoundException extends ImageProcessorException {
    /**
     * Constructs a new NotFoundException with the given message.
     *
     * @param message the message of this NotFoundException
     */
    public NotFoundException(String message) {
      super(message);
    }
  }

  /**
   * An exception that is thrown when a method is not implemented.
   */
  public static class NotImplementedException extends ImageProcessorException {

    /**
     * Constructs a new NotImplementedException with the given message.
     *
     * @param message the message of this NotImplementedException
     */
    public NotImplementedException(String message) {
      super(message);
    }
  }
}
