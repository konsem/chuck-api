package io.chucknorris.api.lib.slack;

import io.chucknorris.api.lib.slack.impl.ResponseAttachment;

public interface SlackCommandResponse {

  ResponseAttachment[] getAttachments();

  String getIconUrl();

  String getResponseType();

  String getText();
}
