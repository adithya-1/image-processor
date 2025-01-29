package app;


import app.parsers.ArgumentParser;
import controller.ImageProcessorController;
import exception.ImageProcessingRunTimeException;
import factories.Factory;

/**
 * The main class for the Image Processor application.
 * This class is responsible for creating
 * the controller and running the application.
 */
public class ImageProcessorApp {

  /**
   * Entry point of the application.
   * Initializes the controller and starts processing commands.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    ArgumentParser parser = Factory.getArgumentParser(args);
    ImageProcessorController controller = parser.createController(args);
    while (true) {
      try {
        controller.processCommands();
      } catch (ImageProcessingRunTimeException.QuitException e) {
        break;
      }
    }
  }
}
