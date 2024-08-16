# Trip Microservice

## Introduction
The 'Trip' microservice is part of a larger project to find "Chained trip" between two airports. This documentation covers the technical details of this microservice only.  
[The overall project description](https://medium.com/@vidime.sa.buduci.rok/explore-europe-by-plane-using-this-tool-0cb52ac69b8b).

## Purpose Overview
The microservice looks for all feasible routes which satisfy the given parameters. This is the main logical core of the
project designed to find adequate routes effectively.

## Technologies Used
- Java 21
- Spring Boot
- Gradle
- PostgreSQL

## Architecture Design
![Architecture design](./images/TripService.jpg)

### Description
The architecture design shows the interaction between the API Gateway and the internal service's components.
The Trip Search Service utilizes modified DFS algorithm or Heuristic traversal algorithm depending on search time to 
find feasible paths, and the Departure Service retrieves flight data from the Flight microservice.

## Endpoints

### Send request
#### URL
`POST /api/v1/trips`

#### Description
Constructs several feasible routes within the given criteria. Requires UI-significant amount of time.

#### Parameters
| Name               | Type          | Required | Default    | Description                                                |
|--------------------|---------------|----------|------------|------------------------------------------------------------|
| `origin`           | String        | Yes      | None       | The IATA code of the departure airport                     |
| `destination`      | String        | No       | `origin`   | The IATA code of the destination airport                   |
| `departureAt`      | String        | Yes      | None       | The date to start the trip in the format `yyyy-MM-dd`      |
| `returnBefore`     | String        | No       | 3000-01-01 | The date to end the trip before in the format `yyyy-MM-dd` |
| `budget`           | double        | Yes      | None       | Maximum amount of money spent on trips                     |
| `maxStay`          | int           | No       | 1          | Maximum days between two flights                           |
| `minStay`          | int           | No       | 1          | Minimum days between two flights                           |
| `schengenOnly`     | boolean       | No       | false      | If `true`, only includes flights within the Schengen Area  |
| `timeLimitSeconds` | int           | No       | 10         | Working time for an algorithm to find feasible trips       |
| `excludedAirports` | Array<String> | No       | None       | Airports to avoid during search                            |
| `userId`           | UUID``        | Yes      | None       | Unique user identifier the search is associated with       |

#### Responses
- **200 OK**
    - **Description**: Successfully found at least one feasible path.
    - **Body**: `null`
- **204 No Content**
    - **Description**: No feasible routes found.
    - **Body**: `null`
- **400 Bad Request**
    - **Description**: Missing or invalid parameters.
    - **Body**: `null`
- **500 Internal Server Error**
    - **Description**: An unexpected error occurred on the server.
    - **Body**: `null`
#### Example request
```bash
curl -X POST http://localhost:60001/api/v1/trips \
-H "Content-Type: application/json" \
-d '{
    "origin": "BGY",
    "departureAt": "2024-10-10",
    "budget": 300,
    "timeLimitSeconds": 100,
    "schengenOnly": "true",
    "userId": "<UUID>"
}'
```

### Get preview
#### URL
`GET /api/v1/trips/preview`

#### Description
Retrieves general information about all found trips for user

#### Parameters
| Name               | Type    | Required | Default    | Description                                                |
|--------------------|---------|----------|------------|------------------------------------------------------------|
| `userId`           | UUID    | No       | None       | Specific user to return all related search results         |

#### Responses
- **200 OK**
  - **Description**: Found at least one result
- **Body**:
  ```json
  [
    {
      "totalPrice": "double",
      "totalFlights": "int",
      "uniqueCities": "int",
      "uniqueCountries": "int",
      "departureAt": "ISO 8601 date-time",
      "arrivalAt": "ISO 8601 date-time",
      "requestId": "UUID"
    },
    ...
  ]
  ```
  
- **204 No Content**
  - **Description**: No results found for the specified request/user
  - **Body**: `null`
- **500 Internal Server Error**
  - **Description**: Data retrieval error
  - **Body**: `null`

#### Example request
```bash
curl -X GET "http://localhost:60001/api/v1/trips/preview?userId=<UUID>"
```

#### Example successful response
```json
[
    {
        "totalPrice": 75.96,
        "totalFlights": 4,
        "uniqueCities": 3,
        "uniqueCountries": 2,
        "departureAt": "2024-11-11T06:00:00",
        "arrivalAt": "2024-11-14T08:15:00",
        "requestId": "d5faa357-eb00-456d-afd8-37e0ec4f1fa7"
    },
    {
        "totalPrice": 134.8,
        "totalFlights": 6,
        "uniqueCities": 4,
        "uniqueCountries": 3,
        "departureAt": "2024-11-11T06:00:00",
        "arrivalAt": "2024-11-16T13:50:00",
        "requestId": "d5faa357-eb00-456d-afd8-37e0ec4f1fa7"
    },
    ...
]
```

### Get results
#### URL
`GET /api/v1/trips/{requestId}`

#### Description
Retrieve the whole results information for user or specific request

#### Parameters
| Name        | Type    | Required | Default    | Description                                   |
|-------------|---------|----------|------------|-----------------------------------------------|
| `requestId` | UUID    | Yes      | None       | Specific request to return all search results |

#### Responses
- **200 OK**
  - **Description**: Found at least one result
  - **Body**:
  ```json
  [
    {
      "totalPrice": "double",
      "totalFlights": "int",
      "uniqueCities": "int",
      "uniqueCountries": "int",
      "departureAt": "ISO 8601 date-time",
      "arrivalAt": "ISO 8601 date-time",
      "requestId": "UUID",
      "tripSchedule": [
        {
          "flightNumber": "string (IATA airline designator followed by numeric identifier)",
          "departureAt": "ISO 8601 date-time",
          "originAirportName": "String",
          "originAirportCode": "IATA code",
          "originCountryCode": "ISO 3166 country code",
          "destinationAirportName": "String",
          "destinationAirportCode": "IATA code",
          "destinationCountryCode": "ISO 3166 country code",
          "price": "double",
          "currencyCode": "ISO 4217 currency code"
        },
        ...
      ]
    },
    ...
  ]
  ```
- **204 No Content**:
  - **Description**: No data found for the given parameters
  - **Body**: `null`
- **400 Bad Request**
  - **Description**: Parameters are absent or in bad format
  - **Body**: `null`
- **500 Internal Server Error**
  - **Description**: Data retrieval error
  - **Body**: `null`

#### Example request
```bash
curl -X GET http://localhost:60001/api/v1/trips?userId=628baaa4-4052-4a07-bd2e-badce6fa9b40
```

#### Example successful response
```json
[
  {
    "totalPrice": 171.96,
    "totalFlights": 3,
    "uniqueCities": 3,
    "uniqueCountries": 3,
    "departureAt": "2024-09-11T05:55:00",
    "arrivalAt": "2024-09-13T12:05:00",
    "requestId": "61ac0848-f96c-4b23-acaf-b0d87e664a0d",
    "tripSchedule": [
      {
        "flightNumber": "FR1055",
        "departureAt": "2024-09-12T15:10:00",
        "originAirportName": "Brussels Charleroi",
        "originAirportCode": "CRL",
        "originCountryCode": "be",
        "destinationAirportName": "Warsaw Modlin",
        "destinationAirportCode": "WMI",
        "destinationCountryCode": "pl",
        "price": 14.99,
        "currencyCode": "EUR"
      },
      {
        "flightNumber": "FR87",
        "departureAt": "2024-09-11T05:55:00",
        "originAirportName": "Vienna",
        "originAirportCode": "VIE",
        "originCountryCode": "at",
        "destinationAirportName": "Brussels Charleroi",
        "destinationAirportCode": "CRL",
        "destinationCountryCode": "be",
        "price": 14.99,
        "currencyCode": "EUR"
      },
      ...
    ]
  },
  ...
]
```

### Get requests

#### URL
`GET /api/v1/trips/requests/{userId}`

#### Description 
Retrieve all user's requests

#### Parameters
| Name     | Type    | Required | Default    | Description                                          |
|----------|---------|----------|------------|------------------------------------------------------|
| `userId` | UUID    | Yes      | None       | Unique user identifier to return all search requests |

#### Responses
- **200 OK**
  - **Description**: At least one request was found
  - **Body**:
  ```json
    [
      {
      "userId": "UUID",
      "origin": "IATA code",
      "destination": "IATA code",
      "departureAt": "ISO 8601 date-time",
      "returnBefore": "ISO 8601 date-time",
      "budget": "double",
      "maxStay": "int",
      "minStay": "int",
      "schengenOnly": "boolean",
      "limitTimeSeconds": "int",
      "excludedAirports": "List<IATA code>"
      },
      ...
    ]
  ```
- **204 No Content**
  - **Description**: No request was found for the specified user
  - **Body**: `null`
- **500 Internal Server Error**
  - **Description**: Data retrieval error
  - **Body**: `null`

[//]: # (## How to run)

[//]: # (### Prerequisites )

[//]: # (- Java 21)

[//]: # (- Gradle)

[//]: # (- Docker)

[//]: # (### Run script)

[//]: # (```bash)

[//]: # (#!/bin/bash)

[//]: # (# Clone the repository)

[//]: # (git clone https://github.com/xbiletskyi/TripService)

[//]: # (cd TripService)

[//]: # ()
[//]: # (# Build the Docker image)

[//]: # (docker build -t tripservice:latest .)

[//]: # ()
[//]: # (# Run the FindRoute container)

[//]: # (docker run -d -p 60001:8080 --name tripservice tripservice:latest)

[//]: # ()
[//]: # (# Display running containers)

[//]: # (docker ps)

[//]: # (```)