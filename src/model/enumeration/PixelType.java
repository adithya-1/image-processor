package model.enumeration;

import java.awt.image.BufferedImage;

import exception.ImageProcessorException;

/**
 * Enum representing the type of pixel.
 */
public enum PixelType {

  RGB;


  /**
   * Get the pixel type from the BufferedImage type.
   *
   * @param type BufferedImage type.
   * @return PixelType object.
   * @throws ImageProcessorException.NotImplementedException
   * if the pixel type is not supported.
   */
  public static PixelType fromBufferedImageType(int type)
          throws ImageProcessorException.NotImplementedException {
    switch (type) {
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_BYTE_BINARY:
        return RGB;
      default:
        throw new ImageProcessorException.NotImplementedException(
                String.format("Received an unsupported image type: %s", type)
        );
    }
  }
}
