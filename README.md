## Mirrored Template for Exercise 1
The Goal of this Exercise is to have a functioning Movie Database for the FH Movie Database - with dummy data in the beginning implemented directly into the code structure.
## K.O. Criteria are the following:
  .) The Project has to have a GUI showing the database movies</br>
  .) The Project has to have proper JUnit Testing, possible to start directly from Maven</br>
  .) The Project has to be based on Maven</br>
  .) The Project has to be done in GitHub with all the Project teammembers set as Contributors</br>

## Following Tasks have to be done:
  .) Create the dummy database with a few movies to pick from and work with</br>
   >   .) The methods name is initializeMovies() and returns a List of Movie Objects
> 
> The Movie object consists of following attributes:
> ![img.png](movieObject.png)
      
  .) Make sure the movies are shown in the correct order from the beginning (shown on the screen with the Sort Button)
   >   .) Pressing the Sort Button has to go From Ascended to Descended and back (with each press)</br>
   >   .) Sorting algorithm is updated via the title of the movie
      
  .) Implement the Feature to Search the Database with a String according to the Exercise Sheet
  >  .) The search has to be updated continously after pressing a button or writing something in the text field</br>
  >  .) The search query has to be inside the title and/or inside the description</br>
  >  .) The search query is NOT case sensitive
      
  .) Implement the feature to filter the Database with a Genre according to the Exercise Sheet
   >   .) The filtering logic can only work after pressing the button on the right</br>
   >   .) Filtering can be undone (option to none in the Dropdown menu or deactivate the filter somehow)
      
  .) Have a logic that updates the given movies, filtered and searched for, on the screen (view)

  .) Implement a Testing Logic for every method / at least 90% Code Coverage
  >    .) Testing the individual sorting, searching and filtering algorithms is mandatory</br>
  >    .) JUnit Tests can be automatically started with maven (mvn test) - implementing the habit to do so (internet)

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
      

## Start the application
Start the application by either running the `main` method in the `FHMDbApplication` class.

## Documentation

For detailed setup and troubleshooting, refer to the following documentation:

- [Maven Setup Guide](docs/MavenSetup.md) – Instructions for installing and configuring Maven.
- [Maven POM.xml Documentation](docs/MavenPomExplanation.md) – Explanation of the `pom.xml` file and dependencies.
- [Maven Testing Guide](docs/MavenTesting.md) – How to implement and run tests with Maven and JUnit.
- [Maven Windows Guide](docs/MavenWindowsGuide.md) – Steps for running Maven on Windows and troubleshooting issues.


