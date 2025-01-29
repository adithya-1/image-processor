package model.pixels;

/**
 * This class represents an abstract pixel.
 * It implements the Pixel interface.
 * It contains the bits of the pixel which indicates
 * the number of bits used to represent the pixel.
 */
public abstract class AbstractPixel implements Pixel {

  /**
   * The number of bits used to represent the pixel.
   */
  protected final int bits;

  /**
   * Constructs an abstract pixel with the given bits.
   *
   * @param bits the number of bits used to represent the pixel
   */
  protected AbstractPixel(int bits) {
    this.bits = bits;
  }

  /**
   * Computes the maximum value that can be represented by the pixel.
   *
   * @return the maximum value that can be represented by the pixel
   */
  protected int computeMaxValue() {
    return (1 << (this.bits / 3)) - 1;
  }
}
