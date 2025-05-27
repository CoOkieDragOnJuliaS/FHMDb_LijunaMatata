package org.fhmdb.fhmdb_lijunamatata.state;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AscendingSortState implements SortState {
    @Override
    public List<Movie> sort(List<Movie> movies) {
        // Sort movies in ascending order by title
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public SortState nextState() {
        // Next state is Descending sort
        return new DescendingSortState();
    }

    @Override
    public String getButtonText() {
        return "Sort (Desc)";
    }
}
