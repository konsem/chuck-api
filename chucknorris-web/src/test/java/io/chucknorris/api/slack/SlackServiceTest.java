package io.chucknorris.api.slack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

class SlackServiceTest {

    @InjectMocks
    private SlackService slackService = new SlackService();

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(
                slackService,
                "whitelistedCategories",
                "career,celebrity,dev,fashion,food,money,movie,travel");
        ReflectionTestUtils.setField(slackService, "clientId", "slack.oauth.client_id");
        ReflectionTestUtils.setField(slackService, "clientSecret", "slack.oauth.client_secret");
        ReflectionTestUtils.setField(slackService, "redirectUrl", "slack.oauth.redirect_uri");
    }

    @Test
    public void testComposeAuthorizeUri() {
        UriComponents authorizeUri = slackService.composeAuthorizeUri();

        Assertions.assertEquals(
                "https://slack.com/oauth/v2/authorize/?client_id=slack.oauth.client_id&redirect_uri=slack.oauth.redirect_uri&scope=commands",
                authorizeUri.toUriString());
    }

    @Test
    public void testFilterNonWhitelistedCategories() {
        Assertions.assertArrayEquals(
                slackService.filterNonWhitelistedCategories(
                        new String[] {
                                "career",
                                "celebrity",
                                "dev",
                                "explicit",
                                "fashion",
                                "food",
                                "money",
                                "movie",
                                "travel"
                        }),
                new String[] { "career", "celebrity", "dev", "fashion", "food", "money", "movie", "travel" });
    }

    @Test
    public void testGetWhitelistedCategoriesReturnsArrayOfCategories() {
        Assertions.assertArrayEquals(
                slackService.getWhitelistedCategories(),
                new String[] { "career", "celebrity", "dev", "fashion", "food", "money", "movie", "travel" });
    }

    @Test
    public void testIfGivenCategoryIsWhitelisted() {
        Assertions.assertFalse(slackService.isWhitelistedCategory("explicit"));
        Assertions.assertFalse(slackService.isWhitelistedCategory("religion"));
        Assertions.assertTrue(slackService.isWhitelistedCategory("dev"));
    }

    //    @Test
    // TODO: Fix this test
    //    public void testRequestAccessTokenSendsRequestAndRetunsToken() {
    //        String code = "my-super-secret-code";
    //        AccessToken accessToken = new AccessToken();
    //
    //        HttpHeaders headers = new HttpHeaders();
    //        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    //        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    //
    //        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    //        map.add("client_id", "slack.oauth.client_id");
    //        map.add("client_secret", "slack.oauth.client_secret");
    //        map.add("code", code);
    //        map.add("redirect_uri", "slack.oauth.redirect_uri");
    //
    //        when(restTemplate.exchange(
    //            "https://slack.com/api/oauth.v2.access",
    //            HttpMethod.POST,
    //            new HttpEntity<MultiValueMap<String, String>>(map, headers),
    //            AccessToken.class))
    //            .thenReturn(new ResponseEntity(accessToken, HttpStatus.OK));
    //
    //        AccessToken response = slackService.requestAccessToken(code);
    //        Assertions.assertEquals(accessToken, response);
    //
    //        verify(restTemplate, times(1))
    //            .exchange(
    //                "https://slack.com/api/oauth.v2.access",
    //                HttpMethod.POST,
    //                new HttpEntity<MultiValueMap<String, String>>(map, headers),
    //                AccessToken.class);
    //        verifyNoMoreInteractions(restTemplate);
    //    }
}
