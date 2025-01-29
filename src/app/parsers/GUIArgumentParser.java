package app.parsers;

import controller.GUIImageProcessorController;
import controller.ImageProcessorController;
import exception.ImageProcessingRunTimeException;
import model.memory.StringMemory;
import view.gui.SwingInput;
import view.gui.SwingOutput;

/**
 * A class that represents a parser for GUI arguments.
 * This class is responsible for creating a controller for GUI
 * based on the arguments required for GUI mode.
 */
public class GUIArgumentParser extends AbstractArgumentParser {

  /**
   * Constructs a GUIArgumentParser.
   * No arguments are passed.
   *
   */
  @Override
  public ImageProcessorController createController(String[] args) throws
          ImageProcessingRunTimeException.QuitException {
    return new GUIImageProcessorController(
            new SwingInput(),
            new SwingOutput(),
            createService(),
            new StringMemory()
    );
  }
}
