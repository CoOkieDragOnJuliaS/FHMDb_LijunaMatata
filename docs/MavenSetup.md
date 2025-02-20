# Maven Setup Guide

This guide explains how to install and configure **Apache Maven** for this project.  
Maven is used for **building, testing, and managing dependencies**.

## 1. Checking Maven Installation

Before installing, check if Maven is already installed:

```sh
mvn -version
```

If Maven is installed, you will see output similar to this:

```
Apache Maven 3.x.x (Revision ...)
Maven home: C:\Program Files\Apache\Maven
Java version: 11.0.12, vendor: Oracle Corporation
Default locale: en_US, platform encoding: UTF-8
```

If not, follow the installation steps below.

---

## 2. Installing Maven

### **Windows**

1. Download Maven from the [official website](https://maven.apache.org/download.cgi).
2. Extract it to a convenient location, e.g., `C:\Program Files\Apache\Maven`.
3. Add the following system environment variables:
    - `MAVEN_HOME` → `C:\Program Files\Apache\Maven`
    - Add `%MAVEN_HOME%\bin` to the system `Path`.
4. Restart the terminal and verify installation:

```sh
mvn -version
```

---

### **Mac/Linux**

#### **Mac (via Homebrew)**

```sh
brew install maven
```

#### **Ubuntu/Debian**

```sh
sudo apt update && sudo apt install maven
```

#### **Verify Installation**

After installation, check if Maven is correctly installed:

```sh
mvn -version
```

If the installation was successful, you should see a message confirming the Maven version.