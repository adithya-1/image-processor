import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;
import model.request.ImageProcessingRequest;
import view.gui.GUIInput;

/**
 * Represents the mock input from GUI.
 */
public class MockGUIInput implements GUIInput {
  private final boolean confirmSplitView;
  private final Integer sliderInput;
  private final String interactiveImageLoadPathInput;
  private final String interactiveImageSavePathInput;
  private final ImageProcessingRequest.Levels interactiveThreeLevelInput;
  private final ImageProcessingRequest.ScalingFactors interactiveScalingFactorsInput;


  /**
   * Constructor for the MockInput class.
   *
   * @param confirmSplitView              the confirm split view input
   * @param sliderInput                   the slider input
   * @param interactiveImageLoadPathInput the interactive image load path input
   * @param interactiveImageSavePathInput the interactive image save path input
   * @param interactiveThreeLevelInput    the interactive three level input
   */
  public MockGUIInput(boolean confirmSplitView,
                      Integer sliderInput,
                      String interactiveImageLoadPathInput,
                      String interactiveImageSavePathInput,
                      ImageProcessingRequest.Levels interactiveThreeLevelInput,
                      ImageProcessingRequest.ScalingFactors interactiveScalingFactorsInput) {
    this.confirmSplitView = confirmSplitView;
    this.sliderInput = sliderInput;
    this.interactiveImageLoadPathInput = interactiveImageLoadPathInput;
    this.interactiveImageSavePathInput = interactiveImageSavePathInput;
    this.interactiveThreeLevelInput = interactiveThreeLevelInput;
    this.interactiveScalingFactorsInput = interactiveScalingFactorsInput;
  }

  @Override
  public boolean confirmSplitView(IntConsumer updateImageCallback) {
    if (getSliderInput().isPresent() && Objects.nonNull(updateImageCallback)) {
      updateImageCallback.accept(getSliderInput().get());
    }
    return this.confirmSplitView;
  }

  @Override
  public Optional<Integer> getSliderInput() {
    return Optional.ofNullable(this.sliderInput);
  }

  @Override
  public Optional<String> interactiveImageLoadPathInput() {
    return Optional.ofNullable(this.interactiveImageLoadPathInput);
  }

  @Override
  public Optional<String> interactiveImageSavePathInput() {
    return Optional.ofNullable(this.interactiveImageSavePathInput);
  }

  @Override
  public Optional<ImageProcessingRequest.Levels> interactiveThreeLevelInput() {
    return Optional.ofNullable(this.interactiveThreeLevelInput);
  }

  @Override
  public Optional<ImageProcessingRequest.ScalingFactors> interactiveScalingFactorsInput() {
    return Optional.ofNullable(this.interactiveScalingFactorsInput);
  }
}
