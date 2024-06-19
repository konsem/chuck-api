package io.chucknorris.lib.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.util.concurrent.AtomicDouble;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

@Data
public class MailingListStatistic implements Serializable {

    @JsonProperty("member_count")
    private AtomicInteger memberCount;

    @JsonProperty("unsubscribe_count")
    private AtomicInteger unsubscribeCount;

    @JsonProperty("cleaned_count")
    private AtomicInteger cleanedCount;

    @JsonProperty("member_count_since_send")
    private AtomicInteger memberCountSinceSend;

    @JsonProperty("unsubscribe_count_since_send")
    private AtomicInteger unsubscribeCountSinceSend;

    @JsonProperty("cleaned_count_since_send")
    private AtomicInteger cleanedCountSinceSend;

    @JsonProperty("campaign_count")
    private AtomicInteger campaignCount;

    @JsonProperty("campaign_last_sent")
    private Date campaignLastSent;

    @JsonProperty("merge_field_count")
    private AtomicInteger mergeFieldCount;

    @JsonProperty("avg_sub_rate")
    private AtomicInteger avgSubRate;

    @JsonProperty("avg_unsub_rate")
    private AtomicInteger avgUnsubRate;

    @JsonProperty("target_sub_rate")
    private AtomicInteger targetSubRate;

    @JsonProperty("openRate")
    private AtomicDouble openRate;

    @JsonProperty("click_rate")
    private AtomicDouble clickRate;

    @JsonProperty("last_sub_date")
    private Date lastDubDate;

    @JsonProperty("last_unsub_date")
    private Date lastUnsubDate;
}
