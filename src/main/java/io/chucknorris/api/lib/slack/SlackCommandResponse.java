package io.chucknorris.api.lib.slack;

public interface SlackCommandResponse {

  SlackCommandResponseAttachment[] getAttachments();

  String getIconUrl();

  String getResponseType();

  String getText();
}
