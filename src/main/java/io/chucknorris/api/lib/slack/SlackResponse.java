package io.chucknorris.api.lib.slack;

import io.chucknorris.api.lib.slack.impl.ResponseAttachment;

public interface SlackResponse {

  ResponseAttachment[] getAttachments();

  String getIconUrl();

  String getResponseType();

  String getText();
}
