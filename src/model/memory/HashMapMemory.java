package model.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import exception.ImageProcessorException;
import model.visual.Image;
import utility.StringUtils;

/**
 * A class that represents a memory that stores images in a HashMap.
 */
public class HashMapMemory implements ImageMemory<Image> {
  /**
   * A map that stores images.
   * The key is the name of the image and the value is the image.
   * Used hashmap for faster access of image.
   */
  private final Map<String, Image> memory;

  /**
   * Constructs a HashMapMemory object.
   * By default, creates an empty HashMap.
   */
  public HashMapMemory() {
    this.memory = new HashMap<>();
  }


  @Override
  public void addImage(String imageName, Image image) {
    if (Objects.isNull(image) || StringUtils.isNullOrEmpty(imageName)) {
      return;
    }
    memory.put(imageName, image);
  }

  @Override
  public Image getImage(String imageName) throws ImageProcessorException.NotFoundException {
    return Optional.ofNullable(memory.get(imageName)).orElseThrow(
        () -> new ImageProcessorException.NotFoundException(
                    String.format("Image with name %s not found in memory",
                            imageName)
        )
    );
  }

  @Override
  public void clearMemory() {
    memory.clear();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof HashMapMemory)) {
      return false;
    }
    HashMapMemory that = (HashMapMemory) obj;
    return memory.equals(that.memory);
  }

  @Override
  public int hashCode() {
    return memory.hashCode();
  }
}
