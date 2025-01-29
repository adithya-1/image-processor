package model.memory;

import java.util.Optional;

import exception.ImageProcessorException;

/**
 * A class that represents a memory that stores images in a String.
 */
public class StringMemory implements ImageMemory<String> {
  private Optional<String> image;

  /**
   * Constructs a StringMemory object.
   * By default, creates an empty String.
   */
  public StringMemory() {
    this.image = Optional.empty();
  }

  /**
   * For string memory, the image is replaced with the name of the image.
   *
   * @param imageName the name of the image
   * @param image     the image to be added
   */
  @Override
  public void addImage(String imageName, String image) {
    this.image = Optional.of(imageName);
  }

  /**
   * Returns the name of the image in memory.
   *
   * @param imageName this is not used in this implementation
   * @return Returns the name of the image in memory.
   * @throws ImageProcessorException.NotFoundException if the image with the
   *                                                   given name is not found
   */
  @Override
  public String getImage(String imageName) throws ImageProcessorException.NotFoundException {
    return image.orElseThrow(
        () -> new ImageProcessorException.NotFoundException("Image not "
        + "loaded in memory"));
  }

  /**
   * Clears the memory and sets image to empty.
   */
  @Override
  public void clearMemory() {
    this.image = Optional.empty();
  }
}
