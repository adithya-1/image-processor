package view.gui;

import java.util.Optional;
import java.util.function.IntConsumer;

import model.request.ImageProcessingRequest;

/**
 * Represents the input that the user can provide through the GUI.
 */
public interface GUIInput {

  /**
   * Confirm the split view.
   *
   * @param updateImageCallback the callback function to update the image.
   * @return true if the user confirms the split view false otherwise.
   */
  boolean confirmSplitView(IntConsumer updateImageCallback);


  /**
   * Returns the slider input.
   * Is present if the user has entered a value.
   * Otherwise, empty.
   *
   * @return the slider input
   */
  Optional<Integer> getSliderInput();

  /**
   * Prompts the user for the path to load an image interactively.
   *
   * @return the path to load the image. Empty if user cancels the operation.
   */
  Optional<String> interactiveImageLoadPathInput();

  /**
   * Prompts the user for the path to save an image interactively.
   *
   * @return the path to save the image. Empty if user cancels the operation.
   */
  Optional<String> interactiveImageSavePathInput();

  /**
   * Prompts the user for the levels to apply interactively.
   *
   * @return the levels to apply. Empty if user cancels the operation.
   */
  Optional<ImageProcessingRequest.Levels> interactiveThreeLevelInput();

  /**
   * Prompts the user for the scaling factors to apply interactively.
   *
   * @return the scaling factors to apply. Empty if user cancels the operation.
   */
  Optional<ImageProcessingRequest.ScalingFactors> interactiveScalingFactorsInput();

}
