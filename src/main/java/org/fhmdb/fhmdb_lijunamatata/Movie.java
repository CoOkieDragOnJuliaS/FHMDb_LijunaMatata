package org.fhmdb.fhmdb_lijunamatata;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;

    Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * creates a mutable ArrayList of movies,
     * adds dummy movie data consisting of title, description and immutable list of genre enums,
     * and returns the list
     */
    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Life Is Beautiful", "When an open-minded Jewish librarian and his son become victims of" +
                " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                "the dangers around their camp.", List.of(Genre.DRAMA, Genre.ROMANCE)));
        movies.add(new Movie("The Usual Suspects", "A sole survivor tells of the twisty events " +
                "leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly " +
                "random police lineup.", List.of(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)));
        movies.add(new Movie("Puss in Boots", "An outlaw cat, his childhood egg-friend, and a seductive thief kitty " +
                "set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor," +
                " and regain the trust of his mother and town.", List.of(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)));
        movies.add(new Movie("Avatar", "A paraplegic Marine dispatched to the moon Pandora on a unique mission " +
                "becomes torn between following his orders and protecting the world he feels is his home.",
                List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION)));
        movies.add(new Movie("The Wolf of Wall Street", "Based on the true story of Jordan Belfort, from his rise to " +
                "a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal " +
                "government.", List.of(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY)));

        return movies;
    }
}
