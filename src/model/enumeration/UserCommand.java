package model.enumeration;

import java.util.Arrays;
import java.util.Optional;


/**
 * Enum representing the user commands.
 * These are possible commands that the user can give to the program.
 */
public enum UserCommand {

  LOAD("load", "load image-path image-name: "
          + "Load an image from the specified path and refer it to "
          + "henceforth in the program by the given image name."),
  SAVE("save", "save image-path image-name: "
          + "Save the image with the given name to the specified path "
          + "which should include the name of the file."),
  RED_COMPONENT("red-component", "red-component image-name dest-image-name: "
          + "Create an image with the red-component of the image with the "
          + "given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name."),
  GREEN_COMPONENT("green-component", "green-component image-name "
          + "dest-image-name: "
          + "Create an image with the green-component of the image with the "
          + "given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name."),
  BLUE_COMPONENT("blue-component", "blue-component image-name dest-image-name: "
          + "Create an image with the blue-component of the image with the "
          + "given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name."),
  VALUE_COMPONENT("value-component", "value-component image-name "
          + "dest-image-name: "
          + "Create an image with the value-component of the image with the "
          + "given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name."),
  LUMA_COMPONENT("luma-component", "luma-component image-name dest-image-name"
          + " split P: "
          + "Create an image with the luma-component of the image with the "
          + "given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name.P is an optional parameter for split view."),
  INTENSITY_COMPONENT("intensity-component", "intensity-component image-name "
          + "dest-image-name: "
          + "Create an image with the intensity-component of the image with "
          + "the given name, "
          + "and refer to it henceforth in the program by the given "
          + "destination name."),
  HORIZONTAL_FLIP("horizontal-flip", "horizontal-flip image-name "
          + "dest-image-name: "
          + "Flip an image horizontally to create a new image, "
          + "referred to henceforth by the given destination name."),
  VERTICAL_FLIP("vertical-flip", "vertical-flip image-name dest-image-name: "
          + "Flip an image vertically to create a new image, "
          + "referred to henceforth by the given destination name."),
  BRIGHTEN("brighten", "brighten increment image-name dest-image-name: "
          + "brighten the image by the given increment to create a new image, "
          + "referred to henceforth by the given destination name. "
          + "The increment may be positive (brightening) or negative "
          + "(darkening)."),
  RGB_SPLIT("rgb-split", "rgb-split image-name "
          + "dest-image-name-red dest-image-name-green dest-image-name-blue: "
          + "split the given image into three images "
          + "containing its red, green and blue components respectively. "
          + "These would be the same images that would be individually "
          + "produced with "
          + "the red-component, green-component and blue-component commands."),
  RGB_COMBINE("rgb-combine", "rgb-combine image-name red-image green-image "
          + "blue-image: "
          + "Combine the three images that are individually red, green and "
          + "blue "
          + "into a single image that gets its red, green and blue components "
          + "from the three images respectively."),
  BLUR("blur", "blur image-name dest-image-name split p: "
          + "blur the given image and store the result in another image with "
          + "the given name.P is an optional parameter for split view."),
  SHARPEN("sharpen", "sharpen image-name dest-image-name split p: "
          + "sharpen the given image and store the result in another image "
          + "with the given name.P is an optional parameter for split view."),
  SEPIA("sepia", "sepia image-name dest-image-name split p: "
          + "produce a sepia-toned version of the given image "
          + "and store the result in another image with the given name."
          + "P is an optional parameter for split view."),
  COMPRESS("compress",
          "compress percentage image-name dest-image-name: "
                  + "compress the given image by the given percentage and "
                  + "store the result in another image with the given name."),
  HISTOGRAM("histogram", "histogram image-name dest-image-name: "
          + "Create a histogram of the given image and store the result in "
          + "another image with the given name."),
  COLOR_CORRECT("color-correct", "color-correct image-name dest-image-name "
          + "split p: "
          + "Color correct the given image and store the result in another "
          + "image with the given name.P is an optional parameter for split "
          + "view."),
  LEVELS_ADJUST("levels-adjust", "levels-adjust b m w image-name "
          + "dest-image-name split p: "
          + "Adjust the levels of the given image and store the result in "
          + "another image with the given name.P is an optional parameter for"
          + " split view."),
  RUN("run", "run script-file: "
          + "Load and run the script commands in the specified file."),
  DOWNSCALE("downscale", "downscale factor image-name dest-image-name: "
          + "Downscale the given image by the given factor and store the "
          + "result in another image with the given name."),

  RESET("reset", "reset: Resets the program's memory."),

  QUIT("quit", "quit: Quit the program."),
  HELP("help", "help: Print this help message.");

  /**
   * Command which the user can give.
   */
  private final String command;
  /**
   * Description of the command.
   */
  private final String description;

  /**
   * Constructor to initialize the UserCommand.
   *
   * @param command     the command
   * @param description the description
   */
  UserCommand(String command, String description) {
    this.command = command;
    this.description = description;
  }

  /**
   * Gets the command type from string.
   *
   * @return the command type
   */
  public static Optional<UserCommand> getCommand(String command) {
    return Arrays.stream(UserCommand.values()).filter(
        userCommand -> userCommand.command.equals(command)).findFirst();
  }

  /**
   * Gets the command value from command type.
   *
   * @return the command value
   */
  public String getCommand() {
    return command;
  }

  /**
   * Gets all possible list of the user commands.
   *
   * @return the user commands
   */
  public static String getUserCommands() {
    StringBuilder userCommands = new StringBuilder();
    for (UserCommand userCommand : UserCommand.values()) {
      userCommands.append(userCommand.description).append("\n");
    }

    return userCommands.toString();
  }

  public String getDescription() {
    return description;
  }
}
