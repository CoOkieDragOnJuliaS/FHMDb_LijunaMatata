# Maven Testing Guide

This guide explains how to **write, implement, and run tests** using **Maven and JUnit 5** in this project.

---

## 1. Setting Up Testing Dependencies

Maven uses **JUnit 5** for testing. Ensure the following dependencies are included in your `pom.xml`:

```xml
<dependencies>
    <!-- JUnit API for writing tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit Engine for running tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 2. Writing a Basic JUnit Test

Create a **test class** in `src/test/java/org/fhmdb/fhmdb_lijunamatata`:

```java
package org.fhmdb.fhmdb_lijunamatata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTest {

    @Test
    void testAddition() {
        int result = 2 + 3;
        assertEquals(5, result, "2 + 3 should equal 5");
    }
}
```

**Assertions:**
- `assertEquals(expected, actual)` → Checks if values are equal.
- `assertTrue(condition)` → Passes if the condition is `true`.
- `assertFalse(condition)` → Passes if the condition is `false`.

---

## 3. Running Tests in Maven

### Run All Tests:
Use the following command in the **IntelliJ Terminal** or **command line**:

```sh
mvn test
```

### Run a Specific Test Class:
```sh
mvn -Dtest=ExampleTest test
```

### Run a Specific Method in a Test Class:
```sh
mvn -Dtest=ExampleTest#testAddition test
```

---

## 4. Maven Surefire Plugin Configuration

If tests are not running, ensure **Maven Surefire Plugin** is configured in `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.5.2</version>
        </plugin>
    </plugins>
</build>
```

---

## 5. Best Practices for Maven Testing

**Follow package structure:** Tests should mirror the structure of `src/main/test/java/`.  
**Use meaningful test names:** Method names should describe the behavior being tested.  
**Keep tests independent:** Avoid dependencies between test cases.

---

## 6. Running Tests in IntelliJ IDEA

1. **Open** `src/test/java/org`.
2. **Left-click** on a needed package.
3. **Right-click** on **FHMDbControllerTest** or **MovieServiceTest**.
4. **Run** and view results in the **JUnit tab**.

---

## 7. Running Tests in CI/CD (Optional)

For continuous integration, add the following step in a GitHub Actions workflow:

```yaml
- name: Run Maven Tests
  run: mvn test
```