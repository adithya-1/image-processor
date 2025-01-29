package view.components;

import controller.Features;
import model.enumeration.UserCommand;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * A component that maps UI button actions to Features interface methods.
 * Uses swing library to create buttons and handle button clicks.
 */
public class FeatureComponent extends JPanel {
  private final List<Features> featureListeners = new ArrayList<>();

  /**
   * Constructs a CommandComponent object.
   * This object is used to map UI button actions to Features interface methods.
   */
  public FeatureComponent() {
    super();
  }

  /**
   * Adds a feature to the list of feature listeners.
   */
  public void addFeatures(Features features) {
    this.featureListeners.add(features);
  }

  /**
   * Adds a command button with the specified action command.
   */
  public void addCommandButton(UserCommand command) {
    JButton button = new JButton(command.getCommand());
    button.setActionCommand(command.getCommand());
    button.addActionListener(this::handleAction);
    this.add(button);
  }


  /**
   * Calls the features to act on closing the window.
   */
  public void closeWindow() {
    for (Features feature : featureListeners) {
      feature.closeWindow();
    }
  }

  /**
   * Handles the event when a button is clicked.
   *
   * @param event the event that occurred
   */
  private void handleAction(ActionEvent event) {
    Optional<UserCommand> userCommand =
            UserCommand.getCommand(event.getActionCommand());
    userCommand.ifPresent(this::handleUserCommand);
  }

  /**
   * Handles the user command by calling the appropriate method in the
   * Features interface.
   *
   * @param command the user command to be handled
   */
  private void handleUserCommand(UserCommand command) {
    for (Features feature : featureListeners) {
      switch (command) {
        case LOAD:
          feature.loadImage();
          break;
        case SAVE:
          feature.saveImage();
          break;
        case SEPIA:
          feature.applySepia();
          break;
        case RESET:
          feature.reset();
          break;
        case VERTICAL_FLIP:
          feature.verticalFlip();
          break;
        case HORIZONTAL_FLIP:
          feature.horizontalFlip();
          break;
        case LUMA_COMPONENT:
          feature.getLuma();
          break;
        case COLOR_CORRECT:
          feature.colorCorrect();
          break;
        case RED_COMPONENT:
          feature.redComponent();
          break;
        case GREEN_COMPONENT:
          feature.greenComponent();
          break;
        case BLUE_COMPONENT:
          feature.blueComponent();
          break;
        case BLUR:
          feature.blurImage();
          break;
        case SHARPEN:
          feature.sharpenImage();
          break;
        case COMPRESS:
          feature.compressImage();
          break;
        case LEVELS_ADJUST:
          feature.levelsAdjust();
          break;
        case DOWNSCALE:
          feature.downscaleImage();
          break;
        default:
          // Invalid Command - Do nothing
          break;
      }
    }
  }
}