package model.request;

import java.util.Optional;

import exception.ImageProcessorException;
import utility.StringUtils;

/**
 * Represents a request for processing an image.
 * This class uses the Builder pattern to create an instance of ImageProcessingRequest.
 * It contains various parameters that can be set to customize the image processing request.
 * The data it can contain includes image path, image name, destination image name, factor,
 * red image name, green image name, blue image name, percentage, and levels.
 */
public class ImageProcessingRequest {

  private final String imagePath;
  private final String imageName;
  private final String destinationImageName;
  private final Integer factor;
  private final String redImageName;
  private final String greenImageName;
  private final String blueImageName;
  private final Integer percentage;
  private final Levels levels;
  private final ScalingFactors scalingFactors;

  private ImageProcessingRequest(ImageProcessingRequestBuilder builder) {
    this.imagePath = builder.imagePath;
    this.imageName = builder.imageName;
    this.destinationImageName = builder.destinationImageName;
    this.factor = builder.factor;
    this.redImageName = builder.redImageName;
    this.greenImageName = builder.greenImageName;
    this.blueImageName = builder.blueImageName;
    this.percentage = builder.percentage;
    this.levels = builder.levels;
    this.scalingFactors = builder.scalingFactors;
  }

  /**
   * Returns a new ImageProcessingRequestBuilder.
   *
   * @return a new ImageProcessingRequestBuilder
   */
  public static ImageProcessingRequestBuilder builder() {
    return new ImageProcessingRequestBuilder();
  }

  /**
   * Returns the name of the image.
   * This is a compulsory field and cannot be null or empty.
   *
   * @return the name of the image
   */
  public String getImageName() {
    return imageName;
  }


  /**
   * Returns the path of the image.
   * If the path is not provided, it returns an empty string.
   *
   * @return the path of the image
   */
  public String getImagePath() {
    return Optional.ofNullable(imagePath).orElse(StringUtils.EMPTY_STRING);
  }


  /**
   * Returns the name of the destination image.
   * If the destination image name is not provided, it returns an empty string.
   *
   * @return the name of the destination image
   */
  public String getDestinationImageName() {
    return Optional.ofNullable(destinationImageName)
            .orElse(StringUtils.EMPTY_STRING);
  }

  /**
   * Returns the factor to be used for calculating brightness.
   * If the factor is not provided, it returns an empty optional.
   *
   * @return the factor to be used for calculating brightness.
   */
  public Optional<Integer> getFactor() {
    return Optional.ofNullable(factor);
  }

  public Optional<Levels> getLevels() {
    return Optional.ofNullable(levels);
  }

  public Optional<ScalingFactors> getScalingFactors() {
    return Optional.ofNullable(scalingFactors);
  }

  /**
   * Returns the name of the red image.
   * If the red image name is not provided, it returns an empty string.
   *
   * @return the name of the red image
   */
  public String getRedImageName() {
    return Optional.ofNullable(redImageName)
            .orElse(StringUtils.EMPTY_STRING);
  }

  /**
   * Returns the name of the green image.
   * If the green image name is not provided, it returns an empty string.
   *
   * @return the name of the green image
   */
  public String getGreenImageName() {
    return Optional.ofNullable(greenImageName)
            .orElse(StringUtils.EMPTY_STRING);
  }

  /**
   * Returns the name of the blue image.
   * If the blue image name is not provided, it returns an empty string.
   *
   * @return the name of the blue image
   */
  public String getBlueImageName() {
    return Optional.ofNullable(blueImageName)
            .orElse(StringUtils.EMPTY_STRING);
  }

  /**
   * Returns the percentage by which to compress the image or the percentage
   * to combine the image.
   * If the percentage is not provided, it returns an empty optional.
   *
   * @return the percentage by which to compress the image
   */
  public Optional<Integer> getPercentage() {
    return Optional.ofNullable(percentage);
  }

  /**
   * Builder class to build an ImageProcessingRequest.
   */
  public static class ImageProcessingRequestBuilder {


    private String imageName;
    private String imagePath;
    private String destinationImageName;
    private Integer factor;
    private String redImageName;
    private String greenImageName;
    private String blueImageName;
    private Integer percentage;
    private Levels levels;
    private ScalingFactors scalingFactors;

    private ImageProcessingRequestBuilder() {
    }

    /**
     * Sets the path of the image.
     *
     * @param imagePath the path of the image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder imagePath(String imagePath) {
      this.imagePath = imagePath;
      return this;
    }

    /**
     * Sets the name of the image.
     *
     * @param imageName the name of the image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder imageName(String imageName) {
      this.imageName = imageName;
      return this;
    }

    /**
     * Sets the name of the destination image.
     *
     * @param destinationImageName the name of the destination image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder destinationImageName(String destinationImageName) {
      this.destinationImageName = destinationImageName;
      return this;
    }

    /**
     * Sets the factor to be used for calculating brightness.
     *
     * @param factor the factor to be used for calculating brightness
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder factor(Integer factor) {
      this.factor = factor;
      return this;
    }

    /**
     * Sets the name of the red image.
     *
     * @param redImageName the name of the red image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder redImageName(String redImageName) {
      this.redImageName = redImageName;
      return this;
    }

    /**
     * Sets the name of the green image.
     *
     * @param greenImageName the name of the green image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder greenImageName(String greenImageName) {
      this.greenImageName = greenImageName;
      return this;
    }

    /**
     * Sets the name of the blue image.
     *
     * @param blueImageName the name of the blue image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder blueImageName(String blueImageName) {
      this.blueImageName = blueImageName;
      return this;
    }

    /**
     * Sets the percentage by which to compress the image.
     *
     * @param percentage the percentage by which to compress the image
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder percentage(Integer percentage) {
      this.percentage = percentage;
      return this;
    }

    /**
     * Sets the levels of the image.
     *
     * @param black the black level
     * @param mid   the mid level
     * @param white the white level
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder levels(int black, int mid, int white) {
      this.levels = new Levels(black, mid, white);
      return this;
    }

    /**
     * Sets the scaling factors of the image.
     *
     * @param widthFactor  the width factor
     * @param heightFactor the height factor
     * @return the ImageProcessingRequestBuilder
     */
    public ImageProcessingRequestBuilder scalingFactors(int widthFactor, int heightFactor) {
      this.scalingFactors = new ScalingFactors(widthFactor, heightFactor);
      return this;
    }

    /**
     * Builds an ImageProcessingRequest.
     *
     * @return the ImageProcessingRequest
     * @throws ImageProcessorException if the image name is null or empty
     */
    public ImageProcessingRequest build() throws ImageProcessorException {
      validateBuildParams();
      return new ImageProcessingRequest(this);
    }

    /**
     * Validates the build parameters.
     *
     * @throws ImageProcessorException if the image name is null or empty
     */
    private void validateBuildParams() throws ImageProcessorException {
      if (StringUtils.isNullOrEmpty(imageName)) {
        throw new ImageProcessorException("Image name cannot be null or empty");
      }
    }

  }

  /**
   * Represents the levels of the image.
   */
  public static class Levels {
    private final int black;
    private final int white;
    private final int mid;

    /**
     * Constructor to initialize the levels.
     *
     * @param black the black level
     * @param mid   the mid level
     * @param white the white level
     */
    public Levels(int black, int mid, int white) {
      this.black = black;
      this.white = white;
      this.mid = mid;
    }

    /**
     * Returns the black value of the level.
     *
     * @return the black level
     */
    public int getBlack() {
      return black;
    }

    /**
     * Returns the white value of the level.
     *
     * @return the white level
     */
    public int getWhite() {
      return white;
    }

    /**
     * Returns the mid value of the level.
     *
     * @return the mid level
     */
    public int getMid() {
      return mid;
    }
  }

  /**
   * Represents the scaling factors of the image.
   */
  public static class ScalingFactors {
    /**
     * The width factor to reduce the wide by.
     */
    private final int widthFactor;
    /**
     * The height factor to reduce the height by.
     */
    private final int heightFactor;

    /**
     * Constructor to initialize the scaling factors.
     *
     * @param widthFactor  the width factor to reduce the wide by
     * @param heightFactor the height factor to reduce the height by
     */
    public ScalingFactors(int widthFactor, int heightFactor) {
      this.widthFactor = widthFactor;
      this.heightFactor = heightFactor;
    }

    /**
     * Returns the width factor.
     *
     * @return the width factor
     */
    public int getWidthFactor() {
      return widthFactor;
    }

    /**
     * Returns the height factor.
     *
     * @return the height factor
     */
    public int getHeightFactor() {
      return heightFactor;
    }
  }
}