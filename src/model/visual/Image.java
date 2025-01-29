package model.visual;


import exception.ImageProcessorException;
import model.enumeration.CompressionType;
import model.enumeration.FilterOption;
import model.pixels.Pixel;

/**
 * Represents an image.
 * Whatever the file format (jpg, png, bmp, etc.)
 * an image is basically a sequence of pixels.
 */
public interface Image {
  /**
   * Get the pixel at the specified coordinates.
   *
   * @param x the x coordinate of the pixel
   * @param y the y coordinate of the pixel
   * @return the pixel at the specified coordinates
   */
  Pixel getPixel(int x, int y);

  /**
   * Adjusts the brightness of the image by a given factor.
   *
   * @param factor the factor to adjust the brightness by
   *               (positive values increase brightness, negative values
   *               decrease brightness)
   * @return the image with the adjusted brightness by creating a new image
   *         object.
   */
  Image adjustImageBrightness(int factor) throws ImageProcessorException;

  /**
   * Get the width of the image.
   * The width is the number of pixels in a row.
   *
   * @return the width of the image
   */
  int getWidth();

  /**
   * Get the height of the image.
   * The height is the number of rows in the image.
   *
   * @return the height of the image
   */
  int getHeight();

  /**
   * Creates a new image with the red component of the pixels.
   *
   * @return the image with the red component of the pixels by creating a new
   *         image object.
   */
  Image createRedComponent() throws ImageProcessorException;

  /**
   * Creates a new image with the green component of the pixels.
   *
   * @return the image with the green component of the pixels by creating a
   *         new image object.
   */
  Image createGreenComponent() throws ImageProcessorException;

  /**
   * Creates a new image with the blue component of the pixels.
   *
   * @return the image with the blue component of the pixels by creating a
   *         new image object.
   */
  Image createBlueComponent() throws ImageProcessorException;

  /**
   * Returns the luma of the image.
   * Luma has weights of 0.2126 for red, 0.7152 for green, and 0.0722 for blue.
   *
   * @return the luma of the image by creating a new image object.
   * @throws ImageProcessorException if the luma cannot be created
   */
  Image getLuma() throws ImageProcessorException;

  /**
   * Returns the sepia of the image.
   * Photographs taken in the 19th and early 20th century had a characteristic
   * reddish brown tone. This is referred to as a sepia tone.
   *
   * @return the sepia of the image by creating a new image object.
   * @throws ImageProcessorException if the sepia cannot be created
   */
  Image getSepia() throws ImageProcessorException;

  /**
   * Returns the intensity of the image.
   * This is the average of the three components for each pixel in an image.
   *
   * @return the intensity of the image by creating a new image object.
   * @throws ImageProcessorException if the intensity cannot be created
   */
  Image getIntensity() throws ImageProcessorException;

  /**
   * Returns the value of the image.
   * This is the maximum value of the three components for each pixel of the
   * image.
   *
   * @return the value of the image by creating a new image object.
   * @throws ImageProcessorException if the value cannot be created
   */
  Image getValue() throws ImageProcessorException;

  /**
   * Returns a new image with the horizontal flip of the original image.
   *
   * @return the image with the horizontal flip of the original image
   *         by creating a new image object.
   */
  Image horizontalFlip() throws ImageProcessorException;

  /**
   * Returns a new image with the vertical flip of the original image.
   *
   * @return the image with the vertical flip of the original image by
   *         creating a new image object.
   * @throws ImageProcessorException if the image cannot be flipped vertically
   */
  Image verticalFlip() throws ImageProcessorException;


  /**
   * Returns a new image with the filter applied to the original image.
   *
   * @param filterOption the filter to apply to the image
   * @return the image with the filter applied to the original image by
   *         creating a new image object.
   * @throws ImageProcessorException if the filter cannot be applied
   */
  Image applyFilter(FilterOption filterOption) throws ImageProcessorException;


  /**
   * Returns the red pixel values of the image as a 2D array.
   *
   * @return the red pixel values of the image as a 2D array.
   */
  int[][] getRedChannel();

  /**
   * Returns the green pixel values of the image as a 2D array.
   *
   * @return the green pixel values of the image as a 2D array.
   */
  int[][] getGreenChannel();

  /**
   * Returns the blue pixel values of the image as a 2D array.
   *
   * @return the blue pixel values of the image as a 2D array.
   */
  int[][] getBlueChannel();

  /**
   * Applies color correction to the image.
   *
   * @return the color-corrected image by creating a new image object.
   * @throws ImageProcessorException if the image cannot be color corrected
   */
  Image colorCorrect() throws ImageProcessorException;

  /**
   * Combines this image with another image provided in argument.
   *
   * @param image      the image to combine with
   * @param percentage the percentage of the first image(current image) on which
   *                   the function is called on.
   * @return the combined image by creating a new image object.
   * @throws ImageProcessorException if the images cannot be combined
   */
  Image combineImages(Image image, int percentage) throws ImageProcessorException;


  /**
   * Compresses the image by the given percentage.
   *
   * @param type       the type of compression to apply
   * @param percentage the percentage to compress the image by
   * @return the compressed image by creating a new image object.
   * @throws ImageProcessorException if the image cannot be compressed
   */
  Image compress(CompressionType type, int percentage) throws ImageProcessorException;

  /**
   * Creates a histogram image from the current image.
   *
   * @return a new Image representing the histogram of the current image
   * @throws ImageProcessorException if the histogram cannot be created
   */
  Image histogram() throws ImageProcessorException;

  /**
   * Adjusts the levels of the image.
   *
   * @param black the black point
   * @param mid   the mid point
   * @param white the white point
   * @return the image with adjusted levels by creating a new image object.
   * @throws ImageProcessorException if the image cannot have its levels
   *                                 adjusted
   */
  Image levelsAdjust(int black, int mid, int white) throws ImageProcessorException;

  /**
   * Resizes the image to the specified dimensions using bilinear interpolation.
   * @param newWidth the desired width of the resized image
   * @param newHeight the desired height of the resized image
   * @return a new RenderedImage with the specified dimensions
   * @throws ImageProcessorException if the new dimensions are invalid
   */
  Image downscale(int newWidth, int newHeight) throws ImageProcessorException;
}
