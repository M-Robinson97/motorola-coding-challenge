# motorola-coding-challenge

### About the application
Basic RESTful API for a file system per the brief. Emphasizes modularity and scalability by separating concerns with a repository pattern. HTTP requests hit one of the FileManagerController's four endpoints (listFiles, getFile, uploadFile, deleteFile). The FileService processes the request data. The FileRepository manages data access (with help from a separate StorageService - this improves the testability of the repository layer).

Encapsulation and modularity are further preserved via the use of interfaces for the repository and services.

Exceptions are intercepted by a GlobalErrorHandler which maps them to simple custom exceptions with the relevant HTTP status code.

### Tests

_Unit tests_ demonstrating the basics of mocking and testing public functions have been set up for the FileRepositoryImpl and FileServiceImpl. This modular tests demonstrate each component's function in isolation. They can be found in: src/test/java/com.test_apps.motorola_coding_challenge/repository and /service.

_Integration tests_ demonstrating the end-to-end functionality of each of the four endpoints have been provided. I've set up an abstract FileManagerController_BaseIT with common functions which are inherited by the other ITs to cut down on code duplication. Each IT class demonstrates end-to-end success and failure cases for a different endpoint. These can be found in: src/test/java/com.test_apps.motorola_coding_challenge/integration_test.

### Running the Application
The simplest way to demonstrate the application's functionality would be to ensure Maven is on your PATH, run 'mvn clean verify' from the app's root folder, and see that all tests pass. However, you may find it more informative to load up the app in your favourite IDE and run the tests from in there.

### Future Features:
- _Open API / Swagger integration._ Currently, all four endpoints are fully demonstrable via the integration tests. However, we would want to manually test a range of inputs and results. Swagger would be my tool of choice. Other tools are available - Postman had a good run at my previous company until someone noticed their privacy policy mentions storing data. 
- _Test coverage._ 41 test cases have been provided. We'd ideally aim for 100% coverage and far more edge cases.
- _Caching._ It would be good to use a cache (Caffeine is my go-to for smaller applications) to store:
  - ListFiles result => Update when postFile or deleteFile is hit.
  - GetFile result per fileName => Update when postFile or deleteFile are called with the relevant file name.
- _Concurrency._ If we were to expand the application, we'd want to manage competing resource requests. 