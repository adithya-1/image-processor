package controller;

import exception.ImageProcessingRunTimeException;

/**
 * The controller interface for the image processor.
 * This is responsible for handling the image module
 * and communication with the view.
 */
public interface ImageProcessorController {
  /**
   * Processes the commands from the view.
   *
   * @throws ImageProcessingRunTimeException.QuitException if the user wants
   *                                                       to quit the application.
   */
  void processCommands() throws ImageProcessingRunTimeException.QuitException;
}
