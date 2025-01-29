package view.gui;

import controller.Features;
import exception.ImageProcessingRunTimeException;
import model.visual.Image;
import view.UserOutput;

/**
 * Represents the graphic output which is used to interact with the user.
 * User can use this to view output through a graphical user interface.
 */
public interface GUIOutput extends UserOutput {

  /**
   * Adds the features that can be executed by user to the output.
   *
   * @param features the features to be added
   */
  void addFeatures(Features features);

  /**
   * Displays the image to the user.
   * If image is null then output will stay the same as before.
   *
   * @param image     the image to be displayed
   * @param histogram the histogram of the image to be displayed
   * @throws ImageProcessingRunTimeException.DisplayException if there is an
   *                                                          error
   *                                                          displaying the
   *                                                          image
   */
  void displayImage(Image image, Image histogram)
          throws
          ImageProcessingRunTimeException.DisplayException;

  /**
   * Clears the image from the output.
   *
   * @throws ImageProcessingRunTimeException.DisplayException if there is an
   *                                                          error clearing
   *                                                          the image
   */
  void clearImage() throws
          ImageProcessingRunTimeException.DisplayException;


  /**
   * Closes the window.
   */
  void closeWindow() throws
          ImageProcessingRunTimeException.QuitException;

  /**
   * Does not close the window.
   */
  void doNotCloseWindow();
}