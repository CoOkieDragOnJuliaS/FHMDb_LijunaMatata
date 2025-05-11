package org.fhmdb.fhmdb_lijunamatata.models;

import org.fhmdb.fhmdb_lijunamatata.api.MovieAPI;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.repositories.MovieRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movie {
    private String id;
    private String title;
    private List<Genre> genres;
    private int releaseYear;
    private String description;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;

    public Movie() {
        //no parameter constructor for testing
    }

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

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    public void setMainCast(List<String> mainCast) {
        this.mainCast = mainCast;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * creates a mutable ArrayList of movies,
     * send request to API,
     * and returns the list
     */
    public static List<Movie> initializeMovies() throws IOException {
        MovieAPI movieAPI = new MovieAPI();
        List<Movie> movies = movieAPI.fetchAllMovies();
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.addAllMovies(MovieEntity.fromMovies(movies));
        return movies;
    }

    /**
     * creates a mutable ArrayList of movies,
     * and returns the list
     */
    public static List<Movie> initializeMoviesTestbase() {

        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie(
                "1",
                "Life Is Beautiful",
                List.of(Genre.DRAMA, Genre.ROMANCE),
                1997,
                "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp.",
                "http://www.example.com/lifeisbeautiful",
                120,
                List.of("Roberto Benigni"),
                List.of("Vincenzo Cerami", "Roberto Benigni"),
                List.of("Roberto Benigni", "Nicoletta Braschi"),
                9.0));

        movies.add(new Movie(
                "2",
                "Inception",
                List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.THRILLER),
                2010,
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                "http://www.example.com/inception",
                148,
                List.of("Christopher Nolan"),
                List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"),
                8.8));

        movies.add(new Movie(
                "3",
                "Parasite",
                List.of(Genre.DRAMA, Genre.THRILLER),
                2019,
                "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
                "http://www.example.com/parasite",
                132,
                List.of("Bong Joon-ho"),
                List.of("Bong Joon-ho", "Han Jin-won"),
                List.of("Song Kang-ho", "Choi Woo-shik"),
                8.6));

        movies.add(new Movie(
                "4",
                "The Godfather",
                List.of(Genre.DRAMA, Genre.CRIME),
                1972,
                "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
                "http://www.example.com/godfather",
                175,
                List.of("Francis Ford Coppola"),
                List.of("Mario Puzo", "Francis Ford Coppola"),
                List.of("Marlon Brando", "Al Pacino"),
                9.2));

        movies.add(new Movie(
                "5",
                "Pulp Fiction",
                List.of(Genre.CRIME, Genre.DRAMA),
                1994,
                "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
                "http://www.example.com/pulpfiction",
                154,
                List.of("Quentin Tarantino"),
                List.of("Quentin Tarantino", "Roger Avary"),
                List.of("John Travolta", "Samuel L. Jackson"),
                8.9));

        movies.add(new Movie(
                "6",
                "Spirited Away",
                List.of(Genre.ANIMATION, Genre.FANTASY, Genre.ADVENTURE),
                2001,
                "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, spirits, and a witch, where humans are changed into beasts.",
                "http://www.example.com/spiritedaway",
                125,
                List.of("Hayao Miyazaki"),
                List.of("Hayao Miyazaki"),
                List.of("Rumi Hiiragi", "Miyu Irino"),
                8.6));

        movies.add(new Movie(
                "7",
                "The Shawshank Redemption",
                List.of(Genre.DRAMA),
                1994,
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                "http://www.example.com/shawshank",
                142,
                List.of("Frank Darabont"),
                List.of("Stephen King", "Frank Darabont"),
                List.of("Tim Robbins", "Morgan Freeman"),
                9.3));

        movies.add(new Movie(
                "8",
                "Interstellar",
                List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE),
                2014,
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                "http://www.example.com/interstellar",
                169,
                List.of("Christopher Nolan"),
                List.of("Jonathan Nolan", "Christopher Nolan"),
                List.of("Matthew McConaughey", "Anne Hathaway"),
                8.6));

        movies.add(new Movie(
                "9",
                "Amélie",
                List.of(Genre.ROMANCE, Genre.COMEDY),
                2001,
                "Amélie is an innocent and naive girl in Paris with her own sense of justice. She decides to help those around her and discovers love along the way.",
                "http://www.example.com/amelie",
                122,
                List.of("Jean-Pierre Jeunet"),
                List.of("Guillaume Laurant", "Jean-Pierre Jeunet"),
                List.of("Audrey Tautou", "Mathieu Kassovitz"),
                8.3));

        movies.add(new Movie(
                "10",
                "Whiplash",
                List.of(Genre.DRAMA, Genre.MUSICAL),
                2014,
                "A promising young drummer enrolls at a cut-throat music conservatory where his dreams of greatness are mentored by an instructor who will stop at nothing to realize a student's potential.",
                "http://www.example.com/whiplash",
                106,
                List.of("Damien Chazelle"),
                List.of("Damien Chazelle"),
                List.of("Tim Robbins", "J.K. Simmons"),
                8.5));

        return movies;
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check if the same object
        if (o == null || getClass() != o.getClass()) return false; // Check for null and class type
        Movie movie = (Movie) o; // Cast to Movie


        //Comparing the variables except id to get the equalsMethod in assertion working
        return  releaseYear == movie.releaseYear &&
                movie.lengthInMinutes == lengthInMinutes &&
                Double.compare(movie.rating, rating) == 0 &&
                Objects.equals(title, movie.title) &&
                Objects.equals(genres, movie.genres) &&
                Objects.equals(description, movie.description) &&
                Objects.equals(imgUrl, movie.imgUrl) &&
                Objects.equals(directors, movie.directors) &&
                Objects.equals(writers, movie.writers) &&
                Objects.equals(mainCast, movie.mainCast);
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