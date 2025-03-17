package org.fhmdb.fhmdb_lijunamatata.models;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private final String id;
    private final String title;
    private final List<Genre> genres;
    private final int releaseYear;
    private final String description;
    private final String imgUrl;
    private final int lengthInMinutes;
    private final List<String> directors;
    private final List<String> writers;
    private final List<String> mainCast;
    private final double rating;

    public Movie(String id, String title, List<Genre> genres, int releaseYear, String description,
                 String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers,
                 List<String> mainCast, double rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    /**
     * creates a mutable ArrayList of movies,
     * adds dummy movie data consisting of title, description and immutable list of genre enums,
     * and returns the list
     */
    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie(
                "",
                "Life Is Beautiful",
                List.of(Genre.DRAMA, Genre.ROMANCE),
                2024,
                "When an open-minded Jewish librarian and his son become victims of" +
                " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                "the dangers around their camp.",
                "http//:www.???",
                120,
                List.of("Director1", "Director2"),
                List.of("Writer1", "Writer2"),
                List.of("mainCast1", "mainCast2"),
                10.0));
        return movies;
    }
}

/*
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "title": "string",
    "genres": [
      "ACTION"
    ],
    "releaseYear": 0,
    "description": "string",
    "imgUrl": "string",
    "lengthInMinutes": 0,
    "directors": [
      "string"
    ],
    "writers": [
      "string"
    ],
    "mainCast": [
      "string"
    ],
    "rating": 0
  }
]
 */