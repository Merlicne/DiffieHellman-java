# DiffieHellman-java

## Overview

This project is a Java implementation of the Diffie-Hellman key exchange algorithm using the Spring Boot framework and Gradle for build automation.

## Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/merlicne/DiffieHellman-java.git
    cd DiffieHellman-java
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

3. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Usage

After starting the application, you can access the Diffie-Hellman key exchange endpoints via HTTP requests. The base URL is `http://localhost:8080`.

### Example Endpoints

- **Compute Shared Secret:**
    ```http
    POST /api/diffie-hellman/compute
    Content-Type: application/json

    {
        "base": "<BASE36_NUMBER>",
        "modulus": "<BASE36_NUMBER>",
        "publicKey": "<BASE36_NUMBER>"
    }
    ```