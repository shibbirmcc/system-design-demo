### Build

    ```
    ./gradlew clean shadowJar
    ```

### Build Docker Image

    ```
    docker build -t appliance:1.0 .
    ```

### Run Docker Container

    ```
    docker run --name appliance -e PING_SERVER_URL='http://host.docker.internal:8080/ping/VLUR4X20005387055' --add-host=host.docker.internal:host-gateway -d appliance:1.0
    ```


### Improvements
* Add Unit test 
* Using testcontainer to launch [ApplianceStatusStore](../ApplianceStatusStore/README.md) and Database to execute intergation testing.