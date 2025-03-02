## **Maven on Windows: Setup and Troubleshooting Guide**

This guide explains how to **set up and use Maven on Windows**, as well as how to run `mvn clean install` and `mvn test` in both **Command Prompt (CMD)** and **IntelliJ IDEA Terminal**.

---

## **1. Checking Maven Installation in Windows**

Before running Maven commands, make sure Maven is properly installed:

```sh
mvn -version
```

If the installation is successful, you should see output similar to this:

```
Apache Maven 3.x.x (Revision ...)
Maven home: C:\Program Files\Apache\Maven
Java version: 11.0.12, vendor: Oracle Corporation
Default locale: en_US, platform encoding: UTF-8
```

If you get an error like **"mvn is not recognized as an internal or external command"**, follow the next steps.

---

## **2. Setting Up Maven on Windows**

1. **Download Maven** from the [official website](https://maven.apache.org/download.cgi).
2. **Extract Maven** to `C:\Program Files\Apache\Maven`.
3. **Configure Environment Variables**:
    - Open **System Properties** → **Advanced** → **Environment Variables**.
    - Under **System Variables**, click **New** and add:
        - **Variable name:** `MAVEN_HOME`
        - **Variable value:** `C:\Program Files\Apache\Maven`
    - Find **Path**, click **Edit**, and **add**:
      ```
      %MAVEN_HOME%\bin
      ```
4. **Verify the installation** by reopening the command prompt and running:
   ```sh
   mvn -version
   ```

---

## **3. Running Maven Commands in Windows CMD**

### **Clean and Build the Project**
```sh
mvn clean install
```
- **`clean`** → Deletes the `target/` directory (removes previous builds).
- **`install`** → Compiles the project, runs tests, and installs the JAR in the local repository.

### **Run Tests**
```sh
mvn test
```
- Runs all unit tests in the `src/test/java/` directory.

---

## **4. Running Maven Commands in IntelliJ IDEA Terminal**

1. **Open IntelliJ IDEA** and load your project.
2. **Open Terminal**:
    - Go to **View → Tool Windows → Terminal**.
3. Run the desired Maven command, for example:
   ```sh
   mvn clean install
   mvn test
   ```

*If you see an error about missing `pom.xml`, navigate to your project root first:*
```sh
cd path\to\your\project
```

---

## **5. Common Maven Issues on Windows (Fixes)**

### **Issue 1: "mvn is not recognized as an internal or external command"**
**Solution:** Ensure the `MAVEN_HOME` and `Path` variables are correctly set and restart the terminal.

### **Issue 2: "JAVA_HOME is not set"**
**Solution:**
1. Find your Java installation path (`C:\Program Files\Java\jdk-XX`).
2. Add an environment variable:
    - **Variable name:** `JAVA_HOME`
    - **Variable value:** `C:\Program Files\Java\jdk-XX`
3. Restart the terminal and verify with:
   ```sh
   echo %JAVA_HOME%
   ```

### **Issue 3: Maven Build Fails with "OutOfMemoryError"**
**Solution:** Increase Maven memory usage:
```sh
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
```

---

## **6. Running Maven in Windows PowerShell (Alternative)**
If you prefer PowerShell instead of CMD:
1. Open **Windows PowerShell**
2. Navigate to your project folder:
   ```sh
   cd C:\path\to\your\project
   ```
3. Run Maven commands as usual:
   ```sh
   mvn clean install
   ```

---

## **7. Additional Tips for Windows Users**

**Use Git Bash for better experience** – If you have issues with Windows CMD, try **Git Bash** (comes with Git for Windows).  
**Always run CMD as Administrator** – If you face permission errors.  
**Check for Windows Defender or Antivirus blocks** – Sometimes, security software interferes with Maven builds.  
**Restart after setting environment variables** – To ensure they take effect.  
