package view.text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import exception.ImageProcessingRunTimeException;
import model.enumeration.UserCommand;
import view.DisplayMessageType;

/**
 * Represents the console output where program gives output to the
 * user.
 */
public class ConsoleOutput implements TextOutput {
  /**
   * The output stream to which is used
   * to communicate with user.
   */
  private final Appendable output;

  /**
   * Constructs a Console Output object with the given output stream.
   *
   * @param output the output stream to which is used to communicate with user.
   * @throws NullPointerException if the output stream is null.
   */
  public ConsoleOutput(Appendable output) {
    Objects.requireNonNull(output, "Output cannot be null");
    this.output = output;
  }

  @Override
  public void displayMessage(String message, DisplayMessageType messageType)
          throws
          ImageProcessingRunTimeException.DisplayException {
    try {
      this.output.append(message).append("\n");
    } catch (IOException e) {
      throw new ImageProcessingRunTimeException.DisplayException(
              String.format("Error displaying message:%s", message), e
      );
    }
  }


  @Override
  public void displayCommands(List<UserCommand> commands)
          throws
          ImageProcessingRunTimeException.DisplayException {
    for (UserCommand command : commands) {
      displayMessage(command.getDescription(), DisplayMessageType.INFO);
    }
  }
}
