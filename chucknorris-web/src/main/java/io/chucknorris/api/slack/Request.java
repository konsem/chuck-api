package io.chucknorris.api.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Request implements Serializable {

  @JsonProperty("channel_id")
  private String channelId;

  @JsonProperty("channel_name")
  private String channelName;

  /**
   * The command that was typed in to trigger this request. This value can be useful if you want to
   * use a single Request URL to service multiple Slash Commands, as it lets you tell them apart.
   */
  @JsonProperty("command")
  private String command;

  @JsonProperty("enterprise_id")
  private String enterpriseId;

  @JsonProperty("enterprise_name")
  private String enterpriseName;

  /** A URL that you can use to respond to the command. */
  @JsonProperty("enterprise_url")
  private String responseUrl;

  @JsonProperty("team_domain")
  private String teamDomain;

  @JsonProperty("team_id")
  private String teamId;

  /**
   * This is the part of the Slash Command after the command itself, and it can contain absolutely
   * anything that the user might decide to type. It is common to use this text parameter to provide
   * extra context for the command.
   */
  @JsonProperty("text")
  private String text;

  /**
   * This is a verification token, a deprecated feature that you shouldn't use any more. It was used
   * to verify that requests were legitimately being sent by Slack to your app, but you should use
   * the signed secrets functionality to do this instead.
   */
  @JsonProperty("token")
  private String token;

  /**
   * If you need to respond to the command by opening a dialog, you'll need this trigger ID to get
   * it to work. You can use this ID with dialog.open up to 3000ms after this data payload is sent.
   */
  @JsonProperty("trigger_id")
  private String triggerId;

  /** The ID of the user who triggered the command. */
  @JsonProperty("user_id")
  private String userId;

  /**
   * The plain text name of the user who triggered the command. As above, do not rely on this field
   * as it is being phased out, use the user_id instead.
   */
  @JsonProperty("user_name")
  private String userName;
}
