package com.github.domcoon.groups.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.domcoon.groups.util.DurationUtil;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DurationFormatTest {

  @Test
  @DisplayName("Duration Format Test: Simple")
  public void testSimple() {
    long l1 = DurationUtil.parseDuration("5s");
    long l2 = DurationUtil.parseDuration("5d5s");
    long l3 = DurationUtil.parseDuration("5d 5s");
    assertEquals(l1, 5000L);
    assertEquals(l2, 432005000L);
    assertEquals(l3, 432005000L);

    assertThrows(DateTimeParseException.class, () -> DurationUtil.parseDuration("5"));
  }
}
