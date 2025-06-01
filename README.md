## Mirrored Template for Exercise 4
This exercise extends the FHMDb application by integrating **Software Design Patterns** into the application structure and refactoring existing functionality accordingly. It also focuses on applying **SOLID principles** to improve software architecture and maintainability.
## Goal

The aim of this exercise is to:
- Understand and apply common design patterns in a JavaFX application.
- Apply and identify SOLID principles in code.
- Refactor the codebase for better maintainability and modularity.

## K.O. Criteria (Must be fulfilled)

- The project **must include a GUI** (no console applications).
- The project **must use Maven**.
- The project **must be available on GitHub** and **public**.

### State Pattern

- Implement sorting logic (unsorted, ascending, descending) using the **State Pattern**.
- Sorting should persist even after filtering.
- The logic for state transitions must be encapsulated in **dedicated state classes**, not in the controller.

### Builder Pattern

- Refactor URL construction for `MovieAPI` requests using the **Builder Pattern**.
- Support flexible filter chaining:
  ```java
  String url = new MovieAPIRequestBuilder(base)
                  .query("word")
                  .genre("ACTION")
                  .releaseYear("2012")
                  .ratingFrom("8.3")
                  .build();
  ```
### Singleton Pattern

- Implement all repository classes (e.g., `MovieRepository`, `WatchlistRepository`) using the Singleton Pattern to ensure a single shared instance.

## Observer Pattern

- Notify the UI when a movie is successfully added to the Watchlist or if it already exists.
- `WatchlistRepository` should be the Observable.
- Controller classes act as Observers.
- Create Observable and Observer interfaces and implement them properly.
  
### Factory Pattern
- JavaFX controllers should be instantiated via a Controller Factory to ensure a Singleton instance.
- Implement a custom MyFactory that returns the same controller instance:
```java
FXMLLoader loader = new FXMLLoader(...);
loader.setControllerFactory(new MyFactory());
  ```
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


