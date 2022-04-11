### Build

    ```
    ./gradlew clean build
    ```

### Build Docker Image

    ```
    docker build -t appliance-status-store:1.0 .
    ```

### Run Docker Container

    ```
    docker run --name appliance-status-store --add-host=host.docker.internal:host-gateway -p 8080:8080 -d appliance-status-store:1.0
    ``