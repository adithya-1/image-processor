package view.gui;

import java.awt.GridLayout;
import java.io.File;
import java.util.Optional;
import java.util.function.IntConsumer;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.enumeration.ImageType;
import model.request.ImageProcessingRequest;

/**
 * Represents the input that the user can provide through the GUI using
 * Java swing library.
 */
public class SwingInput implements GUIInput {

  /**
   * Constructs a SwingInput object to be used to interact with user.
   */
  public SwingInput() {
    // Does not take any parameters.
  }

  /**
   * Prompts the user to confirm the split view.
   *
   * @param updateImageCallback the callback function to update the image
   * @return true if the user confirms the split view false otherwise
   */
  @Override
  public boolean confirmSplitView(IntConsumer updateImageCallback) {
    JLabel value = new JLabel("Value: 100");
    JSlider slider = createSlider(value);
    slider.addChangeListener(e -> {
      if (!slider.getValueIsAdjusting()) {
        updateImageCallback.accept(slider.getValue());
      }
    });
    JPanel panel = new JPanel();
    panel.add(new JLabel("Split View"));
    panel.add(slider);
    panel.add(value);
    int result = JOptionPane.showConfirmDialog(null, panel, "Split View",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    return result == JOptionPane.OK_OPTION;
  }

  /**
   * Creates a slider with a value label.
   *
   * @param valueLabel the label to display the value of the slider
   * @return the slider
   */
  private JSlider createSlider(JLabel valueLabel) {
    JSlider slider = new JSlider(0, 100, 100);
    slider.setMajorTickSpacing(10);
    slider.setMinorTickSpacing(1);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    slider.addChangeListener(e -> valueLabel.setText("Value: " + slider.getValue()));
    return slider;
  }

  /**
   * Returns the slider input.
   * Is present if the user has entered a value.
   * Otherwise, empty.
   *
   * @return the slider input
   */
  @Override
  public Optional<Integer> getSliderInput() {
    JLabel value = new JLabel("Value: 100");
    JSlider slider = createSlider(value);
    JPanel panel = new JPanel();
    panel.add(new JLabel("Enter the value"));
    panel.add(slider);
    panel.add(value);
    int result = JOptionPane.showConfirmDialog(null, panel, "Slider",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      return Optional.of(slider.getValue());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Prompts the user to input a path to load an image.
   *
   * @return an Optional containing the path if the user confirms,
   *         or an empty Optional if the user cancels
   */
  @Override
  public Optional<String> interactiveImageLoadPathInput() {
    JFileChooser fileChooser = createFileChooseWithFilter();
    int returnState = fileChooser.showOpenDialog(null);
    if (returnState == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      return Optional.of(f.getAbsolutePath());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Prompts the user to input a path to save an image.
   *
   * @return an Optional containing the path if the user confirms,
   *         or an empty Optional if the user cancels
   */
  @Override
  public Optional<String> interactiveImageSavePathInput() {
    JFileChooser fileChooser = createFileChooseWithFilter();
    int returnState = fileChooser.showSaveDialog(null);
    if (returnState == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      return Optional.of(f.getAbsolutePath());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Prompts the user to input levels for black, mid, and white points using sliders.
   *
   * @return an Optional containing the levels if the user confirms,
   *         or an empty Optional if the user cancels
   */
  @Override
  public Optional<ImageProcessingRequest.Levels> interactiveThreeLevelInput() {
    JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

    JSpinner blackSpinner = buildSpinner(0);
    JSpinner midSpinner = buildSpinner(128);
    JSpinner whiteSpinner = buildSpinner(255);

    configureLevelSpinnerListeners(blackSpinner, midSpinner, whiteSpinner);

    disableSpinnerTextEditing(blackSpinner, midSpinner, whiteSpinner);

    // Add components to panel
    panel.add(new JLabel("Black Point:"));
    panel.add(blackSpinner);
    panel.add(new JLabel("Mid Point:"));
    panel.add(midSpinner);
    panel.add(new JLabel("White Point:"));
    panel.add(whiteSpinner);

    // Show dialog
    int result = JOptionPane.showConfirmDialog(null, panel,
            "Level Adjustment", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      return Optional.of(new ImageProcessingRequest.Levels(
              (Integer) blackSpinner.getValue(),
              (Integer) midSpinner.getValue(),
              (Integer) whiteSpinner.getValue()));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Creates a file chooser with appropriate image file filters.
   *
   * @return A configured JFileChooser that only shows supported image file
   *         types
   */
  private JFileChooser createFileChooseWithFilter() {
    JFileChooser fileChooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Images",
            ImageType.JPEG.getExtension(),
            ImageType.PNG.getExtension(),
            ImageType.PPM.getExtension(),
            ImageType.JPG.getExtension());
    fileChooser.setFileFilter(filter);
    return fileChooser;
  }

  /**
   * Configures change listeners for a series of spinners to maintain proper
   * ordering.
   * Ensures that each spinner's value is greater than the previous spinner
   * and less
   * than the next spinner.
   *
   * @param spinners Variable number of JSpinner components to be configured
   */
  private void configureLevelSpinnerListeners(JSpinner... spinners) {
    // Verify we have at least two spinners to compare
    if (spinners.length < 2) {
      return;
    }

    // Add change listeners to each spinner
    for (int i = 0; i < spinners.length; i++) {
      final int currentIndex = i;
      spinners[i].addChangeListener(e -> {
        int currentValue = (Integer) spinners[currentIndex].getValue();

        // Check against previous spinner (ensure current value is greater)
        if (currentIndex > 0) {
          int previousValue = (Integer) spinners[currentIndex - 1].getValue();
          if (currentValue <= previousValue) {
            spinners[currentIndex].setValue(previousValue + 1);
            return; // Return to avoid potential circular updates
          }
        }

        // Check against next spinner (ensure current value is lesser)
        if (currentIndex < spinners.length - 1) {
          int nextValue = (Integer) spinners[currentIndex + 1].getValue();
          if (currentValue >= nextValue) {
            spinners[currentIndex].setValue(nextValue - 1);
          }
        }
      });
    }
  }

  /**
   * Creates a number spinner with specified default value and range 0-255.
   *
   * @param defaultValue The initial value for the spinner
   * @return A configured JSpinner with appropriate model and range
   */
  private JSpinner buildSpinner(int defaultValue) {
    SpinnerNumberModel model = new SpinnerNumberModel(defaultValue, 0, 255, 1);
    return new JSpinner(model);
  }

  /**
   * Disables text editing for multiple spinners.
   *
   * @param blackSpinner The spinner for black point
   * @param midSpinner   The spinner for mid point
   * @param whiteSpinner The spinner for white point
   */
  private void disableSpinnerTextEditing(JSpinner blackSpinner,
                                         JSpinner midSpinner,
                                         JSpinner whiteSpinner) {
    disableTextEditing(blackSpinner);
    disableTextEditing(midSpinner);
    disableTextEditing(whiteSpinner);
  }

  /**
   * Disables text editing for a single spinner component.
   *
   * @param spinner The JSpinner component to disable text editing for
   */
  private void disableTextEditing(JSpinner spinner) {
    ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
  }

  /**
   * Prompts the user to input scaling factors for width and height using sliders.
   *
   * @return an Optional containing the scaling factors if the user confirms,
   *         or an empty Optional if the user cancels
   */
  @Override
  public Optional<ImageProcessingRequest.ScalingFactors> interactiveScalingFactorsInput() {
    JLabel widthValue = new JLabel("Width: 100%");
    JLabel heightValue = new JLabel("Height: 100%");

    JSlider widthSlider = createSlider(widthValue);
    JSlider heightSlider = createSlider(heightValue);

    JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));

    panel.add(widthValue);
    panel.add(widthSlider);
    panel.add(heightValue);
    panel.add(heightSlider);

    int result = JOptionPane.showConfirmDialog(null, panel,
            "Downscale Image", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      return Optional.of(new ImageProcessingRequest.ScalingFactors(
              widthSlider.getValue(),
              heightSlider.getValue()));
    } else {
      return Optional.empty();
    }
  }
public static int num;
  public static void main(String[] args) {
num = 3;
if(true){
  int num;
  num =10;
}
System.out.println(num);
  }
}
