package view;

import java.util.List;

import exception.ImageProcessingRunTimeException;
import model.enumeration.UserCommand;

/**
 * Represents the view which is used to interact with the user.
 * This is the only way we get inputs from user or display outputs to user.
 */
public interface UserOutput {

  /**
   * Displays the commands to the user.
   *
   * @param commands the list of commands to be displayed
   * @throws ImageProcessingRunTimeException.DisplayException if there is an
   *                                                          error
   *                                                          displaying the
   *                                                          commands
   */
  void displayCommands(List<UserCommand> commands)
          throws
          ImageProcessingRunTimeException.DisplayException;


  /**
   * Displays the message to the user.
   *
   * @param message     the message to be displayed
   * @param messageType the type of message to be displayed
   * @throws ImageProcessingRunTimeException.DisplayException if there is an
   *                                                          error
   *                                                          displaying the
   *                                                          message
   */
  void displayMessage(String message, DisplayMessageType messageType)
          throws
          ImageProcessingRunTimeException.DisplayException;

}
