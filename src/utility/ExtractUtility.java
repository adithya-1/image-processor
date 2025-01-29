package utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import exception.ImageProcessorException;
import factories.Factory;
import model.pixels.Pixel;
import model.pixels.RGB;
import model.visual.Image;

/**
 * Utility class that contains methods to extract information from an image.
 */
public class ExtractUtility {
  private static final int HISTOGRAM_SIZE = 256;

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private ExtractUtility() {
    // Private constructor to prevent instantiation
  }

  /**
   * Creates a histogram image from the current image.
   *
   * @return a new Image representing the histogram of the current image
   * @throws ImageProcessorException if the histogram cannot be created
   */
  public static Image createHistogram(Image image) throws ImageProcessorException {
    int[] redFreq = calculateColorFrequencies(image, Pixel::getRed);
    int[] greenFreq = calculateColorFrequencies(image, Pixel::getGreen);
    int[] blueFreq = calculateColorFrequencies(image, Pixel::getBlue);

    int maxFreq = findMaxFrequency(redFreq, greenFreq, blueFreq);

    BufferedImage histogramImage = createHistogramImage(redFreq, greenFreq,
            blueFreq, maxFreq);

    Pixel[][] histogramPixels = convertBufferedImageToPixels(histogramImage);

    return Factory.createImage(histogramPixels);
  }

  /**
   * Calculates the frequency of a specific color channel in the given image.
   *
   * @param image          the image to analyze
   * @param transformation a function that extracts the color value from a pixel
   * @return an array representing the frequency of each color value (0-255)
   */
  public static int[] calculateColorFrequencies(Image image, Function<Pixel,
          Integer> transformation) {
    int[] colorFrequencies = new int[HISTOGRAM_SIZE];
    for (int column = 0; column < image.getWidth(); column++) {
      for (int row = 0; row < image.getHeight(); row++) {
        Pixel pixel = image.getPixel(row, column);
        colorFrequencies[transformation.apply(pixel)]++;
      }
    }
    return colorFrequencies;
  }

  /**
   * Finds the maximum frequency among the red, green, and blue frequency
   * arrays.
   *
   * @param redFreq   the frequency array for the red channel
   * @param greenFreq the frequency array for the green channel
   * @param blueFreq  the frequency array for the blue channel
   * @return the maximum frequency value among the three channels
   */
  private static int findMaxFrequency(int[] redFreq, int[] greenFreq,
                                      int[] blueFreq) {
    int maxFreq = 0;
    for (int i = 0; i < HISTOGRAM_SIZE; i++) {
      maxFreq = Math.max(maxFreq, redFreq[i]);
      maxFreq = Math.max(maxFreq, greenFreq[i]);
      maxFreq = Math.max(maxFreq, blueFreq[i]);
    }
    return maxFreq;
  }

  /**
   * Creates a histogram image from the red, green, and blue frequency arrays.
   *
   * @param redFreq   the frequency array for the red channel
   * @param greenFreq the frequency array for the green channel
   * @param blueFreq  the frequency array for the blue channel
   * @param maxFreq   the maximum frequency value among the three channels
   * @return a new BufferedImage representing the histogram
   */
  private static BufferedImage createHistogramImage(int[] redFreq,
                                                    int[] greenFreq,
                                                    int[] blueFreq,
                                                    int maxFreq) {
    BufferedImage histogramImage = new BufferedImage(HISTOGRAM_SIZE,
            HISTOGRAM_SIZE, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = histogramImage.createGraphics();

    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, HISTOGRAM_SIZE, HISTOGRAM_SIZE);

    g2d.setColor(Color.LIGHT_GRAY);
    for (int i = 0; i <= HISTOGRAM_SIZE; i += 13) {
      g2d.drawLine(i, 0, i, 256);
      g2d.drawLine(0, i, 256, i);
    }

    drawHistogramLine(g2d, Color.RED, redFreq, maxFreq);
    drawHistogramLine(g2d, Color.GREEN, greenFreq, maxFreq);
    drawHistogramLine(g2d, Color.BLUE, blueFreq, maxFreq);

    g2d.dispose();
    return histogramImage;
  }

  /**
   * Draws a histogram line for a specific color channel.
   *
   * @param g2d     the Graphics2D object used for drawing
   * @param color   the color of the histogram line
   * @param freq    the frequency array for the color channel
   * @param maxFreq the maximum frequency value among the three channels
   */
  private static void drawHistogramLine(Graphics2D g2d, Color color,
                                        int[] freq, int maxFreq) {
    g2d.setColor(color);
    for (int x = 0; x < 255; x++) {
      int currHeight = (int) ((freq[x] * (HISTOGRAM_SIZE - 1f)) / maxFreq);
      int nextHeight = (int) ((freq[x + 1] * (HISTOGRAM_SIZE - 1f)) / maxFreq);
      g2d.drawLine(x, HISTOGRAM_SIZE - 1 - currHeight, x + 1,
              HISTOGRAM_SIZE - 1 - nextHeight);
    }
  }

  /**
   * Converts a BufferedImage to a 2D array of Pixels.
   *
   * @param histogramImage the BufferedImage to convert
   * @return a 2D array of Pixels representing the image
   */
  private static Pixel[][] convertBufferedImageToPixels(BufferedImage histogramImage) {
    Pixel[][] histogramPixels = new Pixel[HISTOGRAM_SIZE][HISTOGRAM_SIZE];
    for (int y = 0; y < HISTOGRAM_SIZE; y++) {
      for (int x = 0; x < HISTOGRAM_SIZE; x++) {
        int rgb = histogramImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        histogramPixels[y][x] = new RGB(red, green, blue);
      }
    }
    return histogramPixels;
  }
}
