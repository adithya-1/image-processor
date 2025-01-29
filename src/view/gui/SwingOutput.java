package view.gui;

import controller.Features;
import exception.ImageProcessingRunTimeException;
import model.visual.Image;
import view.components.FeatureComponent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;

import model.enumeration.UserCommand;
import utility.IOUtils;
import view.DisplayMessageType;

/**
 * Represents the gui output which is implemented using the swing library.
 */
public class SwingOutput extends JFrame implements GUIOutput {
  private final JPanel mainPanel;
  private final FeatureComponent featurePanel;
  private final JPanel imagePanel;
  private final JPanel histogramPanel;

  private final JScrollPane featureScrollPane;
  private final JScrollPane imageScrollPane;
  private final JScrollPane histogramScrollPane;

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 600;

  /**
   * Constructs a SwingOutput object.
   * This will initialise the required panels and build the layout of the GUI.
   */
  public SwingOutput() {
    super();
    this.setTitle("Image Processing Application");
    this.setSize(600, 900);
    // Initialising panels
    mainPanel = new JPanel();
    featurePanel = new FeatureComponent();
    imagePanel = new JPanel();
    histogramPanel = new JPanel();
    featureScrollPane = new JScrollPane(featurePanel);
    imageScrollPane = new JScrollPane(imagePanel);
    histogramScrollPane = new JScrollPane(histogramPanel);

    // Building the layout
    buildMainPanel();
    buildImagePanel();
    buildCommandPanel();
    buildHistogramPanel();
    buildScrollPanes();
    addScrollPanes();

    this.add(mainPanel);
    addWindowListener(
          new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              featurePanel.closeWindow();
            }
          }
    );
    this.setVisible(true);
  }

  /**
   * Builds the main panel of the GUI.
   */
  private void buildMainPanel() {
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
  }

  /**
   * Builds the scroll panes of the GUI.
   */
  private void buildScrollPanes() {
    setScrollPanes(featureScrollPane,DEFAULT_HEIGHT,200);
    setScrollPanes(imageScrollPane,DEFAULT_HEIGHT,DEFAULT_WIDTH);
    setScrollPanes(histogramScrollPane,DEFAULT_HEIGHT,DEFAULT_WIDTH);
  }

  /**
   * Sets the scroll panes with the specified height and width.
   *
   * @param scrollPane the scroll pane to be set
   * @param height     the height of the scroll pane
   * @param width      the width of the scroll pane
   */
  private void setScrollPanes(JScrollPane scrollPane,int height,int width) {
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(width,height));
  }


  /**
   * Builds the image panel of the GUI.
   */
  private void buildImagePanel() {
    buildPanel(imagePanel, "Image", DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  /**
   * Builds the command panel of the GUI.
   */
  private void buildCommandPanel() {
    featurePanel.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));
    featurePanel.setBorder(new TitledBorder("Commands"));
    featurePanel.setPreferredSize(new Dimension(200, DEFAULT_HEIGHT));
  }

  /**
   * Builds the histogram panel of the GUI.
   */
  private void buildHistogramPanel() {
    buildPanel(histogramPanel, "Histogram", 200, DEFAULT_HEIGHT);
  }

  /**
   * Builds a panel with the specified title, width and height.
   *
   * @param panel  the panel to be built
   * @param title  the title of the panel
   * @param width  the width of the panel
   * @param height the height of the panel
   */
  private void buildPanel(JPanel panel, String title, int width, int height) {
    panel.setLayout(new BorderLayout());
    panel.setPreferredSize(new Dimension(width, height));
    panel.add(new JLabel(), BorderLayout.NORTH);
    panel.setBorder(new TitledBorder(title));
  }

  /**
   * Adds scroll panes to the main panel.
   */
  private void addScrollPanes() {
    mainPanel.add(featureScrollPane);
    mainPanel.add(imageScrollPane);
    mainPanel.add(histogramScrollPane);
  }


  @Override
  public void displayMessage(String message, DisplayMessageType messageType)
          throws
          ImageProcessingRunTimeException.DisplayException {
    if (Objects.requireNonNull(messageType) == DisplayMessageType.INFO) {
      JOptionPane.showMessageDialog(this, message, "Info",
              JOptionPane.INFORMATION_MESSAGE);
    } else if (messageType == DisplayMessageType.ERROR) {
      JOptionPane.showMessageDialog(this, message, "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void displayCommands(List<UserCommand> commands) {
    featurePanel.removeAll();
    for (UserCommand command : commands) {
      featurePanel.addCommandButton(command);
    }
    featurePanel.revalidate();
    featurePanel.repaint();
  }

  @Override
  public void addFeatures(Features features) {
    featurePanel.addFeatures(features);
  }

  @Override
  public void displayImage(Image image, Image histogram) {
    displayImageToPanel(imagePanel, image);
    displayImageToPanel(histogramPanel, histogram);
  }

  @Override
  public void clearImage() throws
          ImageProcessingRunTimeException.DisplayException {
    clearPanel(imagePanel);
    clearPanel(histogramPanel);
  }

  @Override
  public void closeWindow() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void doNotCloseWindow() {
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  }

  /**
   * Clears the panel.
   *
   * @param panel the panel to be cleared
   */
  private void clearPanel(JPanel panel) {
    panel.removeAll();
    panel.revalidate();
    panel.repaint();
  }

  /**
   * Displays the image to the panel.
   * If the image is null, the panel will be stay the same.
   *
   * @param panel the panel to which the image is to be displayed
   * @param image the image to be displayed
   */
  private void displayImageToPanel(JPanel panel, Image image) {
    if (Objects.nonNull(image)) {
      panel.removeAll();
      JLabel imageLabel =
              new JLabel(new ImageIcon(IOUtils.toBufferedImage(image)));
      imageLabel.setHorizontalAlignment(JLabel.CENTER);
      imageLabel.setVerticalAlignment(JLabel.CENTER);
      panel.setPreferredSize(new Dimension(
              Math.max(DEFAULT_WIDTH, image.getWidth()),
              Math.max(DEFAULT_HEIGHT, image.getHeight())
      ));
      panel.add(imageLabel);
      panel.revalidate();
      panel.repaint();
    }
  }
}