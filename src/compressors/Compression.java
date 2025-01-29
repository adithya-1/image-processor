package compressors;

import exception.ImageProcessorException;
import model.visual.Image;

/**
 * Interface to represent the compression of an image.
 * This interface provides the method to compress an image.
 * The compression is done by reducing the size of the image
 * by a given percentage.
 */
public interface Compression {

  /**
   * Compresses the given image by the given percentage.
   *
   * @param image      the image to compress
   * @param percentage the percentage by which to compress the image
   * @return the new compressed image
   * @throws ImageProcessorException if the image cannot be compressed
   */
  Image compress(Image image, int percentage) throws ImageProcessorException;
}
