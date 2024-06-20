package io.chucknorris.api.joke;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.chucknorris.lib.exception.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JokeControllerTest {

    private static String jokeId, jokeValue;
    private static Joke joke;

    private final JokeRepository jokeRepository = Mockito.mock(JokeRepository.class);
    private final MockHttpServletResponse httpServletResponse = Mockito.mock(MockHttpServletResponse.class);

    private final JokeController jokeController = new JokeController(jokeRepository);

    @BeforeEach
    void setUp() {
        jokeId = "ys--0t_-rrifz5jtcparbg";
        jokeValue = "Some people ask for a Kleenex when they sneeze, Chuck Norris asks for a body bag.";
        joke = Joke.builder().categories(new String[] { "dev" }).id(jokeId).value(jokeValue).build();
    }

    @Test
    void testGetCategories() {
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev", "animal"});

        String[] categories = jokeController.getCategories();
        Assertions.assertEquals("dev", categories[0]);
        Assertions.assertEquals("animal", categories[1]);
        Assertions.assertEquals(2, categories.length);

        verify(jokeRepository, times(1)).findAllCategories();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetCategoryValues() {
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev", "animal"});

        String categoryValues = jokeController.getCategoryValues();
        Assertions.assertEquals("dev\nanimal\n", categoryValues);

        verify(jokeRepository, times(1)).findAllCategories();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetJokeReturnsJoke() {
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        Joke joke = jokeController.getJoke(jokeId);
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).findById(jokeId);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetJokeThrowsException() {
        when(jokeRepository.findById("does-not-exist")).thenThrow(new EntityNotFoundException(""));

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> jokeController.getJoke("does-not-exist")
        );

        verify(jokeRepository, times(1)).findById("does-not-exist");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetJokeValueReturnsJokeValue() {
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        String jokeValue = jokeController.getJokeValue(jokeId, this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).findById(jokeId);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetJokeValueReturnsEmptyStringIfEntityNotFound() {
        when(jokeRepository.findById("does-not-exist")).thenThrow(new EntityNotFoundException(""));

        String jokeValue = jokeController.getJokeValue("does-not-exist", this.httpServletResponse);
        Assertions.assertEquals("", jokeValue);

        verify(jokeRepository, times(1)).findById("does-not-exist");
        verify(this.httpServletResponse).setStatus(404);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void getJokeViewReturnsModelAndView() {
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));
        when(jokeRepository.getJokeWindow(jokeId))
            .thenReturn(jokeId + ',' + "yvrhbpauspegla4pf7dxna" + ',' + "id4dTcDiRneK4btgOGpNNw");

        ModelAndView view = jokeController.getJokeView(jokeId);
        Assertions.assertEquals(joke, view.getModel().get("joke"));
        Assertions.assertEquals("/jokes/yvrhbpauspegla4pf7dxna", view.getModel().get("next_joke_url"));
        Assertions.assertEquals("/jokes/ys--0t_-rrifz5jtcparbg", view.getModel().get("current_joke_url"));
        Assertions.assertEquals("/jokes/id4dTcDiRneK4btgOGpNNw", view.getModel().get("prev_joke_url"));

        verify(jokeRepository, times(1)).findById(jokeId);
        verify(jokeRepository, times(1)).getJokeWindow(jokeId);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJoke() {
        when(jokeRepository.getRandomJoke()).thenReturn(joke);

        Joke joke = jokeController.getRandomJoke(null, null);
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).getRandomJoke();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeByCategory() {
        when(jokeRepository.getRandomJokeByCategory("dev")).thenReturn(joke);
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev"});

        Joke joke = jokeController.getRandomJoke("dev", null);
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomJokeByCategory("dev");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeByCategoryThrowsException() {
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{});

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> jokeController.getRandomJoke("dev", null)
        );

        verify(jokeRepository, times(1)).findAllCategories();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeByMultipleCategories() {
        when(jokeRepository.getRandomJokeByCategories("dev,movie")).thenReturn(joke);
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev", "movie"});

        Joke joke = jokeController.getRandomJoke("dev,movie", null);
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomJokeByCategories("dev,movie");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeByMultipleCategoriesThrowsException() {
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{});

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> jokeController.getRandomJoke("dev,does-not-exist", null)
        );

        verify(jokeRepository, times(1)).findAllCategories();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeReturnsJoke() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.getRandomPersonalizedJoke("Bob")).thenReturn(joke);

        Joke joke = jokeController.getRandomJoke(null, "Bob");
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).getRandomPersonalizedJoke("Bob");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeByCategoryReturnsJoke() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.findAllCategories()).thenReturn(new String[] { "dev" });
        when(jokeRepository.getRandomPersonalizedJokeByCategories("Bob", "dev")).thenReturn(joke);

        Joke joke = jokeController.getRandomJoke("dev", "Bob");
        Assertions.assertEquals(JokeControllerTest.joke, joke);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomPersonalizedJokeByCategories("Bob", "dev");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeByCategoryThrowsException() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.findAllCategories()).thenReturn(new String[] { "dev" });
        when(jokeRepository.getRandomPersonalizedJokeByCategories("Bob", "dev")).thenReturn(null);

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> jokeController.getRandomJoke("dev", "Bob"));
    }

    @Test
    void testGetRandomPersonalisedJokeThrowsException() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.getRandomPersonalizedJoke("Bob")).thenReturn(null);

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> jokeController.getRandomJoke(null, "Bob"));

        verify(jokeRepository, times(1)).getRandomPersonalizedJoke("Bob");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeValue() {
        when(jokeRepository.getRandomJoke()).thenReturn(joke);

        String jokeValue = jokeController.getRandomJokeValue(null, null, this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).getRandomJoke();
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeValueByCategory() {
        when(jokeRepository.getRandomJokeByCategory("dev")).thenReturn(joke);
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev"});

        String jokeValue = jokeController.getRandomJokeValue("dev", null, this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomJokeByCategory("dev");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeReturnsJokeValueByMultipleCategories() {
        when(jokeRepository.getRandomJokeByCategories("dev,movie")).thenReturn(joke);
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{"dev", "movie"});

        String jokeValue =
            jokeController.getRandomJokeValue("dev,movie", null, this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomJokeByCategories("dev,movie");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomJokeValueReturnsEmptyStringIfCategoryDoesNotExist() {
        when(jokeRepository.findAllCategories()).thenReturn(new String[]{});

        String jokeValue =
            jokeController.getRandomJokeValue("does-not-exist", null, this.httpServletResponse);
        Assertions.assertEquals("", jokeValue);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(this.httpServletResponse).setStatus(404);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeValueReturnsJokeValue() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.getRandomPersonalizedJoke("Bob")).thenReturn(joke);

        String jokeValue = jokeController.getRandomJokeValue(null, "Bob", this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).getRandomPersonalizedJoke("Bob");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeValueEmptyStringIfNoJokeWasFound() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.getRandomPersonalizedJoke("Bob")).thenReturn(null);

        String jokeValue = jokeController.getRandomJokeValue(null, "Bob", this.httpServletResponse);
        Assertions.assertEquals("", jokeValue);

        verify(jokeRepository, times(1)).getRandomPersonalizedJoke("Bob");
        verify(this.httpServletResponse).setStatus(404);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeValueByCategoryReturnsJokeValue() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.findAllCategories()).thenReturn(new String[] { "dev" });
        when(jokeRepository.getRandomPersonalizedJokeByCategories("Bob", "dev")).thenReturn(joke);

        String jokeValue = jokeController.getRandomJokeValue("dev", "Bob", this.httpServletResponse);
        Assertions.assertEquals(joke.getValue(), jokeValue);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomPersonalizedJokeByCategories("Bob", "dev");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testGetRandomPersonalisedJokeValueByCategoryEmptyStringIfNoJokeWasFound() {
        joke = joke.toBuilder().value(joke.getValue().replace("Chuck Norris", "Bob")).build();
        when(jokeRepository.findAllCategories()).thenReturn(new String[] { "dev" });
        when(jokeRepository.getRandomPersonalizedJokeByCategories("Bob", "dev")).thenReturn(null);

        String jokeValue = jokeController.getRandomJokeValue("dev", "Bob", this.httpServletResponse);
        Assertions.assertEquals("", jokeValue);

        verify(jokeRepository, times(1)).findAllCategories();
        verify(jokeRepository, times(1)).getRandomPersonalizedJokeByCategories("Bob", "dev");
        verify(this.httpServletResponse).setStatus(404);
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testSearch() {
        when(jokeRepository.searchByQuery("Kleenex")).thenReturn(new Joke[]{joke});

        JokeSearchResult jokeSearchResult = jokeController.search("Kleenex");
        Assertions.assertEquals(jokeSearchResult.getTotal(), 1);
        Assertions.assertEquals(jokeSearchResult.getResult()[0], joke);

        verify(jokeRepository, times(1)).searchByQuery("Kleenex");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testSearchValues() {
        when(jokeRepository.searchByQuery("Kleenex")).thenReturn(new Joke[]{joke});

        String searchValues = jokeController.searchValues("Kleenex");
        Assertions.assertEquals(
            "Some people ask for a Kleenex when they sneeze, Chuck Norris asks for a body" + " bag.\n",
            searchValues);

        verify(jokeRepository, times(1)).searchByQuery("Kleenex");
        verifyNoMoreInteractions(jokeRepository);
    }
}
