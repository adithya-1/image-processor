package controller;

/**
 * Features interface that represents the features provided by the image
 * processing application.
 */
public interface Features {
  /**
   * Loads an image into memory from disk.
   */
  void loadImage();

  /**
   * Saves the image to the disk.
   */
  void saveImage();

  /**
   * Creates the sepia component of the image.
   * Photographs taken in the 19th and early 20th century had a characteristic
   * reddish brown tone. This is referred to as a sepia tone.
   */
  void applySepia();

  /**
   * Clears the memory of the image processing application.
   */
  void reset();

  /**
   * Creates the red component of the image.
   */
  void redComponent();

  /**
   * Creates the green component of the image.
   */
  void greenComponent();

  /**
   * Creates the blue component of the image.
   */
  void blueComponent();


  /**
   * Blurs the image by applying guassian blurring filters.
   */
  void blurImage();

  /**
   * Sharpens the image by applying sharpening filters.
   */
  void sharpenImage();


  /**
   * Compresses the image by the given percentage.
   */
  void compressImage();

  /**
   * Performs the required operations before closing the window.
   */
  void closeWindow();


  /**
   * Flips the image vertically.
   */
  void verticalFlip();

  /**
   * Flips the image horizontally.
   */
  void horizontalFlip();

  /**
   * Gets the luma of the image.
   */
  void getLuma();

  /**
   * Color corrects the image.
   */
  void colorCorrect();

  /**
   * Adjusts the levels of the image.
   */
  void levelsAdjust();

  /**
   * Downscale the image.
   */
  void downscaleImage();
}