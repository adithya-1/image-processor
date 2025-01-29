package app.parsers;

import controller.ImageProcessorController;
import exception.ImageProcessingRunTimeException;

/**
 * An interface that represents a parser for command line arguments.
 * The parser is responsible for creating a controller based on the
 * command line arguments.
 */
public interface ArgumentParser {
  /**
   * Creates a controller based on the command line arguments.
   *
   * @param args    The command line arguments.
   * @return The controller that was created.
   * @throws ImageProcessingRunTimeException If there is an
   *                                         error creating the
   *                                         controller.
   */
  ImageProcessorController createController(String[] args) throws
          ImageProcessingRunTimeException;
}
