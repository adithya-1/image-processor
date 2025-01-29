import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import worker.WorkHoursRegister;
import worker.WorkHoursRegisterImpl;

/**
 * The test class below includes 10 tests for Level 3.
 *
 * All have the same score.
 * You are not allowed to modify this file,
 * but feel free to read the source code
 * to better understand what is happening in every specific case.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Level3Tests {

  private WorkHoursRegister whr;

  @BeforeEach
  void setUp() {
    whr = new WorkHoursRegisterImpl();
  }

  @Test
  @Order(1)
  void testLevel3Case01SimplePromotions() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("John", "Junior Developer", 100));
      Assertions.assertFalse(whr.addWorker("John", "Junior Developer", 150));
      Assertions.assertTrue(whr.addWorker("Ashley", "Junior Developer", 100));
      Assertions.assertEquals("success", whr.promote("John", "Middle Developer", 250, 10));
      Assertions.assertEquals("invalid_request", whr.promote("John", "Middle Developer", 200, 15));
      Assertions.assertEquals("invalid_request", whr.promote("Bob", "Middle Developer", 200, 15));
      Assertions.assertEquals("success", whr.promote("Ashley", "Middle Developer", 250, 5));
    });
  }

  @Test
  @Order(2)
  void testLevel2Case02SimpleCalcSalary() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("John", "Junior Developer", 1000));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("John", 0, 50));
      Assertions.assertEquals("registered", whr.register("John", 10));
      Assertions.assertEquals("registered", whr.register("John", 20));
      Assertions.assertEquals(Optional.of(10000), whr.calcSalary("John", 0, 50));
    });
  }

  @Test
  @Order(3)
  void testLevel3Case03SimplePromotionsGet() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("John", "Middle Developer", 200));
      Assertions.assertEquals("registered", whr.register("John", 100));
      Assertions.assertEquals("registered", whr.register("John", 125));
      Assertions.assertEquals("success", whr.promote("John", "Senior Developer", 400, 200));
      Assertions.assertEquals("registered", whr.register("John", 150));
      Assertions.assertEquals(Optional.of(25), whr.get("John"));
      Assertions.assertEquals("registered", whr.register("John", 300));
      Assertions.assertEquals(Optional.of(175), whr.get("John"));
      Assertions.assertEquals("registered", whr.register("John", 325));
      Assertions.assertEquals("registered", whr.register("John", 400));
      Assertions.assertEquals(Optional.of(250), whr.get("John"));
    });
  }

  @Test
  @Order(4)
  void testLevel3Case04SimplePromotionsTopN() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(2000000), () -> {
      Assertions.assertTrue(whr.addWorker("John", "Junior Developer", 100));
      Assertions.assertTrue(whr.addWorker("Ashley", "Junior Developer", 100));
      Assertions.assertEquals("registered", whr.register("John", 150));
      Assertions.assertEquals("registered", whr.register("Ashley", 160));
      Assertions.assertEquals("registered", whr.register("John", 200));
      Assertions.assertEquals("registered", whr.register("Ashley", 220));
      List<String> expected = new ArrayList<>(List.of("Ashley(60)", "John(50)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Junior Developer"));
      Assertions.assertEquals("success", whr.promote("John", "Middle Developer", 200, 500));
      Assertions.assertEquals("registered", whr.register("Ashley", 300));
      Assertions.assertEquals("registered", whr.register("John", 310));
      expected = new ArrayList<>(List.of("Ashley(60)", "John(50)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Junior Developer"));
      Assertions.assertTrue(whr.topNWorkers(10, "Middle Developer").isEmpty());
      Assertions.assertEquals("registered", whr.register("Ashley", 500));
      Assertions.assertEquals("registered", whr.register("John", 510));
      expected = new ArrayList<>(List.of("Ashley(260)", "John(250)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Junior Developer"));
      Assertions.assertTrue(whr.topNWorkers(10, "Middle Developer").isEmpty());
      Assertions.assertEquals("registered", whr.register("John", 600));
      Assertions.assertEquals("registered", whr.register("John", 720));
      expected = new ArrayList<>(List.of("Ashley(260)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Junior Developer"));
      expected = new ArrayList<>(List.of("John(120)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Middle Developer"));
      Assertions.assertEquals("success", whr.promote("John", "Senior Developer", 320, 800));
      Assertions.assertEquals("registered", whr.register("John", 800));
      expected = new ArrayList<>(List.of("Ashley(260)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Junior Developer"));
      Assertions.assertTrue(whr.topNWorkers(10, "Middle Developer").isEmpty());
      expected = new ArrayList<>(List.of("John(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Senior Developer"));
      Assertions.assertEquals("registered", whr.register("John", 877));
      Assertions.assertTrue(whr.topNWorkers(10, "Middle Developer").isEmpty());
      expected = new ArrayList<>(List.of("John(77)"));
      Assertions.assertEquals(expected, whr.topNWorkers(10, "Senior Developer"));
    });
  }

  @Test
  @Order(5)
  void testLevel3Case05CalcSalaryNoPromotionsSimpleIntersections() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("Olivia", "Administrator", 90));
      Assertions.assertTrue(whr.addWorker("Walter", "Developer", 130));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Olivia", 0, 500));
      Assertions.assertTrue(whr.calcSalary("Bob", 200, 450).isEmpty());
      Assertions.assertEquals("registered", whr.register("Olivia", 100));
      Assertions.assertEquals("registered", whr.register("Olivia", 200));
      Assertions.assertEquals("registered", whr.register("Walter", 230));
      Assertions.assertEquals("registered", whr.register("Walter", 280));
      Assertions.assertEquals(Optional.of(9000), whr.calcSalary("Olivia", 0, 500));
      Assertions.assertEquals(Optional.of(6500), whr.calcSalary("Walter", 0, 500));
      Assertions.assertEquals(Optional.of(9000), whr.calcSalary("Olivia", 100, 200));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Olivia", 900, 1500));
      Assertions.assertEquals(Optional.of(4500), whr.calcSalary("Olivia", 50, 150));
      Assertions.assertEquals(Optional.of(130), whr.calcSalary("Walter", 250, 251));
      Assertions.assertEquals("registered", whr.register("Olivia", 300));
      Assertions.assertEquals("registered", whr.register("Olivia", 400));
      Assertions.assertEquals("registered", whr.register("Olivia", 560));
      Assertions.assertEquals("registered", whr.register("Olivia", 617));
      Assertions.assertEquals(Optional.of(18000), whr.calcSalary("Olivia", 0, 500));
      Assertions.assertEquals(Optional.of(23130), whr.calcSalary("Olivia", 0, 1000));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Walter", 350, 600));
      Assertions.assertEquals(Optional.of(19620), whr.calcSalary("Olivia", 111, 589));
    });
  }

  @Test
  @Order(6)
  void testLevel3Case06CalcSalaryWithPromotions() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("Olivia", "Manager", 230));
      Assertions.assertTrue(whr.addWorker("Bob", "Middle ML Engineer", 192));
      Assertions.assertEquals("registered", whr.register("Olivia", 50));
      Assertions.assertEquals("registered", whr.register("Olivia", 150));
      Assertions.assertEquals("registered", whr.register("Olivia", 300));
      Assertions.assertEquals("success", whr.promote("Olivia", "Team Lead", 320, 350));
      Assertions.assertEquals("registered", whr.register("Olivia", 380));
      Assertions.assertEquals("registered", whr.register("Olivia", 430));
      Assertions.assertEquals("registered", whr.register("Olivia", 500));
      Assertions.assertEquals(Optional.of(63800), whr.calcSalary("Olivia", 0, 600));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Olivia", 0, 30));
      Assertions.assertEquals(Optional.of(16100), whr.calcSalary("Olivia", 80, 220));
      Assertions.assertEquals(Optional.of(16500), whr.calcSalary("Olivia", 350, 460));
      Assertions.assertEquals(Optional.of(40800), whr.calcSalary("Olivia", 290, 900));
      Assertions.assertEquals("success", whr.promote("Bob", "Senior ML Engineer", 510, 600));
      Assertions.assertEquals("registered", whr.register("Bob", 620));
      Assertions.assertEquals("registered", whr.register("Bob", 680));
      Assertions.assertEquals("registered", whr.register("Bob", 800));
      Assertions.assertEquals("registered", whr.register("Bob", 915));
      Assertions.assertTrue(whr.calcSalary("Team Lead", 0, 1000).isEmpty());
      Assertions.assertTrue(whr.calcSalary("Manager", 0, 1000).isEmpty());
      Assertions.assertTrue(whr.calcSalary("Alice", 0, 1000).isEmpty());
      Assertions.assertEquals(Optional.of(63800), whr.calcSalary("Olivia", 0, 1000));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Bob", 0, 500));
      Assertions.assertEquals(Optional.of(89250), whr.calcSalary("Bob", 600, 1000));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Bob", 0, 500));
    });
  }

  @Test
  @Order(7)
  void testLevel3Case07PromotionsRegisterSameTimestamp() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("John", "QA Engineer", 73));
      Assertions.assertEquals("registered", whr.register("John", 120));
      Assertions.assertEquals("registered", whr.register("John", 200));
      Assertions.assertEquals(Optional.of(5840), whr.calcSalary("John", 0, 2000));
      Assertions.assertEquals("success", whr.promote("John", "Middle Developer", 119, 325));
      Assertions.assertEquals("registered", whr.register("John", 325));
      Assertions.assertEquals("registered", whr.register("John", 500));
      Assertions.assertEquals("registered", whr.register("John", 600));
      Assertions.assertEquals("registered", whr.register("John", 672));
      Assertions.assertEquals(Optional.of(35233), whr.calcSalary("John", 0, 2000));
      Assertions.assertEquals("registered", whr.register("John", 713));
      Assertions.assertEquals("success", whr.promote("John", "Senior Developer", 187, 809));
      Assertions.assertEquals(Optional.of(35233), whr.calcSalary("John", 0, 2000));
      Assertions.assertEquals("registered", whr.register("John", 809));
      Assertions.assertEquals(Optional.of(46657), whr.calcSalary("John", 0, 2000));
      Assertions.assertEquals("registered", whr.register("John", 962));
      Assertions.assertEquals("registered", whr.register("John", 1044));
      Assertions.assertEquals(Optional.of(61991), whr.calcSalary("John", 0, 2000));
    });
  }

  @Test
  @Order(8)
  void testLevel3Case08PromotionsOverwritingsAndSameParams() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Junior Developer", 100, 100));
      Assertions.assertTrue(whr.addWorker("Walter", "Developer Assistant", 72));
      Assertions.assertEquals("registered", whr.register("Walter", 129));
      Assertions.assertEquals("registered", whr.register("Walter", 221));
      Assertions.assertEquals(Optional.of(92), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(6624), whr.calcSalary("Walter", 0, 1000));
      List<String> expected = new ArrayList<>(List.of("Walter(92)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Developer Assistant"));
      Assertions.assertEquals("success", whr.promote("Walter", "Junior Developer", 119, 300));
      Assertions.assertEquals("registered", whr.register("Walter", 245));
      Assertions.assertEquals(Optional.of(92), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(6624), whr.calcSalary("Walter", 0, 1000));
      expected = new ArrayList<>(List.of("Walter(92)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Developer Assistant"));
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Junior Developer", 128, 300));
      Assertions.assertTrue(whr.topNWorkers(5, "Junior Developer").isEmpty());
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Junior Developer", 107, 300));
      Assertions.assertEquals("registered", whr.register("Walter", 305));
      Assertions.assertEquals("registered", whr.register("Walter", 322));
      Assertions.assertEquals(Optional.of(152), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(10944), whr.calcSalary("Walter", 0, 1000));
      expected = new ArrayList<>(List.of("Walter(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Junior Developer"));
      Assertions.assertEquals("success", whr.promote("Walter", "Middle Deveolper", 155, 405));
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Middle Developre", 155, 405));
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Middel Developer", 155, 405));
      expected = new ArrayList<>(List.of("Walter(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Junior Developer"));
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Middle Developer", 155, 405));
      Assertions.assertEquals("registered", whr.register("Walter", 354));
      Assertions.assertEquals("registered", whr.register("Walter", 401));
      Assertions.assertEquals("registered", whr.register("Walter", 407));
      Assertions.assertEquals(Optional.of(190), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(15466), whr.calcSalary("Walter", 0, 1000));
      Assertions.assertTrue(whr.topNWorkers(5, "Middle Developer").isEmpty());
      Assertions.assertEquals("registered", whr.register("Walter", 421));
      Assertions.assertEquals("registered", whr.register("Walter", 430));
      Assertions.assertEquals(Optional.of(199), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(16861), whr.calcSalary("Walter", 0, 1000));
      Assertions.assertTrue(whr.topNWorkers(5, "Middle Developer").isEmpty());
      Assertions.assertEquals("success", whr.promote("Walter", "Senior Developer", 208, 500));
      Assertions.assertEquals("registered", whr.register("Walter", 451));
      Assertions.assertEquals("registered", whr.register("Walter", 505));
      Assertions.assertEquals(Optional.of(253), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(25231), whr.calcSalary("Walter", 0, 1000));
      Assertions.assertTrue(whr.topNWorkers(5, "Senior Developer").isEmpty());
      Assertions.assertEquals("invalid_request", whr.promote("Walter", "Senior Developer", 213, 700));
      Assertions.assertEquals("registered", whr.register("Walter", 558));
      Assertions.assertEquals("registered", whr.register("Walter", 622));
      Assertions.assertEquals(Optional.of(317), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(38543), whr.calcSalary("Walter", 0, 1000));
      expected = new ArrayList<>(List.of("Walter(64)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Senior Developer"));
      Assertions.assertEquals("registered", whr.register("Walter", 701));
      Assertions.assertEquals("registered", whr.register("Walter", 703));
      Assertions.assertEquals(Optional.of(319), whr.get("Walter"));
      Assertions.assertEquals(Optional.of(38959), whr.calcSalary("Walter", 0, 1000));
      expected = new ArrayList<>(List.of("Walter(66)"));
      Assertions.assertEquals(expected, whr.topNWorkers(5, "Senior Developer"));
    });
  }

  @Test
  @Order(9)
  void testLevel3Case09CalcSalaryRegisterAndPromoteIntersections() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertTrue(whr.addWorker("Alice", "p", 37));
      Assertions.assertEquals("registered", whr.register("Alice", 53));
      Assertions.assertEquals("registered", whr.register("Alice", 79));
      Assertions.assertEquals("registered", whr.register("Alice", 105));
      Assertions.assertEquals("success", whr.promote("Alice", "j", 45, 200));
      Assertions.assertEquals("registered", whr.register("Alice", 134));
      Assertions.assertEquals("registered", whr.register("Alice", 181));
      Assertions.assertEquals("registered", whr.register("Alice", 202));
      Assertions.assertEquals("registered", whr.register("Alice", 221));
      Assertions.assertEquals("registered", whr.register("Alice", 250));
      Assertions.assertEquals("success", whr.promote("Alice", "k", 68, 260));
      Assertions.assertEquals("registered", whr.register("Alice", 259));
      Assertions.assertEquals("registered", whr.register("Alice", 260));
      Assertions.assertEquals("registered", whr.register("Alice", 267));
      Assertions.assertEquals("registered", whr.register("Alice", 300));
      Assertions.assertEquals("success", whr.promote("Alice", "t", 81, 315));
      Assertions.assertEquals("registered", whr.register("Alice", 315));
      Assertions.assertEquals("registered", whr.register("Alice", 359));
      Assertions.assertEquals(Optional.of(777), whr.calcSalary("Alice", 181, 202));
      Assertions.assertEquals(Optional.of(333), whr.calcSalary("Alice", 150, 190));
      Assertions.assertEquals(Optional.of(1305), whr.calcSalary("Alice", 215, 250));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Alice", 204, 219));
      Assertions.assertEquals(Optional.of(2244), whr.calcSalary("Alice", 260, 300));
      Assertions.assertEquals(Optional.of(544), whr.calcSalary("Alice", 260, 275));
      Assertions.assertEquals(Optional.of(2289), whr.calcSalary("Alice", 255, 315));
      Assertions.assertEquals(Optional.of(5319), whr.calcSalary("Alice", 64, 290));
      Assertions.assertEquals(Optional.of(6060), whr.calcSalary("Alice", 119, 329));
      Assertions.assertEquals(Optional.of(1424), whr.calcSalary("Alice", 200, 260));
      Assertions.assertEquals(Optional.of(9970), whr.calcSalary("Alice", 0, 1000));
    });
  }

  @Test
  @Order(10)
  void testLevel3Case10RandomlyOrderedOperations() {
    Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
      Assertions.assertEquals("invalid_request", whr.promote("Alice", "Middle Developer", 134, 200));
      Assertions.assertTrue(whr.addWorker("Bob", "Junior Developer", 90));
      Assertions.assertTrue(whr.addWorker("Alice", "Junior Developer", 99));
      Assertions.assertEquals("registered", whr.register("Bob", 100));
      Assertions.assertEquals("registered", whr.register("Bob", 129));
      Assertions.assertEquals(Optional.of(1530), whr.calcSalary("Bob", 50, 117));
      Assertions.assertEquals(Optional.of(29), whr.get("Bob"));
      List<String> expected = new ArrayList<>(List.of("Bob(29)", "Alice(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Junior Developer"));
      Assertions.assertEquals("registered", whr.register("Alice", 137));
      Assertions.assertEquals("registered", whr.register("Alice", 151));
      Assertions.assertEquals("registered", whr.register("Alice", 180));
      Assertions.assertEquals("registered", whr.register("Bob", 204));
      expected = new ArrayList<>(List.of("Bob(29)", "Alice(14)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Junior Developer"));
      Assertions.assertTrue(whr.topNWorkers(3, "Middle Developer").isEmpty());
      Assertions.assertEquals(Optional.of(1530), whr.calcSalary("Bob", 50, 117));
      Assertions.assertEquals(Optional.of(29), whr.get("Bob"));
      Assertions.assertEquals(Optional.of(0), whr.calcSalary("Alice", 50, 117));
      Assertions.assertEquals(Optional.of(14), whr.get("Alice"));
      Assertions.assertEquals("success", whr.promote("Alice", "Middle Developer", 134, 210));
      expected = new ArrayList<>(List.of("Bob(29)", "Alice(14)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Junior Developer"));
      Assertions.assertTrue(whr.topNWorkers(3, "Middle Developer").isEmpty());
      Assertions.assertEquals(Optional.of(1386), whr.calcSalary("Alice", 35, 181));
      Assertions.assertEquals(Optional.of(14), whr.get("Alice"));
      Assertions.assertEquals("registered", whr.register("Alice", 220));
      Assertions.assertEquals("registered", whr.register("Bob", 235));
      Assertions.assertEquals("registered", whr.register("Alice", 290));
      expected = new ArrayList<>(List.of("Bob(60)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Junior Developer"));
      expected = new ArrayList<>(List.of("Alice(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Middle Developer"));
      Assertions.assertEquals("success", whr.promote("Bob", "Middle Developer", 137, 295));
      Assertions.assertEquals("registered", whr.register("Bob", 291));
      expected = new ArrayList<>(List.of("Bob(60)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Junior Developer"));
      expected = new ArrayList<>(List.of("Alice(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Middle Developer"));
      Assertions.assertEquals(Optional.of(4680), whr.calcSalary("Bob", 68, 227));
      Assertions.assertEquals(Optional.of(60), whr.get("Bob"));
      Assertions.assertEquals(Optional.of(3564), whr.calcSalary("Alice", 95, 202));
      Assertions.assertEquals(Optional.of(54), whr.get("Alice"));
      Assertions.assertEquals("registered", whr.register("Bob", 295));
      Assertions.assertEquals("registered", whr.register("Bob", 298));
      Assertions.assertTrue(whr.topNWorkers(3, "Junior Developer").isEmpty());
      expected = new ArrayList<>(List.of("Alice(0)", "Bob(0)"));
      Assertions.assertEquals(expected, whr.topNWorkers(3, "Middle Developer"));
      Assertions.assertEquals(Optional.of(360), whr.calcSalary("Bob", 284, 297));
      Assertions.assertEquals(Optional.of(64), whr.get("Bob"));
    });
  }
}
