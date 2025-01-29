package utility;

import java.util.Objects;

import exception.ImageProcessorException;
import factories.Factory;
import model.enumeration.FilterOption;
import model.pixels.Pixel;
import model.visual.Image;

/**
 * Utility class for applying filters to an image.
 * Provides a method to apply a filter to an image.
 */
public class FilterUtils {

  private FilterUtils() {
    //Empty private constructor to prevent instantiation.
  }

  /**
   * Applies a filter to an image.
   * The filter is applied by involving the image with the filter's kernel.
   * For each pixel in the image, the kernel is centered on the pixel, and the
   * surrounding pixels are multiplied by the corresponding kernel values.
   * The results are summed to produce the new pixel value.
   *
   * @param image        the image to apply the filter to
   * @param filterOption the filter option to apply
   * @return the image with the filter applied
   * @throws ImageProcessorException if the filter cannot be applied
   */
  public static Image applyFilter(Image image, FilterOption filterOption)
          throws ImageProcessorException {
    Objects.requireNonNull(image);
    Objects.requireNonNull(filterOption);
    int width = image.getWidth();
    int height = image.getHeight();
    int radius = filterOption.getKernel().length / 2;
    Pixel[][] newPixelArray = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {

        double redSum = 0;
        double greenSum = 0;
        double blueSum = 0;

        for (int ky = -radius; ky <= radius; ky++) {
          for (int kx = -radius; kx <= radius; kx++) {
            int pixelX = row + kx;
            int pixelY = col + ky;
            if (pixelX >= 0 && pixelX < height && pixelY >= 0 && pixelY < width) {
              Pixel pixel = image.getPixel(pixelX, pixelY);
              double kernelValue =
                      filterOption.getKernel()[ky + radius][kx + radius];

              redSum += pixel.getRed() * kernelValue;
              greenSum += pixel.getGreen() * kernelValue;
              blueSum += pixel.getBlue() * kernelValue;
            }
          }
        }
        newPixelArray[row][col] = image.getPixel(row, col)
                .createPixel((int) redSum, (int) greenSum, (int) blueSum);
      }
    }

    return Factory.createImage(newPixelArray);
  }
}
