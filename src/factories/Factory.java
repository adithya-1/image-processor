package factories;

import java.util.Objects;

import app.parsers.ArgumentParser;
import app.parsers.CommandLineArgumentParser;
import app.parsers.GUIArgumentParser;
import app.parsers.InteractiveArgumentParser;
import compressors.Compression;
import compressors.HaarCompression;
import exception.ImageProcessingRunTimeException;
import exception.ImageProcessorException;
import model.enumeration.CompressionType;
import model.enumeration.PixelType;
import model.memory.ImageMemory;
import model.pixels.Pixel;
import model.pixels.RGB;
import model.visual.Image;
import model.visual.RenderedImage;
import controller.services.FileImageProcessingService;
import controller.services.ImageProcessingService;
import utility.StringUtils;

/**
 * Factory class to create objects for the Image Processor application.
 */
public class Factory {
  private Factory() {
    //Empty private constructor to prevent instantiation.
  }



  /**
   * Creates an Image object with the given pixel array.
   *
   * @param pixels the pixel array to create the image
   * @return the image object with the given pixel array
   * @throws ImageProcessorException if the pixel array is null
   */
  public static Image createImage(Pixel[][] pixels) throws
          ImageProcessorException {
    return new RenderedImage(pixels);
  }


  /**
   * Combines the red, green, and blue components of an image to create a new
   * image.
   *
   * @param redComponent   the red component of the image
   * @param greenComponent the green component of the image
   * @param blueComponent  the blue component of the image
   * @return the new image with the combined RGB components
   * @throws ImageProcessorException if the RGB components do not have the
   *                                 same dimensions
   */
  public static Image combineRGBComponents(Image redComponent,
                                           Image greenComponent,
                                           Image blueComponent)
          throws
          ImageProcessorException {
    validateRGBComponents(redComponent, greenComponent, blueComponent);
    int height = redComponent.getHeight();
    int width = redComponent.getWidth();
    Pixel[][] newPixelArray = new Pixel[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        newPixelArray[row][col] =
                createRGBPixel(redComponent.getPixel(row, col).getRed(),
                        greenComponent.getPixel(row, col).getGreen(),
                        blueComponent.getPixel(row, col).getBlue());
      }
    }
    return createImage(newPixelArray);
  }

  /**
   * Validates the given RGB components of an image.
   *
   * @param redComponent   the red component of the image
   * @param greenComponent the green component of the image
   * @param blueComponent  the blue component of the image
   * @throws ImageProcessorException if the RGB components do not have the
   *                                 same dimensions
   * @throws NullPointerException    if any of the RGB components are null
   */
  private static void validateRGBComponents(Image redComponent,
                                            Image greenComponent,
                                            Image blueComponent)
          throws
          ImageProcessorException,
          NullPointerException {
    Objects.requireNonNull(redComponent, "Red component cannot be null");
    Objects.requireNonNull(greenComponent, "Green component cannot be null");
    Objects.requireNonNull(blueComponent, "Blue component cannot be null");
    if (redComponent.getWidth() != greenComponent.getWidth()
            || redComponent.getWidth() != blueComponent.getWidth()
            || redComponent.getHeight() != greenComponent.getHeight()
            || redComponent.getHeight() != blueComponent.getHeight()) {
      throw new ImageProcessorException("The RGB components must have the "
              + "same dimensions");
    }
  }

  /**
   * Creates an ImageProcessingService object to process images.
   *
   * @param memory the memory to store images
   * @return the ImageProcessingService object
   */
  public static ImageProcessingService createImageProcessor(ImageMemory<Image> memory) {
    return new FileImageProcessingService(memory);
  }


  /**
   * Creates a pixel based on the given pixel and type.
   *
   * @param pixel the pixel to create the pixel
   * @param type  the type of the pixel
   * @return the pixel based on the given pixel and type
   * @throws ImageProcessorException.NotImplementedException if the pixel is
   *                                                         not implemented
   */
  public static Pixel createPixel(int pixel, PixelType type)
          throws
          ImageProcessorException.NotImplementedException {
    if (type == PixelType.RGB) {
      return createRGBPixel(pixel);
    } else {
      throw new ImageProcessorException.NotImplementedException(
              String.format("Received an unsupported image type: %s", type)
      );
    }
  }


  /**
   * Creates an RGB pixel based on the given pixel.
   *
   * @param pixel the pixel to create the RGB pixel
   * @return the RGB pixel based on the given pixel
   */
  private static Pixel createRGBPixel(int pixel) {
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = pixel & 0xff;
    return createRGBPixel(red, green, blue);
  }

  /**
   * Creates an RGB pixel based on the given red, green, and blue components.
   *
   * @param red   the red component of the pixel
   * @param green the green component of the pixel
   * @param blue  the blue component of the pixel
   * @return the RGB pixel based on the given red, green, and blue components
   */
  public static Pixel createRGBPixel(int red, int green, int blue) {
    return new RGB(red, green, blue);
  }




  /**
   * Creates a Compression object based on the given type.
   *
   * @param type the type of the compression
   * @return the Compression object based on the given type
   * @throws ImageProcessorException.NotImplementedException if the compression
   *                                                         type is not
   *                                                         implemented
   */
  public static Compression createCompression(CompressionType type)
          throws
          ImageProcessorException.NotImplementedException {

    if (CompressionType.HAAR.equals(type)) {
      return new HaarCompression();
    }
    throw new ImageProcessorException
            .NotImplementedException(String.format("Compression type:%s not "
            + "implemented", type));
  }


  /**
   * Combines the given images based on the given percentage.
   * The first image occupies the given percentage of the new image.
   *
   * @param firstImage  the first image to combine
   * @param secondImage the second image to combine
   * @param percentage  the percentage of the first image in the new image
   * @return the new image with the combined images
   * @throws ImageProcessorException if the images do not have the same
   *                                 dimensions or the percentage is invalid
   */
  public static Image combineImage(Image firstImage, Image secondImage,
                                   int percentage) throws
          ImageProcessorException {
    validateImageDimensions(firstImage, secondImage);
    validatePercentage(percentage);
    int height = firstImage.getHeight();
    int width = firstImage.getWidth();
    Pixel[][] newPixelArray = new Pixel[height][width];
    int widthWithPercentage = width * percentage / 100;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < widthWithPercentage; col++) {
        newPixelArray[row][col] = firstImage.getPixel(row, col);
      }
      for (int col = widthWithPercentage; col < width; col++) {
        newPixelArray[row][col] = secondImage.getPixel(row, col);
      }
    }
    return createImage(newPixelArray);
  }

  /**
   * Validates the dimensions of the given images.
   *
   * @param firstImage  the first image to validate
   * @param secondImage the second image to validate
   * @throws ImageProcessorException if the images do not have the same
   *                                 dimensions
   */
  private static void validateImageDimensions(Image firstImage,
                                              Image secondImage) throws
          ImageProcessorException {
    if (firstImage.getWidth() != secondImage.getWidth()
            || firstImage.getHeight() != secondImage.getHeight()) {
      throw new ImageProcessorException("The images must have the same "
              + "dimensions");
    }
  }

  /**
   * Validates the given percentage.
   * The percentage must be between 0 and 100 inclusive.
   *
   * @param percentage the percentage to validate
   * @throws ImageProcessorException if the percentage is invalid
   */
  private static void validatePercentage(int percentage) throws
          ImageProcessorException {
    if (percentage < 0 || percentage > 100) {
      throw new ImageProcessorException("The percentage must be between 0 and"
              + " 100");
    }
  }


  /**
   * Creates an ArgumentParser object based on the given command line arguments.
   *
   * @param args the command line arguments
   * @return the ArgumentParser object based on the given command line arguments
   * @throws ImageProcessingRunTimeException.QuitException if there is an error
   *                                                      creating the parser
   */
  public static ArgumentParser getArgumentParser(String[] args) throws
          ImageProcessingRunTimeException.QuitException {
    if (containsCommandLineArgs(args)) {
      String firstArgument = args[0];
      if (firstArgument.equals("-file")) {
        return new CommandLineArgumentParser();
      } else if (firstArgument.equals("-text")) {
        return new InteractiveArgumentParser();
      } else {
        throw new ImageProcessingRunTimeException.QuitException(
                String.format("Invalid argument provided%s", firstArgument));
      }
    } else {
      return new GUIArgumentParser();
    }
  }

  /**
   * Checks if the command line arguments are provided.
   *
   * @param args The command line arguments.
   * @return true if the command line arguments are provided, false otherwise.
   */
  private static boolean containsCommandLineArgs(String[] args) {
    return args.length > 0 && StringUtils.isNotNullOrEmpty(args[0]);
  }
}
