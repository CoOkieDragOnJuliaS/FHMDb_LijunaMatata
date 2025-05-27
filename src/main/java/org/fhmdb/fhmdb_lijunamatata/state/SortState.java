package org.fhmdb.fhmdb_lijunamatata.state;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

public interface SortState {
    List<Movie> sort(List<Movie> movies);
    SortState nextState();
    String getButtonText();
}
