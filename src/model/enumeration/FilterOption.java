package model.enumeration;

/**
 * Enum representing the different filter options available.
 */
public enum FilterOption {
  GAUSSIAN_BLUR(new double[][]{
          {1.0 / 16, 1.0 / 8, 1.0 / 16},
          {1.0 / 8, 1.0 / 4, 1.0 / 8},
          {1.0 / 16, 1.0 / 8, 1.0 / 16}
  }),

  SHARPEN(new double[][]{
          {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
          {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
          {-1.0 / 8, 1.0 / 4, 1, 1.0 / 4, -1.0 / 8},
          {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
          {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
  });

  /**
   * The kernel for the filter.
   * Kernel is used to apply the filter to the image
   * by multiplying the pixel values with the kernel values.
   */
  private final double[][] kernel;

  /**
   * Constructs a FilterOption with the given kernel.
   *
   * @param kernel the kernel for the filter
   */
  FilterOption(double[][] kernel) {
    this.kernel = kernel;
  }

  /**
   * Gets the kernel for the filter.
   *
   * @return the kernel for the filter
   */
  public double[][] getKernel() {
    return kernel;
  }
}