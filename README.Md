# Image Processor Application

## Setup
### Prerequisites
- Java version 11.0.24

## Steps to Run The Program
- Clone the repository.
- Open the project in an IDE.
- ### Using IntelliJ IDEA
  - run `src/app/ImageProcessorApp.java`
  - This will open up the GUI where you can interact with the application.Buttons are towards the left of panel with clear names. If facing difficulty in understanding the buttons, refer to [USEME.md](USEME.md).
  - To open the Interactive mode using console, run `src/app/ImageProcessorApp.java` with the argument `-text`.
  - To just run a scrip file, run `src/app/ImageProcessorApp.java` with the argument `-file <path-of-script-file>`.
  - In running the script file if you face any issues make sure you are opening the project in the root directory or give the complete path to the script file.
- ### Running the application from JAR file
  - Open the terminal and navigate to the project directory.
  - The jar file is present in res/ folder.
  - Run the following command to execute the jar file:
    - `java -jar res/assignment6.jar`
    - This will open up the GUI mode similar to GUI mode in IntelliJ IDEA.
  - If you need to run the application in Interactive mode using console:
    - `java -jar res/assignment6.jar -text`
  - If you need to directly execute the script file:
    - `java -jar res/assignment6.jar -file res/sample_script.txt`, where `res/sample_script.txt` is the path to the script file.
    - If you face any errors make sure you are in the project directory and not in res folder.
- Output images from sample script given will be saved in `res/` folder.
- Script image file is in `res/sample_input.png` 
- To view test input files for test go to `test_resources/input/`
- To test for a better image use `test_resources/input/dubai.png`

## Design changes done in the project
### Below changes explain the design changes done from Assignment 4 to Assignment 5.
- We didn't need to do much core design changes since the previous assignment due to MVC architecture, we could easily add new features and functionalities.
- Added an additional package `compressors` to implement different compression techniques.
- We added the newer features onto the Image interface due to the fact that this was being used throughout the program and extending to a new interface(for instance ImageWithAdditionalFeatures) would lead us using the new interface everywhere in the program. This would lead us to change existing code in other places(such as ImageMemory) even though the features added had no relation to these classes. So adding additional methods to the controller, service and model classes was the best approach.
- The communication between the controller and the service was now changed from function arguments to `ImageProcessingRequest` object built on Builder pattern. This was done to provide easier way to add new command line arguments in the future without having to change the function signature everywhere. 
- The services was a separate package before. This was moved inside controller after discussing with the professor to help with understanding that services is an extension to controller(MVCS architecture) that handles storing images.
- There were few methods like filter operations which were called from service directly using filter utils, this led to a question on which methods goes to model and which method gets called directly from service which was an unnecessary decision the service had to make. It made sense to move all image operations(other than load/save) to the image model since it was responsible for handling the image data, hence replaced calling filters directly but through the model. 
- A new controller was added to handle and understand the command line arguments by simply extending the existing(InteractiveImageProcessorController) controller and changing the processCommand to help with the processing command line input. 

### Below changes explain the design changes done from Assignment 5 to Assignment 6.
- The main function was handling parsing of arguments and creating a controller before. This responsibility is now delegated to ArgumentParser interface which takes in the arguments and initializes the controller.
- We did not change the existing controller to handle GUI but added a new controller. Similarly we did not have to change the existing views but created a new view for GUI.
- The controller is done differently because the GUI controller acts as a call back to the GUI view(GUIInput, GUIOutput) and making the existing controller(Interactive controller) work with it would meant having GUI view return inputs from GUI the way existing controller understands which meant unnecessary responsibility for the GUI view.
- The GUI view was created differently from TextView due to different functions and responsibilities which is evident from the functions exposed in the interfaces.
- No changes were done from the service layer till model layer since the design followed the MVCS architecture where new view was added easily without changing the core logic.

## Image Citation
- Testing image for script `res/sample_input.png` was taken from [dummyimage.com](https://dummyimage.com/)
- Testing image `test_resources/input/dubai.png` is our own photo and is authorized to be used in this project.
- Testing images `test_resources/input/random.jpeg`,`test_resources/input/random.jpg`,`test_resources/input/random.png`,`test_resources/input/random.ppm` are generated from our own scripts and are authorized to be used in this project.
- Images in res/ folder are generated from our own Image Processor Application and are authorized to be used for evaluation.
- Class diagrams are generated using IntelliJ IDEA and are authorized to be used for evaluation.

## Note: All parts of the project are implemented and tested. Below is the detailed explanation of each part of the project.

## Packages and MVC Structure
### `app` Package
- **Class**: `ImageProcessorApp`
    - **Purpose**: The main class for the Image Processor application. This class is responsible for creating the controller and running the application.
- **Packages**:
  - `parsers`
    - **Interfaces**:
        - `ArgumentParser`
            - **Purpose**: Interface for argument parser. This interface is responsible for parsing the arguments and creating the controller.
    - **Classes**:
        - `AbstractArgumentParser`
            - **Purpose**: Abstract class that provides common functionality for argument parsers.
        - `CommandLineArgumentParser`
            - **Purpose**: Concrete class that implements the `ArgumentParser` interface and process inputs required for `CommandLineImageProcessorController`.
        - `GUIArgumentParser`
            - **Purpose**: Concrete class that implements the `ArgumentParser` interface and provides inputs required for `GUIImageProcessorController.java`.
        - `InteractiveArgumentParser.java`
            - **Purpose**: Concrete class that implements the `ArgumentParser` interface and provides inputs required for `InteractiveImageProcessorController.java`.

### `compressors` Package
- **Interfaces**:
    - `Compression`
        - **Purpose**: Interface for image compressors. Provides a method to compress an image.
- **Classes**:
- `HaarCompression`
    - **Purpose**: Concrete class that implements the `Compression` interface and provides the implementation for compressing an image using the HAAR compression.

### `controller` Package
- **Interfaces**:
    - `ImageProcessorController`
        - **Purpose**: The controller interface for the image processor. This is responsible for handling the image module and communication with the view.
    - `Features`
        - **Purpose**: Interface for additional features that can be added to the controller. Also used as a callback for GUI output. 
- **Packages**
  - `services`
    - **Interfaces**:
        - `ImageProcessingService`
            - **Purpose**: Interface for image processing services. Service class to help with all image processing operations.
      - **Classes**:
          - `FileImageProcessingService.java`
              - **Purpose**: FileImageProcessingService class that implements the `ImageProcessingService` interface and provides the implementation for the methods to process images. It uses the ImageMemory object to store and retrieve images.
- **Classes**:
    - `InteractiveImageProcessorController`
        - **Purpose**: This class Implements the `ImageProcessorController` interface and processes the commands entered by the user. It provides a way for user to interact with the program.
    - `GUIImageProcessorController`
        - **Purpose**: This class Implements the `ImageProcessorController` interface and processes the commands entered by the user. It provides a way for user to interact with the program using GUI and also implements the `Features` interface to provide additional features.
    - `CommandLineImageProcessorController.java`
        - **Purpose**: This class Implements the `ImageProcessorController` interface and processes the commands entered by the user. It is used to understand the arguments provided through command line.
    - `ExecutionStatus`
        - **Purpose**: ExecutionStatus class that represents the status of the execution of a command. It contains a boolean value to indicate the success of the execution and a message.
    - `ControllerType`
        - **Purpose**: Enum representing the different types of controllers available.

### `exception` Package
- **Classes**:
    - `ImageProcessingRunTimeException`
        - **Purpose**: A generic runtime exception that is used across the image processing application.
        - **Subclasses**:
          - DisplayException
              - **Purpose**: An exception that is thrown when there is an error displaying a message.
          - QuitException
            - **Purpose**: An exception that is thrown when the user wants to quit the application.
    - `ImageProcessorException`
      - **Purpose**: A generic exception that is thrown across the image processing application when there is an error processing an image.
      - **Subclasses**:
          - NotFoundException
              - **Purpose**: An exception that is thrown when an image is not found in the memory.
          - NotImplementedException
              - **Purpose**: An exception that is thrown when a method is not implemented.


### `factories` Package
- **Classes**:
    - `Factory`
      - **Purpose**: Factory class to create objects for the Image Processor application.

### `model` Package
- **Enumerable**:
  - `CompressionType`
    - **Purpose**: Enum representing the different types of compressions available.`
  - `FilterOption`
    - **Purpose**: Enum representing the different filter options available.
  - `ImageType`
      - **Purpose**: Enum that represents different types of images.
  - `LinearColorTransformationType`
    - **Purpose**: Represents the type of linear transformation that can be applied to an image.
  - `PixelType`
      - **Purpose**: Enum that represents different types of pixels.
  - `UserCommand`
      - **Purpose**: Enum representing the user commands. These are possible commands that the user can give to the program.
- #### `memory` Package
    - **Interfaces**:
        - `ImageMemory`
            - **Purpose**: A class that represents a memory that stores images. The memory can store images and retrieve them by their name. Memory is stored depending on the implementation.
    - **Classes**:
        - `HashMapImageMemory`
            - **Purpose**: Concrete class that implements the `ImageMemory` interface using a HashMap to store images.
- #### `pixels` Package
    - **Interfaces**:
        - `Pixel`
            - **Purpose**: Interface for to represent a Pixel.
    - **Classes**:
        - `AbstractPixel`
            - **Purpose**: Abstract class that provides common functionality for all pixel classes.
        - `RGB`
            - **Purpose**: Concrete class that represents an RGB pixel. 
- ### `request` Package
  - **Classes**:
      - `ImageProcessingRequest`
          - **Purpose**: Represents a request to process an image. It contains the image name and the parameters for the command.
- #### `visual` Package
    - **Interfaces**:
        - `Image`
            - **Purpose**: Interface to represent an Image.
    - **Classes**:
        - `RenderedImage`
            - **Purpose**: Concrete class that represents a rendered image. An image is a sequence of pixels.


### `utility` Package
- **Classes**:
    - `FilterUtils`
        - **Purpose**: Utility class for applying filters to an image. Provides a method to apply a filter to an image.
    - `IOUtils`
        - **Purpose**: Utility class for IO operations.
    - `StringUtils`
        - **Purpose**: Utility class for String operations.
    - `ExtractionUtils`
        - **Purpose**: Utility class for extracting information from image.

### `view` Package
- #### `text` Package
    - **Interfaces**:
        - `TextInput`
            - **Purpose**: Interface to get user input as text based. This input can be from any source like console, file etc.
        - `TextOutput`
            - **Purpose**: Interface for text output. Represents the output to the user on console or file.
    - **Classes**:
        - `ConsoleInput`
            - **Purpose**: Concrete class that implements the `TextInput` interface and provides console-based user input.
        - `ConsoleOutput`
            - **Purpose**: Concrete class that implements the `TextOutput` interface and provides console-based user output.
- #### `gui` Package
    - **Interfaces**:
        - `GUIInput`
            - **Purpose**: Interface to get user input from GUI.
        - `GUIOutput`
            - **Purpose**: Interface to display output to user from GUI.
    - **Classes**:
        - `SwingInput`
            - **Purpose**: Implements the GUIInput. Input from the user is taken using Java Swing library.
        - `SwingOutput`
            - **Purpose**: Implements the GUIOutput. Output to the user is displayed using the Swing library.
- #### `components` Package
   -  **Classes**
     - `FeatureComponent`
       - **Purpose**: A class that represents a feature component. This class is used to create a feature component that can be added to GUI to handle call backs and map the buttons to corresponding call backs.
- **Interface**:
    - `UserOutput`
      - **Purpose**: Interface for user output. Has common outputs implemented by `TextOutput` and `GUIOutput`.
- **Enumerable**:
   - `DisplayMessageType`
     - **Purpose**: Enum representing the different types of messages info or error that can be displayed to the user.

## MVC Architecture

This project follows the Model-View-Controller (MVC) architecture, which helps in organizing the codebase into three main components:

1. **Model**:
- **Purpose**: Represents the data and the business logic of the application.
- **Packages**:
    - `model/memory`: Manages image memory through interfaces like `ImageMemory` and classes like `HashMapImageMemory`.
    - `model/pixels`: Contains interfaces and classes for pixel representation, such as `Pixel`, `AbstractPixel`, and `RGB`.
    - `model/visual`: Contains interfaces and classes for image representation, such as `Image` and `RenderedImage`.
2. **View**:
- **Purpose**: Handles the presentation layer and user interface.
- **Packages**:
    - `view.gui`: Contains interfaces and classes for getting inputs and displaying outputs to user from GUI.
    - `view.text`: Contains interfaces and classes for getting inputs and displaying outputs to user from text bases systems like console, file.

3. **Controller**:
- **Purpose**: Manages the flow of the application, processes user commands, and updates the model and view.
- **Packages**:
    - `controller`: Contains interfaces and classes for controlling the application, such as `ImageProcessorController`, `SimpleImageProcessorController`, and `ExecutionStatus`.
    - `controller/services`: Contains interfaces and classes for image processing services, such as `ImageProcessingService` and `SimpleImageProcessingService`.

4. **Class Diagram**:
- The raw class diagram just has classes without dependencies for cleaner view.
  - location: res/raw_class_diagram.png
- The dependencies class diagram has classes with dependencies to understand how the dependencies are between classes.
  - location: res/raw_class_diagram_with_dependencies.png
- The complete class diagram has classes,dependencies and methods to see the entire structure of the project.
  - location: res/complete_class_diagram.png
- This was done to help better understand the structure of the project and how the classes are related to each other.

5. **Extra Credit**:
- We implemented 1 Extra Credit Image Downscaling. Since this was a new image processing feature we added a new method in the Image.java, ImageProcessingService.java and Features.java.  
- We added this newer feature onto the Image interface due to the fact that this was being used throughout the program and extending to a new interface(for instance ImageWithAdditionalFeatures) would lead us using the new interface everywhere in the program. This would lead us to change existing code in other places(such as ImageMemory) even though the features added had no relation to these classes. So adding additional methods to the controller, service and model classes was the best approach.
- Downscaling feature was done using the algorithm given in the assignment description. Refer 3.1 of: https://northeastern.instructure.com/courses/192553/assignments/2493533
- This feature is tested in the GUIController test and is implemented only by GUIController.
- Output of this image is in res/ folder
- `res/sample_input.png` is the input image for this feature.
- `res/downscaled_40_height_100_width.png` represents height at 40% and width at 100%.
- `res/downscaled_60_height_60_width.png` represents height at 60% and width at 60%.
- `res/downscaled_80_height_40_width.png` represents height at 80% and width at 40%.