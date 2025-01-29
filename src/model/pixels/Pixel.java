package model.pixels;



/**
 * Represents a pixel in an image.
 * Each pixel has a position in the image (row, column) and a color.
 */
public interface Pixel {

  /**
   * Returns the value of the pixel.
   * This is the maximum value of the three components for each pixel.
   *
   * @return the value of the pixel by creating a new pixel object.
   */
  Pixel getValue();

  /**
   * Returns the luma of the pixel.
   * Luma has weights of 0.2126 for red, 0.7152 for green, and 0.0722 for blue.
   *
   * @return the luma of the pixel by creating a new pixel object.
   */
  Pixel getLuma();

  /**
   * Returns the sepia of the pixel.
   * Photographs taken in the 19th and early 20th century had a characteristic
   * reddish brown tone. This is referred to as a sepia tone.
   *
   * @return the sepia of the pixel by creating a new pixel object.
   */
  Pixel getSepia();

  /**
   * Returns the intensity of the pixel.
   * This is the average of the three components for each pixel
   *
   * @return the intensity of the pixel by creating a new pixel object.
   */
  Pixel getIntensity();

  /**
   * Adjusts the brightness of the pixel by a given factor.
   *
   * @param factor the factor to adjust the brightness by
   *               (positive values increase brightness, negative values decrease brightness)
   * @return the pixel with the adjusted brightness by creating a new pixel object.
   */
  Pixel adjustBrightness(int factor);


  /**
   * Creates a pixel with the red component of the pixel.
   *
   * @return the pixel with the red component of the pixel by creating a new pixel object.
   */
  Pixel createRedComponent();

  /**
   * Creates a pixel with the green component of the pixel.
   *
   * @return the pixel with the green component of the pixel by creating a new pixel object.
   */
  Pixel createGreenComponent();

  /**
   * Creates a pixel with the blue component of the pixel.
   *
   * @return the pixel with the blue component of the pixel by creating a new pixel object.
   */
  Pixel createBlueComponent();

  /**
   * Gets the red component of the pixel.
   *
   * @return the red component of the pixel
   */
  int getRed();

  /**
   * Gets the green component of the pixel.
   *
   * @return the green component of the pixel
   */
  int getGreen();

  /**
   * Gets the blue component of the pixel.
   *
   * @return the blue component of the pixel
   */
  int getBlue();


  /**
   * Creates a pixel with the given red, green, and blue components.
   *
   * @param red   the red component of the pixel
   * @param green the green component of the pixel
   * @param blue  the blue component of the pixel
   * @return the pixel with the given red, green, and blue components
   *         by creating a new pixel object.
   */
  Pixel createPixel(int red, int green, int blue);


  /**
   * Applies the quadratic transformation to a Pixel.
   * The quadratic transformation is defined as:
   * f(x) = Ax^2 + Bx + C
   * where A, B, and C are the coefficients of the transformation.
   *
   * @param coeffA the coefficient A of the quadratic transformation.
   * @param coeffB the coefficient B of the quadratic transformation.
   * @param coeffC the coefficient C of the quadratic transformation.
   *
   * @return the pixel with the quadratic transformation applied by creating a new pixel object.
   */
  Pixel quadraticTransform(double coeffA, double coeffB, double coeffC);
}
