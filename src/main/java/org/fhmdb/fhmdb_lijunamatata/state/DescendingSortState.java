package org.fhmdb.fhmdb_lijunamatata.state;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DescendingSortState implements SortState {
    @Override
    public List<Movie> sort(List<Movie> movies) {
        // Sort movies in descending order by title
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public SortState nextState() {
        // Next state is back to Unsorted
        return new UnsortedState();
    }

    @Override
    public String getButtonText() {
        //Button text is the next state??
        return "Sort (None)";
    }
}
