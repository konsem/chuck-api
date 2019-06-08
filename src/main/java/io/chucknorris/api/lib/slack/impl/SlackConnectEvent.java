package io.chucknorris.api.lib.slack.impl;

import io.chucknorris.api.lib.event.BaseEvent;

public class SlackConnectEvent extends BaseEvent {

  public SlackConnectEvent(AccessToken accessToken) {
    super("SlackConnectEvent", accessToken);
  }
}
