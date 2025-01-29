package controller.services;

import java.util.Objects;

import exception.ImageProcessorException;
import factories.Factory;
import model.enumeration.CompressionType;
import model.enumeration.FilterOption;
import model.enumeration.ImageType;
import model.memory.ImageMemory;
import model.request.ImageProcessingRequest;
import model.visual.Image;
import utility.IOUtils;
import utility.StringUtils;

/**
 * FileImageProcessingService class that implements the
 * ImageProcessingService interface
 * and provides the implementation for the methods to process images.
 * It uses the ImageMemory object to store and retrieve images.
 */
public class FileImageProcessingService implements ImageProcessingService {

  /**
   * ImageMemory object to store and retrieve images.
   */
  private final ImageMemory<Image> memory;

  /**
   * Constructor to initialize the FileImageProcessingService.
   *
   * @param memory ImageMemory object
   * @throws NullPointerException if memory is null
   */
  public FileImageProcessingService(ImageMemory<Image> memory) {
    Objects.requireNonNull(memory, "Memory cannot be null");
    this.memory = memory;
  }

  @Override
  public void loadImage(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImagePath(), request.getImageName());
    ImageType imageType =
            ImageType.getImageTypeFromPath(request.getImagePath());
    Image imageToLoad = IOUtils.read(request.getImagePath(), imageType);
    memory.addImage(request.getImageName(), imageToLoad);
  }

  @Override
  public void saveImage(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImagePath(), request.getImageName());
    Image imageToSave = memory.getImage(request.getImageName());
    ImageType imageType =
            ImageType.getImageTypeFromPath(request.getImagePath());
    IOUtils.write(imageToSave, request.getImagePath(), imageType);
  }

  @Override
  public void createRedComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(),
            image.createRedComponent());
  }

  @Override
  public void createGreenComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(),
            image.createGreenComponent());
  }

  @Override
  public void createBlueComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(),
            image.createBlueComponent());
  }

  @Override
  public void createValueComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(), image.getValue());
  }

  @Override
  public void createLumaComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    Image finalImage = image.getLuma();
    if (request.getPercentage().isPresent()) {
      finalImage = finalImage.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), finalImage);
  }

  @Override
  public void createIntensityComponent(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(), image.getIntensity());
  }

  @Override
  public void horizontalFlip(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(), image.horizontalFlip());
  }


  @Override
  public void verticalFlip(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(), image.verticalFlip());
  }

  @Override
  public void brighten(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(),
            image.adjustImageBrightness(request.getFactor().orElse(0)));
  }

  @Override
  public void rgbSplit(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(
            request.getImageName(),
            request.getRedImageName(),
            request.getGreenImageName(),
            request.getBlueImageName()
    );
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getRedImageName(), image.createRedComponent());
    memory.addImage(request.getGreenImageName(), image.createGreenComponent());
    memory.addImage(request.getBlueImageName(), image.createBlueComponent());
  }

  @Override
  public void rgbCombine(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getRedImageName(),
            request.getGreenImageName(),
            request.getBlueImageName());
    Image redImage = memory.getImage(request.getRedImageName());
    Image greenImage = memory.getImage(request.getGreenImageName());
    Image blueImage = memory.getImage(request.getBlueImageName());
    Image combinedImage = Factory.combineRGBComponents(redImage, greenImage,
            blueImage);
    memory.addImage(request.getImageName(), combinedImage);
  }

  @Override
  public void blurImage(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    Image filteredImage = image.applyFilter(FilterOption.GAUSSIAN_BLUR);
    // If percentage is provided, combines the blur image with the
    // original image.
    if (request.getPercentage().isPresent()) {
      filteredImage = filteredImage.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), filteredImage);
  }

  @Override
  public void sharpenImage(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    Image filteredImage = image.applyFilter(FilterOption.SHARPEN);
    // If percentage is provided, combines the sharpen image with the
    // original image.
    if (request.getPercentage().isPresent()) {
      filteredImage = filteredImage.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), filteredImage);
  }

  @Override
  public void sepiaImage(ImageProcessingRequest request)
          throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    Image sepiaImage = image.getSepia();
    // If percentage is provided, combines the sepia image with the
    // original image.
    if (request.getPercentage().isPresent()) {
      sepiaImage = sepiaImage.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), sepiaImage);
  }

  @Override
  public void compressImage(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(),
            image.compress(CompressionType.HAAR,
                    request.getPercentage().orElse(0)));
  }

  @Override
  public void histogram(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    memory.addImage(request.getDestinationImageName(), image.histogram());
  }

  @Override
  public void colorCorrect(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    Image colorCorrect = image.colorCorrect();
    // If percentage is provided, combines the color correct image with the
    // original image.
    if (request.getPercentage().isPresent()) {
      colorCorrect = colorCorrect.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), colorCorrect);
  }

  @Override
  public void levelsAdjust(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    ImageProcessingRequest.Levels levels = request.getLevels().orElseThrow(
        () -> new ImageProcessorException("Levels not provided")
    );
    int black = levels.getBlack();
    int white = levels.getWhite();
    int mid = levels.getMid();
    Image levelsAdjust = image.levelsAdjust(black, mid, white);
    // If percentage is provided, combines the levels adjusted image with the
    // original image.
    if (request.getPercentage().isPresent()) {
      levelsAdjust = levelsAdjust.combineImages(image,
              request.getPercentage().get());
    }
    memory.addImage(request.getDestinationImageName(), levelsAdjust);
  }

  @Override
  public void downscaleImage(ImageProcessingRequest request) throws
          ImageProcessorException {
    validateStringParams(request.getImageName(),
            request.getDestinationImageName());
    Image image = memory.getImage(request.getImageName());
    ImageProcessingRequest.ScalingFactors factors =
            request.getScalingFactors().orElseThrow(
              () -> new ImageProcessorException("Scaling factors not provided")
    );
    memory.addImage(request.getDestinationImageName(),
            image.downscale(factors.getWidthFactor(),
                    factors.getHeightFactor()));
  }

  @Override
  public Image getImage(String imageName) throws
          ImageProcessorException {
    return memory.getImage(imageName);
  }

  @Override
  public void clearMemory() {
    memory.clearMemory();
  }

  /**
   * Validates the input string parameters.
   *
   * @param strings the input string parameters
   * @throws ImageProcessorException if the input string parameters are null
   *                                 or empty
   */
  private void validateStringParams(String... strings)
          throws
          ImageProcessorException {
    if (StringUtils.isNullOrEmpty(strings)) {
      throw new ImageProcessorException("Received input as null or empty");
    }
  }
}
