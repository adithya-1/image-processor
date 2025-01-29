package model.visual;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import exception.ImageProcessorException;
import factories.Factory;
import model.enumeration.CompressionType;
import model.enumeration.FilterOption;
import model.pixels.Pixel;
import model.pixels.RGB;
import utility.ExtractUtility;
import utility.FilterUtils;

/**
 * RenderedImage class that implements Image interface
 * and represents an image containing pixels.
 */
public class RenderedImage implements Image {
  /**
   * The pixel array of the image.
   * The first index represents the x-coordinate of the pixel.
   * The second index represents the y-coordinate of the pixel.
   * Images are a combination of pixels.
   */
  private final Pixel[][] pixels;

  /**
   * Constructs a RenderedImage object with the given pixel array.
   *
   * @param pixels the pixel array of the image
   * @throws ImageProcessorException if the pixel array is empty.
   */
  public RenderedImage(Pixel[][] pixels)
          throws ImageProcessorException {
    Objects.requireNonNull(pixels, "Pixel array cannot be null");
    if (pixels.length == 0 || pixels[0].length == 0) {
      throw new ImageProcessorException("Cannot render image of 0 width or height");
    }
    this.pixels = pixels;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    return pixels[x][y];
  }


  @Override
  public int getWidth() {
    return pixels[0].length;
  }

  @Override
  public int getHeight() {
    return pixels.length;
  }

  @Override
  public Image createRedComponent() throws ImageProcessorException {
    return transformImage(Pixel::createRedComponent);
  }

  @Override
  public Image createGreenComponent() throws ImageProcessorException {
    return transformImage(Pixel::createGreenComponent);
  }

  @Override
  public Image createBlueComponent() throws ImageProcessorException {
    return transformImage(Pixel::createBlueComponent);
  }

  @Override
  public Image adjustImageBrightness(int factor) throws ImageProcessorException {
    return transformImage(pixel -> pixel.adjustBrightness(factor));
  }

  @Override
  public Image getLuma() throws ImageProcessorException {
    return transformImage(Pixel::getLuma);
  }

  @Override
  public Image getSepia() throws ImageProcessorException {
    return transformImage(Pixel::getSepia);
  }

  @Override
  public Image getIntensity() throws ImageProcessorException {
    return transformImage(Pixel::getIntensity);
  }

  @Override
  public Image getValue() throws ImageProcessorException {
    return transformImage(Pixel::getValue);
  }

  /**
   * Converts the image to a string representation.
   */
  @Override
  public String toString() {
    return "RenderedImage{"
            + "pixels=" + Arrays.deepToString(pixels)
            + '}';
  }

  /**
   * Flips the image horizontally.
   * The image is flipped by reversing the columns of the image.
   */
  @Override
  public Image horizontalFlip() throws ImageProcessorException {
    int height = this.getHeight();
    int width = this.getWidth();
    Pixel[][] newPixelArray = new Pixel[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        newPixelArray[row][col] = this.getPixel(row, width - col - 1);
      }
    }

    return Factory.createImage(newPixelArray);
  }

  /**
   * Flips the image vertically.
   * The image is flipped by reversing the rows of the image.
   */
  @Override
  public Image verticalFlip() throws ImageProcessorException {
    int height = this.getHeight();
    int width = this.getWidth();
    Pixel[][] newPixelArray = new Pixel[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        newPixelArray[row][col] = this.getPixel(height - row - 1, col);
      }
    }
    return Factory.createImage(newPixelArray);
  }

  /**
   * Applies the given filter to the image.
   * The filter is applied to each pixel in the image.
   */
  @Override
  public Image applyFilter(FilterOption filterOption) throws ImageProcessorException {
    return FilterUtils.applyFilter(this, filterOption);
  }

  @Override
  public int[][] getRedChannel() {
    return getChannel(Pixel::getRed);
  }

  @Override
  public int[][] getGreenChannel() {
    return getChannel(Pixel::getGreen);
  }

  @Override
  public int[][] getBlueChannel() {
    return getChannel(Pixel::getBlue);
  }

  /**
   * Helper method to transform the image using the given transformation.
   * Transformation is applied to each pixel in the image.
   */
  private Image transformImage(Function<Pixel, Pixel> transformation)
          throws ImageProcessorException {
    int height = this.getHeight();
    int width = this.getWidth();
    Pixel[][] newPixelArray = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        newPixelArray[row][col] = transformation.apply(this.getPixel(row, col));
      }
    }
    return new RenderedImage(newPixelArray);
  }

  /**
   * Helper method to get the channel of the image.
   * Channel is extracted from each pixel in the image.
   */
  private int[][] getChannel(Function<Pixel, Integer> channel) {
    int height = this.getHeight();
    int width = this.getWidth();
    int[][] channelArray = new int[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        channelArray[row][col] = channel.apply(this.getPixel(row, col));
      }
    }
    return channelArray;
  }

  /**
   * Compares this image with another object.
   * If the object is an instance of RenderedImage, compares the pixel arrays.
   * Otherwise, compares the object with this image.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Image)) {
      return false;
    }
    if (obj instanceof RenderedImage) {
      RenderedImage that = (RenderedImage) obj;
      return that.equalsRenderedImage(this);
    }
    return obj.equals(this);
  }

  protected boolean equalsRenderedImage(RenderedImage that) {
    return Arrays.deepEquals(this.pixels, that.pixels);
  }

  /**
   * Returns the hash code of the image.
   */
  @Override
  public int hashCode() {
    return Arrays.deepHashCode(pixels);
  }

  /**
   * Applies color correction to the image.
   * The color correction is done by adjusting the color levels of the image.
   * The levels are adjusted based on the peak values of the color channels.
   * The peak values are calculated by finding the most frequent color values.
   * The peak values are then adjusted to a common value.
   */
  @Override
  public Image colorCorrect() throws ImageProcessorException {
    int width = getWidth();
    int height = getHeight();

    int[] redFreq = ExtractUtility.calculateColorFrequencies(this,
            Pixel::getRed);
    int[] greenFreq = ExtractUtility.calculateColorFrequencies(this,
            Pixel::getGreen);
    int[] blueFreq = ExtractUtility.calculateColorFrequencies(this,
            Pixel::getBlue);

    int redPeak = findMeaningfulPeak(redFreq);
    int greenPeak = findMeaningfulPeak(greenFreq);
    int bluePeak = findMeaningfulPeak(blueFreq);

    int avgPeak = (redPeak + greenPeak + bluePeak) / 3;

    int redOffset = avgPeak - redPeak;
    int greenOffset = avgPeak - greenPeak;
    int blueOffset = avgPeak - bluePeak;

    Pixel[][] newPixels = new Pixel[height][width];
    for (int column = 0; column < width; column++) {
      for (int row = 0; row < height; row++) {
        Pixel oldPixel = getPixel(row, column);
        int newRed = oldPixel.getRed() + redOffset;
        int newGreen = oldPixel.getGreen() + greenOffset;
        int newBlue = oldPixel.getBlue() + blueOffset;
        newPixels[row][column] = new RGB(newRed, newGreen, newBlue);
      }
    }

    return Factory.createImage(newPixels);
  }

  /**
   * Finds the meaningful peak in a frequency array, ignoring extremities.
   * Only considers values between 10 and 245 to avoid dark/blown-out regions.
   *
   * @param freq the frequency array
   * @return the value where the meaningful peak occurs
   */
  private int findMeaningfulPeak(int[] freq) {
    int maxFreq = 0;
    int peakValue = 0;

    // Only consider values between 10 and 245
    for (int i = 10; i < 245; i++) {
      if (freq[i] > maxFreq) {
        maxFreq = freq[i];
        peakValue = i;
      }
    }

    return peakValue;
  }

  /**
   * Adjusts the levels of the image using the specified black, mid, and
   * white points.
   * Quadratic Transformation used
   * <a href="https://northeastern.instructure.com/courses/192553/assignments/2490204">...</a>
   *
   * @param black the black point value (0-255)
   * @param mid   the mid point value (0-255)
   * @param white the white point value (0-255)
   * @return a new Image with adjusted levels
   * @throws ImageProcessorException if any of the values are out of range
   *                                 (0-255)
   *                                 or not in ascending order
   */
  @Override
  public Image levelsAdjust(int black, int mid, int white) throws ImageProcessorException {
    validateLevels(black, mid, white);

    double a = fittingCoefficientA(black, mid, white);
    double aA = fittingCoefficientAa(black, mid, white);
    double aB = fittingCoefficientAb(black, mid, white);
    double aC = fittingCoefficientAc(black, mid, white);

    double coeffA = aA / a;
    double coeffB = aB / a;
    double coeffC = aC / a;

    int height = this.getHeight();
    int width = this.getWidth();
    Pixel[][] adjustedPixels = new Pixel[height][width];

    for (int column = 0; column < width; column++) {
      for (int row = 0; row < height; row++) {
        adjustedPixels[row][column] = this.getPixel(row, column)
                .quadraticTransform(coeffA, coeffB, coeffC);
      }
    }

    return Factory.createImage(adjustedPixels);
  }

  /**
   * Combines this image with another image provided in the argument.
   *
   * @param image      the image to combine with
   * @param percentage the percentage of the first image(current image on which
   *                   the function is called on.
   * @return the combined image by creating a new image object.
   */
  @Override
  public Image combineImages(Image image, int percentage) throws ImageProcessorException {
    return Factory.combineImage(this, image, percentage);
  }

  /**
   * Compresses the image by the given percentage.
   *
   * @param type       the type of compression
   * @param percentage the percentage to compress by
   * @return the compressed image
   * @throws ImageProcessorException if the percentage is out of range
   */
  @Override
  public Image compress(CompressionType type, int percentage) throws ImageProcessorException {
    return Factory.createCompression(type).compress(
            this, percentage
    );
  }

  /**
   * Extracts the histogram of the image.
   *
   * @return the histogram of the image
   * @throws ImageProcessorException if the histogram cannot be created
   */
  @Override
  public Image histogram() throws ImageProcessorException {
    return ExtractUtility.createHistogram(this);
  }

  /**
   * Validates the black, mid, and white points for levels adjustment.
   *
   * @param black the black point value
   * @param mid   the mid point value
   * @param white the white point value
   * @throws ImageProcessorException if any of the values are out of range
   *                                 (0-255)
   *                                 or not in ascending order
   */
  private void validateLevels(int black, int mid, int white)
          throws ImageProcessorException {
    if (black < 0 || black > 255 || mid < 0 || mid > 255 || white < 0 || white > 255) {
      throw new ImageProcessorException("Levels must be between 0 and 255");
    }
    if (black >= mid || mid >= white) {
      throw new ImageProcessorException("Levels must be in ascending order");
    }
  }

  /**
   * Calculates the fitting coefficient A for the quadratic transformation.
   *
   * @param black the black point value
   * @param mid   the mid point value
   * @param white the white point value
   * @return the fitting coefficient A
   */
  private double fittingCoefficientA(int black, int mid, int white) {
    return Math.pow(black, 2) * (mid - white)
            - black * (Math.pow(mid, 2) - Math.pow(white, 2))
            + Math.pow(mid, 2) * white - Math.pow(white, 2) * mid;
  }

  /**
   * Calculates the fitting coefficient Aa for the quadratic transformation.
   *
   * @param black the black point value
   * @param mid   the mid point value
   * @param white the white point value
   * @return the fitting coefficient Aa
   */
  private double fittingCoefficientAa(int black, int mid, int white) {
    return (-black) * (128 - 255) + 128 * white - 255 * mid;
  }

  /**
   * Calculates the fitting coefficient Ab for the quadratic transformation.
   *
   * @param black the black point value
   * @param mid   the mid point value
   * @param white the white point value
   * @return the fitting coefficient Ab
   */
  private double fittingCoefficientAb(int black, int mid, int white) {
    return Math.pow(black, 2) * (128 - 255)
            + 255 * Math.pow(mid, 2) - 128 * Math.pow(white, 2);
  }

  /**
   * Calculates the fitting coefficient Ac for the quadratic transformation.
   *
   * @param black the black point value
   * @param mid   the mid point value
   * @param white the white point value
   * @return the fitting coefficient Ac
   */
  private double fittingCoefficientAc(int black, int mid, int white) {
    return Math.pow(black, 2) * (255 * mid - 128 * white)
            - black * (255 * Math.pow(mid, 2) - 128 * Math.pow(white, 2));
  }

  /**
   * Downscale the image by the given width and height factors.
   *
   * @param widthFactor  the width scaling factor
   * @param heightFactor the height scaling factor
   * @return the downscaled image
   * @throws ImageProcessorException if the factors are out of range
   */
  @Override
  public Image downscale(int widthFactor, int heightFactor) throws ImageProcessorException {
    validateScale(widthFactor, heightFactor);

    int newWidth = (int)(this.getWidth() * ((double) widthFactor / 100));
    int newHeight = (int)(this.getHeight() * ((double) heightFactor / 100));

    Pixel[][] newPixels = new Pixel[newHeight][newWidth];
    double scaleX = (double) this.getWidth() / newWidth;
    double scaleY = (double) this.getHeight() / newHeight;

    for (int y = 0; y < newHeight; y++) {
      for (int x = 0; x < newWidth; x++) {

        double sourceX = x * scaleX;
        double sourceY = y * scaleY;

        int floorSourceX = (int) Math.floor(sourceX);
        int floorSourceY = (int) Math.floor(sourceY);
        int ceilingSourceX = Math.min(floorSourceX + 1, this.getWidth() - 1);
        int ceilingSourceY = Math.min(floorSourceY + 1, this.getHeight() - 1);

        Pixel topLeft = this.getPixel(floorSourceY, floorSourceX);
        Pixel topRight = this.getPixel(floorSourceY, ceilingSourceX);
        Pixel bottomLeft = this.getPixel(ceilingSourceY, floorSourceX);
        Pixel bottomRight = this.getPixel(ceilingSourceY, ceilingSourceX);

        double dx = sourceX - floorSourceX;
        double dy = sourceY - floorSourceY;

        int red = computeChannelDownscale(
                topLeft.getRed(), topRight.getRed(),
                bottomLeft.getRed(), bottomRight.getRed(),
                dx, dy);
        int green = computeChannelDownscale(
                topLeft.getGreen(), topRight.getGreen(),
                bottomLeft.getGreen(), bottomRight.getGreen(),
                dx, dy);
        int blue = computeChannelDownscale(
                topLeft.getBlue(), topRight.getBlue(),
                bottomLeft.getBlue(), bottomRight.getBlue(),
                dx, dy);

        newPixels[y][x] = Factory.createRGBPixel(red, green, blue);
      }
    }

    return new RenderedImage(newPixels);
  }

  /**
   * Performs downscaling for a single color channel
   * using m = (topLeftValue * (1 - dx) + topRightValue * dx)
   * and n = (bottomLeftValue * (1 - dx) + bottomRightValue * dx).
   * @param topLeftValue top-left value
   * @param topRightValue top-right value
   * @param bottomLeftValue bottom-left value
   * @param bottomRightValue bottom-right value
   * @param dx x-axis interpolation factor
   * @param dy y-axis interpolation factor
   * @return interpolated value
   */
  private int computeChannelDownscale(int topLeftValue, int topRightValue,
                                      int bottomLeftValue, int bottomRightValue,
                                      double dx, double dy) {
    double mCoefficient = (topLeftValue * (1 - dx) + topRightValue * dx);
    double nCoefficient = (bottomLeftValue * (1 - dx) + bottomRightValue * dx);
    return (int) Math.round(mCoefficient * (1 - dy) + nCoefficient * dy);
  }

  /**
   * Validates the scale factors for downscaling.
   * @param widthFactor the width scaling factor
   * @param heightFactor the height scaling factor
   * @throws ImageProcessorException if the factors are out of range
   */
  private void validateScale(int widthFactor, int heightFactor) throws ImageProcessorException {
    if (widthFactor <= 0 || heightFactor <= 0 || widthFactor > 100 || heightFactor > 100) {
      throw new ImageProcessorException("Scaling factors must be within 0 and 100");
    }
  }
}