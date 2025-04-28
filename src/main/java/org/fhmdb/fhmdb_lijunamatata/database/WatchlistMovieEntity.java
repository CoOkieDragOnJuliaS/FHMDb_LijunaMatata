package org.fhmdb.fhmdb_lijunamatata.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField()
    private String apiId;

    // --- Getter for id ---
    public long getId() {
        return id;
    }

    // --- Getter for apiId ---
    public String getApiId() {
        return apiId;
    }

    // --- Setter for apiId ---
    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
