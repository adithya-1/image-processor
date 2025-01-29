package worker;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * `WorkHoursRegister` interface.
 */
public interface WorkHoursRegister {

  /**
   * Should add the `workerId` to the system and save additional
   * information about them: their `position` and `compensation`.
   * If the `workerId` already exists, nothing happens and this
   * operation should return `false`.
   * If the `workerId` was successfully added, return `true`.
   * `workerId` and `position` are guaranteed to contain only
   * English letters and spaces.
   */
  default boolean addWorker(String workerId, String position, int compensation) {
    // default implementation
    return false;
  }

  /**
   * Should register the time when the `workerId` entered or left
   * the office.
   * The time is represented by the `timestamp`.
   * *Note* that `register` operation calls are given in the
   * increasing order of the `timestamp` parameter.
   * If the `workerId` doesn't exist within the system, nothing
   * happens and this operation should return
   * `"invalid_request"`.
   * If the `workerId` is not in the office, this operation
   * registers the time when the `workerId` entered the office.
   * If the `workerId` is already in the office, this operation
   * registers the time when the `workerId` left the office.
   * If the `workerId`'s entering or leaving time was
   * successfully registered, return `"registered"`.
   */
  default String register(String workerId, int timestamp) {
    // default implementation
    return "";
  }

  /**
   * Should return the total calculated amount of time that the
   * `workerId` spent in the office.
   * The amount of time is calculated using finished working
   * sessions only.
   * It means that if the worker has entered the office but
   * hasn't left yet, this visit is not considered in the
   * calculation.
   * If the `workerId` doesn't exist within the system, return
   * `Optional.empty()`.
   */
  default Optional<Integer> get(String workerId) {
    // default implementation
    return Optional.empty();
  }

  /**
   * Should return the list of strings representing ids of the
   * top `n` workers with the given `position` sorted in
   * descending order by the total time spent in the office.
   * The amount of time is calculated using finished working
   * sessions only.
   * In the case of a tie, workers must be sorted in
   * [alphabetical](keyword://alphabetical-order-for-strings)
   * order of their ids.
   * The returned list should be in the following format: `["work
   * erId1(timeSpentInOffice1)", "workerId2(timeSpentInOffice2)",
   *  ..., "workerIdN(timeSpentInOfficeN)"]`.
   * If less than `n` workers with the given `position` exist
   * within the system, then return all ids in the described
   * format.
   * If there are no workers with the given `position` within the
   * system, return an empty list.
   * *Note* that if a worker exists within the system and doesn't
   * have any finished periods of being in the office, their time
   * spent in the office is considered to be `0`.
   */
  default List<String> topNWorkers(int n, String position) {
    // default implementation
    return Collections.emptyList();
  }

  /**
   * Should register a new position and new compensation for the
   * `workerId`.
   * `newPosition` is guaranteed to be different from the current
   * worker's position.
   * New position and new compensation are active from the moment
   * of the first entering the office after or at
   * `startTimestamp`.
   * In other words, the first time period of being in office for
   * the `newPosition` is the first time period that _starts_
   * after or at `startTimestamp`.
   * `startTimestamp` is guaranteed to be _greater_ than
   * `timestamp` parameter of the last `register` call for any
   * worker.
   * If the `promote` operation is called _repeatedly_ for the
   * same `workerId` before they **entered** the office with the
   * `newPosition`, nothing happens, and this operation should
   * return `"invalid_request"`.
   * If `workerId` doesn't exist within the system, nothing
   * happens, and this operation should return
   * `"invalid_request"`.
   * If the worker's promotion was successfully registered,
   * return `"success"`.
   *
   *   **Note**: `topNWorkers` operation should take only the
   *   worker's current position into account.
   *   `get` operation should return the total amount of time
   *   across all the worker's past and current positions.
   */
  default String promote(String workerId, String newPosition, int newCompensation, int startTimestamp) {
    // default implementation
    return "";
  }

  /**
   * Should calculate net salary that `workerId` has earned for
   * the time period between `startTimestamp` and `endTimestamp`.
   * No restrictions are applied to `startTimestamp` and
   * `endTimestamp`, except that it is guaranteed that
   * `endTimestamp > startTimestamp >= 0`.
   * *Note* that workers are only paid for the time they were
   * present in the office.
   * The amount of time is calculated using finished working
   * sessions only.
   * For any finished working session
   * `[sessionStartTimestamp, sessionEndTimestamp]` salary is
   * calculated as `salary = (sessionEndTimestamp -
   *  sessionStartTimestamp) * compensationDuringPeriod`.
   * Note, that `compensationDuringPeriod` may differ for
   * different periods, because workers may be promoted.
   * If `workerId` doesn't exist within the system, nothing
   * happens and this operation should return `Optional.empty()`
   */
  default Optional<Integer> calcSalary(String workerId, int startTimestamp, int endTimestamp) {
    // default implementation
    return Optional.empty();
  }
}
