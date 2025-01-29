package controller;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import exception.ImageProcessingRunTimeException;
import exception.ImageProcessorException;
import model.enumeration.UserCommand;
import model.request.ImageProcessingRequest;
import controller.services.ImageProcessingService;
import utility.StringUtils;
import view.DisplayMessageType;
import view.text.TextInput;
import view.text.TextOutput;

/**
 * InteractiveImageProcessorController class that implements the
 * ImageProcessorController interface and processes the commands entered by
 * the user.
 * It handles communication with user along with controlling the model.
 */
public class InteractiveImageProcessorController implements ImageProcessorController {

  /**
   * Comment prefix to ignore the comments in the script file.
   */
  private static final String COMMENT_PREFIX = "#";


  /**
   * Split command to indicate the optional split percentage.
   */
  private static final String SPLIT_COMMAND = "split";

  /**
   * TextInput used to communicate with user to get inputs.
   */
  protected final TextInput textInput;


  /**
   * TextOutput used to communicate with user to display results.
   */
  private final TextOutput textOutput;


  /**
   * ImageProcessingService object to process the image.
   */
  private final ImageProcessingService imageProcessingService;

  /**
   * Constructor to initialize the SimpleImageProcessorController.
   * Initializes and displays the commands to the user.
   *
   * @param textInput              input used to interact with the user to
   *                               get inputs.
   * @param textOutput             output used to interact with the user to
   *                               display results.
   * @param imageProcessingService ImageProcessingService object.
   * @throws NullPointerException if textInput, textOutput or imageProcessor is
   *                              null
   */
  public InteractiveImageProcessorController(TextInput textInput,
                                             TextOutput textOutput,
                                             ImageProcessingService imageProcessingService) {
    validateInput(textInput, textOutput, imageProcessingService);
    this.textInput = textInput;
    this.textOutput = textOutput;
    this.imageProcessingService = imageProcessingService;
    displayCommands();
  }

  /**
   * Validates the input given to the controller.
   *
   * @param textInput      TextInput object.
   * @param textOutput     TextOutput object.
   * @param imageProcessor ImageProcessingService object.
   * @throws NullPointerException if input, output or imageProcessor is null.
   */
  private void validateInput(TextInput textInput,
                             TextOutput textOutput,
                             ImageProcessingService imageProcessor)
          throws
          ImageProcessingRunTimeException.QuitException {
    Objects.requireNonNull(textInput, "TextInput cannot be null");
    Objects.requireNonNull(imageProcessor, "ImageProcessor cannot be null");
    Objects.requireNonNull(textOutput, "TextOutput cannot be null");
  }

  /**
   * Displays the commands to the user.
   */
  private void displayCommands() {
    textOutput.displayCommands(
            List.of(
                    UserCommand.LOAD,
                    UserCommand.SAVE,
                    UserCommand.RED_COMPONENT,
                    UserCommand.GREEN_COMPONENT,
                    UserCommand.BLUE_COMPONENT,
                    UserCommand.VALUE_COMPONENT,
                    UserCommand.LUMA_COMPONENT,
                    UserCommand.INTENSITY_COMPONENT,
                    UserCommand.HORIZONTAL_FLIP,
                    UserCommand.VERTICAL_FLIP,
                    UserCommand.BRIGHTEN,
                    UserCommand.RGB_SPLIT,
                    UserCommand.RGB_COMBINE,
                    UserCommand.BLUR,
                    UserCommand.SHARPEN,
                    UserCommand.SEPIA,
                    UserCommand.RUN,
                    UserCommand.COMPRESS,
                    UserCommand.HISTOGRAM,
                    UserCommand.COLOR_CORRECT,
                    UserCommand.LEVELS_ADJUST,
                    UserCommand.RESET,
                    UserCommand.HELP,
                    UserCommand.QUIT
            )
    );
  }

  @Override
  public void processCommands() throws
          ImageProcessingRunTimeException.QuitException {
    execute(textInput.getUserInput());
  }


  private void execute(Readable readable) {
    try {
      Scanner scanner = new Scanner(readable);
      while (scanner.hasNext()) {
        String userInput = scanner.next();
        Optional<UserCommand> command = UserCommand.getCommand(userInput);
        if (command.isPresent()) {
          ExecutionStatus status = executeCommand(command.get(), scanner);
          displayMessage(status.getMessage(),
                  status.isSuccess() ? DisplayMessageType.INFO :
                          DisplayMessageType.ERROR
          );
        } else {
          displayMessage(String.format("Invalid command: %s", userInput),
                  DisplayMessageType.ERROR);
        }
      }
    } catch (ImageProcessorException e) {
      displayMessage(e.getMessage(), DisplayMessageType.ERROR);
    }
  }

  /**
   * Executes the command based on the user input.
   *
   * @param command command to be executed.
   * @param scanner scanner to read the command arguments.
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException                       if an error occurs
   *                                                       while executing
   *                                                       the command.
   * @throws ImageProcessingRunTimeException.QuitException if user wants to
   *                                                       quit the application.
   */
  private ExecutionStatus executeCommand(UserCommand command, Scanner scanner)
          throws
          ImageProcessorException {
    switch (command) {
      case LOAD:
        return executeLoadCommand(scanner);
      case SAVE:
        return executeSaveCommand(scanner);
      case RED_COMPONENT:
        return executeRedComponentCommand(scanner);
      case BLUE_COMPONENT:
        return executeBlueComponentCommand(scanner);
      case GREEN_COMPONENT:
        return executeGreenComponentCommand(scanner);
      case VALUE_COMPONENT:
        return executeValueComponentCommand(scanner);
      case LUMA_COMPONENT:
        return executeLumaComponentCommand(scanner);
      case INTENSITY_COMPONENT:
        return executeIntensityComponentCommand(scanner);
      case HORIZONTAL_FLIP:
        return executeHorizontalFlipCommand(scanner);
      case VERTICAL_FLIP:
        return executeVerticalFlipCommand(scanner);
      case BRIGHTEN:
        return executeBrightenCommand(scanner);
      case RGB_SPLIT:
        return executeRgbSplitCommand(scanner);
      case RGB_COMBINE:
        return executeRgbCombineCommand(scanner);
      case BLUR:
        return executeBlurCommand(scanner);
      case SHARPEN:
        return executeSharpenCommand(scanner);
      case SEPIA:
        return executeSepiaCommand(scanner);
      case RUN:
        return executeRunCommand(scanner);
      case COMPRESS:
        return executeCompressionCommand(scanner);
      case HISTOGRAM:
        return executeHistogramCommand(scanner);
      case COLOR_CORRECT:
        return executeColorCorrectionCommand(scanner);
      case LEVELS_ADJUST:
        return executeLevelsAdjustCommand(scanner);
      case RESET:
        return executeResetCommand();
      case HELP:
        return executeHelpCommand();
      case QUIT:
        throw new ImageProcessingRunTimeException.QuitException("Shutting "
                + "down application");
      default:
        return new ExecutionStatus(false, "Invalid command.");
    }
  }

  /**
   * Executes the load command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeLoadCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.loadImage(ImageProcessingRequest
            .builder()
            .imagePath(arguments.get(0))
            .imageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully loaded the image.");
  }

  /**
   * Executes the save command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeSaveCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.saveImage(ImageProcessingRequest
            .builder()
            .imagePath(arguments.get(0))
            .imageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully saved the image.");
  }

  /**
   * Executes to extract red component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeRedComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createRedComponent(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1))
            .build());
    return new ExecutionStatus(true, "Successfully created red component.");
  }

  /**
   * Executes to extract blue component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeBlueComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createBlueComponent(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1))
            .build());
    return new ExecutionStatus(true, "Successfully created blue component.");
  }

  /**
   * Executes to extract green component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeGreenComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createGreenComponent(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully created green component.");
  }

  /**
   * Executes to extract value component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeValueComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createValueComponent(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully created value component.");
  }

  /**
   * Executes to extract luma component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeLumaComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createLumaComponent(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1))
            .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
            .build());
    return new ExecutionStatus(true, "Successfully created luma component.");
  }

  /**
   * Executes to extract intensity component from image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeIntensityComponentCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.createIntensityComponent(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully created intensity "
            + "component.");
  }

  /**
   * Executes to flip the image horizontally.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeHorizontalFlipCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.horizontalFlip(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully flipped the image "
            + "horizontally.");
  }

  /**
   * Executes to flip the image vertically.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeVerticalFlipCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.verticalFlip(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1)).build());
    return new ExecutionStatus(true, "Successfully flipped the image "
            + "vertically.");
  }

  /**
   * Executes to brighten the image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeBrightenCommand(Scanner scanner)
          throws
          ImageProcessorException {
    if (!scanner.hasNextInt()) {
      throw new ImageProcessorException("Invalid factor provided for "
              + "brightening the image.");
    }
    int brightness = scanner.nextInt();
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.brighten(ImageProcessingRequest
            .builder()
            .imageName(arguments.get(0))
            .destinationImageName(arguments.get(1))
            .factor(brightness).build());
    return new ExecutionStatus(true,
            String.format("Successfully brightened the image at factor:%s",
                    brightness));
  }

  /**
   * Executes to split the image into RGB components.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeRgbSplitCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 4);
    imageProcessingService.rgbSplit(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .redImageName(arguments.get(1))
                    .greenImageName(arguments.get(2))
                    .blueImageName(arguments.get(3))
                    .build()
    );
    return new ExecutionStatus(true,
            "Successfully split the image into RGB components.");
  }

  /**
   * Executes to combine the RGB components of the image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeRgbCombineCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 4);
    imageProcessingService.rgbCombine(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .redImageName(arguments.get(1))
                    .greenImageName(arguments.get(2))
                    .blueImageName(arguments.get(3))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully combined the RGB "
            + "components.");
  }

  /**
   * Executes to blur the image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeBlurCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.blurImage(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    // Optional percentage argument to get the split view.
                    .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully blurred the image.");
  }

  /**
   * Executes to sharpen the image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeSharpenCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.sharpenImage(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    // Optional percentage argument to get the split view.
                    .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully sharpened the image.");
  }

  /**
   * Executes to apply sepia filter to the image.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution.
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeSepiaCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.sepiaImage(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    // Optional percentage argument to get the split view.
                    .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully converted the image to "
            + "sepia.");
  }

  /**
   * Executes the run command to execute a script file.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  protected ExecutionStatus executeRunCommand(Scanner scanner) throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 1);
    String scriptFile = arguments.get(0);
    if (Objects.isNull(scriptFile)) {
      throw new ImageProcessorException("Script file path cannot be null.");
    }
    try (BufferedReader reader =
                 new BufferedReader(new FileReader(scriptFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (shouldSkipLine(line)) {
          continue;
        }
        execute(new StringReader(line));
      }

    } catch (IOException e) {
      // Quit the application if an error occurs while reading the script file.
      String errorMessage = String.format("Error reading script file: %s, %s",
              scriptFile, e.getMessage());
      displayMessage(errorMessage, DisplayMessageType.ERROR);
      throw new ImageProcessingRunTimeException.QuitException(errorMessage);
    }

    return new ExecutionStatus(true, "Successfully executed the script file.");
  }

  /**
   * Checks if the line should be skipped.
   * A line should be skipped if it is a comment or empty.
   *
   * @param line line to be checked
   * @return true if the line should be skipped, false otherwise
   */
  private boolean shouldSkipLine(String line) {
    return line.trim().startsWith(COMMENT_PREFIX) || line.trim().isEmpty();
  }


  /**
   * Extracts the arguments from the scanner.
   *
   * @param scanner           scanner to read the arguments
   * @param numberOfArguments number of arguments to be read
   * @return list of arguments
   * @throws ImageProcessorException if the number of arguments is invalid.
   */
  private List<String> extractArguments(Scanner scanner, int numberOfArguments)
          throws
          ImageProcessorException {
    List<String> arguments = new ArrayList<>();
    while (numberOfArguments-- > 0) {
      if (!scanner.hasNext()) {
        throw new ImageProcessorException("Invalid command parameters.");
      }
      arguments.add(scanner.next());
    }
    return arguments;
  }

  /**
   * Extracts the optional split percentage argument from the scanner.
   *
   * @param scanner scanner to read the argument
   * @return optional split percentage
   */
  private Optional<Integer> extractOptionalSplitPercentage(Scanner scanner) {
    if (scanner.hasNext(Pattern.compile(SPLIT_COMMAND))) {
      String next = scanner.next();
      if (next.equals(SPLIT_COMMAND)) {
        return scanner.hasNextInt() ? Optional.of(scanner.nextInt()) :
                Optional.empty();
      }
    }
    return Optional.empty();
  }


  /**
   * Executes the help command.
   *
   * @return ExecutionStatus information of the execution.
   */
  private ExecutionStatus executeHelpCommand() {
    displayCommands();
    return new ExecutionStatus(true, "");
  }


  /**
   * Displays the message to the user.
   *
   * @param message     message to be displayed
   * @param messageType type of the message
   */
  protected void displayMessage(String message,
                                DisplayMessageType messageType) {
    if (StringUtils.isNotNullOrEmpty(message)) {
      this.textOutput.displayMessage(message, messageType);
    }
  }


  /**
   * Executes the compression command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeCompressionCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 3);
    try {
      int compressionPercentage = Integer.parseInt(arguments.get(0));
      imageProcessingService.compressImage(
              ImageProcessingRequest
                      .builder()
                      .imageName(arguments.get(1))
                      .destinationImageName(arguments.get(2))
                      .percentage(compressionPercentage)
                      .build()
      );
      return new ExecutionStatus(true,
              String.format("Successfully compressed the image at %s%%.",
                      compressionPercentage));
    } catch (NumberFormatException e) {
      throw new ImageProcessorException("Invalid compression percentage "
              + "provided.");
    }
  }

  /**
   * Executes the histogram command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeHistogramCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.histogram(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully created histogram of the "
            + "image.");
  }

  /**
   * Executes the color correction command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeColorCorrectionCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 2);
    imageProcessingService.colorCorrect(
            ImageProcessingRequest
                    .builder()
                    .imageName(arguments.get(0))
                    .destinationImageName(arguments.get(1))
                    // Optional percentage argument to get the split view.
                    .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
                    .build()
    );
    return new ExecutionStatus(true, "Successfully color corrected the image.");
  }

  /**
   * Executes the levels adjust command.
   *
   * @param scanner scanner to read the command arguments
   * @return ExecutionStatus information of the execution
   * @throws ImageProcessorException if an error occurs while executing the
   *                                 command
   */
  private ExecutionStatus executeLevelsAdjustCommand(Scanner scanner)
          throws
          ImageProcessorException {
    List<String> arguments = extractArguments(scanner, 5);
    try {
      int black = Integer.parseInt(arguments.get(0));
      int mid = Integer.parseInt(arguments.get(1));
      int white = Integer.parseInt(arguments.get(2));
      imageProcessingService.levelsAdjust(
              ImageProcessingRequest
                      .builder()
                      .imageName(arguments.get(3))
                      .destinationImageName(arguments.get(4))
                      .levels(black, mid, white)
                      // Optional percentage argument to get the split view.
                      .percentage(extractOptionalSplitPercentage(scanner).orElse(null))
                      .build()
      );
      return new ExecutionStatus(true,
              String.format("Successfully adjusted the levels of the image to "
                              + "black:%s, mid:%s, white:%s.",
                      black, mid, white));
    } catch (NumberFormatException e) {
      throw new ImageProcessorException("Invalid levels provided.");
    }
  }

  /**
   * Executes the clear command.
   *
   * @return ExecutionStatus information of the execution
   */
  private ExecutionStatus executeResetCommand() {
    imageProcessingService.clearMemory();
    return new ExecutionStatus(true, "Successfully reset the memory.");
  }
}
