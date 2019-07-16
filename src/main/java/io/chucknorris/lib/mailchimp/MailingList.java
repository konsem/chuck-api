package io.chucknorris.lib.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.chucknorris.lib.mailchimp.MailingListStatistic;
import java.io.Serializable;
import lombok.Data;

@Data
public class MailingList implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("stats")
  private MailingListStatistic mailingListStatistic;

  @JsonProperty("name")
  private String name;
}
