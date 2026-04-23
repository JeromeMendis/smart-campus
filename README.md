Smart Campus REST API
=====================

A RESTful API built with JAX-RS (Jersey) and Apache Tomcat for managing campus rooms, sensors, and sensor readings. All data is stored in-memory using ConcurrentHashMaps - no database required.


Technology Stack
----------------
- Java 23
- JAX-RS (Jersey 2.41)
- Apache Tomcat 7 (via Maven plugin)
- Jackson (JSON serialization)
- Maven


Project Structure
-----------------
src/main/java/com/smartcampus/
  SmartCampusApplication.java
  datastore/
    DataStore.java
  model/
    Room.java
    Sensor.java
    SensorReading.java
  resource/
    DiscoveryResource.java
    RoomResource.java
    SensorResource.java
    SensorReadingResource.java
  exception/
    RoomNotEmptyException.java
    LinkedResourceNotFoundException.java
    SensorUnavailableException.java
  mapper/
    RoomNotEmptyExceptionMapper.java
    LinkedResourceNotFoundExceptionMapper.java
    SensorUnavailableExceptionMapper.java
    GlobalExceptionMapper.java
  filter/
    LoggingFilter.java

src/main/webapp/WEB-INF/
  web.xml


How to Build
------------
Make sure you have Java JDK 11+ and Maven 3.x installed.

  mvn clean package


How to Run
----------
  mvn clean package tomcat7:run

Server starts at: http://localhost:8080/api/v1/

Note: Data is stored in memory. All data is lost when the server stops.


API Endpoints
-------------
GET    /api/v1/                              Discovery endpoint (HATEOAS links)
GET    /api/v1/rooms                         Get all rooms
POST   /api/v1/rooms                         Create a new room
GET    /api/v1/rooms/{roomId}                Get a single room
DELETE /api/v1/rooms/{roomId}                Delete a room (only if no sensors)
GET    /api/v1/sensors                       Get all sensors
GET    /api/v1/sensors?type={type}           Filter sensors by type
POST   /api/v1/sensors                       Create a new sensor
GET    /api/v1/sensors/{sensorId}/readings   Get all readings for a sensor
POST   /api/v1/sensors/{sensorId}/readings   Add a reading for a sensor


curl Examples
-------------

1. Discovery Endpoint
   curl -X GET http://localhost:8080/api/v1/

2. Create a Room
   curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":50}"

3. Get All Rooms
   curl -X GET http://localhost:8080/api/v1/rooms

4. Get a Single Room
   curl -X GET http://localhost:8080/api/v1/rooms/LIB-301

5. Create a Sensor
   curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"roomId\":\"LIB-301\"}"

6. Get All Sensors
   curl -X GET http://localhost:8080/api/v1/sensors

7. Filter Sensors by Type
   curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"

8. Add a Sensor Reading
   curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{\"value\":22.5}"

9. Get All Readings for a Sensor
   curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings

10. Delete a Room with Sensors (returns 409 Conflict)
    curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301

11. Create a Sensor with Invalid RoomId (returns 422)
    curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-999\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"roomId\":\"FAKE-ROOM\"}"

12. Post Reading to a MAINTENANCE Sensor (returns 403)
    curl -X POST http://localhost:8080/api/v1/sensors/CO2-001/readings -H "Content-Type: application/json" -d "{\"value\":500.0}"


Error Responses
---------------
400 - Bad Request          Missing required fields
403 - Forbidden            Sensor is in MAINTENANCE status
404 - Not Found            Room or sensor does not exist
409 - Conflict             Deleting a room that still has sensors
415 - Unsupported Media    Wrong Content-Type header sent
422 - Unprocessable Entity Sensor references a roomId that does not exist
500 - Internal Server Error Unexpected server error