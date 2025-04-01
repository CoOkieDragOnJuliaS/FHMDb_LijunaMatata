
# APIIntegrationGuide.md

## Purpose

This document explains how the `MovieAPI` class integrates with the remote MovieAPI to fetch, filter, and parse movie data into the application. It also describes how this API is tested using MockWebServer and JUnit.

---

## Class: `MovieAPI`

The `MovieAPI` class is responsible for:
- Building URLs with query parameters.
- Sending HTTP GET requests using `OkHttpClient`.
- Parsing JSON responses using `Gson`.
- Returning a list of `Movie` objects.
- Updating UI status messages via JavaFX labels.

### Constructor

```java
public MovieAPI(Label statusLabel, boolean isTesting)
```

- `statusLabel`: JavaFX Label used to show loading messages.
- `isTesting`: Flag to disable JavaFX UI updates during unit testing.

---

## Endpoints

- **Base URL**: `https://prog2.fh-campuswien.ac.at/movies`

### Supported Query Parameters:
- `query` – Free text search
- `genre` – Movie genre
- `releaseYear` – Year of release
- `ratingFrom` – Minimum rating

---

## Core Methods

### `fetchAllMovies()`

Fetches all available movies without filters.

```java
public List<Movie> fetchAllMovies() throws IOException
```

### `fetchMovies(...)`

Fetches filtered movie data.

```java
public List<Movie> fetchMovies(String query, String genre, Integer releaseYear, Double ratingFrom) throws IOException
```

Automatically constructs the URL with only the non-null parameters.

---

## Unit Testing with `MockWebServer`

Unit tests for the API integration are implemented in `MovieAPITest.java` using:
- `MockWebServer` from OkHttp
- `JUnit 5`
- `Gson` for manual response simulation

### Sample Tests

#### ✅ Successful fetch

Simulates a 200 OK response with JSON data:
```java
assertEquals("Inception", movies.get(0).getTitle());
```

#### ❌ API error

Simulates a 403 Forbidden response:
```java
IOException exception = assertThrows(IOException.class, ...);
```

#### ✅ Filtered request

Ensures that passing filters like genre and year still return expected data.

---

## Notes

- The class uses `Platform.runLater()` for safe JavaFX label updates.
- UI updates are disabled when `isTesting = true`.
- In production, missing the `User-Agent` header may cause HTTP 403. (Handled internally by OkHttp.)

---

## Summary

- Clean separation of concerns: HTTP logic is isolated.
- Testable with mocked environments.
- Supports flexible filtering.
- Minimal UI coupling.
