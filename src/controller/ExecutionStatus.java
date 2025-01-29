package controller;

import java.util.Objects;

/**
 * ExecutionStatus class that represents the status of the execution of a command.
 * It contains a boolean value to indicate the success of the execution and a message.
 */
public class ExecutionStatus {

  /**
   * Boolean value to indicate the success of the execution.
   */
  private final boolean success;
  /**
   * Message to indicate the status of the execution.
   */
  private final String message;

  /**
   * Constructs an ExecutionStatus object with the given success and message.
   *
   * @param success boolean value to indicate the success of the execution
   * @param message message to indicate the status of the execution
   * @throws NullPointerException if the message is null
   */
  public ExecutionStatus(boolean success, String message) {
    Objects.requireNonNull(message, "Message cannot be null");
    this.success = success;
    this.message = message;
  }

  /**
   * Get the success of the execution.
   *
   * @return the success of the execution
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Get the message after running the  execution.
   *
   * @return the after running the execution
   */
  public String getMessage() {
    return message;
  }

  /**
   * Check if the given object is equal to this ExecutionStatus object.
   * If the given object is an instance of ExecutionStatus, then it is equal to this object
   * if the success and message fields are equal.
   *
   * @param obj the object to compare
   * @return true if the given object is equal to this ExecutionStatus object, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ExecutionStatus)) {
      return false;
    }
    ExecutionStatus other = (ExecutionStatus) obj;
    return success == other.success && message.equals(other.message);
  }

  /**
   * Get the hash code of this ExecutionStatus object.
   *
   * @return the hash code of this ExecutionStatus object
   */
  @Override
  public int hashCode() {
    return Objects.hash(success, message);
  }
}
