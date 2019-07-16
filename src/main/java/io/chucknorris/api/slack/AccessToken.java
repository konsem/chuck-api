package io.chucknorris.api.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class AccessToken implements Serializable {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("team_id")
  private String teamId;

  @JsonProperty("team_name")
  private String teamName;

  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("user_name")
  private String userName;
}