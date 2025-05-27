package org.fhmdb.fhmdb_lijunamatata.state;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

public class UnsortedState implements SortState {
    @Override
    public List<Movie> sort(List<Movie> movies) {
        // Return movies as they are (no sorting)
        return movies;
    }

    @Override
    public SortState nextState() {
        // Next state is Ascending sort
        return new AscendingSortState();
    }

    @Override
    public String getButtonText() {
        return "Sort (Asc)";
    }
}
