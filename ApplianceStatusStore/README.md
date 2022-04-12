# Appliance Status Store

Application that accepts ping requests from [Appliances](../Appliance/README.md), Updates Appliance
Status and returns list of Appliance Details with Appliance User and Connectivity Status.

### Ping Endpoint

    /ping/{applianceId}

Successful Response: HTTP 200

### Appliance Details Endpoint

    /appliances?page=0&size=25&sortBy=id&sortDirection=ASC

Successful Response:

```json
{
  "content": [
    {
      "name": "Haralds Värdetransporter AB",
      "address": "Budgetvägen 1, 333 33 Uppsala",
      "applianceId": "VLUR4X20005387055",
      "factoryNr": "STU901",
      "applianceStatus": "CONNECTED"
    },
    {
      "name": "Kalles Grustransporter AB",
      "address": "Cementvägen 8, 111 11 Södertälje",
      "applianceId": "VLUR4X20009048066",
      "factoryNr": "GHI789",
      "applianceStatus": "DISCONNECTED"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 25,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 7,
  "size": 25,
  "number": 0,
  "empty": false
}
```

### Database

Run Database in a docker container:

```docker run --name mysql -e MYSQL_ROOT_PASSWORD=electrolux -d mysql:latest```

### Build

```
./gradlew clean build
```

### Build Docker Image

In order to dockerize the application, the property ```spring.datasource.url``` in
the ```application.properties```
to ```jdbc:mysql://host.docker.internal:3306/appliance_status?serverTimezone=UTC&useSSL=false``` and
then execute ```./gradlew clean build```

```
docker build -t appliance-status-store:1.0 .
```

### Run Docker Container

```
docker run --name appliance-status-store --add-host=host.docker.internal:host-gateway -p 8080:8080 -d appliance-status-store:1.0
```

### Improvements

* Add JavaDoc
* Improve Performance by Adding In Memory Key-Value store to store the ping requests and update
  database in the background
* Add repository level tests
* add testcontainer for running tests on mysql database instead of h2