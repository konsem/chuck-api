package io.chucknorris.lib.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.util.concurrent.AtomicDouble;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

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

  public AtomicInteger getMemberCount() {
    return memberCount;
  }

  public AtomicInteger getUnsubscribeCount() {
    return unsubscribeCount;
  }

  public AtomicInteger getCleanedCount() {
    return cleanedCount;
  }

  public AtomicInteger getMemberCountSinceSend() {
    return memberCountSinceSend;
  }

  public AtomicInteger getUnsubscribeCountSinceSend() {
    return unsubscribeCountSinceSend;
  }

  public AtomicInteger getCleanedCountSinceSend() {
    return cleanedCountSinceSend;
  }

  public AtomicInteger getCampaignCount() {
    return campaignCount;
  }

  public Date getCampaignLastSent() {
    return campaignLastSent;
  }

  public AtomicInteger getMergeFieldCount() {
    return mergeFieldCount;
  }

  public AtomicInteger getAvgSubRate() {
    return avgSubRate;
  }

  public AtomicInteger getAvgUnsubRate() {
    return avgUnsubRate;
  }

  public AtomicInteger getTargetSubRate() {
    return targetSubRate;
  }

  public AtomicDouble getOpenRate() {
    return openRate;
  }

  public AtomicDouble getClickRate() {
    return clickRate;
  }

  public Date getLastDubDate() {
    return lastDubDate;
  }

  public Date getLastUnsubDate() {
    return lastUnsubDate;
  }

  public void setMemberCount(AtomicInteger memberCount) {
    this.memberCount = memberCount;
  }

  public void setUnsubscribeCount(AtomicInteger unsubscribeCount) {
    this.unsubscribeCount = unsubscribeCount;
  }

  public void setCleanedCount(AtomicInteger cleanedCount) {
    this.cleanedCount = cleanedCount;
  }

  public void setMemberCountSinceSend(AtomicInteger memberCountSinceSend) {
    this.memberCountSinceSend = memberCountSinceSend;
  }

  public void setUnsubscribeCountSinceSend(
      AtomicInteger unsubscribeCountSinceSend) {
    this.unsubscribeCountSinceSend = unsubscribeCountSinceSend;
  }

  public void setCleanedCountSinceSend(AtomicInteger cleanedCountSinceSend) {
    this.cleanedCountSinceSend = cleanedCountSinceSend;
  }

  public void setCampaignCount(AtomicInteger campaignCount) {
    this.campaignCount = campaignCount;
  }

  public void setCampaignLastSent(Date campaignLastSent) {
    this.campaignLastSent = campaignLastSent;
  }

  public void setMergeFieldCount(AtomicInteger mergeFieldCount) {
    this.mergeFieldCount = mergeFieldCount;
  }

  public void setAvgSubRate(AtomicInteger avgSubRate) {
    this.avgSubRate = avgSubRate;
  }

  public void setAvgUnsubRate(AtomicInteger avgUnsubRate) {
    this.avgUnsubRate = avgUnsubRate;
  }

  public void setTargetSubRate(AtomicInteger targetSubRate) {
    this.targetSubRate = targetSubRate;
  }

  public void setOpenRate(AtomicDouble openRate) {
    this.openRate = openRate;
  }

  public void setClickRate(AtomicDouble clickRate) {
    this.clickRate = clickRate;
  }

  public void setLastDubDate(Date lastDubDate) {
    this.lastDubDate = lastDubDate;
  }

  public void setLastUnsubDate(Date lastUnsubDate) {
    this.lastUnsubDate = lastUnsubDate;
  }
}
