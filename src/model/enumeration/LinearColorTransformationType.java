package model.enumeration;

/**
 * Represents the type of linear transformation
 * that can be applied to an image.
 */
public enum LinearColorTransformationType {


  LUMA(new double[][]{
          {0.2126, 0.7152, 0.0722},
          {0.2126, 0.7152, 0.0722},
          {0.2126, 0.7152, 0.0722}
  }),
  SEPIA(new double[][]{
          {0.393, 0.769, 0.189},
          {0.349, 0.686, 0.168},
          {0.272, 0.534, 0.131}
  });
  /**
   * The kernel for the linear transformation.
   * The kernel is a 3x3 matrix that is multiplied.
   */
  private final double[][] kernel;

  LinearColorTransformationType(double[][] kernel) {
    this.kernel = kernel;
  }

  /**
   * Gets the kernel for the linear transformation.
   *
   * @return the kernel for the linear transformation
   */
  public double[][] getKernel() {
    return kernel;
  }
}
