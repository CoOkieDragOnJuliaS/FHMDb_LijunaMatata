### Maven POM.xml Documentation

This document explains the structure of the **`pom.xml`** file used in this project.  
Maven is responsible for **building**, **testing**, **resolving dependencies**, and **running** the JavaFX application.

---

## Project Metadata

The following section defines the **basic project information**:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.fhmdb</groupId>
    <artifactId>FHMDb_LijunaMatata</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>FHMDb_LijunaMatata</name>
</project>
```
- **`groupId`** – The project's unique identifier (`org.fhmdb`).
- **`artifactId`** – The project's name (`FHMDb_LijunaMatata`).
- **`version`** – The current version (`1.0-SNAPSHOT`).

---

## Project Properties

These properties define **configurable values** used in the project:

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.10.2</junit.version>
</properties>
```
- **`project.build.sourceEncoding`** – Ensures **UTF-8** encoding is used.
- **`junit.version`** – Stores the **JUnit version** used in tests.

---

## Dependencies

This section defines **external libraries** required for the project.

### JavaFX & UI Frameworks

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>17.0.6</version>
</dependency>

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>17.0.6</version>
</dependency>

<dependency>
    <groupId>org.controlsfx</groupId>
    <artifactId>controlsfx</artifactId>
    <version>11.2.1</version>
</dependency>

<dependency>
    <groupId>com.dlsc.formsfx</groupId>
    <artifactId>formsfx-core</artifactId>
    <version>11.6.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.openjfx</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>net.synedra</groupId>
    <artifactId>validatorfx</artifactId>
    <version>0.5.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.openjfx</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.kordamp.ikonli</groupId>
    <artifactId>ikonli-javafx</artifactId>
    <version>12.3.1</version>
</dependency>
```

These dependencies enhance the JavaFX GUI with components, validation, and icons.

---

### API & JSON Parsing

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.9.0</version>
</dependency>
```

- `okhttp` is used to send HTTP requests to the MovieAPI.
- `gson` is used to parse JSON responses into Java objects.

---

### Testing Dependencies

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>${junit.version}</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>${junit.version}</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>${junit.version}</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>mockwebserver</artifactId>
    <version>4.12.0</version>
    <scope>test</scope>
</dependency>
```

These libraries enable modern unit testing, mocking, and mocking HTTP server responses.

---

## Build Plugins

This section **configures Maven plugins** for compiling and running the application.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>16</source>
                <target>16</target>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.5.2</version>
        </plugin>

        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.8</version>
            <executions>
                <execution>
                    <id>default-cli</id>
                    <configuration>
                        <mainClass>org.fhmdb.fhmdb_lijunamatata.FHMDbApplication</mainClass>
                        <launcher>app</launcher>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- **`maven-compiler-plugin`** – Ensures Java **16** is used.
- **`maven-surefire-plugin`** – Runs unit tests.
- **`javafx-maven-plugin`** – Allows **JavaFX apps** to run via Maven.

---

## Running the Project

After setting up Maven, you can start the project using:

```sh
mvn clean javafx:run
```
This will **compile and launch** the JavaFX application.  