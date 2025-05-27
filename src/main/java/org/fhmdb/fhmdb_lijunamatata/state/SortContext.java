package org.fhmdb.fhmdb_lijunamatata.state;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

public class SortContext {
    private SortState currentState;

    public SortContext() {
        // Start with unsorted state of API
        this.currentState = new UnsortedState();
    }

    public List<Movie> sort(List<Movie> movies) {
        return currentState.sort(movies);
    }

    public void nextState() {
        currentState = currentState.nextState();
    }

    public String getButtonText() {
        return currentState.getButtonText();
    }

    public boolean isUnsorted() {
        return currentState instanceof UnsortedState;
    }
}
