import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import controller.ImageProcessorController;
import controller.InteractiveImageProcessorController;
import exception.ImageProcessingRunTimeException;
import exception.ImageProcessorException;
import factories.Factory;
import model.memory.HashMapMemory;
import model.memory.ImageMemory;
import model.pixels.Pixel;
import model.visual.Image;
import controller.services.ImageProcessingService;
import view.text.ConsoleInput;
import view.text.ConsoleOutput;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Integration tests from controller.
 * Tests interactions between different packages starting from controller.
 */
public class ControllerIntegrationTest {

  private static final String INITIAL_IMAGE_NAME = "initialImage";
  private ImageProcessorController controller;
  private ImageMemory<Image> imageMemory;

  /**
   * Cleans up output directory after test is run.
   */
  @AfterClass
  public static void cleanUp() {
    TestUtils.cleanUp("test_resources/output");
  }

  private void initialiseController(String input, StringBuilder output,
                                    Image initialImage) {
    imageMemory = new HashMapMemory();
    ImageProcessingService processingService =
            Factory.createImageProcessor(imageMemory);
    controller = new InteractiveImageProcessorController(
            new ConsoleInput(new StringReader(input)),
            new ConsoleOutput(output),
            processingService
    );
    imageMemory.addImage(INITIAL_IMAGE_NAME, initialImage);
  }

  // Save And Tests
  @Test
  public void testSaveCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("save test_resources/output invalidImageName",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(
            output.toString()
                    .contains("Image with name invalidImageName "
                            + "not found in memory"));
  }

  @Test
  public void testLoadCommandWithInvalidExtension() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("load test_resources.txt invalidImageName",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(
            output.toString()
                    .contains("Image type with extension txt not supported"));


  }

  @Test
  public void testLoadWithInvalidPath() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("load invalidPath.png invalidImageName",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(
            output.toString()
                    .contains("Error loading the image file"));

    initialiseController("load invalidPath invalidImageName",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(
            output.toString()
                    .contains("Error loading the image file"));
  }


  @Test
  public void loadFromPngToPng() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.png "
                            + "random-png1\n")
                    .append("save test_resources/output/random-png1.png"
                            + " random-png1\n")
                    .append("load test_resources/output/random-png1.png "
                            + "random-png2\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));

    assertTrue(new File("test_resources/output/random-png1.png").exists());


    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-png1"));
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-png2"));
    assertEquals(imageMemory.getImage("random-png1"),
            imageMemory.getImage(
                    "random-png2"));
  }

  @Test
  public void loadAndSaveFromPPMToPPM() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.ppm "
                            + "random-ppm1\n")
                    .append("save test_resources/output/random-ppm1.ppm"
                            + " random-ppm1\n")
                    .append("load test_resources/output/random-ppm1.ppm "
                            + "random-ppm2\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));

    assertTrue(new File("test_resources/output/random-ppm1.ppm").exists());
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-ppm1"));
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-ppm2"));
    assertEquals(imageMemory.getImage("random-ppm1"),
            imageMemory.getImage(
                    "random-ppm2"));
  }

  @Test
  public void loadAndSaveFromPPMToPNG() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.ppm "
                            + "random-ppm1\n")
                    .append("save test_resources/output/random-ppm1.png"
                            + " random-ppm1\n")
                    .append("load test_resources/output/random-ppm1.png "
                            + "random-png1\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));

    assertTrue(new File("test_resources/output/random-ppm1.png").exists());
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-ppm1"));
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-png1"));
    assertEquals(imageMemory.getImage("random-ppm1"),
            imageMemory.getImage(
                    "random-png1"));
  }

  @Test
  public void loadAndSaveFromPNGToPPM() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.png "
                            + "random-png1\n")
                    .append("save test_resources/output/random-png1.ppm"
                            + " random-png1\n")
                    .append("load test_resources/output/random-png1.ppm "
                            + "random-ppm1\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));


    assertTrue(new File("test_resources/output/random-png1.ppm").exists());
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-ppm1"));
    assertEquals(TestUtils.randomImage(), imageMemory.getImage("random-png1"));
    assertEquals(imageMemory.getImage("random-ppm1"),
            imageMemory.getImage(
                    "random-png1"));
  }

  @Test
  public void loadAndSaveFromJPEG() {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.jpeg "
                            + "random-jpeg\n")
                    .append("save test_resources/output/random-jpeg.jpeg"
                            + " random-jpeg\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(new File("test_resources/output/random-jpeg.jpeg").exists());
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));
  }

  @Test
  public void loadAndSaveFromJPG() {
    StringBuilder output = new StringBuilder();
    initialiseController(
            new StringBuilder()
                    .append("load test_resources/input/random.jpg "
                            + "random-jpg\n")
                    .append("save test_resources/output/random-jpg.jpg"
                            + " random-jpg\n")
                    .toString(),
            output, null);

    controller.processCommands();
    assertTrue(new File("test_resources/output/random-jpg.jpg").exists());
    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));
  }


  // Quit Command Tests

  @Test(expected = ImageProcessingRunTimeException.QuitException.class)
  public void testQUitCommand() {
    StringBuilder output = new StringBuilder();
    initialiseController("quit", output, null);
    controller.processCommands();
  }


  // RED COMPONENT TESTS
  @Test
  public void testCreateRedComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testRedComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            "red-component invalidImageName redComponent",
            output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testRedComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s", INITIAL_IMAGE_NAME),
            output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateRedComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("red-component %s redComponent", INITIAL_IMAGE_NAME),
            output,
            TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 0},
            {0, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("redComponent"));
  }

  @Test
  public void testCreateRedComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("red-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // GREEN COMPONENT TESTS
  @Test
  public void testCreateGreenComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testGreenComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            "green-component invalidImageName greenComponent",
            output,
            TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testGreenComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s", INITIAL_IMAGE_NAME),
            output,
            TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateGreenComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("green-component %s greenComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created green "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {16777215, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("greenComponent"));
  }

  @Test
  public void testCreateGreenComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("green-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // BLUE COMPONENT TESTS
  @Test
  public void testCreateBlueComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("blue-component %s blueComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("blue-component %s blueComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(
            String.format("blue-component %s blueComponent",
                    INITIAL_IMAGE_NAME),
            output,
            TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blue-component %s blueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blue-component %s blueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blue-component %s blueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testBlueComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("blue-component invalidImageName blueComponent",
            output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testBlueComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blue-component %s",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateBlueComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blue-component %s blueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created blue "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 16777215},
            {0, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blueComponent"));
  }

  @Test
  public void testCreateBlueComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("blue-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // VALUE COMPONENT TESTS
  @Test
  public void testCreateValueComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testValueComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("value-component invalidImageName valueComponent",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testValueComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateValueComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("value-component %s valueComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created value "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("valueComponent"));
  }

  @Test
  public void testCreateValueComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("value-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // INTENSITY COMPONENT TESTS
  @Test
  public void testCreateIntensityComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {5592405, 5592405},
            {5592405, 5592405}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {5592405, 5592405},
            {5592405, 5592405}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {5592405, 5592405},
            {5592405, 5592405}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testIntensityComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("intensity-component invalidImageName "
            + "intensityComponent", output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testIntensityComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateIntensityComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("intensity-component %s "
            + "intensityComponent", INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created intensity "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {5592405, 5592405},
            {5592405, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("intensityComponent"));
  }

  @Test
  public void testCreateIntensityComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("intensity-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // LUMA COMPONENT TESTS
  @Test
  public void testCreateLumaComponentWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {3552822, 3552822},
            {3552822, 3552822}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {1184274, 1184274},
            {1184274, 1184274}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {11974326, 11974326},
            {11974326, 11974326}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711422, 16711422},
            {16711422, 16711422}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "
            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testLumaComponentCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("luma-component invalidImageName lumaComponent",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testLumaComponentCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testCreateLumaComponentWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("luma-component %s lumaComponent",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully created luma "

            + "component."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {3552822, 1184274},
            {11974326, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("lumaComponent"));
  }

  @Test
  public void testCreateLumaComponentWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("luma-component", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // HORIZONTAL FLIP TESTS
  @Test
  public void testHorizontalFlipWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 16711680},
            {16711680, 16711680}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {255, 255},
            {255, 255}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {65280, 65280},
            {65280, 65280}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("horizontal-flip invalidImageName horizontalFlip",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testHorizontalFlipCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testHorizontalFlipWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("horizontal-flip %s horizontalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {255, 16711680},
            {8421504, 65280}
    }));
    assertEquals(expectedImage, imageMemory.getImage("horizontalFlip"));
  }

  @Test
  public void testHorizontalFlipWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("horizontal-flip", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // VERTICAL FLIP TESTS
  @Test
  public void testVerticalFlipWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 16711680},
            {16711680, 16711680}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {255, 255},
            {255, 255}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {65280, 65280},
            {65280, 65280}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("vertical-flip invalidImageName verticalFlip",
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testVerticalFlipCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testVerticalFlipWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("vertical-flip %s verticalFlip",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {65280, 8421504},
            {16711680, 255},
    }));
    assertEquals(expectedImage, imageMemory.getImage("verticalFlip"));
  }

  @Test
  public void testVerticalFlipWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("vertical-flip", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // Brighten tests
  @Test
  public void testBrightenWithPositiveFactor() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("brighten 1 %s brighten",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully brightened the image "
            + "at factor:1"));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711937, 66047},
            {130817, 8487297}
    }));
    assertEquals(expectedImage, imageMemory.getImage("brighten"));
  }

  @Test
  public void testBrightenWithNegativeFactor() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("brighten -1 %s brighten",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully brightened the image "
            + "at factor:-1"));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16646144, 254},
            {65024, 8355711}
    }));
    assertEquals(expectedImage, imageMemory.getImage("brighten"));
  }

  @Test
  public void testBrightenWithZeroFactor() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("brighten 0 %s brighten",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully brightened the image "
            + "at factor:0"));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255},
            {65280, 8421504}
    }));
    assertEquals(expectedImage, imageMemory.getImage("brighten"));
  }

  @Test
  public void testBrightenWithInvalidFactor() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("brighten invalidFactor %s brighten",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid factor provided for "
            + "brightening the image."));
  }

  @Test
  public void testBrightenCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("brighten 1 invalidImageName brighten", output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testBrightenCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("brighten 1 %s", INITIAL_IMAGE_NAME),
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testBrightenCommandWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("brighten 1", output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // RGB Split tests
  @Test
  public void testRGBSplitOfPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitOfPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitOfPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitOfPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {8421504, 8421504},
            {8421504, 8421504}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitOfPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitOfPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s redImage greenImage "
            + "blueImage", INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    Image redImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image greenImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    Image blueImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(redImage, imageMemory.getImage("redImage"));
    assertEquals(greenImage, imageMemory.getImage("greenImage"));
    assertEquals(blueImage, imageMemory.getImage("blueImage"));
  }

  @Test
  public void testRGBSplitCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-split invalidImageName redImage greenImage "
            + "blueImage", output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testRGBSplitCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("rgb-split %s", INITIAL_IMAGE_NAME),
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testRGBSplitWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-split", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // Blur tests
  @Test
  public void testBlurImageWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {9371648, 9371648},
            {9371648, 9371648}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {143, 143},
            {143, 143}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {36608, 36608},
            {36608, 36608}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {4737096, 4737096},
            {4737096, 4737096}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {9408399, 9408399},
            {9408399, 9408399}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("blur invalidImageName blurImage", output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testBlurCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s", INITIAL_IMAGE_NAME), output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }


  // Sharpen tests

  @Test
  public void testBlurImageWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("blur %s blurImage",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully blurred the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {4663079, 3088207},
            {3100447, 3096383}

    }));
    assertEquals(expectedImage, imageMemory.getImage("blurImage"));
  }

  @Test
  public void testBlurImageWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("blur", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testSharpenImageWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 16711680},
            {16711680, 16711680}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {255, 255},
            {255, 255}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {65280, 65280},
            {65280, 65280}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {14737632, 14737632},
            {14737632, 14737632}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("sharpen invalidImageName sharpenImage", output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testSharpenCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s", INITIAL_IMAGE_NAME),
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testSharpenImageWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sharpen %s sharpenImage",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully sharpened the image."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16736095, 6250495},
            {6291295, 12566463}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sharpenImage"));
  }

  @Test
  public void testSharpenImageWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("sharpen", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // Sepia tests
  @Test
  public void testSepiaImageWithPureRedImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {6576197, 6576197},
            {6576197, 6576197}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithPureBlueImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {3156513, 3156513},
            {3156513, 3156513}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithPureGreenImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greenImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {12889736, 12889736},
            {12889736, 12889736}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithPureGreyImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.greyImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {11311479, 11311479},
            {11311479, 11311479}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithPureWhiteImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777198, 16777198},
            {16777198, 16777198}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithPureBlackImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaCommandWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("sepia invalidImageName sepiaImage", output,
            TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalidImageName "
            + "not found in memory"));
  }

  @Test
  public void testSepiaCommandWithInvalidDestinationImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s", INITIAL_IMAGE_NAME),
            output, TestUtils.redImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  @Test
  public void testSepiaImageWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController(String.format("sepia %s sepiaImage",
            INITIAL_IMAGE_NAME), output, TestUtils.randomImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {6576197, 3156513},
            {12889736, 11311479}
    }));
    assertEquals(expectedImage, imageMemory.getImage("sepiaImage"));
  }

  @Test
  public void testSepiaImageWithoutImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("sepia", output, TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // Tests for RGB combine
  @Test
  public void testRGBCombineWithPureRedGreenBlueImages() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-combine combinedImage redImage greenImage "
            + "blueImage", output, TestUtils.redImage());
    imageMemory.addImage("redImage", TestUtils.redImage());
    imageMemory.addImage("greenImage", TestUtils.greenImage());
    imageMemory.addImage("blueImage", TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString()
            .contains("Successfully combined the RGB components."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("combinedImage"));
  }

  @Test
  public void testRGBCombineWithAllWhiteImages() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-combine combinedImage redImage greenImage "
            + "blueImage", output, TestUtils.whiteImage());
    imageMemory.addImage("redImage", TestUtils.whiteImage());
    imageMemory.addImage("greenImage", TestUtils.whiteImage());
    imageMemory.addImage("blueImage", TestUtils.whiteImage());
    controller.processCommands();
    assertTrue(output.toString()
            .contains("Successfully combined the RGB components."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16777215, 16777215},
            {16777215, 16777215}
    }));
    assertEquals(expectedImage, imageMemory.getImage("combinedImage"));
  }

  @Test
  public void testRGBCombineWithAllBlackImages() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-combine combinedImage redImage greenImage "
            + "blueImage", output, TestUtils.blackImage());
    imageMemory.addImage("redImage", TestUtils.blackImage());
    imageMemory.addImage("greenImage", TestUtils.blackImage());
    imageMemory.addImage("blueImage", TestUtils.blackImage());
    controller.processCommands();
    assertTrue(output.toString()
            .contains("Successfully combined the RGB components."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {0, 0},
            {0, 0}
    }));
    assertEquals(expectedImage, imageMemory.getImage("combinedImage"));
  }

  @Test
  public void testRGBCombineWithRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    initialiseController("rgb-combine combinedImage redImage greenImage "
            + "blueImage", output, TestUtils.randomImage());
    imageMemory.addImage("redImage", TestUtils.redImage());
    imageMemory.addImage("greenImage", TestUtils.randomImage());
    imageMemory.addImage("blueImage", TestUtils.blueImage());
    controller.processCommands();
    assertTrue(output.toString()
            .contains("Successfully combined the RGB components."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711935, 16711935},
            {16777215, 16744703}
    }));
    assertEquals(expectedImage, imageMemory.getImage("combinedImage"));
  }

  // RUN Command Tests
  @Test
  public void testRunCommandWithValidScriptFile() {
    StringBuilder output = new StringBuilder();
    initialiseController("run test_resources/test_valid_script.txt",
            output,
            null);
    controller.processCommands();

    assertTrue(output.toString().contains("Successfully loaded the image."));
    assertTrue(output.toString().contains("Successfully saved the image."));
    assertTrue(output.toString().contains("Successfully created red component"
            + "."));

    assertTrue(new File("test_resources/output/saved_sample_image.png").exists());
    assertTrue(new File("test_resources/output"
            + "/saved_sample_image_red_component.png").exists());
  }

  @Test
  public void testRunCommandWithInvalidScriptFile() {
    StringBuilder output = new StringBuilder();
    initialiseController("run test_resources/test_invalid_script.txt",
            output,
            null);
    controller.processCommands();

    assertTrue(output.toString().contains("Invalid command"));
  }

  @Test
  public void testRunCommandWithFileNotFound() {
    StringBuilder output = new StringBuilder();
    initialiseController("run test_resources/invalid_file.txt",
            output,
            null);
    assertThrows(ImageProcessingRunTimeException.QuitException.class,
        () -> controller.processCommands());

    assertTrue(output.toString().contains("Error reading script file: "
            + "test_resources/invalid_file.txt"));
  }

  @Test
  public void testRunCommandWithEmptyScriptFile() {
    StringBuilder output = new StringBuilder();
    initialiseController("run test_resources/test_empty_script.txt",
            output,
            null);
    controller.processCommands();

    assertTrue(output.toString().contains("Successfully executed the script "
            + "file"));
  }

  @Test
  public void testRunCommandWithInvalidCommandInScriptFile() {
    StringBuilder output = new StringBuilder();
    initialiseController("run ",
            output,
            null);
    controller.processCommands();

    assertTrue(output.toString().contains("Invalid command parameters."));
  }


  @Test
  public void testVerticalAndHorizontalFlipCombined() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomImage();
    initialiseController(
            new StringBuilder()
                    .append(String.format("horizontal-flip %s horizontalFlip\n",
                            INITIAL_IMAGE_NAME))
                    .append("vertical-flip horizontalFlip verticalFlip\n")
                    .append("horizontal-flip verticalFlip horizontalFlipNew\n")
                    .append("vertical-flip horizontalFlipNew "
                            + "verticalFlipNew\n")
                    .toString(),
            output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "horizontally."));
    assertTrue(output.toString().contains("Successfully flipped the image "
            + "vertically."));
    assertEquals(
            Factory.createImage(TestUtils.createPixels(
                    new int[][]{
                            {255, 16711680},
                            {8421504, 65280},
                    }
            )),
            imageMemory.getImage("horizontalFlip")

    );
    assertEquals(
            Factory.createImage(TestUtils.createPixels(
                    new int[][]{
                            {8421504, 65280},
                            {255, 16711680},
                    }
            )),
            imageMemory.getImage("verticalFlip")

    );

    assertEquals(
            Factory.createImage(TestUtils.createPixels(
                    new int[][]{
                            {65280, 8421504},
                            {16711680, 255}
                    }
            )),
            imageMemory.getImage("horizontalFlipNew")

    );

    assertEquals(randomImage, imageMemory.getImage("verticalFlipNew"));
  }


  @Test
  public void rgbSplitAndCombineCombined() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomImage();
    initialiseController(
            new StringBuilder()
                    .append("rgb-split ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" redImage greenImage blueImage\n")
                    .append("brighten 1 redImage redImage\n")
                    .append("brighten 1 greenImage greenImage\n")
                    .append("brighten 1 blueImage blueImage\n")
                    .append("rgb-combine combinedImage redImage greenImage "
                            + "blueImage\n")
                    .toString(),
            output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Successfully split the image into "
            + "RGB components."));
    assertTrue(output.toString()
            .contains("Successfully combined the RGB components."));
    Image expectedImage = Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711937, 66047},
            {130817, 8487297}
        }
    ));
    assertEquals(expectedImage, imageMemory.getImage("combinedImage"));
  }


  @Test
  public void performMultipleOperationsOntheSameImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image blackImage = TestUtils.blackImage();
    initialiseController(
            new StringBuilder()
                    .append("brighten 1 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("brighten 0 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("brighten -1 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .toString(),
            output, blackImage);

    controller.processCommands();
    assertEquals(blackImage, imageMemory.getImage(INITIAL_IMAGE_NAME));
  }

  @Test
  public void performMultipleOperationsOnRandomImage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image blackImage = TestUtils.randomImage();
    initialiseController(
            new StringBuilder()
                    .append("brighten 1 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("brighten 0 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("brighten -1 ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .toString(),
            output, blackImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16646144, 254},
                            {65024, 8421504}
                    }
            )
    ), imageMemory.getImage(INITIAL_IMAGE_NAME));
  }


  @Test
  public void testWithMultipleBlurs() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image blackImage = TestUtils.randomImage();
    initialiseController(
            new StringBuilder()
                    .append("blur ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("blur ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("blur ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .toString(),
            output, blackImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {1052688, 1052433},
                            {1052943, 1052688}
                    }
            )
    ), imageMemory.getImage(INITIAL_IMAGE_NAME));
  }

  @Test
  public void testWithMultipleSharpen() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image blackImage = TestUtils.randomImage();
    initialiseController(
            new StringBuilder()
                    .append("sharpen ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("sharpen ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .append("sharpen ")
                    .append(INITIAL_IMAGE_NAME)
                    .append(" ").append(INITIAL_IMAGE_NAME).append("\n")
                    .toString(),
            output, blackImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16777215, 16777215},
                            {16777215, 16777215}
                    }
            )
    ), imageMemory.getImage(INITIAL_IMAGE_NAME));
  }

  @Test
  public void testColorCorrection() {
    try {
      StringBuilder output = new StringBuilder();
      Image inputImage = Factory.createImage(
              new Pixel[][]{
                      {Factory.createRGBPixel(100, 101, 102),
                              Factory.createRGBPixel(150, 151, 152)},
                      {Factory.createRGBPixel(200, 201, 202),
                              Factory.createRGBPixel(250, 251, 252)}
              }
      );

      initialiseController(
              String.format("color-correct %s color-corrected-input-image",
                      INITIAL_IMAGE_NAME),
              output,
              inputImage);

      controller.processCommands();
      assertTrue(output.toString().contains("Successfully color corrected the"
              + " image."));

      Image expectedImage = Factory.createImage(
              new Pixel[][]{
                      {Factory.createRGBPixel(101, 101, 101),
                              Factory.createRGBPixel(151, 151, 151)},
                      {Factory.createRGBPixel(201, 201, 201),
                              Factory.createRGBPixel(251, 251, 251)}
              }
      );
      assertEquals(expectedImage, imageMemory.getImage("color-corrected-input"
              + "-image"));

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testLevelsAdjustment() {
    try {
      StringBuilder output = new StringBuilder();
      Image inputImage = Factory.createImage(
              new Pixel[][]{
                      {Factory.createRGBPixel(100, 100, 100),
                              Factory.createRGBPixel(150, 150, 150)},
                      {Factory.createRGBPixel(200, 200, 200),
                              Factory.createRGBPixel(250, 250, 250)}
              }
      );

      initialiseController(
              String.format("levels-adjust 10 120 255 %s "
                              + "levels-adjusted-input-image",
                      INITIAL_IMAGE_NAME),
              output,
              inputImage);

      controller.processCommands();
      assertTrue(output.toString().contains("Successfully adjusted the levels"
              + " of the image to "
              + "black:10, mid:120, white:255."));

      Image expectedImage = Factory.createImage(
              new Pixel[][]{
                      {Factory.createRGBPixel(106, 106, 106),
                              Factory.createRGBPixel(159, 159, 159)},
                      {Factory.createRGBPixel(207, 207, 207),
                              Factory.createRGBPixel(251, 251, 251)}
              }
      );
      assertEquals(expectedImage, imageMemory.getImage("levels-adjusted-input"
              + "-image"));

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testLevelsAdjustmentWithInvalidBlackValues() {
    try {
      StringBuilder output = new StringBuilder();
      Image inputImage = Factory.createImage(
              new Pixel[][]{{Factory.createRGBPixel(0, 0, 0)}}
      );

      initialiseController(
              String.format("levels-adjust invalid_input 120 255 %s "
                              + "levels-adjusted-input-image",
                      INITIAL_IMAGE_NAME),
              output,
              inputImage);

      controller.processCommands();
      assertTrue(output.toString().contains("Invalid levels provided."));
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testLevelsAdjustmentWithInvalidMidValues() {
    try {
      StringBuilder output = new StringBuilder();
      Image inputImage = Factory.createImage(
              new Pixel[][]{{Factory.createRGBPixel(0, 0, 0)}}
      );

      initialiseController(
              String.format("levels-adjust 10 invalid_value 255 %s "
                              + "levels-adjusted-input-image",
                      INITIAL_IMAGE_NAME),
              output,
              inputImage);

      controller.processCommands();
      assertTrue(output.toString().contains("Invalid levels provided."));
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testLevelsAdjustmentWithInvalidValues() {
    try {
      StringBuilder output = new StringBuilder();
      Image inputImage = Factory.createImage(
              new Pixel[][]{{Factory.createRGBPixel(0, 0, 0)}}
      );

      initialiseController(
              String.format("levels-adjust 10 120 invalid_value %s "
                              + "levels-adjusted-input-image",
                      INITIAL_IMAGE_NAME),
              output,
              inputImage);

      controller.processCommands();
      assertTrue(output.toString().contains("Invalid levels provided."));
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testBlurWithInvalidOptionalParameter() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage invalid",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6230063, 4663127, 999231},
                            {6234159, 6233935, 2039631}
                    }
            )
    ), imageMemory.getImage("blurImage"));

    assertTrue(output.toString().contains("Successfully blurred the image.\n"
            + "Invalid command: invalid"));


  }

  @Test
  public void testBlurWithZeroPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split 0",
                    INITIAL_IMAGE_NAME),
            output, randomImage);
    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("Successfully blurred the image."));
  }

  @Test
  public void testBlurWithNegativePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split -1",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testBlurWithHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage 100",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6230063, 4663127, 999231},
                            {6234159, 6233935, 2039631}
                    }
            )
    ), imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("Successfully blurred the image."));
  }

  @Test
  public void testBlurWithGreaterThanHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split 101",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testBlurWithThirtyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split 30",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("Successfully blurred the image."));
  }

  @Test
  public void testBlurWithFiftyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split 50",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6230063, 255, 65280},
                            {6234159, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("Successfully blurred the image."));
  }

  @Test
  public void testBlurWithSeventyFivePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("blur %s blurImage split 75",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6230063, 4663127, 65280},
                            {6234159, 6233935, 255}
                    }
            )
    ), imageMemory.getImage("blurImage"));
    assertTrue(output.toString().contains("Successfully blurred the image."));
  }


  @Test
  public void testSharpenWithInvalidOptionalParameter() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage invalid",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711743, 10444799, 1044335},
                            {16736415, 16736159, 995327}
                    }
            )
    ), imageMemory.getImage("sharpenImage"));

    assertTrue(output.toString().contains("Successfully sharpened the image"
            + ".\n"
            + "Invalid command: invalid"));
  }

  @Test
  public void testSharpenWithZeroPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split 0",
                    INITIAL_IMAGE_NAME),
            output, randomImage);
    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("Successfully sharpened the image."));
  }

  @Test
  public void testSharpenWithNegativePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split -1",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testSharpenWithHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage 100",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
                    TestUtils.createPixels(
                            new int[][]{
                                    {16711743, 10444799, 1044335},
                                    {16736415, 16736159, 995327}
                            }
                    )),
            imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("Successfully sharpened the image."));
  }

  @Test
  public void testSharpenWithGreaterThanHundredPercentageSplitView()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split 101",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testSharpenWithThirtyPercentageSplitView()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split 30",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("Successfully sharpened the image."));
  }

  @Test
  public void testSharpenWithFiftyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split 50",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711743, 255, 65280},
                            {16736415, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("Successfully sharpened the image."));
  }

  @Test
  public void testSharpenWithSeventyFivePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sharpen %s sharpenImage split 75",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711743, 10444799, 65280},
                            {16736415, 16736159, 255}
                    }
            )
    ), imageMemory.getImage("sharpenImage"));
    assertTrue(output.toString().contains("Successfully sharpened the image."));
  }

  @Test
  public void testSepiaWithInvalidOptionalParameter() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage invalid",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6576197, 3156513, 12889736},
                            {11311479, 6576197, 3156513}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));

    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia.\n"
            + "Invalid command: invalid"));
  }

  @Test
  public void testSepiaWithZeroPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split 0",
                    INITIAL_IMAGE_NAME),
            output, randomImage);
    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
  }

  @Test
  public void testSepiaWithNegativePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split -1",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testSepiaWithHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage 100",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6576197, 3156513, 12889736},
                            {11311479, 6576197, 3156513}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
  }

  @Test
  public void testSepiaWithGreaterThanHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split 101",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testSepiaWithThirtyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split 30",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {16711680, 255, 65280},
                            {8421504, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
  }

  @Test
  public void testSepiaWithFiftyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split 50",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6576197, 255, 65280},
                            {11311479, 16711680, 255}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
  }

  @Test
  public void testSepiaWithSeventyFivePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("sepia %s sepiaImage split 75",
                    INITIAL_IMAGE_NAME),
            output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(
            TestUtils.createPixels(
                    new int[][]{
                            {6576197, 3156513, 65280},
                            {11311479, 6576197, 255}
                    }
            )
    ), imageMemory.getImage("sepiaImage"));
    assertTrue(output.toString().contains("Successfully converted the image "
            + "to sepia."));
  }




  @Test
  public void testLevelAdjustmentWithInvalidOptionalParameter() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                            + "levelAdjustedImage invalid",
                    INITIAL_IMAGE_NAME), output,
            randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }

  @Test
  public void testLevelAdjustmentWithZeroPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage 0",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }

  @Test
  public void testLevelAdjustmentWithNegativePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage split -1",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testLevelAdjustmentWithHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage 100",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(randomImage, imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }

  @Test
  public void testLevelAdjustmentWithGreaterThanHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage split 101",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertThrows(ImageProcessorException.NotFoundException.class, () -> {
      imageMemory.getImage("levelAdjustedImage");
    });
    assertTrue(output.toString()
            .contains("The percentage must be between 0 and 100"));
  }

  @Test
  public void testLevelAdjustmentWithThirtyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage 30",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }

  @Test
  public void testLevelAdjustmentWithFiftyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage 50",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }

  @Test
  public void testLevelAdjustmentWithSeventyFivePercentageSplitView()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("levels-adjust 0 128 255 %s "
                    + "levelAdjustedImage 75",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("levelAdjustedImage"));
    assertTrue(output.toString()
            .contains("Successfully adjusted the levels of the image"));
  }


  @Test
  public void testColorCorrectionWithInvalidOptionalParameter() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "invalid", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }

  @Test
  public void testColorCorrectionWithZeroPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "0", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }

  @Test
  public void testColorCorrectionWithNegativePercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "split -1", INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertThrows(
            ImageProcessorException.NotFoundException.class,
        () -> imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testColorCorrectionWithHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "100", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(randomImage, imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }

  @Test
  public void testColorCorrectionWithGreaterThanHundredPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "split 101", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertThrows(ImageProcessorException.NotFoundException.class, () -> {
      imageMemory.getImage("colorCorrectedImage");
    });
    assertTrue(output.toString().contains("The percentage must be between 0 "
            + "and 100"));
  }

  @Test
  public void testColorCorrectionWithThirtyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "30", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }

  @Test
  public void testColorCorrectionWithFiftyPercentageSplitView() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "50", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }

  @Test
  public void testColorCorrectionWithSeventyFivePercentageSplitView()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("color-correct %s colorCorrectedImage "
            + "75", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("colorCorrectedImage"));
    assertTrue(output.toString().contains("Successfully color corrected the "
            + "image."));
  }


  @Test
  public void testCompressionWithZeroPercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 0 %s compressedImage",
                    INITIAL_IMAGE_NAME),
            output,
            randomImage);
    controller.processCommands();

    assertEquals(Factory.createImage(
            TestUtils.createPixels(new int[][]{
                    {14618624, 2101487, 61200},
                    {6328432, 16715776, 255}
            })
    ), imageMemory.getImage("compressedImage"));

    assertTrue(output.toString()
            .contains("Successfully compressed the image"));
  }

  @Test
  public void testCompressionWithTwentyFivePercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 25 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();

    assertEquals(Factory.createImage(
            TestUtils.createPixels(new int[][]{
                    {16723968, 4206767, 61200},
                    {4206767, 16723968, 255}
            })
    ), imageMemory.getImage("compressedImage"));

    assertTrue(output.toString().contains("Successfully compressed the image"));
  }

  @Test
  public void testCompressionWithFiftyPercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 50 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();

    assertEquals(Factory.createImage(
            TestUtils.createPixels(new int[][]{
                    {6225920, 175, 48976},
                    {175, 6225920, 80}
            })
    ), imageMemory.getImage("compressedImage"));

    assertTrue(output.toString().contains("Successfully compressed the image"));
  }

  @Test
  public void testCompressionWithSeventyFivePercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 75 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();

    assertEquals(Factory.createImage(
            TestUtils.createPixels(new int[][]{
                    {0, 96, 0},
                    {96, 0, 0}
            })
    ), imageMemory.getImage("compressedImage"));

    assertTrue(output.toString().contains("Successfully compressed the image"));
  }

  @Test
  public void testCompressionWithHundredPercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 100 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();

    assertEquals(Factory.createImage(
            TestUtils.createPixels(new int[][]{
                    {0, 0, 0},
                    {0, 0, 0}
            })
    ), imageMemory.getImage("compressedImage"));

    assertTrue(output.toString().contains("Successfully compressed the image"));
  }

  @Test
  public void testCompressionWithPNGToPNGAndPPMFormats() throws
          IOException {
    for (int compression = 0; compression <= 100; compression++) {
      StringBuilder output = new StringBuilder();
      initialiseController(
              String.format("load test_resources/input/random.png randomImage "
                              + "compress %s randomImage compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.png "
                              + "compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.ppm "
                              + "compressedImage%s",
                      compression, compression, compression, compression,
                      compression, compression),
              output, null);

      controller.processCommands();
      Path currentFilePath = Paths.get(
              "test_resources/input/random.png"
      );
      Path compressedPngPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.png",
                      compression)
      );


      Path compressedPpmPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.ppm",
                      compression)
      );

      assertTrue(Files.exists(currentFilePath));
      assertTrue(Files.exists(compressedPngPath));
      assertTrue(Files.exists(compressedPpmPath));

      assertTrue(Files.size(compressedPngPath) <= Files.size(currentFilePath));
      assertTrue(Files.size(compressedPpmPath) <= Files.size(currentFilePath));
    }
  }

  @Test
  public void testCompressionWithJPGToPNGAndPPMFormats() throws
          IOException {
    for (int compression = 0; compression <= 100; compression++) {
      StringBuilder output = new StringBuilder();
      initialiseController(
              String.format("load test_resources/input/random.jpg randomImage "
                              + "compress %s randomImage compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.png "
                              + "compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.ppm "
                              + "compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.jpg "
                              + "compressedImage%s",
                      compression, compression, compression, compression,
                      compression, compression, compression, compression),
              output, null);

      controller.processCommands();
      Path currentFilePath = Paths.get(
              "test_resources/input/random.jpg"
      );
      Path compressedPngPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.png",
                      compression)
      );
      Path compressedJpgPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.jpg",
                      compression)
      );

      Path compressedPpmPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.ppm",
                      compression)
      );

      assertTrue(Files.exists(currentFilePath));
      assertTrue(Files.exists(compressedPngPath));
      assertTrue(Files.exists(compressedPpmPath));
      assertTrue(Files.exists(compressedJpgPath));

      assertTrue(Files.size(compressedPngPath) <= Files.size(currentFilePath));
      assertTrue(Files.size(compressedPpmPath) <= Files.size(currentFilePath));
      assertTrue(Files.size(compressedJpgPath) <= Files.size(currentFilePath));
    }
  }

  @Test
  public void testCompressionWithPPMToPPM() throws
          IOException {
    for (int compression = 0; compression <= 100; compression++) {
      StringBuilder output = new StringBuilder();
      initialiseController(
              String.format("load test_resources/input/random.ppm randomImage "
                              + "compress %s randomImage compressedImage%s"
                              + " save test_resources/output/compressedImage"
                              + "%s.ppm "
                              + "compressedImage%s",
                      compression, compression,
                      compression, compression),
              output, null);

      controller.processCommands();
      Path currentFilePath = Paths.get(
              "test_resources/input/random.ppm"
      );
      Path compressedPpmPath = Paths.get(
              String.format("test_resources/output/compressedImage%s.ppm",
                      compression)
      );

      assertTrue(Files.exists(currentFilePath));
      assertTrue(Files.exists(compressedPpmPath));
      assertTrue(Files.size(compressedPpmPath) <= Files.size(currentFilePath));
    }
  }

  @Test
  public void compressionWithInvalidCommandAfterCompressing() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 0 %s compressedImage invalid",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command: invalid"));
  }

  @Test
  public void compressionWithNegativeCompressionPercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress -1 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid compression percentage"));
  }

  @Test
  public void compressionWithGreaterThanHundredCompressionPercentage() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 101 %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid compression percentage"));
  }

  @Test
  public void compressionWithInvalidCompressionPercentage()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress invalid %s compressedImage",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid compression percentage"));
  }

  @Test
  public void compressionWithInvalidImageName() throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController("compress 0 invalid compressedImage",
            output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Image with name invalid not found "
            + "in memory"));
  }

  @Test
  public void testCompressionWithInvalidDestinationName()
          throws
          ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("compress 0 %s",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertTrue(output.toString().contains("Invalid command parameters."));
  }

  // SPLIT VIEW TEST FOR LUMA
  @Test
  public void testLumaWithZeroPercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s "
            + "lumaImage split 0", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("lumaImage"));
    assertTrue(output.toString().contains("Successfully created luma component"));
  }

  @Test
  public void testLumaWithNegativePercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s lumaImage split -1",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertThrows(ImageProcessorException.class, () -> {
      imageMemory.getImage("lumaImage");
    });
    assertTrue(output.toString().contains("The percentage must be between 0 and 100"));
  }

  @Test
  public void testLumaWithHundredPercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s "
            + "lumaImage split 100", INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {3552822, 1184274, 11974326}, {8421504, 3552822, 1184274}
    })), imageMemory.getImage("lumaImage"));
    assertTrue(output.toString().contains("Successfully created luma component"));
  }

  @Test
  public void testLumaWithGreaterThanHundredPercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s lumaImage split 101",
            INITIAL_IMAGE_NAME), output, randomImage);
    controller.processCommands();
    assertThrows(ImageProcessorException.class, () -> {
      imageMemory.getImage("lumaImage");
    });
    assertTrue(output.toString().contains("The percentage must be between 0 and 100"));
  }

  @Test
  public void testLumaWithThirtyPercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s lumaImage split 30",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {16711680, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("lumaImage"));
    assertTrue(output.toString().contains("Successfully created luma component"));
  }

  @Test
  public void testLumaWithFiftyPercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s lumaImage split 50",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();

    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {3552822, 255, 65280}, {8421504, 16711680, 255}
    })), imageMemory.getImage("lumaImage"));
    assertTrue(output.toString().contains("Successfully created luma component"));
  }

  @Test
  public void testLumaWithSeventyFivePercentageSplitView() throws ImageProcessorException {
    StringBuilder output = new StringBuilder();
    Image randomImage = TestUtils.randomRectangleImage();
    initialiseController(String.format("luma-component %s lumaImage split 75",
            INITIAL_IMAGE_NAME), output, randomImage);

    controller.processCommands();
    assertEquals(Factory.createImage(TestUtils.createPixels(new int[][]{
            {3552822, 1184274, 65280}, {8421504, 3552822, 255}
    })), imageMemory.getImage("lumaImage"));
    assertTrue(output.toString().contains("Successfully created luma component"));
  }
}
