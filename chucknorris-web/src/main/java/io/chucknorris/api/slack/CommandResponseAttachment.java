package io.chucknorris.api.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class CommandResponseAttachment implements Serializable, SlackCommandResponseAttachment {

  @JsonProperty("fallback")
  private String fallback;

  @JsonProperty("mrkdwn_in")
  private String[] mrkdownIn = new String[] {"text"};

  @JsonProperty("text")
  private String text;

  @JsonProperty("title")
  private String title;

  @JsonProperty("title_link")
  private String titleLink;
}
