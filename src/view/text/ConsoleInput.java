package view.text;


import java.util.Objects;

/**
 * Represents the console view where user interacts
 * with the program through console input.
 */
public class ConsoleInput implements TextInput {

  /**
   * The input stream from which the user is communicating.
   */
  private final Readable input;

  /**
   * Constructs a Console Input object with the given input stream.
   *
   * @param input  the input stream from which the user is communicating.
   * @throws NullPointerException if the output stream is null.
   */
  public ConsoleInput(Readable input) {
    Objects.requireNonNull(input, "Input cannot be null");
    this.input = input;
  }

  @Override
  public Readable getUserInput() {
    return this.input;
  }
}
