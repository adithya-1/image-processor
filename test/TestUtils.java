import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import exception.ImageProcessingRunTimeException;
import exception.ImageProcessorException;
import factories.Factory;
import model.enumeration.PixelType;
import model.pixels.Pixel;
import model.visual.Image;

/**
 * Utility class for testing.
 */
public class TestUtils {

  private TestUtils() {
    // Prevent instantiation
  }

  /**
   * Creates a red image.
   *
   * @return the red image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image redImage() throws
          ImageProcessorException {
    int[][] redArray = new int[][]{
            {16711680, 16711680},
            {16711680, 16711680}
    };
    return Factory.createImage(createPixels(redArray));
  }

  /**
   * Creates a uniform offset image.
   *
   * @return the uniform offset image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image uniformOffsetImage() throws ImageProcessorException {
    int[][] uniformArray = new int[][]{
            {6579558, 9869208},
            {9869208, 16448508}
    };

    return Factory.createImage(createPixels(uniformArray));
  }


  /**
   * Creates a random greyscale image.
   *
   * @return the random greyscale image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image randomGreyscaleImage() throws ImageProcessorException {
    int[][] uniformArray = new int[][]{
            {6579300, 9868950},
            {13158600, 16448250}
    };

    return Factory.createImage(createPixels(uniformArray));
  }

  /**
   * Creates a random image with edge distributed pixels.
   *
   * @return the random image with edge distributed pixels
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image randomImageWithEdgeDistributedPixels() throws ImageProcessorException {
    int[][] uniformArray = new int[][]{
            {622847, 8355969, 622847}
    };

    return Factory.createImage(createPixels(uniformArray));
  }

  /**
   * Creates a blue image.
   *
   * @return the blue image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image blueImage() throws
          ImageProcessorException {
    int[][] blueArray = new int[][]{
            {255, 255},
            {255, 255}
    };
    return Factory.createImage(createPixels(blueArray));
  }

  /**
   * Creates a green image.
   *
   * @return the green image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image greenImage() throws
          ImageProcessorException {
    int[][] greenArray = new int[][]{
            {65280, 65280},
            {65280, 65280}
    };
    return Factory.createImage(createPixels(greenArray));
  }

  /**
   * Creates a grey image.
   *
   * @return the grey image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image greyImage() throws
          ImageProcessorException {
    int[][] greyArray = new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    };
    return Factory.createImage(createPixels(greyArray));
  }

  /**
   * Creates a white image.
   *
   * @return the white image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image whiteImage() throws
          ImageProcessorException {
    int[][] whiteArray = new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    };
    return Factory.createImage(createPixels(whiteArray));
  }

  /**
   * Creates a black image.
   *
   * @return the black image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image blackImage() throws
          ImageProcessorException {
    int[][] blackArray = new int[][]{
            {0, 0},
            {0, 0}
    };
    return Factory.createImage(createPixels(blackArray));
  }

  /**
   * Creates a random image.
   *
   * @return the random image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image randomImage() throws
          ImageProcessorException {
    int[][] randomArray = new int[][]{
            {16711680, 255},
            {65280, 8421504}
    };
    return Factory.createImage(createPixels(randomArray));
  }

  /**
   * Creates a random rectangle image.
   *
   * @return the random rectangle image
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Image randomRectangleImage() throws
          ImageProcessorException {
    int[][] randomArray = new int[][]{
            {16711680, 255, 65280},
            {8421504, 16711680, 255}
    };
    return Factory.createImage(createPixels(randomArray));
  }

  /**
   * Create a Pixel array with the given 2D array.
   *
   * @return the 2D pixel array
   * @throws ImageProcessorException if the image cannot be created
   */
  public static Pixel[][] createPixels(int[][] array) {
    try {
      Pixel[][] pixels = new Pixel[array.length][array[0].length];
      for (int i = 0; i < array.length; i++) {
        for (int j = 0; j < array[0].length; j++) {
          pixels[i][j] = Factory.createPixel(array[i][j], PixelType.RGB);
        }
      }
      return pixels;
    } catch (ImageProcessorException e) {
      throw new ImageProcessingRunTimeException("Invalid pixel type");
    }
  }

  /**
   * Clean up the output directory by deleting it.
   *
   * @param path the path to the directory
   */
  public static void cleanUp(String path) {
    try {
      deleteDirectory(Paths.get(path));
    } catch (IOException e) {
      try {
        deleteDirectory(Paths.get(path));
      } catch (IOException e1) {
        System.out.println("Error deleting output directory");
      }
    }
  }

  /**
   * Delete a directory.
   *
   * @param path the path to the directory
   * @throws IOException if the directory cannot be deleted
   */
  private static void deleteDirectory(Path path) throws
          IOException {
    if (Files.exists(path)) {
      Files.walk(path)
              .sorted(Comparator.reverseOrder())
              .map(Path::toFile)
              .forEach(File::delete);
    }
  }
}
