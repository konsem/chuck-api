package io.chucknorris.api.feed;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.util.concurrent.AtomicDouble;
import io.chucknorris.api.feed.dailychuck.DailyChuck;
import io.chucknorris.api.feed.dailychuck.DailyChuckIssue;
import io.chucknorris.api.feed.dailychuck.DailyChuckRss;
import io.chucknorris.api.feed.dailychuck.DailyChuckService;
import io.chucknorris.api.joke.JokeRepository;
import io.chucknorris.lib.DateUtil;
import io.chucknorris.lib.event.EventService;
import io.chucknorris.lib.mailchimp.MailchimpService;
import io.chucknorris.lib.mailchimp.MailingListStatistic;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class FeedControllerTest {

    private DailyChuck dailyChuck;
    private DailyChuckIssue dailyChuckIssue;
    private DailyChuckService dailyChuckService = Mockito.mock(DailyChuckService.class);
    private DateUtil dateUtil = Mockito.mock(DateUtil.class);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private EventService eventService = Mockito.mock(EventService.class);
    private JokeRepository jokeRepository = Mockito.mock(JokeRepository.class);
    private MailchimpService mailchimpService = Mockito.mock(MailchimpService.class);
    private MailingListStatistic mailingListStatistic;

    private FeedController feedController = new FeedController(
            dailyChuckService,
            dateUtil,
            eventService,
            mailchimpService);

    private String mailingListId;

    @BeforeEach
    public void setUp() throws ParseException {
        dailyChuckIssue = new DailyChuckIssue();
        dailyChuckIssue.setDate(dateFormat.parse("2019-01-01"));
        dailyChuckIssue.setJokeId("c5k7tulvqjs76evwb3brfg");

        dailyChuck = new DailyChuck();
        dailyChuck.setIssues(new DailyChuckIssue[] { dailyChuckIssue });
        dailyChuck.setIssueNumber(Long.valueOf(1));

        ReflectionTestUtils.setField(feedController, "dailyChuckListId", "xxxxxxxxxx");

        mailingListId = "xxxxxxxxxx";
        mailingListStatistic = new MailingListStatistic();
        mailingListStatistic.setMemberCount(new AtomicInteger(228));
        mailingListStatistic.setUnsubscribeCount(new AtomicInteger(122));
        mailingListStatistic.setCleanedCount(new AtomicInteger(48));
        mailingListStatistic.setCampaignCount(new AtomicInteger(465));
        mailingListStatistic.setAvgSubRate(new AtomicInteger(23));
        mailingListStatistic.setAvgUnsubRate(new AtomicInteger(7));
        mailingListStatistic.setClickRate(new AtomicDouble(0.30748722));

        when(mailchimpService.fetchListStats(mailingListId)).thenReturn(mailingListStatistic);
    }

    @Test
    public void testDailyChuckJsonReturnsDailyChuckWithoutComposingANewIssueIfItHasAlreadyBeenIssued()
        throws IOException, ParseException {
        when(dailyChuckService.getDailyChuck()).thenReturn(dailyChuck);
        when(dateUtil.now()).thenReturn(dateFormat.parse("2019-01-01"));

        Assertions.assertEquals(dailyChuck, feedController.dailyChuckJson());

        verify(dailyChuckService, times(1)).getDailyChuck();
        verifyNoMoreInteractions(dailyChuckService);

        verify(dateUtil, times(1)).now();
        verifyNoMoreInteractions(dateUtil);

        verify(eventService, times(0)).publishEvent(any());
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void testDailyChuckJsonReturnsDailyChuckWithComposingANewIssue()
            throws IOException, ParseException {
        DailyChuckIssue newDailyChuckIssue = new DailyChuckIssue();

        when(dailyChuckService.getDailyChuck()).thenReturn(dailyChuck);
        when(dateUtil.now()).thenReturn(dateFormat.parse("2019-01-02"));
        when(dailyChuckService.composeDailyChuckIssue(any())).thenReturn(newDailyChuckIssue);

        Assertions.assertEquals(dailyChuck, feedController.dailyChuckJson());

        verify(dailyChuckService, times(1)).getDailyChuck();
        verify(dailyChuckService, times(1)).composeDailyChuckIssue(any());
        verify(dailyChuckService, times(1)).persist(dailyChuck);
        verifyNoMoreInteractions(dailyChuckService);

        verify(dateUtil, times(1)).now();
        verifyNoMoreInteractions(dateUtil);

        verify(eventService, times(1)).publishEvent(any());
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void testDailyChuckRssReturnsDailyChuckWithoutComposingANewIssueIfItHasAlreadyBeenIssued()
            throws IOException, ParseException {
        DailyChuckRss dailyChuckRss = new DailyChuckRss("", dailyChuck, jokeRepository);

        when(dailyChuckService.getDailyChuck()).thenReturn(dailyChuck);
        when(dateUtil.now()).thenReturn(dateFormat.parse("2019-01-01"));
        when(dailyChuckService.toRss(dailyChuck)).thenReturn(dailyChuckRss);

        Assertions.assertEquals(dailyChuckRss, feedController.dailyChuckRss());

        verify(dailyChuckService, times(1)).getDailyChuck();
        verify(dailyChuckService, times(1)).toRss(dailyChuck);

        verify(dateUtil, times(1)).now();
        verifyNoMoreInteractions(dateUtil);

        verify(eventService, times(0)).publishEvent(any());
        verifyNoMoreInteractions(eventService);

        verify(mailchimpService, times(1)).fetchListStats(mailingListId);
        verifyNoMoreInteractions(mailchimpService);
    }

    @Test
    public void testDailyChuckRssReturnsDailyChuckWithComposingANewIssue()
            throws IOException, ParseException {
        DailyChuckRss dailyChuckRss = new DailyChuckRss("", dailyChuck, jokeRepository);
        DailyChuckIssue newDailyChuckIssue = new DailyChuckIssue();

        when(dailyChuckService.getDailyChuck()).thenReturn(dailyChuck);
        when(dateUtil.now()).thenReturn(dateFormat.parse("2019-01-02"));
        when(dailyChuckService.composeDailyChuckIssue(any())).thenReturn(newDailyChuckIssue);
        when(dailyChuckService.toRss(dailyChuck)).thenReturn(dailyChuckRss);

        Assertions.assertEquals(dailyChuckRss, feedController.dailyChuckRss());

        verify(dailyChuckService, times(1)).getDailyChuck();
        verify(dailyChuckService, times(1)).composeDailyChuckIssue(any());
        verify(dailyChuckService, times(1)).persist(dailyChuck);
        verify(dailyChuckService, times(1)).toRss(dailyChuck);
        verifyNoMoreInteractions(dailyChuckService);

        verify(dateUtil, times(1)).now();
        verifyNoMoreInteractions(dateUtil);

        verify(eventService, times(1)).publishEvent(any());
        verifyNoMoreInteractions(eventService);

        verify(mailchimpService, times(1)).fetchListStats(mailingListId);
        verifyNoMoreInteractions(mailchimpService);
    }

    @Test
    public void testDailyChuckStatsReturnsStats() {
        MailingListStatistic response = feedController.dailyChuckStats();
        Assertions.assertEquals(response, mailingListStatistic);

        verify(mailchimpService, times(1)).fetchListStats(mailingListId);
        verifyNoMoreInteractions(mailchimpService);
    }
}
