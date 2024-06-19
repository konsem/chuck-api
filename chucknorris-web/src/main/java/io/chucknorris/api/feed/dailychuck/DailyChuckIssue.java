package io.chucknorris.api.feed.dailychuck;

import java.util.Date;
import lombok.Data;

@Data
public class DailyChuckIssue {

    private Date date;
    private String jokeId;
}
