package io.chucknorris.api.controller;

import io.chucknorris.api.lib.slack.impl.SlackService;
import io.chucknorris.api.repository.JokeRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  private JokeRepository jokeRepository;
  private SlackService slackService;

  /**
   * Instantiates a new HomeController {@link HomeController}.
   */
  public HomeController(JokeRepository jokeRepository, SlackService slackService) {
    this.jokeRepository = jokeRepository;
    this.slackService = slackService;
  }

  /**
   * Returns the model for the home view.
   */
  public @RequestMapping(
      value = "/",
      method = RequestMethod.GET,
      headers = HttpHeaders.ACCEPT + "=" + MediaType.TEXT_HTML_VALUE,
      produces = MediaType.TEXT_HTML_VALUE
  ) ModelAndView view() {
    ModelAndView modelAndView = new ModelAndView("home");
    modelAndView.addObject("joke", jokeRepository.getRandomJoke());
    modelAndView.addObject("slack_authorize_url", slackService.composeAuthorizeUri());

    return modelAndView;
  }
}
