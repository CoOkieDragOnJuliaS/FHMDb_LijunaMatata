package org.fhmdb.fhmdb_lijunamatata.api;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieAPIRequestBuilderTest {

    private static final String BASE_URL = "https://example.com/movies";

    @Test
    @DisplayName("Builder includes all parameters correctly in the URL")
    void builder_includes_all_parameters() {
        String result = new MovieAPIRequestBuilder(BASE_URL)
                .query("inception")
                .genre("ACTION")
                .releaseYear("2010")
                .ratingFrom("8.5")
                .build();

        HttpUrl url = HttpUrl.parse(result);
        assertNotNull(url);
        assertEquals("inception", url.queryParameter("query"));
        assertEquals("ACTION", url.queryParameter("genre"));
        assertEquals("2010", url.queryParameter("releaseYear"));
        assertEquals("8.5", url.queryParameter("ratingFrom"));
    }

    @Test
    @DisplayName("Builder skips null parameters and returns valid URL")
    void builder_skips_null_parameters() {
        String result = new MovieAPIRequestBuilder(BASE_URL)
                .query(null)
                .genre("DRAMA")
                .releaseYear(null)
                .ratingFrom("7.0")
                .build();

        HttpUrl url = HttpUrl.parse(result);
        assertNotNull(url);
        assertNull(url.queryParameter("query"));
        assertEquals("DRAMA", url.queryParameter("genre"));
        assertNull(url.queryParameter("releaseYear"));
        assertEquals("7.0", url.queryParameter("ratingFrom"));
    }

    @Test
    @DisplayName("Builder throws exception on invalid base URL")
    void builder_throws_on_invalid_base_url() {
        assertThrows(IllegalArgumentException.class, () ->
                new MovieAPIRequestBuilder("not-a-valid-url"));
    }

    @Test
    @DisplayName("Builder builds base URL without any parameters")
    void builder_with_no_parameters_returns_base_url() {
        String result = new MovieAPIRequestBuilder(BASE_URL).build();
        assertEquals(BASE_URL, result);
    }
}
