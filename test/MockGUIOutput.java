import java.util.List;
import java.util.Objects;

import controller.Features;
import exception.ImageProcessingRunTimeException;
import model.enumeration.UserCommand;
import model.visual.Image;
import view.DisplayMessageType;
import view.gui.GUIOutput;

/**
 * Represents the mock gui output which is used for testing purposes.
 */
public class MockGUIOutput implements GUIOutput {
  private final StringBuilder log;

  /**
   * Constructor for the MockInput class.
   * @param log                           the log to log the output
   */
  public MockGUIOutput(StringBuilder log) {
    this.log = log;
  }


  @Override
  public void displayMessage(String message, DisplayMessageType messageType) throws
          ImageProcessingRunTimeException.DisplayException {
    log(message);
    log(messageType.toString());
  }

  @Override
  public void displayCommands(List<UserCommand> commands) throws
          ImageProcessingRunTimeException.DisplayException {
    log(commands.toString());
  }

  @Override
  public void addFeatures(Features features) {
    // Do nothing as this is mock output
  }

  @Override
  public void displayImage(Image image, Image histogram) throws
          ImageProcessingRunTimeException.DisplayException {
    if (Objects.nonNull(image)) {
      log(image.toString());
    }
    if (Objects.nonNull(histogram)) {
      log(histogram.toString());
    }
  }

  @Override
  public void clearImage() throws
          ImageProcessingRunTimeException.DisplayException {
    log("Image cleared");
  }

  @Override
  public void closeWindow() throws
          ImageProcessingRunTimeException.QuitException {
    log("Window closed");

  }

  @Override
  public void doNotCloseWindow() {
    log("Window not closed");
  }

  private void log(String message) {
    log.append(message).append("\n");
  }
}
