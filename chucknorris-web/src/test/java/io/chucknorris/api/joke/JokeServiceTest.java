package io.chucknorris.api.joke;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class JokeServiceTest {

    private final String jokeId = "ys--0t_-rrifz5jtcparbg";
    private final String jokeValue = "Some people ask for a Kleenex when they sneeze, Chuck Norris asks for a body bag.";

    private final Joke joke = Joke.builder()
            .categories(new String[] { "dev" })
            .id(jokeId)
            .value(jokeValue)
            .build();

    private final JokeRepository jokeRepository = Mockito.mock(JokeRepository.class);
    private final JokeService jokeService = new JokeService(jokeRepository);

    @Test
    void testRandomJokeByCategoriesReturnsJoke() {
        // setup:
        when(jokeRepository.getRandomJokeByCategories("dev,movie")).thenReturn(joke);

        // when:
        var actual = jokeService.randomJokeByCategories(new String[]{"dev", "movie"});

        // then:
        Assertions.assertEquals(this.joke, actual);

        // and:
        verify(jokeRepository, times(1)).getRandomJokeByCategories("dev,movie");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testRandomPersonalizedJokeByCategoriesReturnsJoke() {
        String substitute = "Bob";
        String[] categories = new String[] { "dev", "movie" };

        joke.setValue(joke.getValue().replace("Chuck Norris", substitute));
        when(jokeRepository.getRandomPersonalizedJokeByCategories(substitute, "dev,movie"))
                .thenReturn(joke);

        Joke joke = jokeService.randomPersonalizedJokeByCategories(substitute, categories);
        Assertions.assertEquals(this.joke, joke);

        verify(jokeRepository, times(1)).getRandomPersonalizedJokeByCategories(substitute, "dev,movie");
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    void testSearchWithCategoryFilter() {
        String query = "Kleenex";
        String[] categories = new String[] { "dev", "movie" };
        Pageable pageable = PageRequest.of(1, 5);

        when(jokeRepository.findByValueContainsAndFilter(query, "dev,movie", pageable))
                .thenReturn(Page.empty());

        jokeService.searchWithCategoryFilter(query, categories, pageable);

        verify(jokeRepository, times(1)).findByValueContainsAndFilter(query, "dev,movie", pageable);
        verifyNoMoreInteractions(jokeRepository);
    }
}
