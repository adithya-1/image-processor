package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkHoursRegisterImpl implements WorkHoursRegister {
  private Map<String, Worker> workers;

  public WorkHoursRegisterImpl() {
    this.workers = new HashMap<>();
  }

  @Override
  public boolean addWorker(String workerId, String position, int compensation) {
    if (workers.containsKey(workerId)) {
      return false;
    }
    Worker worker = new Worker(workerId, position, compensation);
    workers.put(workerId, worker);
    return true;
  }

  @Override
  public String register(String workerId, int timestamp) {
    Worker worker = workers.get(workerId);
    if (worker == null) {
      return "invalid_request";
    }

    if (worker.isInOffice()) {
      worker.registerExit(timestamp);
    } else {
      worker.registerEntry(timestamp);
    }
    return "registered";
  }

  @Override
  public Optional<Integer> get(String workerId) {
    Worker worker = workers.get(workerId);
    if (worker == null) {
      return Optional.empty();
    }
    return Optional.of(worker.getTotalTimeInOffice());
  }

  @Override
  public List<String> topNWorkers(int n, String position) {
    List<Worker> filteredWorkers = new ArrayList<>();
    for (Worker worker : workers.values()) {
      // Check the worker's last known position that matches the queried position
      String lastMatchingPosition = worker.getLastKnownPositionMatching(position);
      if (lastMatchingPosition != null) {
        filteredWorkers.add(worker);
      }
    }

    filteredWorkers.sort((w1, w2) -> {
      int timeComparison = Integer.compare(
              w2.getPositionTime(position),
              w1.getPositionTime(position)
      );
      if (timeComparison == 0) {
        return w1.getWorkerId().compareTo(w2.getWorkerId());
      }
      return timeComparison;
    });

    List<String> result = new ArrayList<>();
    for (int i = 0; i < Math.min(n, filteredWorkers.size()); i++) {
      Worker worker = filteredWorkers.get(i);
      result.add(worker.getWorkerId() + "(" + worker.getPositionTime(position) + ")");
    }

    return result;
  }

  @Override
  public String promote(String workerId, String newPosition, int newCompensation, int startTimestamp) {
    Worker worker = workers.get(workerId);
    if (worker == null) {
      return "invalid_request";
    }

    return worker.promote(newPosition, newCompensation, startTimestamp);
  }

  @Override
  public Optional<Integer> calcSalary(String workerId, int startTimestamp, int endTimestamp) {
    Worker worker = workers.get(workerId);
    if (worker == null) {
      return Optional.empty();
    }

    return Optional.of(worker.calculateSalary(startTimestamp, endTimestamp));
  }
}
