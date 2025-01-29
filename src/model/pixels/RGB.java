package model.pixels;

import java.util.Objects;


import model.enumeration.LinearColorTransformationType;

/**
 * Represents a pixel in the RGB color space.
 * It has three channels: red, green, and blue.
 * Each channel has a value between 0 and 255(8*3 bits).
 */
public class RGB extends AbstractPixel {
  /**
   * The red channel of the pixel.
   * Used integer to store the value of the red channel.
   * This can have value between 0 and 255.
   *
   */
  private final int red;
  /**
   * The green channel of the pixel.
   * Used integer to store the value of the green channel.
   * This can have value between 0 and 255.
   */
  private final int green;
  /**
   * The blue channel of the pixel.
   * Used integer to store the value of the blue channel.
   * This can have value between 0 and 255.
   */
  private final int blue;

  /**
   * Constructs an RGB pixel with the given red, green, and blue values.
   * If the given values are less than 0, the value is set to 0.
   * If the given values are greater than 255, the value is set to 255.
   *
   * @param red   the red channel of the pixel
   * @param green the green channel of the pixel
   * @param blue  the blue channel of the pixel
   */
  public RGB(int red, int green, int blue) {
    super(24);
    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  /**
   * Matrix operation to multiply a kernel with RGB values
   * to get a new RGB pixel.
   *
   * @param pixel  the pixel to be multiplied with the kernel
   * @param colorTransformationType the type of color transformation to be applied
   * @return the new RGB pixel after the matrix operation.
   */
  private static Pixel matrixOperation(
          RGB pixel,
          LinearColorTransformationType colorTransformationType
  ) {
    int r = pixel.getRed();
    int g = pixel.getGreen();
    int b = pixel.getBlue();
    double[][] kernel = colorTransformationType.getKernel();
    double rPrime = kernel[0][0] * r + kernel[0][1] * g + kernel[0][2] * b;
    double gPrime = kernel[1][0] * r + kernel[1][1] * g + kernel[1][2] * b;
    double bPrime = kernel[2][0] * r + kernel[2][1] * g + kernel[2][2] * b;
    return new RGB((int) rPrime, (int) gPrime, (int) bPrime);
  }

  @Override
  public int getRed() {
    return red;
  }

  @Override
  public int getGreen() {
    return green;
  }

  @Override
  public int getBlue() {
    return blue;
  }

  @Override
  public Pixel createPixel(int red, int green, int blue) {
    return new RGB(red, green, blue);
  }

  @Override
  public Pixel createRedComponent() {
    return this.createPixel(this.red, this.red, this.red);
  }

  @Override
  public Pixel createGreenComponent() {
    return this.createPixel(this.green, this.green, this.green);
  }

  @Override
  public Pixel createBlueComponent() {
    return this.createPixel(this.blue, this.blue, this.blue);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof RGB)) {
      return false;
    }
    RGB rgb = (RGB) obj;
    return rgb.equalsRGB(this);
  }

  protected boolean equalsRGB(RGB rgb) {
    return red == rgb.getRed() && green == rgb.getGreen() && blue == rgb.getBlue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(red, green, blue);
  }

  @Override
  public String toString() {
    return "RGB(" + red + ", " + green + ", " + blue + ")";
  }

  @Override
  public Pixel adjustBrightness(int factor) {
    int newRed = getRed() + factor;
    int newGreen = getGreen() + factor;
    int newBlue = getBlue() + factor;
    return this.createPixel(newRed, newGreen, newBlue);
  }

  @Override
  public Pixel getIntensity() {
    int intensity = (getRed() + getGreen() + getBlue()) / 3;
    return this.createPixel(intensity, intensity, intensity);
  }

  @Override
  public Pixel getValue() {
    int value = Math.max(Math.max(getRed(), getGreen()), getBlue());
    return this.createPixel(value, value, value);
  }

  @Override
  public Pixel getLuma() {
    return matrixOperation(this, LinearColorTransformationType.LUMA);
  }

  @Override
  public Pixel getSepia() {
    return matrixOperation(this, LinearColorTransformationType.SEPIA);
  }


  @Override
  public Pixel quadraticTransform(double coeffA, double coeffB, double coeffC) {
    int newRed = applyQuadraticTransform(getRed(), coeffA, coeffB, coeffC);
    int newGreen = applyQuadraticTransform(getGreen(), coeffA, coeffB, coeffC);
    int newBlue = applyQuadraticTransform(getBlue(), coeffA, coeffB, coeffC);
    return this.createPixel(newRed, newGreen, newBlue);
  }

  /**
   * Applies the quadratic transformation to a single color value.
   *
   * @param value The original color value
   * @param coeffA Quadratic coefficient a
   * @param coeffB Quadratic coefficient b
   * @param coeffC Quadratic coefficient c
   * @return The transformed color value, clamped between 0 and 255
   */
  private int applyQuadraticTransform(int value, double coeffA, double coeffB, double coeffC) {
    double result = coeffA * Math.pow(value, 2) + coeffB * value + coeffC;

    return (int) Math.round(result);
  }

  /**
   * Clamps a value between 0 and 255.
   *
   * @param value the value to clamp
   * @return the clamped value
   */
  private int clamp(int value) {
    return Math.max(0, Math.min(computeMaxValue(), value));
  }
}