## Mirrored Template for Exercise 3
The goal of this exercise is to extend the FHMDb application by introducing a multi-layered architecture (UI Layer, Business/Logic Layer, Data Layer), adding persistence with an H2 database using ORMLite, and implementing robust exception handling.
## K.O. Criteria are the following:
- The project must include a GUI (no console applications allowed).
- The project must be based on Maven.
- The project must be available on GitHub with public access.
- If any K.O. criterion is not met, the submission will be evaluated as failed.
## Tasks

### Presentation Layer

- Implement navigation between the **MovieScreen** (all movies) and the **WatchlistScreen** (saved movies).
- In the **MovieScreen**, allow users to **add movies to the Watchlist** (e.g., via a button).
- In the **WatchlistScreen**, allow users to **remove movies from the Watchlist**.

### Data Layer

- Implement the following classes:
  - `MovieEntity`: Represents movie data to be stored in the database.  
    (Lists like directors, writers, and mainCast are excluded; genres are stored as comma-separated strings.)
  - `WatchlistMovieEntity`: Represents entries in the Watchlist (stores only the API ID of the movie).
  - `DatabaseManager`: Manages the database connection and DAO instances.
  - `MovieRepository` and `WatchlistRepository`: Provide functions for:
    - Retrieving all entries from the respective database tables.
    - Deleting all or specific entries.
    - Adding a movie to the Watchlist (only if it does not already exist).

### Business/Logic Layer

- Extend the **Controller** to interact between the UI and the Data Layer.
- When a user clicks **"Add to Watchlist"** or **"Remove from Watchlist"**, handle the action using a **Lambda Expression**.
- Create a `ClickEventHandler` functional interface with the method:
```java
void onClick(T t);
```
Example usage:

## In MovieCell class:
```java
watchlistBtn.setOnMouseClicked(mouseEvent -> {
    addToWatchlistClicked.onClick(getItem());
});
```
## In the Controller:
```java
private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
    // code to add movie to watchlist
};
```
### Exception Handling
- Implement two custom exception classes:
  - `DatabaseException`
  - `MovieApiException`
- Properly propagate exceptions:
  - Data Layer exceptions → `DatabaseException`
  - API Layer exceptions → `MovieApiException`
- Handle exceptions in the **Controller**.

- Display user-friendly error messages in the GUI.
- If the MovieAPI is unavailable, fallback to cached movies from the database.
- The application must never crash and should always provide meaningful feedback to users.

## Coding Conventions:
  .) Naming Conventions are in English, camelCase is mandatory</br>
  .) Beware of method/function names and variables - state that it is clear what the variable does or the function/method</br>
  .) methodNames start with small letters and class names start with big letters</br>
  .) Write JavaDoc commentary for methods to ensure clear aspect

## How to commit/push a code to the branch:
  > .) checkout the branch you need to work on</br>
> .) work on the branch, and change code</br>
  > .) go to the commit window, and mark the changes you want to commit (after testing and if there are NO bugs/errors)</br>
  > .) Commit message template:  Exercise and number of Exercise [name] commit message</br>
  > Example: </br>
  > [ Exercise$nr$ [$name$] tag: $commit-message$ ] --> Exercise$1$ [$Julia$]  $bugfix$: had problems, i had to fix ]</br>
  >> Tags: </br>
  > .) $feature$: Commits, that add or remove a new feature to the API or UI</br>
    .) $bugfix$: Commits, that fix a API or UI bug of a preceded feat commit</br>
    .) $refactor$: Commits, that rewrite/restructure your code, however do not change any API or UI behaviour</br>
    .) $style$: Commits, that do not affect the meaning (white-space, formatting, missing semi-colons, etc)</br>
    .) $test$: Commits, that add missing tests or correcting existing tests</br>
    .) $docs$: Commits, that affect documentation only</br>
    .) $build$: Commits, that affect build components like build tool, ci pipeline, dependencies, project version, ...</br>
    .) $chore$: Miscellaneous commits e.g. modifying .gitignore</br>
> 
  > FIRST: update the branch by right click & update</br></br>
  > THEN: push the committed code if no merge conflicts come up</br></br>
  > IF there are merge conflicts -> first merge them, test and bugfix again and afterwards commit & push anew
      
## Start the Application

You can start the application using one of the following methods:

### 1. Running from IntelliJ IDEA  
1. Open **IntelliJ IDEA** and load the project.
2. Navigate to `src/main/java/org/fhmdb/fhmdb_lijunamatata/FHMDbApplication.java`.
3. Right-click the `FHMDbApplication` class and select **Run 'FHMDbApplication'**.

### 2. Running from the Command Line with Maven  
Ensure that **Maven** is installed and properly configured.  
Run the following command from the project root directory:

```sh
mvn clean javafx:run
```
This will clean previous builds and launch the JavaFX application.

### 3. Running from a Packaged JAR (After Building)  
If you have already built the project using Maven (`mvn clean install`), you can run the generated `.jar` file:

```sh
java -jar target/FHMDb_LijunaMatata-1.0-SNAPSHOT.jar
```
Replace the JAR name with the actual generated filename if different.

### Troubleshooting  
- If the application does not start, ensure you have the correct **JavaFX** dependencies.  
- Check that **Java 16+** is installed and properly set in `JAVA_HOME`.  
- Refer to the [Maven Windows Guide](docs/MavenWindowsGuide.md) if you're facing issues on Windows.  

## Documentation

For detailed setup and troubleshooting, refer to the following documentation:

- [Maven Setup Guide](docs/MavenSetup.md) – Instructions for installing and configuring Maven.
- [Maven POM.xml Documentation](docs/MavenPomExplanation.md) – Explanation of the `pom.xml` file and dependencies.
- [Maven Testing Guide](docs/MavenTesting.md) – How to implement and run tests with Maven and JUnit.
- [Maven Windows Guide](docs/MavenWindowsGuide.md) – Steps for running Maven on Windows and troubleshooting issues.


