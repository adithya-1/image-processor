package controller.services;

import exception.ImageProcessorException;
import model.request.ImageProcessingRequest;
import model.visual.Image;

/**
 * Service class to help with
 * all image processing operations.
 */
public interface ImageProcessingService {

  /**
   * Loads an image into memory using given name
   * from the given path.
   *
   * @param request the request to load the image.
   *                Contains the path and name of the image.
   * @throws ImageProcessorException if the image cannot be loaded.
   */
  void loadImage(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Saves an image from memory to the given path.
   *
   * @param request the request to save the image.
   *                Contains the path and name of the image.
   * @throws ImageProcessorException if the image cannot be saved.
   */
  void saveImage(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Creates the red component of the image.
   *
   * @param request the request to create the red component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the red component cannot be created.
   */
  void createRedComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Creates the green component of the image.
   *
   * @param request the request to create the green component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the green component cannot be created.
   */
  void createGreenComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Creates the blue component of the image.
   *
   * @param request the request to create the blue component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the blue component cannot be created.
   */
  void createBlueComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Creates the value component of the image.
   * This is the maximum value of the three components for each pixel of the
   * image.
   *
   * @param request the request to create the value component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the value component cannot be created.
   */
  void createValueComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Creates the luma component of the image.
   * Luma has weights of 0.2126 for red, 0.7152 for green, and 0.0722 for blue.
   *
   * @param request the request to create the luma component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the luma component cannot be created.
   */
  void createLumaComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Creates the intensity component of the image.
   * This is the average of the three components for each pixel in an image.
   *
   * @param request the request to create the intensity component.
   * @throws ImageProcessorException if the intensity component cannot be
   *                                 created.
   */
  void createIntensityComponent(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Flips the image horizontally.
   *
   * @param request the request to flip the image horizontally.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the image cannot be flipped vertically.
   */
  void horizontalFlip(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Flips the image vertically.
   *
   * @param request the request to flip the image vertically.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the image cannot be flipped vertically.
   */
  void verticalFlip(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Brightens the image by applying a factor to the image.
   *
   * @param request the request to brighten the image.
   *                Contains the name of the image, the destination image,
   *                and the factor to brighten the image by.
   * @throws ImageProcessorException if the image cannot be brightened.
   */
  void brighten(ImageProcessingRequest request)
          throws ImageProcessorException;

  /**
   * Split the RGB components of the image.
   *
   * @param request the request to split the RGB components.
   *                Contains the name of the image, and the names of the
   *                red, green, and blue images.
   * @throws ImageProcessorException if the RGB components cannot be split.
   */
  void rgbSplit(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Combines the RGB components of the image.
   *
   * @param request the request to combine the RGB components.
   *                Contains the names of the red, green, and blue images,
   *                and the destination image.
   * @throws ImageProcessorException if the RGB components cannot be combined.
   */
  void rgbCombine(ImageProcessingRequest request) throws ImageProcessorException;


  /**
   * Blurs the image by applying blurring filters.
   *
   * @param request the request to blur the image.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the image cannot be blurred.
   */
  void blurImage(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Sharpens the image by applying sharpening filters.
   *
   * @param request the request to sharpen the image.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the image cannot be sharpened.
   */
  void sharpenImage(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Creates the sepia component of the image.
   * Photographs taken in the 19th and early 20th century had a characteristic
   * reddish brown tone. This is referred to as a sepia tone.
   *
   * @param request the request to create the sepia component.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the sepia component cannot be created.
   */
  void sepiaImage(ImageProcessingRequest request) throws ImageProcessorException;


  /**
   * Compresses the image by the given percentage.
   *
   * @param request the request to compress the image.
   *                Contains the name of the image, the destination image,
   *                and the percentage to compress the image by.
   * @throws ImageProcessorException if the image cannot be compressed.
   */
  void compressImage(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Creates a histogram of the image.
   *
   * @param request the request to create a histogram of the image.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the histogram cannot be created.
   */
  void histogram(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Color corrects the image.
   *
   * @param request the request to color correct the image.
   *                Contains the name of the image and the destination image.
   * @throws ImageProcessorException if the image cannot be color corrected.
   */
  void colorCorrect(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Adjusts the levels of the image.
   *
   * @param request the request to adjust the levels of the image.
   *                Contains the name of the image, the destination image,
   *                and the levels to adjust the image by.
   * @throws ImageProcessorException if the image cannot have its levels
   *                                 adjusted.
   */
  void levelsAdjust(ImageProcessingRequest request) throws ImageProcessorException;

  /**
   * Downscale the image by the given percentage.
   *
   * @param request the request to downscale the image.
   *                Contains the name of the image, the destination image,
   *                and the percentage to downscale the image by.
   * @throws ImageProcessorException if the image cannot be downscaled.
   */
  void downscaleImage(ImageProcessingRequest request) throws ImageProcessorException;


  /**
   * Gets the image from memory using the given name.
   *
   * @param imageName the name of the image to get.
   * @return the image with the given name.
   * @throws ImageProcessorException if the image cannot be retrieved.
   */
  Image getImage(String imageName) throws ImageProcessorException;

  /**
   * Clears the memory present in the service.
   */
  void clearMemory();
}
