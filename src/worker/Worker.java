package worker;

import java.util.ArrayList;
import java.util.List;



public class Worker {
  private String workerId;
  private String currentPosition;
  private int currentCompensation;
  private List<Integer> entryTimestamps;
  private List<Integer> exitTimestamps;
  private List<PositionChange> positionHistory;

  public Worker(String workerId, String position, int compensation) {
    this.workerId = workerId;
    this.currentPosition = position;
    this.currentCompensation = compensation;
    this.entryTimestamps = new ArrayList<>();
    this.exitTimestamps = new ArrayList<>();
    this.positionHistory = new ArrayList<>();
    this.positionHistory.add(new PositionChange(position, compensation, 0));
  }

  public void registerEntry(int timestamp) {
    entryTimestamps.add(timestamp);
  }

  public void registerExit(int timestamp) {
    exitTimestamps.add(timestamp);
  }

  public boolean isInOffice() {
    return entryTimestamps.size() > exitTimestamps.size();
  }

  public int getTotalTimeInOffice() {
    int totalTime = 0;
    for (int i = 0; i < Math.min(entryTimestamps.size(), exitTimestamps.size()); i++) {
      totalTime += exitTimestamps.get(i) - entryTimestamps.get(i);
    }
    return totalTime;
  }

  public String getPosition() {
    return currentPosition;
  }

  public String getWorkerId() {
    return workerId;
  }

  public List<Integer> getEntryTimestamps() {
    return entryTimestamps;
  }

  public List<Integer> getExitTimestamps() {
    return exitTimestamps;
  }

  public int getCompensation() {
    return currentCompensation;
  }

  public String getCurrentPosition() {
    return currentPosition;
  }

  public int getCurrentPositionTimeInOffice() {
    int totalTime = 0;
    PositionChange currentPositionInfo = positionHistory.get(positionHistory.size() - 1);

    for (int i = 0; i < Math.min(entryTimestamps.size(), exitTimestamps.size()); i++) {
      int entryTime = entryTimestamps.get(i);
      int exitTime = exitTimestamps.get(i);

      // If the session started before the current position's start,
      // only count time from the position start
      int effectiveEntryTime = Math.max(entryTime, currentPositionInfo.timestamp);

      // Only count time if the session is after or at the current position's start
      if (effectiveEntryTime < exitTime) {
        totalTime += exitTime - effectiveEntryTime;
      }
    }

    return totalTime;
  }

  public String promote(String newPosition, int newCompensation, int startTimestamp) {
    // Check if promotion is invalid (same position or premature promotion)
    if (currentPosition.equals(newPosition)) {
      return "invalid_request";
    }

    // Add new position change
    positionHistory.add(new PositionChange(newPosition, newCompensation, startTimestamp));

    // Update current position and compensation
    currentPosition = newPosition;
    currentCompensation = newCompensation;

    return "success";
  }

  public int calculateSalary(int startTimestamp, int endTimestamp) {
    int totalSalary = 0;

    for (int i = 0; i < Math.min(entryTimestamps.size(), exitTimestamps.size()); i++) {
      int entryTime = entryTimestamps.get(i);
      int exitTime = exitTimestamps.get(i);

      // Skip sessions outside the salary period
      if (entryTime >= endTimestamp || exitTime <= startTimestamp) {
        continue;
      }

      // Calculate effective time and compensation
      int effectiveStart = Math.max(entryTime, startTimestamp);
      int effectiveEnd = Math.min(exitTime, endTimestamp);
      int compensationForPeriod = findCompensationForTimestamp(effectiveStart);

      totalSalary += (effectiveEnd - effectiveStart) * compensationForPeriod;
    }

    return totalSalary;
  }

  private int findCompensationForTimestamp(int timestamp) {
    for (int i = positionHistory.size() - 1; i >= 0; i--) {
      PositionChange change = positionHistory.get(i);
      if (timestamp >= change.timestamp) {
        return change.compensation;
      }
    }
    // Fallback to initial compensation if no matching record
    return positionHistory.get(0).compensation;
  }

  public boolean wasInPosition(String position) {
    for (PositionChange positionChange : positionHistory) {
      if (positionChange.position.equals(position)) {
        return true;
      }
    }
    return false;
  }


  private static class PositionChange {
    String position;
    int compensation;
    int timestamp;

    PositionChange(String position, int compensation, int timestamp) {
      this.position = position;
      this.compensation = compensation;
      this.timestamp = timestamp;
    }
  }

  public String getLastKnownPositionMatching(String position) {
    for (int i = positionHistory.size() - 1; i >= 0; i--) {
      PositionChange change = positionHistory.get(i);
      if (change.position.equals(position)) {
        return change.position;
      }
    }
    return null;
  }

  public int getPositionTime(String position) {
    int totalTime = 0;

    for (int i = 0; i < Math.min(entryTimestamps.size(), exitTimestamps.size()); i++) {
      int entryTime = entryTimestamps.get(i);
      int exitTime = exitTimestamps.get(i);

      // Find the position at the time of this session
      String sessionPosition = findPositionAtTimestamp(entryTime);

      // If the session's position matches the queried position, add the time
      if (sessionPosition.equals(position)) {
        totalTime += exitTime - entryTime;
      }
    }

    return totalTime;
  }

  private String findPositionAtTimestamp(int timestamp) {
    for (int i = positionHistory.size() - 1; i >= 0; i--) {
      PositionChange change = positionHistory.get(i);
      if (timestamp >= change.timestamp) {
        return change.position;
      }
    }
    // Fallback to initial position
    return positionHistory.get(0).position;
  }
}