package org.fhmdb.fhmdb_lijunamatata.api;

import okhttp3.HttpUrl;

/**
 * Builder class for constructing a movie API request URL using the Builder Pattern.
 * Provides a fluent API to incrementally add query parameters to the URL.
 */
public class MovieAPIRequestBuilder {
    private final HttpUrl.Builder urlBuilder;

    /**
     * Initializes the URL builder with the provided base URL.
     *
     * @param baseUrl The base URL to which query parameters will be added.
     * @throws IllegalArgumentException if the base URL is invalid.
     */
    public MovieAPIRequestBuilder(String baseUrl) {
        HttpUrl base = HttpUrl.parse(baseUrl);
        if (base == null) {
            throw new IllegalArgumentException("Invalid base URL: " + baseUrl);
        }
        this.urlBuilder = base.newBuilder();
    }

    /**
     * Adds a query parameter to filter movies by a free-text search query.
     *
     * @param query The search term to include in the URL, or null if not specified.
     * @return The current builder instance.
     */
    public MovieAPIRequestBuilder query(String query) {
        if (query != null) {
            urlBuilder.addQueryParameter("query", query);
        }
        return this;
    }

    /**
     * Adds a query parameter to filter movies by genre.
     *
     * @param genre The genre as a string, or null if not specified.
     * @return The current builder instance.
     */
    public MovieAPIRequestBuilder genre(String genre) {
        if (genre != null) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        return this;
    }

    /**
     * Adds a query parameter to filter movies by release year.
     *
     * @param year The release year as a string, or null if not specified.
     * @return The current builder instance.
     */
    public MovieAPIRequestBuilder releaseYear(String year) {
        if (year != null) {
            urlBuilder.addQueryParameter("releaseYear", year);
        }
        return this;
    }

    /**
     * Adds a query parameter to filter movies by minimum rating.
     *
     * @param rating The minimum rating as a string, or null if not specified.
     * @return The current builder instance.
     */
    public MovieAPIRequestBuilder ratingFrom(String rating) {
        if (rating != null) {
            urlBuilder.addQueryParameter("ratingFrom", rating);
        }
        return this;
    }

    /**
     * Builds the final URL string with all specified query parameters.
     *
     * @return The fully constructed URL as a string.
     */
    public String build() {
        return urlBuilder.build().toString();
    }
}
