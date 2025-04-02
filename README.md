## Mirrored Template for Exercise 2
The goal of this exercise is to extend the FHMDb application by connecting it to a remote MovieAPI and enabling dynamic data loading, filtering, and stream-based analysis.
## K.O. Criteria are the following:
- The application must connect to the MovieAPI and display fetched movies in the GUI.
- The application must use a proper Java HTTP library (e.g., OkHttp) to send GET requests.
- The application must parse JSON responses using Gson or Jackson into Java objects.
- The project must be based on Maven.
- The project must include JUnit Testing, runnable with Maven.
- The project must be hosted on GitHub with all contributors added.

## Following Tasks have to be done:
- Create a `MovieAPI` class to handle HTTP requests and responses from the API.
- The `MovieAPI` class must be able to build correct URLs with query parameters (`genre`, `query`, `releaseYear`, `ratingFrom`).
- Make sure to include a `User-Agent` header in every request to avoid HTTP 403 errors.

- Adapt your existing `Movie` class to match the attributes provided by the MovieAPI.

- Update your GUI to allow users to configure query parameters such as:
  - Genre
  - Release Year
  - Rating From
  - Search Query

- On application start, load all movies from the API with no filters.
  - Example URL: `https://prog2.fh-campuswien.ac.at/movies`

- Enable dynamic API requests when the user performs a search or applies filters.
  - Example URL: `https://prog2.fh-campuswien.ac.at/movies?query=darkknight&genre=ACTION`

- Parse the returned JSON using **Gson** or **Jackson** and map the data into `Movie` objects.

- Display the parsed movies in the GUI and update the view when filters or search queries are applied.

- Implement the following methods using **Java Streams** only (no loops allowed), and write Unit Tests for each of them:

  ```java
  String getMostPopularActor(List<Movie> movies)
  // Returns the actor who appears most frequently in the mainCast list of all movies.

  int getLongestMovieTitle(List<Movie> movies)
  // Returns the length (character count) of the longest movie title.

  long countMoviesFrom(List<Movie> movies, String director)
  // Returns the number of movies directed by the specified director.

  List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear)
  // Returns a list of movies released between the given years.

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


