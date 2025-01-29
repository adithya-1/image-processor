import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;


import app.parsers.ArgumentParser;
import app.parsers.CommandLineArgumentParser;
import app.parsers.GUIArgumentParser;
import app.parsers.InteractiveArgumentParser;
import controller.CommandLineImageProcessorController;
import controller.ExecutionStatus;
import app.ImageProcessorApp;
import controller.GUIImageProcessorController;
import controller.ImageProcessorController;
import controller.InteractiveImageProcessorController;
import exception.ImageProcessingRunTimeException;
import exception.ImageProcessorException;
import factories.Factory;
import model.enumeration.FilterOption;
import model.memory.ImageMemory;
import model.request.ImageProcessingRequest;
import controller.services.FileImageProcessingService;
import controller.services.ImageProcessingService;
import utility.ExtractUtility;
import utility.FilterUtils;
import model.enumeration.ImageType;
import model.enumeration.LinearColorTransformationType;
import model.enumeration.PixelType;
import model.enumeration.UserCommand;
import model.memory.HashMapMemory;
import model.pixels.Pixel;
import model.pixels.RGB;
import model.visual.Image;
import model.visual.RenderedImage;
import utility.IOUtils;
import view.text.ConsoleInput;
import view.DisplayMessageType;
import view.text.ConsoleOutput;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains all the unit tests for the Image Process Application.
 */
@RunWith(Enclosed.class)
public class UnitTestForImageProcessor {

  /**
   * Contains all the unit tests for the Display Exception.
   */
  public static class DisplayExceptionTest {

    @Test
    public void testDisplayExceptionMessage() {
      String message = "Error displaying image";
      Throwable cause = new Throwable("Cause of the error");
      ImageProcessingRunTimeException.DisplayException exception =
              new ImageProcessingRunTimeException.DisplayException(message,
                      cause);
      assertEquals(message, exception.getMessage());
    }

    @Test
    public void testDisplayExceptionMessageAndCause() {
      String message = "Error displaying image";
      Throwable cause = new Throwable("Cause of the error");
      ImageProcessingRunTimeException.DisplayException exception =
              new ImageProcessingRunTimeException.DisplayException(message,
                      cause);
      assertEquals(message, exception.getMessage());
      assertEquals(cause, exception.getCause());
    }
  }

  /**
   * Contains all the unit tests for the Image Processing Run Exception.
   */
  public static class ImageProcessingRunTimeExceptionTest {
    @Test
    public void testConstructorWithMessage() {
      String message = "Test message";
      ImageProcessingRunTimeException exception =
              new ImageProcessingRunTimeException(message);
      assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
      String message = "Test message";
      Throwable cause = new Throwable("Cause message");
      ImageProcessingRunTimeException exception =
              new ImageProcessingRunTimeException(message, cause);
      assertEquals(message, exception.getMessage());
      assertEquals(cause, exception.getCause());
    }
  }

  /**
   * Contains all the unit tests for the Image Processing Exception.
   */
  public static class ImageProcessorExceptionTest {
    @Test
    public void testImageProcessorExceptionMessage() {
      String errorMessage = "Error processing image";
      ImageProcessorException exception =
              new ImageProcessorException(errorMessage);
      assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testImageProcessorExceptionMessageAndCause() {
      String errorMessage = "Error processing image";
      Throwable cause = new Throwable("Cause of the error");
      ImageProcessorException exception =
              new ImageProcessorException(errorMessage, cause);
      assertEquals(errorMessage, exception.getMessage());
      assertEquals(cause, exception.getCause());
    }
  }

  /**
   * Contains all the unit tests for the Not Found Exception.
   */
  public static class NotFoundExceptionTest {

    @Test
    public void testNotFoundExceptionMessage() {
      String errorMessage = "Error processing image";
      ImageProcessorException.NotFoundException exception =
              new ImageProcessorException.NotFoundException(errorMessage);
      assertEquals(errorMessage, exception.getMessage());
    }

  }

  /**
   * Contains all the unit tests for the Not Implemented Exception.
   */
  public static class NotImplementedExceptionTest {

    @Test
    public void testConstructorWithMessage() {
      String message = "Test message";
      ImageProcessorException.NotImplementedException exception =
              new ImageProcessorException.NotImplementedException(message);
      assertEquals(message, exception.getMessage());
    }
  }

  /**
   * Contains all the unit tests for the Quit Exception.
   */
  public static class QuitExceptionTest {

    @Test
    public void testConstructorWithMessage() {
      String message = "Test message";
      ImageProcessingRunTimeException.QuitException exception =
              new ImageProcessingRunTimeException.QuitException(message);
      assertEquals(message, exception.getMessage());
    }
  }

  /**
   * Contains all the unit tests for the Image class.
   */
  public static class ImageTest {
    @Test
    public void testGetPixel() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(255, 0, 0);
      pixels[0][1] = new RGB(0, 255, 0);
      pixels[1][0] = new RGB(0, 0, 255);
      pixels[1][1] = new RGB(255, 255, 0);

      Image image = new RenderedImage(pixels);

      assertEquals(pixels[0][0], image.getPixel(0, 0));
      assertEquals(pixels[0][1], image.getPixel(0, 1));
      assertEquals(pixels[1][0], image.getPixel(1, 0));
      assertEquals(pixels[1][1], image.getPixel(1, 1));
    }

    @Test
    public void testBrightenImage() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 100, 100);
      pixels[0][1] = new RGB(150, 150, 150);
      pixels[1][0] = new RGB(200, 200, 200);
      pixels[1][1] = new RGB(250, 250, 250);

      Image image = new RenderedImage(pixels);
      Image brightenedImage = image.adjustImageBrightness(50);

      assertEquals(new RGB(150, 150, 150), brightenedImage.getPixel(0, 0));
      assertEquals(new RGB(200, 200, 200), brightenedImage.getPixel(0, 1));
      assertEquals(new RGB(250, 250, 250), brightenedImage.getPixel(1, 0));
      assertEquals(new RGB(255, 255, 255), brightenedImage.getPixel(1, 1));
    }

    @Test
    public void testColorCorrect() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 101, 102);
      pixels[0][1] = new RGB(150, 151, 152);
      pixels[1][0] = new RGB(200, 201, 202);
      pixels[1][1] = new RGB(250, 251, 252);

      Image image = new RenderedImage(pixels);
      Image colorCorrectedImage = image.colorCorrect();

      assertEquals(new RGB(101, 101, 101), colorCorrectedImage.getPixel(0, 0));
      assertEquals(new RGB(151, 151, 151), colorCorrectedImage.getPixel(0, 1));
      assertEquals(new RGB(201, 201, 201), colorCorrectedImage.getPixel(1, 0));
      assertEquals(new RGB(251, 251, 251), colorCorrectedImage.getPixel(1, 1));
    }

    @Test
    public void testColorCorrect_RandomDistribution() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][3];
      pixels[0][0] = new RGB(120, 130, 140);
      pixels[0][1] = new RGB(150, 160, 170);
      pixels[0][2] = new RGB(180, 190, 200);
      pixels[1][0] = new RGB(210, 220, 230);
      pixels[1][1] = new RGB(240, 250, 255);
      pixels[1][2] = new RGB(100, 110, 120);


      Image image = new RenderedImage(pixels);
      Image colorCorrectedImage = image.colorCorrect();

      assertEquals(new RGB(130, 130, 130), colorCorrectedImage.getPixel(0, 0));
      assertEquals(new RGB(160, 160, 160), colorCorrectedImage.getPixel(0, 1));
      assertEquals(new RGB(190, 190, 190), colorCorrectedImage.getPixel(0, 2));
      assertEquals(new RGB(220, 220, 220), colorCorrectedImage.getPixel(1, 0));
      assertEquals(new RGB(250, 250, 245), colorCorrectedImage.getPixel(1, 1));
      assertEquals(new RGB(110, 110, 110), colorCorrectedImage.getPixel(1, 2));
    }

    @Test
    public void testColorCorrectPreservesNaturalGrey() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 100, 100);
      pixels[0][1] = new RGB(150, 150, 150);
      pixels[1][0] = new RGB(200, 200, 200);
      pixels[1][1] = new RGB(250, 250, 250);

      Image image = new RenderedImage(pixels);
      Image colorCorrectedImage = image.colorCorrect();

      assertEquals(new RGB(100, 100, 100), colorCorrectedImage.getPixel(0, 0));
      assertEquals(new RGB(150, 150, 150), colorCorrectedImage.getPixel(0, 1));
      assertEquals(new RGB(200, 200, 200), colorCorrectedImage.getPixel(1, 0));
      assertEquals(new RGB(250, 250, 250), colorCorrectedImage.getPixel(1, 1));
    }

    @Test
    public void testColorCorrectIgnoreDistribitionAtEdges() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][3];
      pixels[0][0] = new RGB(9, 128, 255);
      pixels[0][1] = new RGB(127, 128, 129);
      pixels[0][2] = new RGB(9, 128, 255);

      Image image = new RenderedImage(pixels);
      Image colorCorrectedImage = image.colorCorrect();

      assertEquals(new RGB(10, 128, 254), colorCorrectedImage.getPixel(0, 0));
      assertEquals(new RGB(128, 128, 128), colorCorrectedImage.getPixel(0, 1));
      assertEquals(new RGB(10, 128, 254), colorCorrectedImage.getPixel(0, 2));
    }

    @Test
    public void testColorCorrectWithSinglePixel() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(50, 100, 150);

      Image testImage = new RenderedImage(pixels);
      Image result = testImage.colorCorrect();

      Pixel pixel = result.getPixel(0, 0);
      assertEquals(new RGB(100, 100, 100), result.getPixel(0, 0));
    }

    @Test
    public void testLevelsAdjust() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 100, 100);
      pixels[0][1] = new RGB(150, 150, 150);
      pixels[1][0] = new RGB(200, 200, 200);
      pixels[1][1] = new RGB(250, 250, 250);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(0, 128, 255);

      assertEquals(new RGB(100, 100, 100), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(150, 150, 150), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(200, 200, 200), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(250, 250, 250), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test
    public void testLevelsAdjustCurve() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 100, 100);
      pixels[0][1] = new RGB(150, 150, 150);
      pixels[1][0] = new RGB(200, 200, 200);
      pixels[1][1] = new RGB(250, 250, 250);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(10, 120, 255);

      assertEquals(new RGB(106, 106, 106), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(159, 159, 159), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(207, 207, 207), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(251, 251, 251), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test
    public void testLevelsAdjustCurve_ClampingAT0() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(10, 10, 10);
      pixels[0][1] = new RGB(255, 255, 255);
      pixels[1][0] = new RGB(20, 20, 20);
      pixels[1][1] = new RGB(128, 128, 128);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(10, 20, 50);

      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(128, 128, 128), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test
    public void testLevelsAdjustCurve_ClampingAT255() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(10, 10, 10);
      pixels[0][1] = new RGB(210, 210, 210);
      pixels[1][0] = new RGB(101, 101, 101);
      pixels[1][1] = new RGB(128, 128, 128);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(100, 200, 210);

      assertEquals(new RGB(255, 255, 255), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test
    public void testLevelsAdjustCurve_AdjacentPoints() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(10, 10, 10);
      pixels[0][1] = new RGB(210, 210, 210);
      pixels[1][0] = new RGB(101, 101, 101);
      pixels[1][1] = new RGB(128, 128, 128);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(128, 129, 130);

      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test
    public void testLevelsAdjustCurve_CheckIfPointsMatchCurve() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(10, 10, 10);
      pixels[0][1] = new RGB(128, 128, 128);
      pixels[1][0] = new RGB(235, 235, 235);
      pixels[1][1] = new RGB(255, 255, 255);

      Image image = new RenderedImage(pixels);
      Image levelsAdjustedImage = image.levelsAdjust(10, 128, 235);

      assertEquals(new RGB(0, 0, 0), levelsAdjustedImage.getPixel(0, 0));
      assertEquals(new RGB(128, 128, 128), levelsAdjustedImage.getPixel(0, 1));
      assertEquals(new RGB(255, 255, 255), levelsAdjustedImage.getPixel(1, 0));
      assertEquals(new RGB(255, 255, 255), levelsAdjustedImage.getPixel(1, 1));
    }

    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_BLessThan0() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(-1, 128, 256);
    }

    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_BGreaterThan255() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(256, 128, 256);
    }

    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_MLessThan0() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(10, -1, 255);
    }


    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_MGreaterThan255() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(10, 256, 255);
    }

    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_WLessThan0() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(10, 128, -2);
    }


    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_WGreaterThan255() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(10, 128, 256);
    }

    @Test(expected = ImageProcessorException.class)
    public void testLevelsAdjustCurve_BMWNotInOrder() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[1][1];
      pixels[0][0] = new RGB(10, 10, 10);
      Image image = new RenderedImage(pixels);

      image.levelsAdjust(255, 128, 0);
    }

    @Test
    public void testDarkenImage() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 100, 100);
      pixels[0][1] = new RGB(150, 150, 150);
      pixels[1][0] = new RGB(200, 200, 200);
      pixels[1][1] = new RGB(250, 250, 250);

      Image image = new RenderedImage(pixels);
      Image darkenedImage = image.adjustImageBrightness(-50);

      assertEquals(new RGB(50, 50, 50), darkenedImage.getPixel(0, 0));
      assertEquals(new RGB(100, 100, 100), darkenedImage.getPixel(0, 1));
      assertEquals(new RGB(150, 150, 150), darkenedImage.getPixel(1, 0));
      assertEquals(new RGB(200, 200, 200), darkenedImage.getPixel(1, 1));
    }
  }


  /**
   * Contains all the unit tests for user console output.
   */
  public static class ConsoleViewTest {

    private StringReader stringReader;
    private StringWriter stringWriter;
    private ConsoleInput consoleInput;
    private ConsoleOutput consoleOutput;

    @Before
    public void setUp() {
      stringReader = new StringReader("");
      stringWriter = new StringWriter();
      consoleInput = new ConsoleInput(stringReader);
      consoleOutput = new ConsoleOutput(stringWriter);

    }

    @Test
    public void testDisplayMessage()
            throws
            ImageProcessingRunTimeException.DisplayException {
      String message = "Hello, World!";
      consoleOutput.displayMessage(message, DisplayMessageType.INFO);
      assertEquals("Hello, World!\n", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOutput() {
      new ConsoleInput(null);
    }

    @Test(expected = ImageProcessingRunTimeException.DisplayException.class)
    public void testDisplayMessageThrowsDisplayException()
            throws
            ImageProcessingRunTimeException.DisplayException {
      Appendable failingAppendable = new Appendable() {
        @Override
        public Appendable append(CharSequence csq) throws
                IOException {
          throw new IOException("Forced IOException");
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws
                IOException {
          throw new IOException("Forced IOException");
        }

        @Override
        public Appendable append(char c) throws
                IOException {
          throw new IOException("Forced IOException");
        }
      };
      ConsoleOutput failingConsoleOutPut = new ConsoleOutput(failingAppendable);
      failingConsoleOutPut.displayMessage("This will fail",
              DisplayMessageType.ERROR);
    }
  }

  /**
   * Contains all the unit tests for hash map memory.
   */
  public static class HashMapMemoryTest {

    private ImageMemory memory;
    private Image testImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      memory = new HashMapMemory();
      Pixel[][] pixels = new Pixel[1][1];
      testImage = new RenderedImage(pixels);
    }

    @Test
    public void testEqualsAndHashCode() throws
            ImageProcessorException {
      Pixel[][] pixels1 = new Pixel[2][2];
      pixels1[0][0] = new RGB(255, 0, 0);
      pixels1[0][1] = new RGB(0, 255, 0);
      pixels1[1][0] = new RGB(0, 0, 255);
      pixels1[1][1] = new RGB(255, 255, 0);

      Pixel[][] pixels2 = new Pixel[2][2];
      pixels2[0][0] = new RGB(255, 0, 0);
      pixels2[0][1] = new RGB(0, 255, 0);
      pixels2[1][0] = new RGB(0, 0, 255);
      pixels2[1][1] = new RGB(255, 255, 0);

      Image image1 = new RenderedImage(pixels1);
      Image image2 = new RenderedImage(pixels2);

      HashMapMemory memory1 = new HashMapMemory();
      HashMapMemory memory2 = new HashMapMemory();

      memory1.addImage("image1", image1);
      memory2.addImage("image1", image2);

      assertTrue(memory1.equals(memory2));
      assertEquals(memory1.hashCode(), memory2.hashCode());

      Pixel[][] pixels3 = new Pixel[2][2];
      pixels3[0][0] = new RGB(0, 0, 0);
      pixels3[0][1] = new RGB(0, 0, 0);
      pixels3[1][0] = new RGB(0, 0, 0);
      pixels3[1][1] = new RGB(0, 0, 0);

      Image image3 = new RenderedImage(pixels3);

      HashMapMemory memory3 = new HashMapMemory();
      memory3.addImage("image3", image3);

      assertFalse(memory1.equals(memory3));
      assertNotEquals(memory1.hashCode(), memory3.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
      HashMapMemory memory1 = new HashMapMemory();
      assertTrue(memory1.equals(memory1));
    }

    @Test
    public void testEqualsDifferentType() {
      HashMapMemory memory1 = new HashMapMemory();
      assertFalse(memory1.equals("Not a HashMapMemory"));
    }

    @Test
    public void testEqualsNull() {
      HashMapMemory memory1 = new HashMapMemory();
      assertNotEquals(null, memory1);
    }

    @Test
    public void testEqualsDifferentMemory() throws
            ImageProcessorException {
      HashMapMemory memory1 = new HashMapMemory();
      HashMapMemory memory2 = new HashMapMemory();
      memory1.addImage("image1", new RenderedImage(new Pixel[1][1]));
      assertFalse(memory1.equals(memory2));
    }

    @Test
    public void testConstructor() {
      assertNotNull(memory);
    }

    @Test
    public void testAddAndRetrieveImage() throws
            ImageProcessorException.NotFoundException {
      memory.addImage("testImage", testImage);
      assertEquals(testImage, memory.getImage("testImage"));
    }

    @Test(expected = ImageProcessorException.NotFoundException.class)
    public void testAddNullImage() throws
            ImageProcessorException.NotFoundException {
      memory.addImage("nullImage", null);
      memory.getImage("nullImage");
    }

    @Test(expected = ImageProcessorException.NotFoundException.class)
    public void testAddUnnamedImage() throws
            ImageProcessorException.NotFoundException {
      memory.addImage(null, testImage);
      memory.getImage(null);
    }

    @Test(expected = ImageProcessorException.NotFoundException.class)
    public void testRetrieveNonExistentImage() throws
            ImageProcessorException.NotFoundException {
      memory.getImage("nonExistentImage");
    }
  }


  /**
   * Contains all the unit tests for a black pixel.
   */
  public static class BlackPixelTest {
    private final int BLACK = 0;
    private Pixel blackPixel;

    @Before
    public void setUp() {
      blackPixel = new RGB(BLACK, BLACK, BLACK);
    }

    @Test
    public void testConstructor() {
      assertEquals(BLACK, blackPixel.getRed());
      assertEquals(BLACK, blackPixel.getGreen());
      assertEquals(BLACK, blackPixel.getBlue());
    }

    @Test
    public void testCreatePixel() {
      Pixel newPixel = blackPixel.createPixel(0, 0, 0);
      assertTrue(newPixel instanceof RGB);
      assertEquals(BLACK, newPixel.getRed());
      assertEquals(BLACK, newPixel.getGreen());
      assertEquals(BLACK, newPixel.getBlue());
    }

    @Test
    public void testCreateRedComponent() {
      Pixel redComponent = blackPixel.createRedComponent();
      assertEquals(BLACK, redComponent.getRed());
      assertEquals(BLACK, redComponent.getGreen());
      assertEquals(BLACK, redComponent.getBlue());
    }

    @Test
    public void testCreateGreenComponent() {
      Pixel greenComponent = blackPixel.createGreenComponent();
      assertEquals(BLACK, greenComponent.getRed());
      assertEquals(BLACK, greenComponent.getGreen());
      assertEquals(BLACK, greenComponent.getBlue());
    }

    @Test
    public void testCreateBlueComponent() {
      Pixel blueComponent = blackPixel.createBlueComponent();
      assertEquals(BLACK, blueComponent.getRed());
      assertEquals(BLACK, blueComponent.getGreen());
      assertEquals(BLACK, blueComponent.getBlue());
    }

    @Test
    public void testEqualsReflexive() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      assertEquals("Reflexive property failed", x, x);
    }

    @Test
    public void testEqualsSymmetric() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      Pixel y = new RGB(BLACK, BLACK, BLACK);

      assertEquals("Forward symmetric test failed", x, y);
      assertEquals("Backward symmetric test failed", y, x);
    }

    @Test
    public void testEqualsTransitive() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      Pixel y = new RGB(BLACK, BLACK, BLACK);
      Pixel z = new RGB(BLACK, BLACK, BLACK);

      assertEquals("First transitive condition failed", x, y);
      assertEquals("Second transitive condition failed", y, z);
      assertEquals("Transitive property failed", x, z);
    }

    @Test
    public void testEqualsConsistent() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      Pixel y = new RGB(BLACK, BLACK, BLACK);

      boolean firstResult = x.equals(y);
      for (int i = 0; i < 5; i++) {
        assertEquals("Consistent property failed on iteration " + i,
                firstResult, x.equals(y));
      }
    }

    @Test
    public void testEqualsNullComparison() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      assertNotEquals("Null comparison failed", null, x);
    }

    @Test
    public void testEqualsDifferentTypes() {
      Pixel x = new RGB(BLACK, BLACK, BLACK);
      Object nonPixel = "Not a pixel";
      assertNotEquals("Different type comparison failed", x, nonPixel);
    }

    @Test
    public void testAdjustBrightness() {
      Pixel brighterPixel = blackPixel.adjustBrightness(50);
      assertEquals(50, brighterPixel.getRed());
      assertEquals(50, brighterPixel.getGreen());
      assertEquals(50, brighterPixel.getBlue());

      Pixel darkerPixel = blackPixel.adjustBrightness(-50);
      assertEquals(BLACK, darkerPixel.getRed());
      assertEquals(BLACK, darkerPixel.getGreen());
      assertEquals(BLACK, darkerPixel.getBlue());
    }

    @Test
    public void testGetIntensity() {
      Pixel intensity = blackPixel.getIntensity();
      assertEquals(BLACK, intensity.getRed());
      assertEquals(BLACK, intensity.getGreen());
      assertEquals(BLACK, intensity.getBlue());
    }

    @Test
    public void testGetValue() {
      Pixel value = blackPixel.getValue();
      assertEquals(BLACK, value.getRed());
      assertEquals(BLACK, value.getGreen());
      assertEquals(BLACK, value.getBlue());
    }

    @Test
    public void testGetLuma() {
      Pixel luma = blackPixel.getLuma();
      assertEquals(BLACK, luma.getRed());
      assertEquals(BLACK, luma.getGreen());
      assertEquals(BLACK, luma.getBlue());
    }

    @Test
    public void testGetSepia() {
      Pixel sepia = blackPixel.getSepia();
      assertEquals(BLACK, sepia.getRed());
      assertEquals(BLACK, sepia.getGreen());
      assertEquals(BLACK, sepia.getBlue());
    }
  }

  /**
   * Contains all the unit tests for RGB pixel.
   */
  public static class RGBTest {
    private final int RED = 100;
    private final int GREEN = 150;
    private final int BLUE = 200;
    private Pixel pixel;

    @Before
    public void setUp() {
      pixel = new RGB(RED, GREEN, BLUE);
    }

    @Test
    public void testToString() {
      String expectedString = "RGB(" + RED + ", " + GREEN + ", " + BLUE + ")";
      assertEquals(expectedString, pixel.toString());
    }

    @Test
    public void testHashCode() {
      Pixel samePixel = new RGB(RED, GREEN, BLUE);
      assertEquals(pixel.hashCode(), samePixel.hashCode());
    }

    @Test
    public void testConstructor() {
      assertEquals(RED, pixel.getRed());
      assertEquals(GREEN, pixel.getGreen());
      assertEquals(BLUE, pixel.getBlue());
    }

    @Test
    public void testConstructorWithNegativeValues() {
      Pixel pixel = new RGB(-10, -20, -30);
      assertEquals(0, pixel.getRed());
      assertEquals(0, pixel.getGreen());
      assertEquals(0, pixel.getBlue());
    }

    @Test
    public void testConstructorWithOverflowValues() {
      Pixel pixel = new RGB(300, 400, 500);
      assertEquals(255, pixel.getRed());
      assertEquals(255, pixel.getGreen());
      assertEquals(255, pixel.getBlue());
    }

    @Test
    public void testCreatePixel() {
      Pixel newPixel = pixel.createPixel(50, 100, 150);
      assertTrue(newPixel instanceof RGB);
      assertEquals(50, newPixel.getRed());
      assertEquals(100, newPixel.getGreen());
      assertEquals(150, newPixel.getBlue());
    }

    @Test
    public void testCreateRedComponent() {
      Pixel redComponent = pixel.createRedComponent();
      assertEquals(RED, redComponent.getRed());
      assertEquals(RED, redComponent.getGreen());
      assertEquals(RED, redComponent.getBlue());
    }

    @Test
    public void testCreateGreenComponent() {
      Pixel greenComponent = pixel.createGreenComponent();
      assertEquals(GREEN, greenComponent.getRed());
      assertEquals(GREEN, greenComponent.getGreen());
      assertEquals(GREEN, greenComponent.getBlue());
    }

    @Test
    public void testCreateBlueComponent() {
      Pixel blueComponent = pixel.createBlueComponent();
      assertEquals(BLUE, blueComponent.getRed());
      assertEquals(BLUE, blueComponent.getGreen());
      assertEquals(BLUE, blueComponent.getBlue());
    }

    @Test
    public void testEquals() {
      Pixel samePixel = new RGB(RED, GREEN, BLUE);
      Pixel differentPixel = new RGB(RED + 1, GREEN, BLUE);

    }

    @Test
    public void testEqualsReflexive() {
      Pixel x = new RGB(100, 150, 200);
      assertEquals(x, x);
    }

    @Test
    public void testEqualsSymmetric() {
      Pixel x = new RGB(100, 150, 200);
      Pixel y = new RGB(100, 150, 200);

      assertEquals(x, y);
      assertEquals(y, x);
    }

    @Test
    public void testEqualsTransitive() {
      Pixel x = new RGB(100, 150, 200);
      Pixel y = new RGB(100, 150, 200);
      Pixel z = new RGB(100, 150, 200);

      assertEquals(x, y);
      assertEquals(y, z);
      assertEquals(x, z);
    }

    @Test
    public void testAdjustBrightness() {
      Pixel brighterPixel = pixel.adjustBrightness(50);
      assertEquals(RED + 50, brighterPixel.getRed());
      assertEquals(GREEN + 50, brighterPixel.getGreen());
      assertEquals(BLUE + 50, brighterPixel.getBlue());

      Pixel darkerPixel = pixel.adjustBrightness(-50);
      assertEquals(RED - 50, darkerPixel.getRed());
      assertEquals(GREEN - 50, darkerPixel.getGreen());
      assertEquals(BLUE - 50, darkerPixel.getBlue());
    }

    @Test
    public void testGetIntensity() {
      int expectedIntensity = (RED + GREEN + BLUE) / 3;
      Pixel intensity = pixel.getIntensity();
      assertEquals(expectedIntensity, intensity.getRed());
      assertEquals(expectedIntensity, intensity.getGreen());
      assertEquals(expectedIntensity, intensity.getBlue());
    }

    @Test
    public void testGetValue() {
      int expectedValue = BLUE;
      Pixel value = pixel.getValue();
      assertEquals(expectedValue, value.getRed());
      assertEquals(expectedValue, value.getGreen());
      assertEquals(expectedValue, value.getBlue());
    }

    @Test
    public void testGetLuma() {
      Pixel luma = pixel.getLuma();
      double[][] lumaKernel = LinearColorTransformationType.LUMA.getKernel();

      int expectedRed =
              (int) (lumaKernel[0][0] * RED + lumaKernel[0][1] * GREEN + lumaKernel[0][2] * BLUE);
      int expectedGreen =
              (int) (lumaKernel[1][0] * RED + lumaKernel[1][1] * GREEN + lumaKernel[1][2] * BLUE);
      int expectedBlue =
              (int) (lumaKernel[2][0] * RED + lumaKernel[2][1] * GREEN + lumaKernel[2][2] * BLUE);

      assertEquals(expectedRed, luma.getRed());
      assertEquals(expectedGreen, luma.getGreen());
      assertEquals(expectedBlue, luma.getBlue());
    }

    @Test
    public void testGetSepia() {
      Pixel sepia = pixel.getSepia();
      double[][] sepiaKernel = LinearColorTransformationType.SEPIA.getKernel();

      int expectedRed =
              (int) (sepiaKernel[0][0] * RED
                      + sepiaKernel[0][1] * GREEN
                      + sepiaKernel[0][2] * BLUE);
      int expectedGreen =
              (int) (sepiaKernel[1][0] * RED
                      + sepiaKernel[1][1] * GREEN
                      + sepiaKernel[1][2] * BLUE);
      int expectedBlue =
              (int) (sepiaKernel[2][0] * RED
                      + sepiaKernel[2][1] * GREEN
                      + sepiaKernel[2][2] * BLUE);

      assertEquals(expectedRed, sepia.getRed());
      assertEquals(expectedGreen, sepia.getGreen());
      assertEquals(expectedBlue, sepia.getBlue());
    }

    @Test
    public void testBrightnessAdjustmentClamping() {
      Pixel veryBright = pixel.adjustBrightness(1000);
      assertEquals(255, veryBright.getRed());
      assertEquals(255, veryBright.getGreen());
      assertEquals(255, veryBright.getBlue());

      Pixel veryDark = pixel.adjustBrightness(-1000);
      assertEquals(0, veryDark.getRed());
      assertEquals(0, veryDark.getGreen());
      assertEquals(0, veryDark.getBlue());
    }
  }

  /**
   * Contains all the unit tests for white pixel.
   */
  public static class WhitePixelTest {
    private final int WHITE = 255;
    private Pixel whitePixel;

    @Before
    public void setUp() {
      whitePixel = new RGB(WHITE, WHITE, WHITE);
    }

    @Test
    public void testConstructor() {
      assertEquals(WHITE, whitePixel.getRed());
      assertEquals(WHITE, whitePixel.getGreen());
      assertEquals(WHITE, whitePixel.getBlue());
    }

    @Test
    public void testCreatePixel() {
      Pixel newPixel = whitePixel.createPixel(255, 255, 255);
      assertTrue(newPixel instanceof RGB);
      assertEquals(WHITE, newPixel.getRed());
      assertEquals(WHITE, newPixel.getGreen());
      assertEquals(WHITE, newPixel.getBlue());
    }

    @Test
    public void testCreateRedComponent() {
      Pixel redComponent = whitePixel.createRedComponent();
      assertEquals(WHITE, redComponent.getRed());
      assertEquals(WHITE, redComponent.getGreen());
      assertEquals(WHITE, redComponent.getBlue());
    }

    @Test
    public void testCreateGreenComponent() {
      Pixel greenComponent = whitePixel.createGreenComponent();
      assertEquals(WHITE, greenComponent.getRed());
      assertEquals(WHITE, greenComponent.getGreen());
      assertEquals(WHITE, greenComponent.getBlue());
    }

    @Test
    public void testCreateBlueComponent() {
      Pixel blueComponent = whitePixel.createBlueComponent();
      assertEquals(WHITE, blueComponent.getRed());
      assertEquals(WHITE, blueComponent.getGreen());
      assertEquals(WHITE, blueComponent.getBlue());
    }

    @Test
    public void testEqualsReflexive() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      assertEquals("Reflexive property failed", x, x);
    }

    @Test
    public void testEqualsSymmetric() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      Pixel y = new RGB(WHITE, WHITE, WHITE);

      assertEquals("Forward symmetric test failed", x, y);
      assertEquals("Backward symmetric test failed", y, x);
    }

    @Test
    public void testEqualsTransitive() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      Pixel y = new RGB(WHITE, WHITE, WHITE);
      Pixel z = new RGB(WHITE, WHITE, WHITE);

      assertEquals("First transitive condition failed", x, y);
      assertEquals("Second transitive condition failed", y, z);
      assertEquals("Transitive property failed", x, z);
    }

    @Test
    public void testEqualsConsistent() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      Pixel y = new RGB(WHITE, WHITE, WHITE);

      boolean firstResult = x.equals(y);
      for (int i = 0; i < 5; i++) {
        assertEquals("Consistent property failed on iteration " + i,
                firstResult, x.equals(y));
      }
    }

    @Test
    public void testEqualsNullComparison() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      assertNotEquals("Null comparison failed", null, x);
    }

    @Test
    public void testEqualsDifferentTypes() {
      Pixel x = new RGB(WHITE, WHITE, WHITE);
      Object nonPixel = "Not a pixel";
      assertNotEquals("Different type comparison failed", x, nonPixel);
    }

    @Test
    public void testAdjustBrightness() {
      Pixel brighterPixel = whitePixel.adjustBrightness(50);
      assertEquals(WHITE, brighterPixel.getRed());
      assertEquals(WHITE, brighterPixel.getGreen());
      assertEquals(WHITE, brighterPixel.getBlue());

      Pixel darkerPixel = whitePixel.adjustBrightness(-50);
      assertEquals(WHITE - 50, darkerPixel.getRed());
      assertEquals(WHITE - 50, darkerPixel.getGreen());
      assertEquals(WHITE - 50, darkerPixel.getBlue());
    }

    @Test
    public void testGetIntensity() {
      Pixel intensity = whitePixel.getIntensity();
      assertEquals(WHITE, intensity.getRed());
      assertEquals(WHITE, intensity.getGreen());
      assertEquals(WHITE, intensity.getBlue());
    }

    @Test
    public void testGetValue() {
      Pixel value = whitePixel.getValue();
      assertEquals(WHITE, value.getRed());
      assertEquals(WHITE, value.getGreen());
      assertEquals(WHITE, value.getBlue());
    }

    @Test
    public void testGetLuma() {
      Pixel luma = whitePixel.getLuma();
      assertEquals(254, luma.getRed());
      assertEquals(254, luma.getGreen());
      assertEquals(254, luma.getBlue());
    }

    @Test
    public void testGetSepia() {
      Pixel sepia = whitePixel.getSepia();
      double[][] sepiaKernel = LinearColorTransformationType.SEPIA.getKernel();

      int expectedRed =
              (int) ((sepiaKernel[0][0] + sepiaKernel[0][1]
                      + sepiaKernel[0][2]) * WHITE);
      int expectedGreen =
              (int) ((sepiaKernel[1][0]
                      + sepiaKernel[1][1]
                      + sepiaKernel[1][2]) * WHITE);
      int expectedBlue =
              (int) ((sepiaKernel[2][0]
                      + sepiaKernel[2][1]
                      + sepiaKernel[2][2]) * WHITE);

      expectedRed = Math.min(255, expectedRed);
      expectedGreen = Math.min(255, expectedGreen);
      expectedBlue = Math.min(255, expectedBlue);

      assertEquals(expectedRed, sepia.getRed());
      assertEquals(expectedGreen, sepia.getGreen());
      assertEquals(expectedBlue, sepia.getBlue());
    }
  }

  /**
   * Contains all the unit tests for Image Type.
   */
  public static class ImageTypeTest {

    @Test
    public void testGetImageTypeFromPath() throws
            ImageProcessorException {
      assertEquals(ImageType.JPG, ImageType.getImageTypeFromPath("image.jpg"));
      assertEquals(ImageType.PNG, ImageType.getImageTypeFromPath("image.png"));
      assertEquals(ImageType.JPEG, ImageType.getImageTypeFromPath("image"
              + ".jpeg"));
      assertEquals(ImageType.PPM, ImageType.getImageTypeFromPath("image.ppm"));
    }

    @Test
    public void testFromExtension_png() throws
            ImageProcessorException {
      ImageType imageType = ImageType.fromExtension("png");
      assertEquals(ImageType.PNG, imageType);
    }

    @Test
    public void testFromExtension_ppm() throws
            ImageProcessorException {
      ImageType imageType = ImageType.fromExtension("ppm");
      assertEquals(ImageType.PPM, imageType);
    }

    @Test
    public void testFromExtension_jpg() throws
            ImageProcessorException {
      ImageType imageType = ImageType.fromExtension("jpg");
      assertEquals(ImageType.JPG, imageType);
    }

    @Test
    public void testFromExtension_jpeg() throws
            ImageProcessorException {
      ImageType imageType = ImageType.fromExtension("jpeg");
      assertEquals(ImageType.JPEG, imageType);
    }

    @Test(expected =
            ImageProcessorException.NotImplementedException.class)
    public void testFromExtension_unsupported() throws
            ImageProcessorException {
      ImageType.fromExtension("gif");
    }

    @Test
    public void testGetExtension() {
      assertEquals("png", ImageType.PNG.getExtension());
      assertEquals("ppm", ImageType.PPM.getExtension());
      assertEquals("jpg", ImageType.JPG.getExtension());
      assertEquals("jpeg", ImageType.JPEG.getExtension());
    }
  }

  /**
   * Contains all the unit tests for LinerColorTransformation Type.
   */
  public static class LinearColorTransformationTypeTest {
    @Test
    public void testLumaKernel() {
      double[][] expectedLumaKernel = {
              {0.2126, 0.7152, 0.0722},
              {0.2126, 0.7152, 0.0722},
              {0.2126, 0.7152, 0.0722}
      };
      assertArrayEquals(expectedLumaKernel,
              LinearColorTransformationType.LUMA.getKernel());
    }

    @Test
    public void testSepiaKernel() {
      double[][] expectedSepiaKernel = {
              {0.393, 0.769, 0.189},
              {0.349, 0.686, 0.168},
              {0.272, 0.534, 0.131}
      };
      assertArrayEquals(expectedSepiaKernel,
              LinearColorTransformationType.SEPIA.getKernel());
    }
  }

  /**
   * Contains all the unit tests for Pixel Type.
   */
  public static class PixelTypeTest {

    @Test
    public void testFromBufferedImageTypeSupportedTypes() throws
            ImageProcessorException {
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_INT_RGB));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_INT_ARGB));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_INT_ARGB_PRE));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_INT_BGR));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_3BYTE_BGR));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_4BYTE_ABGR));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_4BYTE_ABGR_PRE));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_USHORT_565_RGB));
      assertEquals(PixelType.RGB,
              PixelType.fromBufferedImageType(BufferedImage.TYPE_USHORT_555_RGB));
    }

    @Test(expected =
            ImageProcessorException.NotImplementedException.class)
    public void testFromBufferedImageTypeUnsupportedType() throws
            ImageProcessorException {
      PixelType.fromBufferedImageType(BufferedImage.TYPE_USHORT_GRAY);
    }

    @Test(expected =
            ImageProcessorException.NotImplementedException.class)
    public void testFromBufferedImageTypeInvalidType() throws
            ImageProcessorException {
      PixelType.fromBufferedImageType(-1);
    }
  }

  /**
   * Contains all the unit tests for the User Command.
   */
  public static class UserCommandTest {
    @Test
    public void testGetCommand() {
      assertEquals(Optional.of(UserCommand.LOAD), UserCommand.getCommand(
              "load"));
      assertEquals(Optional.of(UserCommand.SAVE), UserCommand.getCommand(
              "save"));
      assertEquals(Optional.of(UserCommand.RED_COMPONENT),
              UserCommand.getCommand("red-component"));
      assertEquals(Optional.of(UserCommand.GREEN_COMPONENT),
              UserCommand.getCommand("green-component"));
      assertEquals(Optional.of(UserCommand.BLUE_COMPONENT),
              UserCommand.getCommand("blue-component"));
      assertEquals(Optional.of(UserCommand.VALUE_COMPONENT),
              UserCommand.getCommand("value-component"));
      assertEquals(Optional.of(UserCommand.LUMA_COMPONENT),
              UserCommand.getCommand("luma-component"));
      assertEquals(Optional.of(UserCommand.INTENSITY_COMPONENT),
              UserCommand.getCommand("intensity-component"));
      assertEquals(Optional.of(UserCommand.HORIZONTAL_FLIP),
              UserCommand.getCommand("horizontal-flip"));
      assertEquals(Optional.of(UserCommand.VERTICAL_FLIP),
              UserCommand.getCommand("vertical-flip"));
      assertEquals(Optional.of(UserCommand.BRIGHTEN), UserCommand.getCommand(
              "brighten"));
      assertEquals(Optional.of(UserCommand.RGB_SPLIT),
              UserCommand.getCommand("rgb-split"));
      assertEquals(Optional.of(UserCommand.RGB_COMBINE),
              UserCommand.getCommand("rgb-combine"));
      assertEquals(Optional.of(UserCommand.BLUR), UserCommand.getCommand(
              "blur"));
      assertEquals(Optional.of(UserCommand.SHARPEN), UserCommand.getCommand(
              "sharpen"));
      assertEquals(Optional.of(UserCommand.SEPIA), UserCommand.getCommand(
              "sepia"));
      assertEquals(Optional.of(UserCommand.RUN), UserCommand.getCommand("run"));
      assertEquals(Optional.of(UserCommand.QUIT), UserCommand.getCommand(
              "quit"));
      assertEquals(Optional.of(UserCommand.HELP), UserCommand.getCommand(
              "help"));
      assertEquals(Optional.empty(),
              UserCommand.getCommand("non-existent-command"));
    }

    @Test
    public void testGetUserCommands() {
      String expectedCommands = "load image-path image-name: Load an image "
              + "from the specified path and refer it to henceforth in the "
              + "program by the given image name.\n"
              + "save image-path image-name: Save the image with the given name"
              + " to the specified path which should include the name of the "
              + "file.\n"
              + "red-component image-name dest-image-name: Create an "
              + "image "
              + "with the red-component of the image with the given name, and "
              + "refer to it henceforth in the program by the given "
              + "destination name.\n"
              + "green-component image-name dest-image-name: Create an "
              + "image with the green-component of the image with the given "
              + "name, and refer to it henceforth in the program by the given "
              + "destination name.\n"
              + "blue-component image-name dest-image-name: Create an"
              + " image "
              + "with the blue-component of the image with the given name, and"
              + " refer to it henceforth in the program by the given "
              + "destination name.\n"
              + "value-component image-name dest-image-name: Create an image "
              + "with the value-component of the image with the given name, and"
              + " refer to it henceforth in the program by the given "
              + "destination name.\n"
              + "luma-component image-name dest-image-name split P: Create an"
              + " image "
              + "with the luma-component of the image with the given name, and "
              + "refer to it henceforth in the program by the given destination"
              + " name.P is an optional parameter for split view.\n"
              + "intensity-component image-name dest-image-name: Create an "
              + "image with the intensity-component of the image with the given"
              + " name, and refer to it henceforth in the program by the given "
              + "destination name.\n"
              + "horizontal-flip image-name dest-image-name: Flip an image "
              + "horizontally to create a new image, referred to henceforth by "
              + "the given destination name.\n"
              + "vertical-flip image-name dest-image-name: Flip an image "
              + "vertically to create a new image, referred to henceforth by "
              + "the given destination name.\n"
              + "brighten increment image-name dest-image-name: brighten the "
              + "image by the given increment to create a new image, referred "
              + "to henceforth by the given destination name. The increment may"
              + " be positive (brightening) or negative (darkening).\n"
              + "rgb-split image-name dest-image-name-red dest-image-name-green"
              + " dest-image-name-blue: split the given image into three images"
              + " containing its red, green and blue components respectively. "
              + "These would be the same images that would be individually "
              + "produced with the red-component, green-component and "
              + "blue-component commands.\n"
              + "rgb-combine image-name red-image green-image blue-image: "
              + "Combine the three images that are individually red, green and "
              + "blue into a single image that gets its red, green and blue "
              + "components from the three images respectively.\n"
              + "blur image-name dest-image-name split p: blur the given "
              + "image"
              + " and "
              + "store the result in another image with the given name."
              + "P is an optional parameter for split view.\n"
              + "sharpen image-name dest-image-name split p: sharpen the "
              + "given"
              + " image "
              + "and store the result in another image with the given name."
              + "P is an optional parameter for split view.\n"
              + "sepia image-name dest-image-name split p: produce a "
              + "sepia-toned "
              + "version of the given image and store the result in another "
              + "image with the given name."
              + "P is an optional parameter for split view.\n"
              + "compress percentage image-name dest-image-name: "
              + "compress the given image by the given percentage and store "
              + "the result in another image with the given name.\n"
              + "histogram image-name dest-image-name: Create a histogram of "
              + "the given image "
              + "and store the result in another image with the given name.\n"
              + "color-correct image-name dest-image-name split p: Color "
              + "correct the "
              + "given image "
              + "and store the result in another image with the given name."
              + "P is an optional parameter for split view.\n"
              + "levels-adjust b m w image-name dest-image-name split p: "
              + "Adjust the "
              + "levels of the "
              + "given image and store the result in another image with the "
              + "given name."
              + "P is an optional parameter for split view.\n"
              + "run script-file: Load and run the script commands in the "
              + "specified file.\n"
              + "downscale factor image-name dest-image-name: "
              + "Downscale the given image by the given factor and store the "
              + "result in another image with the given name.\n"
              + "reset: Resets the program's memory.\n"
              + "quit: Quit the program.\n"
              + "help: Print this help message.\n";

      assertEquals(expectedCommands, UserCommand.getUserCommands());
    }


  }

  /**
   * Contains all the unit tests for Rendered Black Image.
   */
  public static class RenderedImageBlackTest {
    private Image blackImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] blackPixels = new Pixel[2][2];
      blackPixels[0][0] = new RGB(0, 0, 0);
      blackPixels[0][1] = new RGB(0, 0, 0);
      blackPixels[1][0] = new RGB(0, 0, 0);
      blackPixels[1][1] = new RGB(0, 0, 0);
      blackImage = new RenderedImage(blackPixels);
    }

    @Test
    public void testConstructor() {
      assertNotNull("Black image should be created successfully", blackImage);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() throws
            ImageProcessorException {
      new RenderedImage(null);
    }

    @Test
    public void testGetPixel() {
      assertEquals(new RGB(0, 0, 0), blackImage.getPixel(0, 0));
      assertEquals(new RGB(0, 0, 0), blackImage.getPixel(0, 1));
      assertEquals(new RGB(0, 0, 0), blackImage.getPixel(1, 0));
      assertEquals(new RGB(0, 0, 0), blackImage.getPixel(1, 1));
    }

    @Test
    public void testGetWidth() {
      assertEquals(2, blackImage.getWidth());
    }

    @Test
    public void testGetHeight() {
      assertEquals(2, blackImage.getHeight());
    }

    @Test
    public void testCreateRedComponent() throws
            ImageProcessorException {
      Image redImage = blackImage.createRedComponent();
      assertEquals(new RGB(0, 0, 0), redImage.getPixel(0, 0));
      assertEquals(new RGB(0, 0, 0), redImage.getPixel(0, 1));
    }

    @Test
    public void testCreateGreenComponent() throws
            ImageProcessorException {
      Image greenImage = blackImage.createGreenComponent();
      assertEquals(new RGB(0, 0, 0), greenImage.getPixel(0, 0));
      assertEquals(new RGB(0, 0, 0), greenImage.getPixel(0, 1));
    }

    @Test
    public void testCreateBlueComponent() throws
            ImageProcessorException {
      Image blueImage = blackImage.createBlueComponent();
      assertEquals(new RGB(0, 0, 0), blueImage.getPixel(0, 0));
      assertEquals(new RGB(0, 0, 0), blueImage.getPixel(0, 1));
    }

    @Test
    public void testAdjustImageBrightness() throws
            ImageProcessorException {
      Image brightenedImage = blackImage.adjustImageBrightness(50);
      assertEquals(new RGB(50, 50, 50), brightenedImage.getPixel(0, 0));

      Image darkenedImage = blackImage.adjustImageBrightness(-50);
      assertEquals(new RGB(0, 0, 0), darkenedImage.getPixel(0, 0));
    }

    @Test
    public void testGetLuma() throws
            ImageProcessorException {
      Image lumaImage = blackImage.getLuma();
      for (int x = 0; x < blackImage.getWidth(); x++) {
        for (int y = 0; y < blackImage.getHeight(); y++) {
          assertEquals(new RGB(0, 0, 0), lumaImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetSepia() throws
            ImageProcessorException {
      Image sepiaImage = blackImage.getSepia();
      for (int x = 0; x < blackImage.getWidth(); x++) {
        for (int y = 0; y < blackImage.getHeight(); y++) {
          assertEquals(
                  new RGB(0, 0, 0),
                  sepiaImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetIntensity() throws
            ImageProcessorException {
      Image intensityImage = blackImage.getIntensity();
      for (int x = 0; x < blackImage.getWidth(); x++) {
        for (int y = 0; y < blackImage.getHeight(); y++) {
          assertEquals(
                  new RGB(0, 0, 0),
                  intensityImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetValue() throws
            ImageProcessorException {
      Image valueImage = blackImage.getValue();
      for (int x = 0; x < blackImage.getWidth(); x++) {
        for (int y = 0; y < blackImage.getHeight(); y++) {
          assertEquals(new RGB(0, 0, 0), valueImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testHorizontalFlip() throws
            ImageProcessorException {
      Image flippedImage = blackImage.horizontalFlip();
      assertEquals(blackImage.getPixel(0, 0), flippedImage.getPixel(1, 0));
      assertEquals(blackImage.getPixel(1, 0), flippedImage.getPixel(0, 0));
      assertEquals(blackImage.getPixel(0, 1), flippedImage.getPixel(1, 1));
      assertEquals(blackImage.getPixel(1, 1), flippedImage.getPixel(0, 1));
    }

    @Test
    public void testVerticalFlip() throws
            ImageProcessorException {
      Image flippedImage = blackImage.verticalFlip();
      assertEquals(blackImage.getPixel(0, 0), flippedImage.getPixel(0, 1));
      assertEquals(blackImage.getPixel(0, 1), flippedImage.getPixel(0, 0));
      assertEquals(blackImage.getPixel(1, 0), flippedImage.getPixel(1, 1));
      assertEquals(blackImage.getPixel(1, 1), flippedImage.getPixel(1, 0));
    }

    @Test
    public void testEdgeCases() throws
            ImageProcessorException {
      Image overBrightened = blackImage.adjustImageBrightness(300);
      assertTrue(overBrightened.getPixel(0, 0).getRed() <= 255);

      Image overDarkened = blackImage.adjustImageBrightness(-300);
      assertTrue(overDarkened.getPixel(0, 0).getRed() >= 0);
    }
  }

  /**
   * Contains all the unit tests for Rendered Rectangle Image.
   */
  public static class RenderedImageRectangleTest {
    private Image image;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[3][2];
      pixels[0][0] = new RGB(100, 150, 200);
      pixels[0][1] = new RGB(50, 100, 150);
      pixels[1][0] = new RGB(200, 100, 50);
      pixels[1][1] = new RGB(150, 200, 100);
      pixels[2][0] = new RGB(25, 75, 125);
      pixels[2][1] = new RGB(75, 125, 175);
      image = new RenderedImage(pixels);
    }

    @Test
    public void testConstructor() {
      assertNotNull("Image should be created successfully", image);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() throws
            ImageProcessorException {
      new RenderedImage(null);
    }

    @Test
    public void testGetPixel() {
      assertEquals(new RGB(100, 150, 200), image.getPixel(0, 0));
      assertEquals(new RGB(50, 100, 150), image.getPixel(0, 1));
      assertEquals(new RGB(200, 100, 50), image.getPixel(1, 0));
      assertEquals(new RGB(150, 200, 100), image.getPixel(1, 1));
      assertEquals(new RGB(25, 75, 125), image.getPixel(2, 0));
      assertEquals(new RGB(75, 125, 175), image.getPixel(2, 1));
    }

    @Test
    public void testGetWidth() {
      assertEquals(2, image.getWidth());
    }

    @Test
    public void testGetHeight() {
      assertEquals(3, image.getHeight());
    }

    @Test
    public void testCreateRedComponent() throws
            ImageProcessorException {
      Image redImage = image.createRedComponent();

      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          Pixel original = image.getPixel(row, col);
          Pixel redPixel = redImage.getPixel(row, col);
          assertEquals(redPixel.getRed(), original.getRed());
          assertEquals(redPixel.getGreen(), original.getRed());
          assertEquals(redPixel.getBlue(), original.getRed());
        }
      }
    }

    @Test
    public void testCreateGreenComponent() throws
            ImageProcessorException {
      Image greenImage = image.createGreenComponent();
      assertEquals(new RGB(150, 150, 150), greenImage.getPixel(0, 0));
      assertEquals(new RGB(100, 100, 100), greenImage.getPixel(0, 1));
    }

    @Test
    public void testCreateBlueComponent() throws
            ImageProcessorException {
      Image blueImage = image.createBlueComponent();
      assertEquals(new RGB(200, 200, 200), blueImage.getPixel(0, 0));
      assertEquals(new RGB(150, 150, 150), blueImage.getPixel(0, 1));
    }

    @Test
    public void testAdjustImageBrightness() throws
            ImageProcessorException {
      Image brightenedImage = image.adjustImageBrightness(50);
      assertEquals(new RGB(150, 200, 250), brightenedImage.getPixel(0, 0));

      Image darkenedImage = image.adjustImageBrightness(-50);
      assertEquals(new RGB(50, 100, 150), darkenedImage.getPixel(0, 0));
    }

    @Test
    public void testGetLuma() throws
            ImageProcessorException {
      Image lumaImage = image.getLuma();

      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          Pixel original = image.getPixel(row, col);
          int expectedLuma = (int) (0.2126 * original.getRed()
                  + 0.7152 * original.getGreen()
                  + 0.0722 * original.getBlue());
          Pixel lumaPixel = lumaImage.getPixel(row, col);

          assertEquals(new RGB(expectedLuma, expectedLuma, expectedLuma),
                  lumaPixel);
        }
      }

    }

    @Test
    public void testGetSepia() throws
            ImageProcessorException {
      Image sepiaImage = image.getSepia();

      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          Pixel original = image.getPixel(row, col);
          int originalRed = original.getRed();
          int originalGreen = original.getGreen();
          int originalBlue = original.getBlue();

          int expectedRed = Math.min(255, (int) (0.393 * originalRed
                  + 0.769 * originalGreen
                  + 0.189 * originalBlue));
          int expectedGreen = Math.min(255, (int) (
                  0.349 * originalRed
                          + 0.686 * originalGreen
                          + 0.168 * originalBlue));
          int expectedBlue = Math.min(255, (int) (0.272 * originalRed
                  + 0.534 * originalGreen
                  + 0.131 * originalBlue));

          Pixel sepiaPixel = sepiaImage.getPixel(row, col);

          assertEquals(new RGB(expectedRed, expectedGreen, expectedBlue),
                  sepiaPixel);
        }
      }

    }

    @Test
    public void testGetIntensity() throws
            ImageProcessorException {
      Image intensityImage = image.getIntensity();

      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          Pixel original = image.getPixel(row, col);
          int expectedIntensity = (original.getRed()
                  + original.getGreen()
                  + original.getBlue()) / 3;

          Pixel intensityPixel = intensityImage.getPixel(row, col);

          assertEquals(new RGB(expectedIntensity, expectedIntensity,
                          expectedIntensity),
                  intensityPixel);
        }
      }
    }

    @Test
    public void testGetValue() throws
            ImageProcessorException {
      Image valueImage = image.getValue();
      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          Pixel original = image.getPixel(row, col);
          int expectedValue = Math.max(Math.max(original.getRed(),
                          original.getGreen()),
                  original.getBlue());

          Pixel valuePixel = valueImage.getPixel(row, col);

          assertEquals(new RGB(expectedValue, expectedValue, expectedValue),
                  valuePixel);
        }
      }
    }

    @Test
    public void testHorizontalFlip() throws
            ImageProcessorException {
      Image flippedImage = image.horizontalFlip();
      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          assertEquals(image.getPixel(row, col),
                  flippedImage.getPixel(row, image.getWidth() - 1 - col));
        }
      }

      assertEquals(image.getWidth(), flippedImage.getWidth());
      assertEquals(image.getHeight(), flippedImage.getHeight());
    }

    @Test
    public void testVerticalFlip() throws
            ImageProcessorException {
      Image flippedImage = image.verticalFlip();
      for (int row = 0; row < image.getHeight(); row++) {
        for (int col = 0; col < image.getWidth(); col++) {
          assertEquals(image.getPixel(row, col),
                  flippedImage.getPixel(image.getHeight() - 1 - row, col));
        }
      }

      assertEquals(image.getWidth(), flippedImage.getWidth());
      assertEquals(image.getHeight(), flippedImage.getHeight());
    }

    @Test
    public void testEdgeCases() throws
            ImageProcessorException {
      Pixel[][] singlePixel = new Pixel[][]{{new RGB(100, 100, 100)}};
      RenderedImage smallImage = new RenderedImage(singlePixel);
      assertEquals(1, smallImage.getWidth());
      assertEquals(1, smallImage.getHeight());

      Image overBrightened = image.adjustImageBrightness(300);
      assertTrue(overBrightened.getPixel(0, 0).getRed() <= 255);

      Image overDarkened = image.adjustImageBrightness(-300);
      assertTrue(overDarkened.getPixel(0, 0).getRed() >= 0);
    }

  }

  /**
   * Contains all the unit tests for Rendered  Image.
   */
  public static class RenderedImageTest {
    private Image image;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[2][2];
      pixels[0][0] = new RGB(100, 150, 200);
      pixels[0][1] = new RGB(50, 100, 150);
      pixels[1][0] = new RGB(200, 100, 50);
      pixels[1][1] = new RGB(150, 200, 100);
      image = new RenderedImage(pixels);
    }

    @Test
    public void testConstructor() {
      assertNotNull("Image should be created successfully", image);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() throws
            ImageProcessorException {
      new RenderedImage(null);
    }

    @Test(expected = ImageProcessorException.class)
    public void testConstructorWithEmptyPixel() throws
            ImageProcessorException {
      new RenderedImage(new Pixel[][]{});
    }

    @Test
    public void testGetPixel() {
      assertEquals(new RGB(100, 150, 200), image.getPixel(0, 0));
      assertEquals(new RGB(50, 100, 150), image.getPixel(0, 1));
      assertEquals(new RGB(200, 100, 50), image.getPixel(1, 0));
      assertEquals(new RGB(150, 200, 100), image.getPixel(1, 1));
    }

    @Test
    public void testGetWidth() {
      assertEquals(2, image.getWidth());
    }

    @Test
    public void testGetHeight() {
      assertEquals(2, image.getHeight());
    }

    @Test
    public void testMultiScaling() throws ImageProcessorException {
      Image randomImage = TestUtils.randomRectangleImage();
      Image result = randomImage.downscale(100, 50)
                                .downscale(50, 100);

      assertEquals(Factory.createImage(new Pixel[][]{{new RGB(255, 0, 0)}}), result);
    }

    // 1. Input Validation Tests
    @Test(expected = ImageProcessorException.class)
    public void testNegativeWidthFactor() throws ImageProcessorException {
      image.downscale(-50, 50);
    }

    @Test(expected = ImageProcessorException.class)
    public void testNegativeHeightFactor() throws ImageProcessorException {
      image.downscale(50, -50);
    }

    @Test(expected = ImageProcessorException.class)
    public void testZeroWidthFactor() throws ImageProcessorException {
      image.downscale(0, 50);
    }

    @Test(expected = ImageProcessorException.class)
    public void testZeroHeightFactor() throws ImageProcessorException {
      image.downscale(50, 0);
    }

    @Test(expected = ImageProcessorException.class)
    public void testWidthFactorOver100() throws ImageProcessorException {
      image.downscale(101, 50);
    }

    @Test(expected = ImageProcessorException.class)
    public void testHeightFactorOver100() throws ImageProcessorException {
      image.downscale(50, 101);
    }

    @Test
    public void testNoChange() throws ImageProcessorException {
      Image result = image.downscale(100, 100);

      assertEquals(2, result.getWidth());
      assertEquals(2, result.getHeight());

      // Verify all pixels remain unchanged
      assertEquals(100, result.getPixel(0, 0).getRed());
      assertEquals(150, result.getPixel(0, 0).getGreen());
      assertEquals(200, result.getPixel(0, 0).getBlue());

      assertEquals(50, result.getPixel(0, 1).getRed());
      assertEquals(100, result.getPixel(0, 1).getGreen());
      assertEquals(150, result.getPixel(0, 1).getBlue());

      assertEquals(200, result.getPixel(1, 0).getRed());
      assertEquals(100, result.getPixel(1, 0).getGreen());
      assertEquals(50, result.getPixel(1, 0).getBlue());

      assertEquals(150, result.getPixel(1, 1).getRed());
      assertEquals(200, result.getPixel(1, 1).getGreen());
      assertEquals(100, result.getPixel(1, 1).getBlue());
    }

    @Test
    public void testHorizontalOnlyDownscale() throws ImageProcessorException {
      Image result = image.downscale(50, 100);

      assertEquals(1, result.getWidth());
      assertEquals(2, result.getHeight());

      Pixel topPixel = result.getPixel(0, 0);
      assertEquals(new RGB(100, 150, 200), topPixel);

      Pixel bottomPixel = result.getPixel(1, 0);
      assertEquals(new RGB(200, 100, 50), bottomPixel);
    }

    @Test
    public void testVerticalOnlyDownscale() throws ImageProcessorException {
      Image result = image.downscale(100, 50);

      assertEquals(2, result.getWidth());
      assertEquals(1, result.getHeight());

      Pixel leftPixel = result.getPixel(0, 0);
      assertEquals(new RGB(100, 150, 200), leftPixel);

      Pixel rightPixel = result.getPixel(0, 1);
      assertEquals(new RGB(50, 100, 150), rightPixel);
    }

    // 5. Non-Standard Scale Tests
    @Test
    public void test75PercentScale() throws ImageProcessorException {
      Image result = image.downscale(75, 75);

      // Should round down to 1x1 since 2 * 0.75 = 1.5
      assertEquals(1, result.getWidth());
      assertEquals(1, result.getHeight());

      // Values should be weighted average based on area coverage
      Pixel resultPixel = result.getPixel(0, 0);
      // Verify the interpolation results
      assertTrue(resultPixel.getRed() >= 100 && resultPixel.getRed() <= 200);
      assertTrue(resultPixel.getGreen() >= 100 && resultPixel.getGreen() <= 200);
      assertTrue(resultPixel.getBlue() >= 50 && resultPixel.getBlue() <= 200);
    }

    @Test
    public void testCreateRedComponent() throws
            ImageProcessorException {
      Image redImage = image.createRedComponent();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          Pixel redPixel = redImage.getPixel(x, y);
          assertEquals(redPixel.getRed(), original.getRed());
          assertEquals(redPixel.getGreen(), original.getRed());
          assertEquals(redPixel.getBlue(), original.getRed());
        }
      }
    }

    @Test
    public void testCreateGreenComponent() throws
            ImageProcessorException {
      Image greenImage = image.createGreenComponent();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          Pixel green = greenImage.getPixel(x, y);
          assertEquals(green.getRed(), original.getGreen());
          assertEquals(green.getGreen(), original.getGreen());
          assertEquals(green.getBlue(), original.getGreen());
        }
      }
    }

    @Test
    public void testCreateBlueComponent() throws
            ImageProcessorException {
      Image blueImage = image.createBlueComponent();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          Pixel blue = blueImage.getPixel(x, y);
          assertEquals(blue.getRed(), original.getBlue());
          assertEquals(blue.getGreen(), original.getBlue());
          assertEquals(blue.getBlue(), original.getBlue());
        }
      }
    }

    @Test
    public void testAdjustImageBrightness() throws
            ImageProcessorException {
      Image brightenedImage = image.adjustImageBrightness(50);
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          Pixel brightened = brightenedImage.getPixel(x, y);
          assertEquals(Math.min(255, original.getRed() + 50),
                  brightened.getRed());
          assertEquals(Math.min(255, original.getGreen() + 50),
                  brightened.getGreen());
          assertEquals(Math.min(255, original.getBlue() + 50),
                  brightened.getBlue());
        }
      }

      Image darkenedImage = image.adjustImageBrightness(-50);
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          Pixel darkened = darkenedImage.getPixel(x, y);
          assertEquals(Math.max(0, original.getRed() - 50), darkened.getRed());
          assertEquals(Math.max(0, original.getGreen() - 50),
                  darkened.getGreen());
          assertEquals(Math.max(0, original.getBlue() - 50),
                  darkened.getBlue());
        }
      }
    }

    @Test
    public void testGetLuma() throws
            ImageProcessorException {
      Image lumaImage = image.getLuma();

      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          int expectedLuma = (int) (0.2126 * original.getRed()
                  + 0.7152 * original.getGreen()
                  + 0.0722 * original.getBlue());
          Pixel lumaPixel = lumaImage.getPixel(x, y);

          assertEquals(new RGB(expectedLuma, expectedLuma, expectedLuma),
                  lumaPixel);
        }
      }
    }

    @Test
    public void testGetSepia() throws
            ImageProcessorException {
      Image sepiaImage = image.getSepia();

      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          int originalRed = original.getRed();
          int originalGreen = original.getGreen();
          int originalBlue = original.getBlue();

          int expectedRed = Math.min(255, (int) (0.393 * originalRed
                  + 0.769 * originalGreen
                  + 0.189 * originalBlue));
          int expectedGreen = Math.min(255, (int) (0.349 * originalRed
                  + 0.686 * originalGreen
                  + 0.168 * originalBlue));
          int expectedBlue = Math.min(255, (int) (0.272 * originalRed
                  + 0.534 * originalGreen
                  + 0.131 * originalBlue));

          Pixel sepiaPixel = sepiaImage.getPixel(x, y);

          assertEquals(new RGB(expectedRed, expectedGreen, expectedBlue),
                  sepiaPixel);
        }
      }
    }

    @Test
    public void testGetIntensity() throws
            ImageProcessorException {
      Image intensityImage = image.getIntensity();

      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          int expectedIntensity = (original.getRed()
                  + original.getGreen()
                  + original.getBlue()) / 3;

          Pixel intensityPixel = intensityImage.getPixel(x, y);

          assertEquals(new RGB(expectedIntensity, expectedIntensity,
                          expectedIntensity),
                  intensityPixel);
        }
      }
    }

    @Test
    public void testGetValue() throws
            ImageProcessorException {
      Image valueImage = image.getValue();

      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          Pixel original = image.getPixel(x, y);
          int expectedValue = Math.max(Math.max(original.getRed(),
                          original.getGreen()),
                  original.getBlue());

          Pixel valuePixel = valueImage.getPixel(x, y);

          assertEquals(new RGB(expectedValue, expectedValue, expectedValue),
                  valuePixel);
        }
      }
    }

    @Test
    public void testHorizontalFlip() throws
            ImageProcessorException {
      Image flippedImage = image.horizontalFlip();
      assertEquals(image.getPixel(0, 0), flippedImage.getPixel(0, 1));
      assertEquals(image.getPixel(0, 1), flippedImage.getPixel(0, 0));
      assertEquals(image.getPixel(1, 0), flippedImage.getPixel(1, 1));
      assertEquals(image.getPixel(1, 1), flippedImage.getPixel(1, 0));
    }

    @Test
    public void testVerticalFlip() throws
            ImageProcessorException {
      Image flippedImage = image.verticalFlip();
      assertEquals(image.getPixel(0, 0), flippedImage.getPixel(1, 0));
      assertEquals(image.getPixel(1, 0), flippedImage.getPixel(0, 0));
      assertEquals(image.getPixel(0, 1), flippedImage.getPixel(1, 1));
      assertEquals(image.getPixel(1, 1), flippedImage.getPixel(0, 1));
    }

    @Test
    public void testEdgeCases() throws
            ImageProcessorException {
      Pixel[][] singlePixel = new Pixel[][]{{new RGB(100, 100, 100)}};
      RenderedImage smallImage = new RenderedImage(singlePixel);
      assertEquals(1, smallImage.getWidth());
      assertEquals(1, smallImage.getHeight());

      Image overBrightened = image.adjustImageBrightness(300);
      assertTrue(overBrightened.getPixel(0, 0).getRed() <= 255);

      Image overDarkened = image.adjustImageBrightness(-300);
      assertTrue(overDarkened.getPixel(0, 0).getRed() >= 0);
    }

  }

  /**
   * Contains all the unit tests for Rendered White Image.
   */
  public static class RenderedImageWhiteTest {
    private Image whiteImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] whitePixels = new Pixel[2][2];
      whitePixels[0][0] = new RGB(255, 255, 255);
      whitePixels[0][1] = new RGB(255, 255, 255);
      whitePixels[1][0] = new RGB(255, 255, 255);
      whitePixels[1][1] = new RGB(255, 255, 255);
      whiteImage = new RenderedImage(whitePixels);
    }

    @Test
    public void testConstructor() {
      assertNotNull("White image should be created successfully", whiteImage);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() throws
            ImageProcessorException {
      new RenderedImage(null);
    }

    @Test
    public void testMultiScaling() throws ImageProcessorException {
      Image result = whiteImage.downscale(100, 50)
              .downscale(50, 100);

      assertEquals(Factory.createImage(new Pixel[][]{{new RGB(255, 255, 255)}}), result);
    }

    @Test
    public void testGetPixel() {
      assertEquals(new RGB(255, 255, 255), whiteImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), whiteImage.getPixel(0, 1));
      assertEquals(new RGB(255, 255, 255), whiteImage.getPixel(1, 0));
      assertEquals(new RGB(255, 255, 255), whiteImage.getPixel(1, 1));
    }

    @Test
    public void testGetWidth() {
      assertEquals(2, whiteImage.getWidth());
    }

    @Test
    public void testGetHeight() {
      assertEquals(2, whiteImage.getHeight());
    }

    @Test
    public void testCreateRedComponent() throws
            ImageProcessorException {
      Image redImage = whiteImage.createRedComponent();
      assertEquals(new RGB(255, 255, 255), redImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), redImage.getPixel(0, 1));
    }

    @Test
    public void testCreateGreenComponent() throws
            ImageProcessorException {
      Image greenImage = whiteImage.createGreenComponent();
      assertEquals(new RGB(255, 255, 255), greenImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), greenImage.getPixel(0, 1));
    }

    @Test
    public void testCreateBlueComponent() throws
            ImageProcessorException {
      Image blueImage = whiteImage.createBlueComponent();
      assertEquals(new RGB(255, 255, 255), blueImage.getPixel(0, 0));
      assertEquals(new RGB(255, 255, 255), blueImage.getPixel(0, 1));
    }

    @Test
    public void testAdjustImageBrightness() throws
            ImageProcessorException {
      Image brightenedImage = whiteImage.adjustImageBrightness(50);
      assertEquals(new RGB(255, 255, 255), brightenedImage.getPixel(0, 0));

      Image darkenedImage = whiteImage.adjustImageBrightness(-50);
      assertEquals(new RGB(205, 205, 205), darkenedImage.getPixel(0, 0));
    }

    @Test
    public void testGetLuma() throws
            ImageProcessorException {
      Image lumaImage = whiteImage.getLuma();
      for (int x = 0; x < whiteImage.getWidth(); x++) {
        for (int y = 0; y < whiteImage.getHeight(); y++) {
          assertEquals(new RGB(254, 254, 254), lumaImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetSepia() throws
            ImageProcessorException {
      Image sepiaImage = whiteImage.getSepia();
      for (int x = 0; x < whiteImage.getWidth(); x++) {
        for (int y = 0; y < whiteImage.getHeight(); y++) {
          assertEquals(new RGB(255, 255, 238), sepiaImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetIntensity() throws
            ImageProcessorException {
      Image intensityImage = whiteImage.getIntensity();
      for (int x = 0; x < whiteImage.getWidth(); x++) {
        for (int y = 0; y < whiteImage.getHeight(); y++) {
          assertEquals(new RGB(255, 255, 255), intensityImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testGetValue() throws
            ImageProcessorException {
      Image valueImage = whiteImage.getValue();
      for (int x = 0; x < whiteImage.getWidth(); x++) {
        for (int y = 0; y < whiteImage.getHeight(); y++) {
          assertEquals(new RGB(255, 255, 255), valueImage.getPixel(x, y));
        }
      }
    }

    @Test
    public void testHorizontalFlip() throws
            ImageProcessorException {
      Image flippedImage = whiteImage.horizontalFlip();
      assertEquals(whiteImage.getPixel(0, 0), flippedImage.getPixel(1, 0));
      assertEquals(whiteImage.getPixel(1, 0), flippedImage.getPixel(0, 0));
      assertEquals(whiteImage.getPixel(0, 1), flippedImage.getPixel(1, 1));
      assertEquals(whiteImage.getPixel(1, 1), flippedImage.getPixel(0, 1));
    }

    @Test
    public void testVerticalFlip() throws
            ImageProcessorException {
      Image flippedImage = whiteImage.verticalFlip();
      assertEquals(whiteImage.getPixel(0, 0), flippedImage.getPixel(0, 1));
      assertEquals(whiteImage.getPixel(0, 1), flippedImage.getPixel(0, 0));
      assertEquals(whiteImage.getPixel(1, 0), flippedImage.getPixel(1, 1));
      assertEquals(whiteImage.getPixel(1, 1), flippedImage.getPixel(1, 0));
    }

    @Test
    public void testEdgeCases() throws
            ImageProcessorException {
      Image overBrightened = whiteImage.adjustImageBrightness(300);
      assertEquals(255, overBrightened.getPixel(0, 0).getRed());

      Image overDarkened = whiteImage.adjustImageBrightness(-300);
      assertEquals(0, overDarkened.getPixel(0, 0).getRed());
    }
  }

  /**
   * Contains all the unit tests for Single pixel image.
   */
  public static class SinglePixelImageTest {
    private Image singlePixelImage;
    private Pixel originalPixel;

    @Before
    public void setUp() throws
            ImageProcessorException {
      originalPixel = new RGB(120, 180, 240);
      Pixel[][] pixels = new Pixel[][]{{originalPixel}};
      singlePixelImage = new RenderedImage(pixels);
    }

    @Test
    public void testBasicProperties() {
      assertEquals("Width should be 1", 1, singlePixelImage.getWidth());
      assertEquals("Height should be 1", 1, singlePixelImage.getHeight());

      Pixel retrievedPixel = singlePixelImage.getPixel(0, 0);
      assertEquals("Original pixel should match retrieved pixel",
              originalPixel, retrievedPixel);
    }

    @Test
    public void testDownscale() throws ImageProcessorException {
      Image result = singlePixelImage.downscale(100, 100);

      assertEquals(Factory.createImage(new Pixel[][]{{new RGB(120, 180, 240)}}), result);
    }

    @Test
    public void testDownscaleMulti() throws ImageProcessorException {
      Image result = singlePixelImage.downscale(100, 100)
              .downscale(100, 100);

      assertEquals(Factory.createImage(new Pixel[][]{{new RGB(120, 180, 240)}}), result);
    }

    @Test(expected = exception.ImageProcessorException.class)
    public void testDownscale_50() throws ImageProcessorException {
      Image result = singlePixelImage.downscale(50, 50);

      assertEquals(Factory.createImage(new Pixel[][]{{new RGB(120, 180, 240)}}), result);
    }

    @Test
    public void testLuma() throws
            ImageProcessorException {
      Image lumaImage = singlePixelImage.getLuma();

      int expectedLuma = (int) (0.2126 * originalPixel.getRed()
              + 0.7152 * originalPixel.getGreen()
              + 0.0722 * originalPixel.getBlue());

      Pixel lumaPixel = lumaImage.getPixel(0, 0);

      assertEquals("Luma transformation should match expected value",
              new RGB(expectedLuma, expectedLuma, expectedLuma), lumaPixel);
    }

    @Test
    public void testSepia() throws
            ImageProcessorException {
      Image sepiaImage = singlePixelImage.getSepia();

      int originalRed = originalPixel.getRed();
      int originalGreen = originalPixel.getGreen();
      int originalBlue = originalPixel.getBlue();

      int expectedRed = Math.min(255, (int) (0.393 * originalRed
              + 0.769 * originalGreen
              + 0.189 * originalBlue));
      int expectedGreen = Math.min(255, (int) (0.349 * originalRed
              + 0.686 * originalGreen
              + 0.168 * originalBlue));
      int expectedBlue = Math.min(255, (int) (0.272 * originalRed
              + 0.534 * originalGreen
              + 0.131 * originalBlue));

      Pixel sepiaPixel = sepiaImage.getPixel(0, 0);

      assertEquals("Sepia transformation should match expected values",
              new RGB(expectedRed, expectedGreen, expectedBlue), sepiaPixel);
    }

    @Test
    public void testIntensity() throws
            ImageProcessorException {
      Image intensityImage = singlePixelImage.getIntensity();

      int expectedIntensity = (originalPixel.getRed()
              + originalPixel.getGreen()
              + originalPixel.getBlue()) / 3;

      Pixel intensityPixel = intensityImage.getPixel(0, 0);

      assertEquals("Intensity transformation should match expected value",
              new RGB(expectedIntensity, expectedIntensity,
                      expectedIntensity), intensityPixel);
    }

    @Test
    public void testValue() throws
            ImageProcessorException {
      Image valueImage = singlePixelImage.getValue();

      int expectedValue = Math.max(Math.max(originalPixel.getRed(),
                      originalPixel.getGreen()),
              originalPixel.getBlue());

      Pixel valuePixel = valueImage.getPixel(0, 0);

      assertEquals("Value transformation should match expected value",
              new RGB(expectedValue, expectedValue, expectedValue), valuePixel);
    }

    @Test
    public void testColorComponents() throws
            ImageProcessorException {
      Image redImage = singlePixelImage.createRedComponent();
      Pixel redPixel = redImage.getPixel(0, 0);
      assertEquals("Red component should preserve red value only",
              new RGB(originalPixel.getRed(), originalPixel.getRed(),
                      originalPixel.getRed()), redPixel);

      Image greenImage = singlePixelImage.createGreenComponent();
      Pixel greenPixel = greenImage.getPixel(0, 0);
      assertEquals("Green component should preserve green value only",
              new RGB(originalPixel.getGreen(), originalPixel.getGreen(),
                      originalPixel.getGreen()), greenPixel);

      Image blueImage = singlePixelImage.createBlueComponent();
      Pixel bluePixel = blueImage.getPixel(0, 0);
      assertEquals("Blue component should preserve blue value only",
              new RGB(originalPixel.getBlue(), originalPixel.getBlue(),
                      originalPixel.getBlue()), bluePixel);
    }

    @Test
    public void testBrightnessAdjustment() throws
            ImageProcessorException {
      int brightenFactor = 50;
      Image brightenedImage =
              singlePixelImage.adjustImageBrightness(brightenFactor);
      Pixel brightenedPixel = brightenedImage.getPixel(0, 0);

      assertEquals("Brightened pixel should have increased RGB values",
              new RGB(originalPixel.getRed() + brightenFactor,
                      originalPixel.getGreen() + brightenFactor,
                      originalPixel.getBlue() + brightenFactor),
              brightenedPixel);

      int darkenFactor = -30;
      Image darkenedImage =
              singlePixelImage.adjustImageBrightness(darkenFactor);
      Pixel darkenedPixel = darkenedImage.getPixel(0, 0);

      assertEquals("Darkened pixel should have decreased RGB values",
              new RGB(originalPixel.getRed() + darkenFactor,
                      originalPixel.getGreen() + darkenFactor,
                      originalPixel.getBlue() + darkenFactor), darkenedPixel);

    }
  }

  /**
   * Test class for applying Blur filter to black images.
   */
  public static class BlurBlackImageTest extends BlurTestBase {
    private Image blackImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          pixels[i][j] = Factory.createRGBPixel(0, 0, 0);
        }
      }
      blackImage = Factory.createImage(pixels);
    }

    @Test
    public void testBlurBlackImage() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(blackImage,
              FilterOption.GAUSSIAN_BLUR);

      Pixel[][] expectedPixels = new Pixel[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          expectedPixels[i][j] = Factory.createRGBPixel(0, 0, 0);
        }
      }

      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          Pixel resultPixel = blurredImage.getPixel(i, j);
          Pixel expectedPixel = expectedPixels[i][j];
          assertEquals("Blurred image red value should be as expected",
                  expectedPixel.getRed(), resultPixel.getRed());
          assertEquals("Blurred image green value should be as expected",
                  expectedPixel.getGreen(), resultPixel.getGreen());
          assertEquals("Blurred image blue value should be as expected",
                  expectedPixel.getBlue(), resultPixel.getBlue());
        }
      }
    }
  }

  /**
   * Test class for applying Blur filter to normal RGB images.
   */
  public static class BlurNormalImageTest extends BlurTestBase {
    private Image testImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          pixels[i][j] = Factory.createRGBPixel(100, 150, 200);
        }
      }
      testImage = Factory.createImage(pixels);
    }

    @Test
    public void testBlurUniformImage() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(testImage,
              FilterOption.GAUSSIAN_BLUR);
      Pixel[][] expectedPixels = {
              {Factory.createRGBPixel(56, 84, 112),
                      Factory.createRGBPixel(75, 112, 150),
                      Factory.createRGBPixel(56, 84, 112)},
              {Factory.createRGBPixel(75, 112, 150),
                      Factory.createRGBPixel(100, 150, 200),
                      Factory.createRGBPixel(75, 112, 150)},
              {Factory.createRGBPixel(56, 84, 112),
                      Factory.createRGBPixel(75, 112, 150),
                      Factory.createRGBPixel(56, 84, 112)}
      };

      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          Pixel resultPixel = blurredImage.getPixel(i, j);
          Pixel expectedPixel = expectedPixels[i][j];
          assertEquals("Pixel red value should be as expected",
                  expectedPixel.getRed(), resultPixel.getRed());
          assertEquals("Pixel green value should be as expected",
                  expectedPixel.getGreen(), resultPixel.getGreen());
          assertEquals("Pixel blue value should be as expected",
                  expectedPixel.getBlue(), resultPixel.getBlue());
        }
      }
    }

    @Test
    public void testImageDimensionsPreserved() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(testImage,
              FilterOption.GAUSSIAN_BLUR);
      assertEquals("Image width should be preserved",
              testImage.getWidth(), blurredImage.getWidth());
      assertEquals("Image height should be preserved",
              testImage.getHeight(), blurredImage.getHeight());
    }
  }

  /**
   * Test class for applying Blur filter to single pixel images.
   */
  public static class BlurSinglePixelTest extends BlurTestBase {
    private Image singlePixelImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixel = new Pixel[1][1];
      pixel[0][0] = Factory.createRGBPixel(100, 100, 100);
      singlePixelImage = Factory.createImage(pixel);
    }

    @Test
    public void testBlurSinglePixel() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(singlePixelImage,
              FilterOption.GAUSSIAN_BLUR);
      Pixel resultPixel = blurredImage.getPixel(0, 0);

      assertEquals("Single pixel red value should be affected by kernel",
              25, resultPixel.getRed());
      assertEquals("Single pixel green value should be affected by kernel",
              25, resultPixel.getGreen());
      assertEquals("Single pixel blue value should be affected by kernel",
              25, resultPixel.getBlue());
    }

    @Test
    public void testSinglePixelDimensionsPreserved() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(singlePixelImage,
              FilterOption.GAUSSIAN_BLUR);
      assertEquals("Image width should remain 1",
              1, blurredImage.getWidth());
      assertEquals("Image height should remain 1",
              1, blurredImage.getHeight());
    }
  }

  /**
   * Base test class for Blur filter tests containing common setup and
   * utilities.
   */
  public abstract static class BlurTestBase {
    private static final double DELTA = 0.0001;

    @Test
    public void testGaussianKernelValues() {
      double[][] kernel = FilterOption.GAUSSIAN_BLUR.getKernel();
      assertEquals("Kernel should be 3x3", 3, kernel.length);
      assertEquals("Kernel should be 3x3", 3, kernel[0].length);

      assertEquals("Center value should be 1/4", 0.25, kernel[1][1], DELTA);
      assertEquals("Corner value should be 1/16", 0.0625, kernel[0][0], DELTA);
      assertEquals("Edge value should be 1/8", 0.125, kernel[0][1], DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyFilterWithNullImage() throws
            ImageProcessorException {
      FilterUtils.applyFilter(null, FilterOption.GAUSSIAN_BLUR);
    }
  }

  /**
   * Test class for applying Blur filter to white images.
   */
  public static class BlurWhiteImageTest extends BlurTestBase {
    private Image whiteImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          pixels[i][j] = Factory.createRGBPixel(255, 255, 255);
        }
      }
      whiteImage = Factory.createImage(pixels);
    }

    @Test
    public void testBlurWhiteImage() throws
            ImageProcessorException {
      Image blurredImage = FilterUtils.applyFilter(whiteImage,
              FilterOption.GAUSSIAN_BLUR);

      Pixel[][] expectedPixels = {
              {Factory.createRGBPixel(143, 143, 143),
                      Factory.createRGBPixel(191, 191, 191),
                      Factory.createRGBPixel(143, 143, 143)},
              {Factory.createRGBPixel(191, 191, 191),
                      Factory.createRGBPixel(255, 255, 255),
                      Factory.createRGBPixel(191, 191, 191)},
              {Factory.createRGBPixel(143, 143, 143),
                      Factory.createRGBPixel(191, 191, 191),
                      Factory.createRGBPixel(143, 143, 143)}
      };

      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          assertEquals(expectedPixels[i][j], blurredImage.getPixel(i, j));
        }
      }
    }
  }

  /**
   * Test class for testing the Sharpen filter functionality on white images.
   */
  public static class SharpenWhiteImageTest {
    protected static final double DELTA = 0.0001;
    private Image whiteImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          pixels[i][j] = Factory.createRGBPixel(255, 255, 255);
        }
      }
      whiteImage = Factory.createImage(pixels);
    }

    @Test
    public void testSharpenKernelValues() {
      double[][] kernel = FilterOption.SHARPEN.getKernel();
      assertEquals("Kernel should be 5x5", 5, kernel.length);
      assertEquals("Kernel should be 5x5", 5, kernel[0].length);

      assertEquals("Center value should be 1", 1.0, kernel[2][2], DELTA);
      assertEquals("Edge value should be -1/8", -0.125, kernel[0][0], DELTA);
      assertEquals("Near center value should be 1/4", 0.25, kernel[1][2],
              DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyFilterWithNullImage() throws
            ImageProcessorException {
      FilterUtils.applyFilter(null, FilterOption.SHARPEN);
    }

    @Test
    public void testSharpenWhiteImage() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(whiteImage,
              FilterOption.SHARPEN);

      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          assertEquals("Sharpened image should remain white (red)",
                  255, sharpenedImage.getPixel(i, j).getRed());
          assertEquals("Sharpened image should remain white (green)",
                  255, sharpenedImage.getPixel(i, j).getGreen());
          assertEquals("Sharpened image should remain white (blue)",
                  255, sharpenedImage.getPixel(i, j).getBlue());
        }
      }
    }
  }

  /**
   * Test class for testing the Sharpen filter functionality on black images.
   */
  public static class SharpenBlackImageTest {
    private Image blackImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          pixels[i][j] = Factory.createRGBPixel(0, 0, 0);
        }
      }
      blackImage = Factory.createImage(pixels);
    }

    @Test
    public void testSharpenBlackImage() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(blackImage,
              FilterOption.SHARPEN);
      Pixel centerPixel = sharpenedImage.getPixel(2, 2);

      assertEquals("Black image should remain black (red)",
              0, centerPixel.getRed());
      assertEquals("Black image should remain black (green)",
              0, centerPixel.getGreen());
      assertEquals("Black image should remain black (blue)",
              0, centerPixel.getBlue());
    }
  }

  /**
   * Test class for testing the Sharpen filter functionality on normal RGB
   * images.
   */
  public static class SharpenNormalImageTest {
    private Image testImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixels = new Pixel[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          pixels[i][j] = Factory.createRGBPixel(100, 150, 200);
        }
      }
      testImage = Factory.createImage(pixels);
    }

    @Test
    public void testSharpenUniformImage() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(testImage,
              FilterOption.SHARPEN);
      Pixel[][] expectedPixels = {
              {Factory.createRGBPixel(112, 168, 225),
                      Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(112, 168, 225),
                      Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(112, 168, 225)},
              {Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(212, 255, 255),
                      Factory.createRGBPixel(162, 243, 255),
                      Factory.createRGBPixel(212, 255, 255),
                      Factory.createRGBPixel(150, 225, 255)},
              {Factory.createRGBPixel(112, 168, 225),
                      Factory.createRGBPixel(162, 243, 255),
                      Factory.createRGBPixel(100, 150, 200),
                      Factory.createRGBPixel(162, 243, 255),
                      Factory.createRGBPixel(112, 168, 225)},
              {Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(212, 255, 255),
                      Factory.createRGBPixel(162, 243, 255),
                      Factory.createRGBPixel(212, 255, 255),
                      Factory.createRGBPixel(150, 225, 255)},
              {Factory.createRGBPixel(112, 168, 225),
                      Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(112, 168, 225),
                      Factory.createRGBPixel(150, 225, 255),
                      Factory.createRGBPixel(112, 168, 225)}
      };

      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          assertEquals(expectedPixels[i][j], sharpenedImage.getPixel(i, j));
        }
      }
    }

    @Test
    public void testImageDimensionsPreserved() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(testImage,
              FilterOption.SHARPEN);
      assertEquals("Image width should be preserved",
              testImage.getWidth(), sharpenedImage.getWidth());
      assertEquals("Image height should be preserved",
              testImage.getHeight(), sharpenedImage.getHeight());
    }
  }

  /**
   * Test class for testing the Sharpen filter functionality on single pixel
   * images.
   */
  public static class SharpenSinglePixelTest {
    private Image singlePixelImage;

    @Before
    public void setUp() throws
            ImageProcessorException {
      Pixel[][] pixel = new Pixel[1][1];
      pixel[0][0] = Factory.createRGBPixel(100, 100, 100);
      singlePixelImage = Factory.createImage(pixel);
    }

    @Test
    public void testSharpenSinglePixel() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(singlePixelImage,
              FilterOption.SHARPEN);
      Pixel resultPixel = sharpenedImage.getPixel(0, 0);

      assertEquals("Single pixel red value should be affected by kernel",
              100, resultPixel.getRed());
      assertEquals("Single pixel green value should be affected by kernel",
              100, resultPixel.getGreen());
      assertEquals("Single pixel blue value should be affected by kernel",
              100, resultPixel.getBlue());
    }

    @Test
    public void testSinglePixelDimensionsPreserved() throws
            ImageProcessorException {
      Image sharpenedImage = FilterUtils.applyFilter(singlePixelImage,
              FilterOption.SHARPEN);
      assertEquals("Image width should remain 1",
              1, sharpenedImage.getWidth());
      assertEquals("Image height should remain 1",
              1, sharpenedImage.getHeight());
    }
  }

  /**
   * Test class for testing instantiation of various classes
   * by factory.
   */
  public static class FactoryTest {
    private Pixel[][] testPixels;
    private Image redComponent;
    private Image greenComponent;
    private Image blueComponent;
    private StringReader input;
    private StringWriter output;
    private ImageMemory memory;

    @Before
    public void setUp() throws
            ImageProcessorException {
      testPixels = new Pixel[2][2];
      testPixels[0][0] = new RGB(100, 150, 200);
      testPixels[0][1] = new RGB(50, 100, 150);
      testPixels[1][0] = new RGB(200, 250, 300);
      testPixels[1][1] = new RGB(150, 200, 250);

      // Create RGB component images
      Pixel[][] redPixels = new Pixel[2][2];
      Pixel[][] greenPixels = new Pixel[2][2];
      Pixel[][] bluePixels = new Pixel[2][2];

      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          redPixels[i][j] = new RGB(testPixels[i][j].getRed(), 0, 0);
          greenPixels[i][j] = new RGB(0, testPixels[i][j].getGreen(), 0);
          bluePixels[i][j] = new RGB(0, 0, testPixels[i][j].getBlue());
        }
      }

      redComponent = Factory.createImage(redPixels);
      greenComponent = Factory.createImage(greenPixels);
      blueComponent = Factory.createImage(bluePixels);

      input = new StringReader("");
      output = new StringWriter();

      memory = new HashMapMemory();
    }

    @Test
    public void testCreateImage() throws
            ImageProcessorException {
      Image image = Factory.createImage(testPixels);

      assertNotNull("Image should not be null", image);
      assertEquals("Image width should match pixel array width", 2,
              image.getWidth());
      assertEquals("Image height should match pixel array height", 2,
              image.getHeight());

      assertEquals("Pixel values should match", testPixels[0][0],
              image.getPixel(0, 0));
      assertEquals("Pixel values should match", testPixels[1][1],
              image.getPixel(1, 1));
    }

    @Test
    public void testCombineRGBComponents() throws
            ImageProcessorException {
      Image combinedImage = Factory.combineRGBComponents(redComponent,
              greenComponent, blueComponent);

      assertNotNull("Combined image should not be null", combinedImage);
      assertEquals("Combined image width should match component width",
              redComponent.getWidth(), combinedImage.getWidth());
      assertEquals("Combined image height should match component height",
              redComponent.getHeight(), combinedImage.getHeight());

      // Test if pixels are correctly combined
      for (int x = 0; x < combinedImage.getWidth(); x++) {
        for (int y = 0; y < combinedImage.getHeight(); y++) {
          Pixel pixel = combinedImage.getPixel(x, y);
          assertEquals("Red component should match",
                  redComponent.getPixel(x, y).getRed(), pixel.getRed());
          assertEquals("Green component should match",
                  greenComponent.getPixel(x, y).getGreen(), pixel.getGreen());
          assertEquals("Blue component should match",
                  blueComponent.getPixel(x, y).getBlue(), pixel.getBlue());
        }
      }
    }

    @Test(expected = ImageProcessorException.class)
    public void testCombineRGBComponentsDifferentDimensions() throws
            ImageProcessorException {
      Pixel[][] differentSizePixels = new Pixel[3][3];
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          differentSizePixels[i][j] = new RGB(100, 100, 100);
        }
      }
      Image differentSizeImage = Factory.createImage(differentSizePixels);

      Factory.combineRGBComponents(redComponent, differentSizeImage,
              blueComponent);
    }

    @Test(expected = NullPointerException.class)
    public void testCombineRGBComponentsNullComponent() throws
            ImageProcessorException {
      Factory.combineRGBComponents(null, greenComponent, blueComponent);
    }

    @Test
    public void testGetImageMemory() {
      ImageMemory<Image> imageMemory = new HashMapMemory();

      assertNotNull("Image memory should not be null", imageMemory);
      assertTrue("Image memory should be instance of HashMapMemory",
              imageMemory instanceof HashMapMemory);
    }

    @Test
    public void testCreateImageProcessor() {
      ImageProcessingService processor = Factory.createImageProcessor(memory);

      assertNotNull("Image processor should not be null", processor);
      assertTrue("Image processor should be instance of "
                      + "FileImageProcessingService",
              processor instanceof FileImageProcessingService);
    }

    @Test
    public void testCreateRGBPixel() {
      int red = 100;
      int green = 150;
      int blue = 200;

      Pixel pixel = Factory.createRGBPixel(red, green, blue);

      assertNotNull("Pixel should not be null", pixel);
      assertEquals("Red component should match", red, pixel.getRed());
      assertEquals("Green component should match", green, pixel.getGreen());
      assertEquals("Blue component should match", blue, pixel.getBlue());
    }

    @Test(expected = ImageProcessorException.NotImplementedException.class)
    public void testCreateInvalidPixel() throws
            ImageProcessorException {
      int pixelValue = 0xFF9966; // RGB(255, 153, 102)
      Pixel pixel = Factory.createPixel(pixelValue, null);
    }
  }

  /**
   * Test class for testing the FileImageProcessingService class.
   */
  public static class FileImageProcessingServiceTest {
    private FileImageProcessingService service;
    private ImageMemory<Image> memory;

    // Create a simple 2x2 test image
    private Image createTestImage() throws
            ImageProcessorException {
      Pixel[][] pixels = {
              {new RGB(255, 0, 0), new RGB(0, 255, 0)},    // First row: red
              // and green pixels
              {new RGB(0, 0, 255), new RGB(255, 255, 255)}  // Second row:
              // blue and white pixels
      };
      return new RenderedImage(pixels);
    }

    @Before
    public void setUp() {
      memory = new HashMapMemory();
      service = new FileImageProcessingService(memory);
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNullMemory() {
      new FileImageProcessingService(null);
    }

    @Test(expected = ImageProcessorException.class)
    public void loadImageShouldThrowOnNullPath() throws
            ImageProcessorException {
      service.loadImage(ImageProcessingRequest.builder().imagePath(null).imageName(
              "test").build());
    }

    @Test(expected = ImageProcessorException.class)
    public void loadImageShouldThrowOnEmptyName() throws
            ImageProcessorException {
      service.loadImage(ImageProcessingRequest.builder()
              .imagePath("test" + ".jpeg")
              .imageName("")
              .build());
    }

    @Test
    public void testRedComponent() throws
            ImageProcessorException {
      // Setup
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      // Execute
      service.createRedComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("red").build());

      // Verify
      Image redImage = memory.getImage("red");
      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel pixel = redImage.getPixel(row, col);
          Pixel originaPixel = testImage.getPixel(row, col);

          assertEquals(originaPixel.getRed(), pixel.getRed());
          assertEquals(originaPixel.getRed(), pixel.getGreen());
          assertEquals(originaPixel.getRed(), pixel.getBlue());
        }
      }
    }

    @Test
    public void testGreenComponent() throws
            ImageProcessorException {
      // Setup
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      // Execute
      service.createGreenComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("green").build());

      // Verify
      Image greenImage = memory.getImage("green");
      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel pixel = greenImage.getPixel(row, col);
          Pixel originaPixel = testImage.getPixel(row, col);

          assertEquals(originaPixel.getGreen(), pixel.getGreen());
          assertEquals(originaPixel.getGreen(), pixel.getRed());
          assertEquals(originaPixel.getGreen(), pixel.getBlue());
        }
      }
    }

    @Test
    public void testBlueComponent() throws
            ImageProcessorException {
      // Setup
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      // Execute
      service.createBlueComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("blue").build());

      // Verify
      Image blueImage = memory.getImage("blue");
      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel pixel = blueImage.getPixel(row, col);
          Pixel originaPixel = testImage.getPixel(row, col);

          assertEquals(originaPixel.getBlue(), pixel.getBlue());
          assertEquals(originaPixel.getBlue(), pixel.getRed());
          assertEquals(originaPixel.getBlue(), pixel.getGreen());
        }
      }
    }

    @Test
    public void testBrighten() throws
            ImageProcessorException {
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      service.brighten(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("brightened").factor(50).build());

      Image brightenedImage = memory.getImage("brightened");
      Pixel brightenedPixel = brightenedImage.getPixel(0, 0);

      assertEquals(255, brightenedPixel.getRed());
      assertEquals(50, brightenedPixel.getGreen());
      assertEquals(50, brightenedPixel.getBlue());
    }

    @Test
    public void testVerticalFlip() throws
            ImageProcessorException {
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      service.verticalFlip(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("flipped").build());

      Image flippedImage = memory.getImage("flipped");

      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel originalPixel = testImage.getPixel(row, col);
          Pixel flippedPixel =
                  flippedImage.getPixel(flippedImage.getHeight() - 1 - row,
                          col);// Flipped vertically

          assertEquals(originalPixel.getRed(), flippedPixel.getRed());
          assertEquals(originalPixel.getGreen(), flippedPixel.getGreen());
          assertEquals(originalPixel.getBlue(), flippedPixel.getBlue());
        }
      }
    }

    @Test
    public void testHorizontalFlip() throws
            ImageProcessorException {
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      service.horizontalFlip(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("flipped").build());

      Image flippedImage = memory.getImage("flipped");

      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel originalPixel = testImage.getPixel(row, col);
          Pixel flippedPixel =
                  flippedImage.getPixel(row,
                          flippedImage.getWidth() - col - 1); //
          // Flipped horizontally

          assertEquals(originalPixel.getRed(), flippedPixel.getRed());
          assertEquals(originalPixel.getGreen(), flippedPixel.getGreen());
          assertEquals(originalPixel.getBlue(), flippedPixel.getBlue());
        }
      }
    }

    @Test
    public void testRGBSplit() throws
            ImageProcessorException {
      Image testImage = createTestImage();
      memory.addImage("original", testImage);

      service.rgbSplit(ImageProcessingRequest.builder().imageName(
                      "original").redImageName("red")
              .greenImageName("green")
              .blueImageName("blue").build());

      Image redImage = memory.getImage("red");
      Image greenImage = memory.getImage("green");
      Image blueImage = memory.getImage("blue");

      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel originalPixel = testImage.getPixel(row, col);

          assertEquals(originalPixel.getRed(),
                  redImage.getPixel(row, col).getRed());
          assertEquals(originalPixel.getRed(),
                  redImage.getPixel(row, col).getGreen());
          assertEquals(originalPixel.getRed(),
                  redImage.getPixel(row, col).getBlue());

          assertEquals(originalPixel.getGreen(), greenImage.getPixel(row,
                  col).getRed());
          assertEquals(originalPixel.getGreen(), greenImage.getPixel(row,
                  col).getGreen());
          assertEquals(originalPixel.getGreen(), greenImage.getPixel(row,
                  col).getBlue());

          assertEquals(originalPixel.getBlue(),
                  blueImage.getPixel(row, col).getRed());
          assertEquals(originalPixel.getBlue(),
                  blueImage.getPixel(row, col).getGreen());
          assertEquals(originalPixel.getBlue(),
                  blueImage.getPixel(row, col).getBlue());
        }
      }
    }

    @Test
    public void testRGBCombine() throws
            ImageProcessorException {
      Image testImage = createTestImage();
      memory.addImage("original", testImage);
      service.rgbSplit(ImageProcessingRequest.builder().imageName(
                      "original").redImageName("red")
              .greenImageName("green")
              .blueImageName("blue").build());

      service.rgbCombine(ImageProcessingRequest.builder().imageName(
                      "combined").redImageName("red")
              .greenImageName("green")
              .blueImageName("blue").build());

      Image combinedImage = memory.getImage("combined");
      Image originalImage = memory.getImage("original");

      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 2; col++) {
          Pixel originalPixel = originalImage.getPixel(row, col);
          Pixel combinedPixel = combinedImage.getPixel(row, col);

          assertEquals(originalPixel.getRed(), combinedPixel.getRed());
          assertEquals(originalPixel.getGreen(), combinedPixel.getGreen());
          assertEquals(originalPixel.getBlue(), combinedPixel.getBlue());
        }
      }
    }

    @Test
    public void testBlur() throws
            ImageProcessorException {
      Image testImage =
              Factory.createImage(new Pixel[][]{new Pixel[]{new RGB(100, 100,
                      100)}});
      memory.addImage("original", testImage);

      service.blurImage(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("blurred").build());

      Image blurredImage = memory.getImage("blurred");
      assertNotNull(blurredImage);

      assertEquals(25, blurredImage.getPixel(0, 0).getRed());
      assertEquals(25, blurredImage.getPixel(0, 0).getGreen());
      assertEquals(25, blurredImage.getPixel(0, 0).getBlue());
    }

    @Test
    public void testSepia() throws
            ImageProcessorException {
      Image testImage =
              Factory.createImage(new Pixel[][]{new Pixel[]{
                  new RGB(100, 100, 100)}});
      memory.addImage("original", testImage);

      service.sepiaImage(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("sepia").build());

      Image sepiaImage = memory.getImage("sepia");
      assertNotNull(sepiaImage);

      Pixel sepiaPixel = sepiaImage.getPixel(0, 0);
      assertEquals(135, sepiaPixel.getRed());
      assertEquals(120, sepiaPixel.getGreen());
      assertEquals(93, sepiaPixel.getBlue());
    }

    @Test
    public void testValueComponent() throws
            ImageProcessorException {
      Image testImage =
              Factory.createImage(new Pixel[][]{new Pixel[]{
                  new RGB(100, 100, 100)}});
      memory.addImage("original", testImage);

      service.createValueComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("value").build());

      assertEquals(100, memory.getImage("value").getPixel(0, 0).getRed());
      assertEquals(100, memory.getImage("value").getPixel(0, 0).getGreen());
      assertEquals(100, memory.getImage("value").getPixel(0, 0).getBlue());
    }

    @Test
    public void testLumaComponent() throws
            ImageProcessorException {
      Image testImage =
              Factory.createImage(new Pixel[][]{new Pixel[]{new RGB(100, 100,
                      100)}});
      memory.addImage("original", testImage);

      service.createLumaComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("luma").build());

      assertEquals(100, memory.getImage("luma").getPixel(0, 0).getRed());
      assertEquals(100, memory.getImage("luma").getPixel(0, 0).getGreen());
      assertEquals(100, memory.getImage("luma").getPixel(0, 0).getBlue());
    }

    @Test
    public void testIntenistyComponent() throws
            ImageProcessorException {
      Image testImage =
              Factory.createImage(new Pixel[][]{new Pixel[]{new RGB(100, 100,
                      100)}});
      memory.addImage("original", testImage);

      service.createIntensityComponent(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("intensity").build());

      assertEquals(100, memory.getImage("intensity").getPixel(0, 0).getRed());
      assertEquals(100, memory.getImage("intensity").getPixel(0, 0).getGreen());
      assertEquals(100, memory.getImage("intensity").getPixel(0, 0).getBlue());
    }

    @Test
    public void testSharpen() throws
            ImageProcessorException {
      Image testImage = Factory.createImage(
              new Pixel[][]{new Pixel[]{new RGB(100, 100, 100)}}
      );
      memory.addImage("original", testImage);

      service.sharpenImage(ImageProcessingRequest.builder().imageName(
              "original").destinationImageName("sharpened").build());

      Image sharpenedImage = memory.getImage("sharpened");
      assertNotNull(sharpenedImage);

      Pixel sharpenedPixel = sharpenedImage.getPixel(0, 0);
      assertEquals(100, sharpenedPixel.getRed());
      assertEquals(100, sharpenedPixel.getGreen());
      assertEquals(100, sharpenedPixel.getBlue());
    }
  }

  /**
   * Test class for image processor app.
   */
  public static class TestImageProcessorApplication {

    @Test
    public void testMainWithCommandLineArguments() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      String[] args = new String[]{"-file", "test_resources/test_valid_script"
              + ".txt"};
      ImageProcessorApp.main(args);
      assertTrue(outContent.toString().contains("Successfully executed the "
              + "script file."));
      System.setOut(System.out);
    }

    @Test
    public void testMainWithThreeCommandLineArguments() {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      String[] args = new String[]{"-file",
          "test_resources/test_valid_script.txt", "invalid command line"};
      ImageProcessorApp.main(args);
      assertTrue(outContent.toString().contains("Successfully executed the "
              + "script file."));
      System.setOut(System.out);
    }
  }


  /**
   * Test class for testing the argument parsers that parsers main arguments.
   */
  public static class TestArgumentParsers {

    @Test
    public void testArgumentParserWithNoArguments() {
      String[] args = new String[]{};
      ArgumentParser parser = Factory.getArgumentParser(
              args
      );
      assertTrue(parser instanceof GUIArgumentParser);
      ImageProcessorController controller = parser.createController(args);
      assertTrue(controller instanceof GUIImageProcessorController);
    }

    @Test
    public void testArgumentParserWithFileArgument() throws
            ImageProcessorException {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      String[] args = new String[]{"-file", "test_resources/test_valid_script"
              + ".txt"};
      ImageProcessorApp.main(args);
      assertTrue(outContent.toString().contains("Successfully executed the "
              + "script file."));
      ArgumentParser parser = Factory.getArgumentParser(
              args
      );
      assertTrue(parser instanceof CommandLineArgumentParser);
      ImageProcessorController controller = parser.createController(args);
      assertTrue(controller instanceof CommandLineImageProcessorController);
      Image actualImage = IOUtils.read("test_resources/output"
              + "/saved_sample_image_red_component.png", ImageType.PNG);
      assertEquals(
              Factory.createImage(
                      TestUtils.createPixels(
                              new int[][]{{16777215, 0}, {0, 8421504}}
                      )
              ),
              actualImage
      );
      System.setOut(System.out);

    }

    @Test
    public void testArgumentParserWithInteractiveArgument()
            throws
            ImageProcessorException {
      String[] args = new String[]{"-text"};
      ArgumentParser parser = Factory.getArgumentParser(
              args
      );
      String input = "load test_resources/input/random.png sample-image\n"
              + "red-component sample-image sample-image-red-component\n"
              + "save test_resources/output/"
              + "saved_sample_image_red_component.png "
              + "sample-image-red-component\n"
              + "quit";
      InputStream stream =
              new ByteArrayInputStream(input.getBytes());
      System.setIn(stream);
      assertTrue(parser instanceof InteractiveArgumentParser);
      ImageProcessorController controller = parser.createController(args);
      assertTrue(controller instanceof InteractiveImageProcessorController);
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      ImageProcessorApp.main(args);
      assertTrue(outContent.toString().contains("Successfully created red "
              + "component"));
      Image actualImage = IOUtils.read("test_resources/output"
              + "/saved_sample_image_red_component.png", ImageType.PNG);
      assertEquals(
              Factory.createImage(
                      TestUtils.createPixels(
                              new int[][]{{16777215, 0}, {0, 8421504}}
                      )
              ),
              actualImage
      );
      System.setIn(System.in);
      System.setOut(System.out);
    }

    @Test
    public void testWithInvalidArguments() {
      String[] args = new String[]{"-invalid"};
      assertThrows(
              ImageProcessingRunTimeException.QuitException.class,
          () -> Factory.getArgumentParser(args));
    }


    @Test
    public void testWithInvalidFileArgument() throws
            ImageProcessorException {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      String[] args = new String[]{"-file", "invalid_file"};
      ImageProcessorApp.main(args);
      assertTrue(outContent.toString()
              .contains("Error reading script file: invalid_file"));
      ArgumentParser parser = Factory.getArgumentParser(
              args
      );
      assertTrue(parser instanceof CommandLineArgumentParser);
      ImageProcessorController controller = parser.createController(args);
      assertTrue(controller instanceof CommandLineImageProcessorController);
    }
  }

  /**
   * Test class for execution status.
   */
  public static class TestExecutionStatus {


    @Test(expected = NullPointerException.class)
    public void testConstructor() {
      new ExecutionStatus(true, null);
    }


    @Test
    public void testGetters() {
      ExecutionStatus status = new ExecutionStatus(true, "test");
      assertEquals("test", status.getMessage());
      assertTrue(status.isSuccess());
    }

    @Test
    public void testEqualsAndHashCode() {

      ExecutionStatus statusOne = new ExecutionStatus(true, "test");
      ExecutionStatus statusTwo = new ExecutionStatus(true, "test");
      ExecutionStatus statusThree = new ExecutionStatus(false, "test");
      ExecutionStatus statusFour = new ExecutionStatus(true, "test1");

      assertEquals(statusOne, statusTwo);
      assertEquals(statusOne.hashCode(), statusTwo.hashCode());

      assertNotEquals(statusOne, statusThree);
      assertNotEquals(statusOne.hashCode(), statusThree.hashCode());

      assertNotEquals(statusOne, statusFour);
      assertNotEquals(statusOne.hashCode(), statusFour.hashCode());

      assertNotEquals(statusOne, null);
      assertNotEquals(statusOne, "test");
      assertEquals(statusOne, statusOne);
    }
  }

  /**
   * Test class for Extract Utility.
   */
  public static class TestExtractUtility {

    @Test
    public void testExtractHistogram_SinglePixelImage() throws
            ImageProcessorException {
      Image image = new RenderedImage(
              new Pixel[][]{new Pixel[]{new RGB(100, 150, 200)}}
      );
      Image histogram = ExtractUtility.createHistogram(image);

      assertEquals(histogram.getPixel(0, 100), new RGB(255, 0, 0));
      assertEquals(histogram.getPixel(0, 101), new RGB(192, 192, 192));

      assertEquals(histogram.getPixel(0, 150), new RGB(0, 255, 0));
      assertEquals(histogram.getPixel(0, 151), new RGB(192, 192, 192));

      assertEquals(histogram.getPixel(0, 200), new RGB(0, 0, 255));
    }

    @Test
    public void testExtractHistogram_2x2Image() throws
            ImageProcessorException {
      Image image = new RenderedImage(
              new Pixel[][]{
                      {new RGB(100, 150, 200), new RGB(50, 100, 150)},
                      {new RGB(200, 250, 255), new RGB(150, 200, 250)}
              }
      );
      Image histogram = ExtractUtility.createHistogram(image);

      assertEquals(histogram.getPixel(0, 100), new RGB(0, 255, 0));
      assertEquals(histogram.getPixel(0, 150), new RGB(0, 0, 255));
      assertEquals(histogram.getPixel(0, 200), new RGB(0, 0, 255));
      assertEquals(histogram.getPixel(0, 50), new RGB(255, 0, 0));
      assertEquals(histogram.getPixel(0, 250), new RGB(0, 0, 255));
      assertEquals(histogram.getPixel(0, 255), new RGB(0, 0, 255));
    }

    @Test
    public void testExtractHistogram_2x2Image_samePixel() throws
            ImageProcessorException {
      Image image = new RenderedImage(
              new Pixel[][]{
                      {new RGB(100, 150, 200), new RGB(100, 150, 200)},
                      {new RGB(100, 150, 200), new RGB(100, 150, 200)}
              }
      );
      Image histogram = ExtractUtility.createHistogram(image);

      assertEquals(histogram.getPixel(0, 100), new RGB(255, 0, 0));
      assertEquals(histogram.getPixel(0, 150), new RGB(0, 255, 0));
      assertEquals(histogram.getPixel(0, 200), new RGB(0, 0, 255));
    }

    @Test
    public void testExtractHistogram_2x2Image_BlueImage() throws
            ImageProcessorException {
      Image image = new RenderedImage(
              new Pixel[][]{
                      {new RGB(0, 0, 255), new RGB(0, 0, 255)},
                      {new RGB(0, 0, 255), new RGB(0, 0, 255)}
              }
      );
      Image histogram = ExtractUtility.createHistogram(image);

      assertEquals(histogram.getPixel(0, 255), new RGB(0, 0, 255));
      assertEquals(histogram.getPixel(0, 0), new RGB(0, 255, 0));
      for (int i = 0; i < 255; i++) {
        assertEquals(histogram.getPixel(255, i), new RGB(0, 0, 255));
      }
    }

    @Test
    public void testCountFrequency() throws
            ImageProcessorException {
      // Create a simple 2x2 test image
      Image image = new RenderedImage(
              new Pixel[][]{
                      {new RGB(255, 0, 0), new RGB(0, 255, 0)},
                      {new RGB(0, 0, 255), new RGB(255, 255, 255)},
                      //add random non boundary values
                      {new RGB(100, 100, 100), new RGB(150, 150, 150)},
                      {new RGB(200, 200, 200), new RGB(250, 250, 250)}
              }
      );

      // Define the expected frequency arrays
      int[] expectedRedFreq = new int[256];
      int[] expectedGreenFreq = new int[256];
      int[] expectedBlueFreq = new int[256];
      expectedRedFreq[255] = 2;
      expectedRedFreq[0] = 2;
      expectedRedFreq[100] = 1;
      expectedRedFreq[150] = 1;
      expectedRedFreq[200] = 1;
      expectedRedFreq[250] = 1;

      expectedGreenFreq[255] = 2;
      expectedGreenFreq[0] = 2;
      expectedGreenFreq[100] = 1;
      expectedGreenFreq[150] = 1;
      expectedGreenFreq[200] = 1;
      expectedGreenFreq[250] = 1;

      expectedBlueFreq[255] = 2;
      expectedBlueFreq[0] = 2;
      expectedBlueFreq[100] = 1;
      expectedBlueFreq[150] = 1;
      expectedBlueFreq[200] = 1;
      expectedBlueFreq[250] = 1;

      int[] redFreq = ExtractUtility.calculateColorFrequencies(image,
              Pixel::getRed);
      int[] greenFreq = ExtractUtility.calculateColorFrequencies(image,
              Pixel::getGreen);
      int[] blueFreq = ExtractUtility.calculateColorFrequencies(image,
              Pixel::getBlue);

      assertArrayEquals(expectedRedFreq, redFreq);
      assertArrayEquals(expectedGreenFreq, greenFreq);
      assertArrayEquals(expectedBlueFreq, blueFreq);
    }
  }

  /**
   * Test class for the ExtractUtility class.
   */
  public static class ExtractUtilityTest {

    @Test
    public void testHistogram() {
      try {
        int[][] result = {
                {0, 0, 255, 0, 0},
                {0, 0, 255, 1, 0},
                {0, 0, 255, 2, 0},
                {0, 0, 255, 3, 0},
                {0, 0, 255, 4, 0},
                {0, 0, 255, 5, 0},
                {0, 0, 255, 6, 0},
                {0, 0, 255, 7, 0},
                {0, 0, 255, 8, 0},
                {0, 0, 255, 9, 0},
                {0, 0, 255, 10, 0},
                {0, 0, 255, 11, 0},
                {0, 0, 255, 12, 0},
                {0, 0, 255, 13, 0},
                {0, 0, 255, 14, 0},
                {0, 0, 255, 15, 0},
                {0, 0, 255, 16, 0},
                {0, 0, 255, 17, 0},
                {0, 0, 255, 18, 0},
                {0, 0, 255, 19, 0},
                {0, 0, 255, 20, 0},
                {0, 0, 255, 21, 0},
                {0, 0, 255, 22, 0},
                {0, 0, 255, 23, 0},
                {0, 0, 255, 24, 0},
                {0, 0, 255, 25, 0},
                {0, 0, 255, 26, 0},
                {0, 0, 255, 27, 0},
                {0, 0, 255, 28, 0},
                {0, 0, 255, 29, 0},
                {0, 0, 255, 30, 0},
                {0, 0, 255, 31, 0},
                {0, 0, 255, 32, 0},
                {0, 0, 255, 33, 0},
                {0, 0, 255, 34, 0},
                {0, 0, 255, 35, 0},
                {0, 0, 255, 36, 0},
                {0, 0, 255, 37, 0},
                {0, 0, 255, 38, 0},
                {0, 0, 255, 39, 0},
                {0, 0, 255, 40, 0},
                {0, 0, 255, 41, 0},
                {0, 0, 255, 42, 0},
                {0, 0, 255, 43, 0},
                {0, 0, 255, 44, 0},
                {0, 0, 255, 45, 0},
                {0, 0, 255, 46, 0},
                {0, 0, 255, 47, 0},
                {0, 0, 255, 48, 0},
                {0, 0, 255, 49, 0},
                {0, 0, 255, 50, 0},
                {0, 0, 255, 51, 0},
                {0, 0, 255, 52, 0},
                {0, 0, 255, 53, 0},
                {0, 0, 255, 54, 0},
                {0, 0, 255, 55, 0},
                {0, 0, 255, 56, 0},
                {0, 0, 255, 57, 0},
                {0, 0, 255, 58, 0},
                {0, 0, 255, 59, 0},
                {0, 0, 255, 60, 0},
                {0, 0, 255, 61, 0},
                {0, 0, 255, 62, 0},
                {0, 0, 255, 63, 0},
                {0, 0, 255, 64, 0},
                {0, 0, 255, 65, 0},
                {0, 0, 255, 66, 0},
                {0, 0, 255, 67, 0},
                {0, 0, 255, 68, 0},
                {0, 0, 255, 69, 0},
                {0, 0, 255, 70, 0},
                {0, 0, 255, 71, 0},
                {0, 0, 255, 72, 0},
                {0, 0, 255, 73, 0},
                {0, 0, 255, 74, 0},
                {0, 0, 255, 75, 0},
                {0, 0, 255, 76, 0},
                {0, 0, 255, 77, 0},
                {0, 0, 255, 78, 0},
                {0, 0, 255, 79, 0},
                {0, 0, 255, 80, 0},
                {0, 0, 255, 81, 0},
                {0, 0, 255, 82, 0},
                {0, 0, 255, 83, 0},
                {0, 0, 255, 84, 0},
                {0, 0, 255, 85, 0},
                {0, 0, 255, 86, 0},
                {0, 0, 255, 87, 0},
                {0, 0, 255, 88, 0},
                {0, 0, 255, 89, 0},
                {0, 0, 255, 90, 0},
                {0, 0, 255, 91, 0},
                {0, 0, 255, 92, 0},
                {0, 0, 255, 93, 0},
                {0, 0, 255, 94, 0},
                {0, 0, 255, 95, 0},
                {0, 0, 255, 96, 0},
                {0, 0, 255, 97, 0},
                {0, 0, 255, 98, 0},
                {0, 0, 255, 99, 0},
                {0, 0, 255, 100, 0},
                {0, 0, 255, 101, 0},
                {0, 0, 255, 102, 0},
                {0, 0, 255, 103, 0},
                {0, 0, 255, 104, 0},
                {0, 0, 255, 105, 0},
                {0, 0, 255, 106, 0},
                {0, 0, 255, 107, 0},
                {0, 0, 255, 108, 0},
                {0, 0, 255, 109, 0},
                {0, 0, 255, 110, 0},
                {0, 0, 255, 111, 0},
                {0, 0, 255, 112, 0},
                {0, 0, 255, 113, 0},
                {0, 0, 255, 114, 0},
                {0, 0, 255, 115, 0},
                {0, 0, 255, 116, 0},
                {0, 0, 255, 117, 0},
                {0, 0, 255, 118, 0},
                {0, 0, 255, 119, 0},
                {0, 0, 255, 120, 0},
                {0, 0, 255, 121, 0},
                {0, 0, 255, 122, 0},
                {0, 0, 255, 123, 0},
                {0, 0, 255, 124, 0},
                {0, 0, 255, 125, 0},
                {0, 0, 255, 126, 0},
                {0, 0, 255, 127, 0},
                {0, 0, 255, 128, 1},
                {0, 0, 255, 128, 128},
                {0, 0, 255, 128, 255},
                {0, 0, 255, 129, 1},
                {0, 0, 255, 129, 128},
                {0, 0, 255, 129, 255},
                {0, 0, 255, 130, 1},
                {0, 0, 255, 130, 128},
                {0, 0, 255, 130, 255},
                {0, 0, 255, 131, 1},
                {0, 0, 255, 131, 128},
                {0, 0, 255, 131, 255},
                {0, 0, 255, 132, 1},
                {0, 0, 255, 132, 128},
                {0, 0, 255, 132, 255},
                {0, 0, 255, 133, 1},
                {0, 0, 255, 133, 128},
                {0, 0, 255, 133, 255},
                {0, 0, 255, 134, 1},
                {0, 0, 255, 134, 128},
                {0, 0, 255, 134, 255},
                {0, 0, 255, 135, 1},
                {0, 0, 255, 135, 128},
                {0, 0, 255, 135, 255},
                {0, 0, 255, 136, 1},
                {0, 0, 255, 136, 128},
                {0, 0, 255, 136, 255},
                {0, 0, 255, 137, 1},
                {0, 0, 255, 137, 128},
                {0, 0, 255, 137, 255},
                {0, 0, 255, 138, 1},
                {0, 0, 255, 138, 128},
                {0, 0, 255, 138, 255},
                {0, 0, 255, 139, 1},
                {0, 0, 255, 139, 128},
                {0, 0, 255, 139, 255},
                {0, 0, 255, 140, 1},
                {0, 0, 255, 140, 128},
                {0, 0, 255, 140, 255},
                {0, 0, 255, 141, 1},
                {0, 0, 255, 141, 128},
                {0, 0, 255, 141, 255},
                {0, 0, 255, 142, 1},
                {0, 0, 255, 142, 128},
                {0, 0, 255, 142, 255},
                {0, 0, 255, 143, 1},
                {0, 0, 255, 143, 128},
                {0, 0, 255, 143, 255},
                {0, 0, 255, 144, 1},
                {0, 0, 255, 144, 128},
                {0, 0, 255, 144, 255},
                {0, 0, 255, 145, 1},
                {0, 0, 255, 145, 128},
                {0, 0, 255, 145, 255},
                {0, 0, 255, 146, 1},
                {0, 0, 255, 146, 128},
                {0, 0, 255, 146, 255},
                {0, 0, 255, 147, 1},
                {0, 0, 255, 147, 128},
                {0, 0, 255, 147, 255},
                {0, 0, 255, 148, 1},
                {0, 0, 255, 148, 128},
                {0, 0, 255, 148, 255},
                {0, 0, 255, 149, 1},
                {0, 0, 255, 149, 128},
                {0, 0, 255, 149, 255},
                {0, 0, 255, 150, 1},
                {0, 0, 255, 150, 128},
                {0, 0, 255, 150, 255},
                {0, 0, 255, 151, 1},
                {0, 0, 255, 151, 128},
                {0, 0, 255, 151, 255},
                {0, 0, 255, 152, 1},
                {0, 0, 255, 152, 128},
                {0, 0, 255, 152, 255},
                {0, 0, 255, 153, 1},
                {0, 0, 255, 153, 128},
                {0, 0, 255, 153, 255},
                {0, 0, 255, 154, 1},
                {0, 0, 255, 154, 128},
                {0, 0, 255, 154, 255},
                {0, 0, 255, 155, 1},
                {0, 0, 255, 155, 128},
                {0, 0, 255, 155, 255},
                {0, 0, 255, 156, 1},
                {0, 0, 255, 156, 128},
                {0, 0, 255, 156, 255},
                {0, 0, 255, 157, 1},
                {0, 0, 255, 157, 128},
                {0, 0, 255, 157, 255},
                {0, 0, 255, 158, 1},
                {0, 0, 255, 158, 128},
                {0, 0, 255, 158, 255},
                {0, 0, 255, 159, 1},
                {0, 0, 255, 159, 128},
                {0, 0, 255, 159, 255},
                {0, 0, 255, 160, 1},
                {0, 0, 255, 160, 128},
                {0, 0, 255, 160, 255},
                {0, 0, 255, 161, 1},
                {0, 0, 255, 161, 128},
                {0, 0, 255, 161, 255},
                {0, 0, 255, 162, 1},
                {0, 0, 255, 162, 128},
                {0, 0, 255, 162, 255},
                {0, 0, 255, 163, 1},
                {0, 0, 255, 163, 128},
                {0, 0, 255, 163, 255},
                {0, 0, 255, 164, 1},
                {0, 0, 255, 164, 128},
                {0, 0, 255, 164, 255},
                {0, 0, 255, 165, 1},
                {0, 0, 255, 165, 128},
                {0, 0, 255, 165, 255},
                {0, 0, 255, 166, 1},
                {0, 0, 255, 166, 128},
                {0, 0, 255, 166, 255},
                {0, 0, 255, 167, 1},
                {0, 0, 255, 167, 128},
                {0, 0, 255, 167, 255},
                {0, 0, 255, 168, 1},
                {0, 0, 255, 168, 128},
                {0, 0, 255, 168, 255},
                {0, 0, 255, 169, 1},
                {0, 0, 255, 169, 128},
                {0, 0, 255, 169, 255},
                {0, 0, 255, 170, 1},
                {0, 0, 255, 170, 128},
                {0, 0, 255, 170, 255},
                {0, 0, 255, 171, 1},
                {0, 0, 255, 171, 128},
                {0, 0, 255, 171, 255},
                {0, 0, 255, 172, 1},
                {0, 0, 255, 172, 128},
                {0, 0, 255, 172, 255},
                {0, 0, 255, 173, 1},
                {0, 0, 255, 173, 128},
                {0, 0, 255, 173, 255},
                {0, 0, 255, 174, 1},
                {0, 0, 255, 174, 128},
                {0, 0, 255, 174, 255},
                {0, 0, 255, 175, 1},
                {0, 0, 255, 175, 128},
                {0, 0, 255, 175, 255},
                {0, 0, 255, 176, 1},
                {0, 0, 255, 176, 128},
                {0, 0, 255, 176, 255},
                {0, 0, 255, 177, 1},
                {0, 0, 255, 177, 128},
                {0, 0, 255, 177, 255},
                {0, 0, 255, 178, 1},
                {0, 0, 255, 178, 128},
                {0, 0, 255, 178, 255},
                {0, 0, 255, 179, 1},
                {0, 0, 255, 179, 128},
                {0, 0, 255, 179, 255},
                {0, 0, 255, 180, 1},
                {0, 0, 255, 180, 128},
                {0, 0, 255, 180, 255},
                {0, 0, 255, 181, 1},
                {0, 0, 255, 181, 128},
                {0, 0, 255, 181, 255},
                {0, 0, 255, 182, 1},
                {0, 0, 255, 182, 128},
                {0, 0, 255, 182, 255},
                {0, 0, 255, 183, 1},
                {0, 0, 255, 183, 128},
                {0, 0, 255, 183, 255},
                {0, 0, 255, 184, 1},
                {0, 0, 255, 184, 128},
                {0, 0, 255, 184, 255},
                {0, 0, 255, 185, 1},
                {0, 0, 255, 185, 128},
                {0, 0, 255, 185, 255},
                {0, 0, 255, 186, 1},
                {0, 0, 255, 186, 128},
                {0, 0, 255, 186, 255},
                {0, 0, 255, 187, 1},
                {0, 0, 255, 187, 128},
                {0, 0, 255, 187, 255},
                {0, 0, 255, 188, 1},
                {0, 0, 255, 188, 128},
                {0, 0, 255, 188, 255},
                {0, 0, 255, 189, 1},
                {0, 0, 255, 189, 128},
                {0, 0, 255, 189, 255},
                {0, 0, 255, 190, 1},
                {0, 0, 255, 190, 128},
                {0, 0, 255, 190, 255},
                {0, 0, 255, 191, 1},
                {0, 0, 255, 191, 128},
                {0, 0, 255, 191, 255},
                {0, 0, 255, 192, 1},
                {0, 0, 255, 192, 127},
                {0, 0, 255, 192, 129},
                {0, 0, 255, 192, 254},
                {0, 0, 255, 193, 1},
                {0, 0, 255, 193, 127},
                {0, 0, 255, 193, 129},
                {0, 0, 255, 193, 254},
                {0, 0, 255, 194, 1},
                {0, 0, 255, 194, 127},
                {0, 0, 255, 194, 129},
                {0, 0, 255, 194, 254},
                {0, 0, 255, 195, 1},
                {0, 0, 255, 195, 127},
                {0, 0, 255, 195, 129},
                {0, 0, 255, 195, 254},
                {0, 0, 255, 196, 1},
                {0, 0, 255, 196, 127},
                {0, 0, 255, 196, 129},
                {0, 0, 255, 196, 254},
                {0, 0, 255, 197, 1},
                {0, 0, 255, 197, 127},
                {0, 0, 255, 197, 129},
                {0, 0, 255, 197, 254},
                {0, 0, 255, 198, 1},
                {0, 0, 255, 198, 127},
                {0, 0, 255, 198, 129},
                {0, 0, 255, 198, 254},
                {0, 0, 255, 199, 1},
                {0, 0, 255, 199, 127},
                {0, 0, 255, 199, 129},
                {0, 0, 255, 199, 254},
                {0, 0, 255, 200, 1},
                {0, 0, 255, 200, 127},
                {0, 0, 255, 200, 129},
                {0, 0, 255, 200, 254},
                {0, 0, 255, 201, 1},
                {0, 0, 255, 201, 127},
                {0, 0, 255, 201, 129},
                {0, 0, 255, 201, 254},
                {0, 0, 255, 202, 1},
                {0, 0, 255, 202, 127},
                {0, 0, 255, 202, 129},
                {0, 0, 255, 202, 254},
                {0, 0, 255, 203, 1},
                {0, 0, 255, 203, 127},
                {0, 0, 255, 203, 129},
                {0, 0, 255, 203, 254},
                {0, 0, 255, 204, 1},
                {0, 0, 255, 204, 127},
                {0, 0, 255, 204, 129},
                {0, 0, 255, 204, 254},
                {0, 0, 255, 205, 1},
                {0, 0, 255, 205, 127},
                {0, 0, 255, 205, 129},
                {0, 0, 255, 205, 254},
                {0, 0, 255, 206, 1},
                {0, 0, 255, 206, 127},
                {0, 0, 255, 206, 129},
                {0, 0, 255, 206, 254},
                {0, 0, 255, 207, 1},
                {0, 0, 255, 207, 127},
                {0, 0, 255, 207, 129},
                {0, 0, 255, 207, 254},
                {0, 0, 255, 208, 1},
                {0, 0, 255, 208, 127},
                {0, 0, 255, 208, 129},
                {0, 0, 255, 208, 254},
                {0, 0, 255, 209, 1},
                {0, 0, 255, 209, 127},
                {0, 0, 255, 209, 129},
                {0, 0, 255, 209, 254},
                {0, 0, 255, 210, 1},
                {0, 0, 255, 210, 127},
                {0, 0, 255, 210, 129},
                {0, 0, 255, 210, 254},
                {0, 0, 255, 211, 1},
                {0, 0, 255, 211, 127},
                {0, 0, 255, 211, 129},
                {0, 0, 255, 211, 254},
                {0, 0, 255, 212, 1},
                {0, 0, 255, 212, 127},
                {0, 0, 255, 212, 129},
                {0, 0, 255, 212, 254},
                {0, 0, 255, 213, 1},
                {0, 0, 255, 213, 127},
                {0, 0, 255, 213, 129},
                {0, 0, 255, 213, 254},
                {0, 0, 255, 214, 1},
                {0, 0, 255, 214, 127},
                {0, 0, 255, 214, 129},
                {0, 0, 255, 214, 254},
                {0, 0, 255, 215, 1},
                {0, 0, 255, 215, 127},
                {0, 0, 255, 215, 129},
                {0, 0, 255, 215, 254},
                {0, 0, 255, 216, 1},
                {0, 0, 255, 216, 127},
                {0, 0, 255, 216, 129},
                {0, 0, 255, 216, 254},
                {0, 0, 255, 217, 1},
                {0, 0, 255, 217, 127},
                {0, 0, 255, 217, 129},
                {0, 0, 255, 217, 254},
                {0, 0, 255, 218, 1},
                {0, 0, 255, 218, 127},
                {0, 0, 255, 218, 129},
                {0, 0, 255, 218, 254},
                {0, 0, 255, 219, 1},
                {0, 0, 255, 219, 127},
                {0, 0, 255, 219, 129},
                {0, 0, 255, 219, 254},
                {0, 0, 255, 220, 1},
                {0, 0, 255, 220, 127},
                {0, 0, 255, 220, 129},
                {0, 0, 255, 220, 254},
                {0, 0, 255, 221, 1},
                {0, 0, 255, 221, 127},
                {0, 0, 255, 221, 129},
                {0, 0, 255, 221, 254},
                {0, 0, 255, 222, 1},
                {0, 0, 255, 222, 127},
                {0, 0, 255, 222, 129},
                {0, 0, 255, 222, 254},
                {0, 0, 255, 223, 1},
                {0, 0, 255, 223, 127},
                {0, 0, 255, 223, 129},
                {0, 0, 255, 223, 254},
                {0, 0, 255, 224, 1},
                {0, 0, 255, 224, 127},
                {0, 0, 255, 224, 129},
                {0, 0, 255, 224, 254},
                {0, 0, 255, 225, 1},
                {0, 0, 255, 225, 127},
                {0, 0, 255, 225, 129},
                {0, 0, 255, 225, 254},
                {0, 0, 255, 226, 1},
                {0, 0, 255, 226, 127},
                {0, 0, 255, 226, 129},
                {0, 0, 255, 226, 254},
                {0, 0, 255, 227, 1},
                {0, 0, 255, 227, 127},
                {0, 0, 255, 227, 129},
                {0, 0, 255, 227, 254},
                {0, 0, 255, 228, 1},
                {0, 0, 255, 228, 127},
                {0, 0, 255, 228, 129},
                {0, 0, 255, 228, 254},
                {0, 0, 255, 229, 1},
                {0, 0, 255, 229, 127},
                {0, 0, 255, 229, 129},
                {0, 0, 255, 229, 254},
                {0, 0, 255, 230, 1},
                {0, 0, 255, 230, 127},
                {0, 0, 255, 230, 129},
                {0, 0, 255, 230, 254},
                {0, 0, 255, 231, 1},
                {0, 0, 255, 231, 127},
                {0, 0, 255, 231, 129},
                {0, 0, 255, 231, 254},
                {0, 0, 255, 232, 1},
                {0, 0, 255, 232, 127},
                {0, 0, 255, 232, 129},
                {0, 0, 255, 232, 254},
                {0, 0, 255, 233, 1},
                {0, 0, 255, 233, 127},
                {0, 0, 255, 233, 129},
                {0, 0, 255, 233, 254},
                {0, 0, 255, 234, 1},
                {0, 0, 255, 234, 127},
                {0, 0, 255, 234, 129},
                {0, 0, 255, 234, 254},
                {0, 0, 255, 235, 1},
                {0, 0, 255, 235, 127},
                {0, 0, 255, 235, 129},
                {0, 0, 255, 235, 254},
                {0, 0, 255, 236, 1},
                {0, 0, 255, 236, 127},
                {0, 0, 255, 236, 129},
                {0, 0, 255, 236, 254},
                {0, 0, 255, 237, 1},
                {0, 0, 255, 237, 127},
                {0, 0, 255, 237, 129},
                {0, 0, 255, 237, 254},
                {0, 0, 255, 238, 1},
                {0, 0, 255, 238, 127},
                {0, 0, 255, 238, 129},
                {0, 0, 255, 238, 254},
                {0, 0, 255, 239, 1},
                {0, 0, 255, 239, 127},
                {0, 0, 255, 239, 129},
                {0, 0, 255, 239, 254},
                {0, 0, 255, 240, 1},
                {0, 0, 255, 240, 127},
                {0, 0, 255, 240, 129},
                {0, 0, 255, 240, 254},
                {0, 0, 255, 241, 1},
                {0, 0, 255, 241, 127},
                {0, 0, 255, 241, 129},
                {0, 0, 255, 241, 254},
                {0, 0, 255, 242, 1},
                {0, 0, 255, 242, 127},
                {0, 0, 255, 242, 129},
                {0, 0, 255, 242, 254},
                {0, 0, 255, 243, 1},
                {0, 0, 255, 243, 127},
                {0, 0, 255, 243, 129},
                {0, 0, 255, 243, 254},
                {0, 0, 255, 244, 1},
                {0, 0, 255, 244, 127},
                {0, 0, 255, 244, 129},
                {0, 0, 255, 244, 254},
                {0, 0, 255, 245, 1},
                {0, 0, 255, 245, 127},
                {0, 0, 255, 245, 129},
                {0, 0, 255, 245, 254},
                {0, 0, 255, 246, 1},
                {0, 0, 255, 246, 127},
                {0, 0, 255, 246, 129},
                {0, 0, 255, 246, 254},
                {0, 0, 255, 247, 1},
                {0, 0, 255, 247, 127},
                {0, 0, 255, 247, 129},
                {0, 0, 255, 247, 254},
                {0, 0, 255, 248, 1},
                {0, 0, 255, 248, 127},
                {0, 0, 255, 248, 129},
                {0, 0, 255, 248, 254},
                {0, 0, 255, 249, 1},
                {0, 0, 255, 249, 127},
                {0, 0, 255, 249, 129},
                {0, 0, 255, 249, 254},
                {0, 0, 255, 250, 1},
                {0, 0, 255, 250, 127},
                {0, 0, 255, 250, 129},
                {0, 0, 255, 250, 254},
                {0, 0, 255, 251, 1},
                {0, 0, 255, 251, 127},
                {0, 0, 255, 251, 129},
                {0, 0, 255, 251, 254},
                {0, 0, 255, 252, 1},
                {0, 0, 255, 252, 127},
                {0, 0, 255, 252, 129},
                {0, 0, 255, 252, 254},
                {0, 0, 255, 253, 1},
                {0, 0, 255, 253, 127},
                {0, 0, 255, 253, 129},
                {0, 0, 255, 253, 254},
                {0, 0, 255, 254, 1},
                {0, 0, 255, 254, 127},
                {0, 0, 255, 254, 129},
                {0, 0, 255, 254, 254},
                {0, 0, 255, 255, 1},
                {0, 0, 255, 255, 2},
                {0, 0, 255, 255, 3},
                {0, 0, 255, 255, 4},
                {0, 0, 255, 255, 5},
                {0, 0, 255, 255, 6},
                {0, 0, 255, 255, 7},
                {0, 0, 255, 255, 8},
                {0, 0, 255, 255, 9},
                {0, 0, 255, 255, 10},
                {0, 0, 255, 255, 11},
                {0, 0, 255, 255, 12},
                {0, 0, 255, 255, 13},
                {0, 0, 255, 255, 14},
                {0, 0, 255, 255, 15},
                {0, 0, 255, 255, 16},
                {0, 0, 255, 255, 17},
                {0, 0, 255, 255, 18},
                {0, 0, 255, 255, 19},
                {0, 0, 255, 255, 20},
                {0, 0, 255, 255, 21},
                {0, 0, 255, 255, 22},
                {0, 0, 255, 255, 23},
                {0, 0, 255, 255, 24},
                {0, 0, 255, 255, 25},
                {0, 0, 255, 255, 26},
                {0, 0, 255, 255, 27},
                {0, 0, 255, 255, 28},
                {0, 0, 255, 255, 29},
                {0, 0, 255, 255, 30},
                {0, 0, 255, 255, 31},
                {0, 0, 255, 255, 32},
                {0, 0, 255, 255, 33},
                {0, 0, 255, 255, 34},
                {0, 0, 255, 255, 35},
                {0, 0, 255, 255, 36},
                {0, 0, 255, 255, 37},
                {0, 0, 255, 255, 38},
                {0, 0, 255, 255, 39},
                {0, 0, 255, 255, 40},
                {0, 0, 255, 255, 41},
                {0, 0, 255, 255, 42},
                {0, 0, 255, 255, 43},
                {0, 0, 255, 255, 44},
                {0, 0, 255, 255, 45},
                {0, 0, 255, 255, 46},
                {0, 0, 255, 255, 47},
                {0, 0, 255, 255, 48},
                {0, 0, 255, 255, 49},
                {0, 0, 255, 255, 50},
                {0, 0, 255, 255, 51},
                {0, 0, 255, 255, 52},
                {0, 0, 255, 255, 53},
                {0, 0, 255, 255, 54},
                {0, 0, 255, 255, 55},
                {0, 0, 255, 255, 56},
                {0, 0, 255, 255, 57},
                {0, 0, 255, 255, 58},
                {0, 0, 255, 255, 59},
                {0, 0, 255, 255, 60},
                {0, 0, 255, 255, 61},
                {0, 0, 255, 255, 62},
                {0, 0, 255, 255, 63},
                {0, 0, 255, 255, 64},
                {0, 0, 255, 255, 65},
                {0, 0, 255, 255, 66},
                {0, 0, 255, 255, 67},
                {0, 0, 255, 255, 68},
                {0, 0, 255, 255, 69},
                {0, 0, 255, 255, 70},
                {0, 0, 255, 255, 71},
                {0, 0, 255, 255, 72},
                {0, 0, 255, 255, 73},
                {0, 0, 255, 255, 74},
                {0, 0, 255, 255, 75},
                {0, 0, 255, 255, 76},
                {0, 0, 255, 255, 77},
                {0, 0, 255, 255, 78},
                {0, 0, 255, 255, 79},
                {0, 0, 255, 255, 80},
                {0, 0, 255, 255, 81},
                {0, 0, 255, 255, 82},
                {0, 0, 255, 255, 83},
                {0, 0, 255, 255, 84},
                {0, 0, 255, 255, 85},
                {0, 0, 255, 255, 86},
                {0, 0, 255, 255, 87},
                {0, 0, 255, 255, 88},
                {0, 0, 255, 255, 89},
                {0, 0, 255, 255, 90},
                {0, 0, 255, 255, 91},
                {0, 0, 255, 255, 92},
                {0, 0, 255, 255, 93},
                {0, 0, 255, 255, 94},
                {0, 0, 255, 255, 95},
                {0, 0, 255, 255, 96},
                {0, 0, 255, 255, 97},
                {0, 0, 255, 255, 98},
                {0, 0, 255, 255, 99},
                {0, 0, 255, 255, 100},
                {0, 0, 255, 255, 101},
                {0, 0, 255, 255, 102},
                {0, 0, 255, 255, 103},
                {0, 0, 255, 255, 104},
                {0, 0, 255, 255, 105},
                {0, 0, 255, 255, 106},
                {0, 0, 255, 255, 107},
                {0, 0, 255, 255, 108},
                {0, 0, 255, 255, 109},
                {0, 0, 255, 255, 110},
                {0, 0, 255, 255, 111},
                {0, 0, 255, 255, 112},
                {0, 0, 255, 255, 113},
                {0, 0, 255, 255, 114},
                {0, 0, 255, 255, 115},
                {0, 0, 255, 255, 116},
                {0, 0, 255, 255, 117},
                {0, 0, 255, 255, 118},
                {0, 0, 255, 255, 119},
                {0, 0, 255, 255, 120},
                {0, 0, 255, 255, 121},
                {0, 0, 255, 255, 122},
                {0, 0, 255, 255, 123},
                {0, 0, 255, 255, 124},
                {0, 0, 255, 255, 125},
                {0, 0, 255, 255, 126},
                {0, 0, 255, 255, 127},
                {0, 0, 255, 255, 129},
                {0, 0, 255, 255, 130},
                {0, 0, 255, 255, 131},
                {0, 0, 255, 255, 132},
                {0, 0, 255, 255, 133},
                {0, 0, 255, 255, 134},
                {0, 0, 255, 255, 135},
                {0, 0, 255, 255, 136},
                {0, 0, 255, 255, 137},
                {0, 0, 255, 255, 138},
                {0, 0, 255, 255, 139},
                {0, 0, 255, 255, 140},
                {0, 0, 255, 255, 141},
                {0, 0, 255, 255, 142},
                {0, 0, 255, 255, 143},
                {0, 0, 255, 255, 144},
                {0, 0, 255, 255, 145},
                {0, 0, 255, 255, 146},
                {0, 0, 255, 255, 147},
                {0, 0, 255, 255, 148},
                {0, 0, 255, 255, 149},
                {0, 0, 255, 255, 150},
                {0, 0, 255, 255, 151},
                {0, 0, 255, 255, 152},
                {0, 0, 255, 255, 153},
                {0, 0, 255, 255, 154},
                {0, 0, 255, 255, 155},
                {0, 0, 255, 255, 156},
                {0, 0, 255, 255, 157},
                {0, 0, 255, 255, 158},
                {0, 0, 255, 255, 159},
                {0, 0, 255, 255, 160},
                {0, 0, 255, 255, 161},
                {0, 0, 255, 255, 162},
                {0, 0, 255, 255, 163},
                {0, 0, 255, 255, 164},
                {0, 0, 255, 255, 165},
                {0, 0, 255, 255, 166},
                {0, 0, 255, 255, 167},
                {0, 0, 255, 255, 168},
                {0, 0, 255, 255, 169},
                {0, 0, 255, 255, 170},
                {0, 0, 255, 255, 171},
                {0, 0, 255, 255, 172},
                {0, 0, 255, 255, 173},
                {0, 0, 255, 255, 174},
                {0, 0, 255, 255, 175},
                {0, 0, 255, 255, 176},
                {0, 0, 255, 255, 177},
                {0, 0, 255, 255, 178},
                {0, 0, 255, 255, 179},
                {0, 0, 255, 255, 180},
                {0, 0, 255, 255, 181},
                {0, 0, 255, 255, 182},
                {0, 0, 255, 255, 183},
                {0, 0, 255, 255, 184},
                {0, 0, 255, 255, 185},
                {0, 0, 255, 255, 186},
                {0, 0, 255, 255, 187},
                {0, 0, 255, 255, 188},
                {0, 0, 255, 255, 189},
                {0, 0, 255, 255, 190},
                {0, 0, 255, 255, 191},
                {0, 0, 255, 255, 192},
                {0, 0, 255, 255, 193},
                {0, 0, 255, 255, 194},
                {0, 0, 255, 255, 195},
                {0, 0, 255, 255, 196},
                {0, 0, 255, 255, 197},
                {0, 0, 255, 255, 198},
                {0, 0, 255, 255, 199},
                {0, 0, 255, 255, 200},
                {0, 0, 255, 255, 201},
                {0, 0, 255, 255, 202},
                {0, 0, 255, 255, 203},
                {0, 0, 255, 255, 204},
                {0, 0, 255, 255, 205},
                {0, 0, 255, 255, 206},
                {0, 0, 255, 255, 207},
                {0, 0, 255, 255, 208},
                {0, 0, 255, 255, 209},
                {0, 0, 255, 255, 210},
                {0, 0, 255, 255, 211},
                {0, 0, 255, 255, 212},
                {0, 0, 255, 255, 213},
                {0, 0, 255, 255, 214},
                {0, 0, 255, 255, 215},
                {0, 0, 255, 255, 216},
                {0, 0, 255, 255, 217},
                {0, 0, 255, 255, 218},
                {0, 0, 255, 255, 219},
                {0, 0, 255, 255, 220},
                {0, 0, 255, 255, 221},
                {0, 0, 255, 255, 222},
                {0, 0, 255, 255, 223},
                {0, 0, 255, 255, 224},
                {0, 0, 255, 255, 225},
                {0, 0, 255, 255, 226},
                {0, 0, 255, 255, 227},
                {0, 0, 255, 255, 228},
                {0, 0, 255, 255, 229},
                {0, 0, 255, 255, 230},
                {0, 0, 255, 255, 231},
                {0, 0, 255, 255, 232},
                {0, 0, 255, 255, 233},
                {0, 0, 255, 255, 234},
                {0, 0, 255, 255, 235},
                {0, 0, 255, 255, 236},
                {0, 0, 255, 255, 237},
                {0, 0, 255, 255, 238},
                {0, 0, 255, 255, 239},
                {0, 0, 255, 255, 240},
                {0, 0, 255, 255, 241},
                {0, 0, 255, 255, 242},
                {0, 0, 255, 255, 243},
                {0, 0, 255, 255, 244},
                {0, 0, 255, 255, 245},
                {0, 0, 255, 255, 246},
                {0, 0, 255, 255, 247},
                {0, 0, 255, 255, 248},
                {0, 0, 255, 255, 249},
                {0, 0, 255, 255, 250},
                {0, 0, 255, 255, 251},
                {0, 0, 255, 255, 252},
                {0, 0, 255, 255, 253},
                {0, 0, 255, 255, 254}
        };

        Image image = IOUtils.read("test_resources/input/random.png",
                ImageType.PNG);
        Image actual = ExtractUtility.createHistogram(image);

        for (int[] res : result) {

          int r = res[0];
          int g = res[1];
          int b = res[2];
          int x = res[3];
          int y = res[4];

          Pixel pixel = actual.getPixel(x, y);

          int actualR = pixel.getRed();
          int actualG = pixel.getGreen();
          int actualB = pixel.getBlue();

          assertEquals(r, actualR);
          assertEquals(g, actualG);
          assertEquals(b, actualB);
        }
      } catch (Exception e) {
        fail("Unexpected exception: " + e.getMessage());
      }
    }
  }
}
