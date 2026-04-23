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




ANSWERS FOR QUESTIONS 

Part 1: Service Architecture & Setup (10 Marks) 
1.	Project & Application Configuration (5 Marks):

Answer: 
The default JAX-RS configuration works with a new instance of a resource class being created with each incoming HTTP request. This per-request scope implies that there is no sharing of instance variables among requests and therefore, any shared state must exist outside the resource class. A specialized DataStore class is used in this project, with three static ConcurrentHashMap variables, one of the rooms, one of the sensors, and one of the sensor readings. The use of ConcurrentHashMap instead of just a plain HashMap was due to the fact that the Grizzly HTTP server issues concurrent requests on multiple threads. ConcurrentHashMap offers concurrent read and write operations internally, avoiding race conditions and data corruption without needing explicit synchronisation blocks in the resource methods.




2.	The “Discovery” Endpoint (5 Marks):

Answer: 
HATEOAS (Hypermedia as the Engine of Application State) is a constraint of REST in which all responses in an API contain hyperlinks to other related resources and actions allowed. Rather than writing code to hard-code the URLs in the client code or relying purely on external documentation, clients can use the API dynamically by following the links in the responses - just as a user would navigate a web site by clicking hyperlinks. The discovery endpoint in this project provides links to /api/v1/rooms, and /api/v1/sensors, enabling a client to access all major collections at one endpoint. This also minimizes the coupling between the client and the server: when the server modifies a URL in later versions, the clients who use the hypermedia links will automatically discover the new URL and not break.











Part 2: Room Management(20Marks) 
1.	RoomResource Implementation (10 Marks):

Answer:
Returning IDs only reduces the size of response payloads, minimizing bandwidth usage, and is useful when collections are large, or when clients only require a reference to choose one item to further detail. But it makes the client send a different GET request to each ID it desires to access, so that the number of round trips is increased, and latency is incurred. Full object returns provide all data in a single response, which is more efficient when the client frequently requires the details, but adds to the payload size even when the detail is not required. The desirable solution in this API is that the collection response should contain complete objects, as the extra fields (name, capacity, sensorIds) are very small, and nearly always required by a client displaying a list of rooms.


2.	RoomDeletion & Safety Logic (10 Marks):

Answer: 
Yes, the DELETE is idempotent here. The initial DELETE request of an already existing room eliminates it in the DataStore and sends 204 No Content. Further DELETE requests to the same room do not have a match and get a 404 Not Found response. Though the HTTP status code of the initial and the subsequent call is different, the state of the server remains the same after both: the room is not present in the data store. Idempotency holds that when an identical request is repeated, a server-side effect is produced (not necessarily the same response code). Since the same side effect is not seen on the second or third call, the operation meets the definition of idempotency in RFC 9110.

Part 3: Sensor Operations & Linking (20 Marks) 
1.	Sensor Resource & Integrity (10 Marks):
Answer:
JAX-RS checks the Content-Type of the request received and invokes the resource method. When the header value does not correspond to any of the given media types in an @Consumes annotation, e.g., when a client transmits text/plain or application/xml to a resource with an endpoint marked application/json, the JAX-RS runtime sends an error response of HTTP 415 Unsupported Media Type. There is no code required to be a developer to deal with this case; it is purely enforced by the framework. This not only defends the resource methods against receiving data they are unable to deserialise but also gives API consumers an understandable and standards-conformant error signal.
2.	Filtered Retrieval & Search (10 Marks):

Answer:
Path-based filtering (e.g. /api/v1/sensors/type/CO2) is not as good as using an @QueryParam (e.g. /api/v1/sensors?type=CO2) because of a number of reasons. First, query parameters are optional by default: the endpoint itself yields all the sensors when the parameter is not provided, and a filtered subset when it is, hence no duplicate endpoint is necessary. Second, the inclusion of a filter in the path means that there is a hierarchical relationship between resources, indicating that the type is in itself a resource identifier and not a search criterion, which is semantically incorrect. Third, query parameters are the common HTTP standard of filtering, searching, sorting, and paginating collections, and the API is easier to use among developers who adhere to the well-known conventions of the REST.




Part 4: Deep Nesting with Sub- Resources (20 Marks)

1.	The Sub-Resource Locator Pattern (10 Marks):

Answer:
The Sub-Resource Locator pattern entrusts the management of a nested path to a special class, instead of storing all the methods in a single giant resource class. This project has a locator method in SensorResource which instantiates SensorReadingResource and uses the sensorId as context; then all the logic of reading resides in the separate class. The main advantage is the separation of concerns: every class has one, clear responsibility, and the codebase is much easier to read, maintain, and extend. It can also be used to independently test the sub-resource without having to build the parent. This pattern enables resource classes to avoid becoming unmanageable monoliths in large APIs with numerous nesting collections, and enables various team members to concurrently work on each sub-resource without any merge conflicts.









Part 5: Advanced Error Handling, Exception Mapping & Logging (30 Marks)
2.	Dependency Validation (422 Unprocessable Entity) (10 Marks):

Answers:
HTTP 404 Not Found is an indication that the URL requested is not in existence on the server. The HTTP 422 Unprocessable Entity response indicates that the request path is correct, the request body is syntactically a valid JSON, but the semantic information contained in the body cannot be handled. Getting a sensor with a roomId that is not there will give the URL /api/v1/sensors a valid URL, and the JSON form is well-formed, but the referential integrity constraint is violated within the request body. A 404 would be a false signal to the client that the sensors endpoint has been lost. A 422 response is the correct way to indicate that the server received the request, but it could not be completed due to the fact that the mentioned resource does not exist, providing the client with actionable information to fix the payload, instead of the URL.


4.   The Global Safety Net (500) (5 Marks):
Answers:
There are several security risks of exposing raw Java stack traces to external consumers. Stack traces also provide the internal package and class names, and this provides attackers with a map of the architecture of the application. They also reveal the libraries used by the third party, as well as the version, enabling attackers to match the known CVEs to the specific version and write targeted exploits. They are able to reveal server file-system paths and configuration information. They also disclose certain types of exceptions and when they occur, which assists the attackers to recognize and exploit edge cases. In this project, the GlobalExceptionMapper captures all the unmanaged exceptions and logs the complete stack trace to the server console so that the developers can see it, and sends back just a generic 500 response to the client so that no internal information can leak out.





5.	API Request & Response Logging Filters (5 Marks):

Answer:
The adoption of a JAX-RS ContainerRequestFilter and ContainerResponseFilter to log follows the DRY (Don't Repeat Yourself) principle. One annotated (with annotation) class, a LoggingFilter, annotated with the annotation of provider, is automatically registered by Jersey and used on all requests and responses without any configuration. This ensures uniform coverage of logs: the addition of a new endpoint will not cause a Logger.info() call to be missed. It is a clear division of the cross-cutting issue of logging and the business logic within each resource method which makes those methods focused and readable. In case the logging format or destination should be changed in the future then only the filter class should be changed but not all the methods throughout the codebase. It is also easy to enable or disable logging around the world by merely deleting or deregistering the filter.
