package com.github.domcoon.groups.tests;

import com.github.domcoon.groups.util.DurationUtil;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DurationFormatTest {

    @Test
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
