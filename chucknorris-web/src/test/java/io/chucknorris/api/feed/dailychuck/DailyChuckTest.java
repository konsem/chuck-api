package io.chucknorris.api.feed.dailychuck;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DailyChuckTest {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private DailyChuck dailyChuck;
    private DailyChuckIssue dailyChuckIssue;

    @BeforeEach
    public void setUp() throws ParseException {
        dailyChuckIssue = new DailyChuckIssue();
        dailyChuckIssue.setDate(dateFormat.parse("2019-01-01"));
        dailyChuckIssue.setJokeId("c5k7tulvqjs76evwb3brfg");

        dailyChuck = new DailyChuck();
        dailyChuck.setIssues(new DailyChuckIssue[] { dailyChuckIssue });
    }

    @Test
    void testFindIssueByJokeIdReturnsNullDoesNotExist() {
        Assertions.assertNull(dailyChuck.findIssueByJokeId("does-not-exist"));
    }

    @Test
    void testFindIssueByJokeIdReturnsDailyChuckIssueIfDoesExist() {
        Assertions.assertEquals(dailyChuck.findIssueByJokeId("c5k7tulvqjs76evwb3brfg"), dailyChuckIssue);
    }

    @Test
    void testFindIssueByDateReturnsNullDoesNotExist() throws ParseException {
        Assertions.assertNull(dailyChuck.findIssueByDate(dateFormat.parse("2019-01-02")));
    }

    @Test
    void testFindIssueByDateDailyChuckIssueIfDoesExist() throws ParseException {
        Assertions.assertEquals(
                dailyChuck.findIssueByDate(dateFormat.parse("2019-01-01")), dailyChuckIssue);
    }
}
