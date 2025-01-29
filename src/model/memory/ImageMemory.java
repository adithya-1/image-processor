package model.memory;

import exception.ImageProcessorException;

/**
 * A class that represents a memory that stores images.
 * The memory can store images and retrieve them by their name.
 * Memory is stored depending on the implementation.
 */
public interface ImageMemory<T> {

  /**
   * Adds an image to the memory.
   *
   * @param imageName the name of the image
   * @param image     the image to be added
   */
  void addImage(String imageName, T image);

  /**
   * Retrieves an image from the memory.
   *
   * @param imageName the name of the image to retrieve
   * @return the image with the given name
   * @throws ImageProcessorException.NotFoundException if the image with the
   *                                                   given name is not found
   */
  T getImage(String imageName) throws ImageProcessorException.NotFoundException;

  void clearMemory();

}
