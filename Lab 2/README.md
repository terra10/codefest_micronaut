# Lab 2 - Creating a webserver

In this lab we'll be adding a webserver with API to our basic application. 
We'll be adding new features and beans, either by hand or with the *mn* tool, annd using
and using Micronaut server annotations to define our endpoints and paths.

We'll also be modifying our application properties to change the behaviour of our server.

## Add webserver bean with mn command

Open a terminal and navigate to you project directory. You can optionally create a new
project with the mn command if you like. 

To add a Controller Bean, with required dependencies and a basic unit test,
execute the following command from the root of you project:

````
$ mn create-controller RestController
````

This will create a number of new files for you:
- RestController.java
- RestControllerTest.java

Let's walk through both files real quick. If you don't want to use
the mn command, you can follow allong with the next steps to create your RestController and RestControllerTest classes.

### RestController

````java
@Controller("/restController") // 1
public class RestControllerController {

    @Get(uri="/", produces="text/plain") // 2
    public String index() {
        return "Example Response";
    }
}
````

Anyone familiar with Spring should recognize the @Controller annotation on this class **(1)**. This annotation
is indicates that this class is a Singleton Bean that should be initialized on boot of the application. It also 
allows us to configure a basePath for our Rest API. In this case, it's generated with the path '/restController'.

The second annotation is the @Get on our index() method **(2)**. This indicates that this method should be called 
when a GET is done on the provided uri. In this case, the uri is "/". This is added on top of our base path. The 
produces variable indicates the content type for this endpoint. This is generated as 'text/plain'

### RestControllerTest

````Java
@MicronautTest // 1
public class RestControllerControllerTest {

    @Inject // 2
    @Client("/") // 3
    RxHttpClient client;

    @Test
    public void testIndex() throws Exception {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/restController").status()); // 4
    }
}

````

Let's have a look at our unit test. Most important here is the **@MicronautTest** annotation **(1)**. Borrowing
from the Spring Boots counterpart, this will bootstrap the application and all beans for to be used in your
unit test. It will also make sure you are running on the 'test' profile.

The @Inject annotation **(2)** is our plain JavaEE inject annotation and will attempt to inject a bean into this field.
As we are running with the @MicronautTest we can actually use this to inject a basic RxHttpClient. The @Client annotation **(3)**
makes sure to inject the correct client for the "/" path, using any Rest configuration we setup. In our case, this will create
a HttpClient for localhost:8080/. 

There is a single test generated for us. This will use our injected client to call the "/restController" endpoint. 
The RxHttpClient from Micronaut is uses RxJava for non-blocking calls. In this test, we'll just use the a blocking 'exchange'
call and get it's status. More information on the RxHttpClient can be found here: 
[Client Basics](https://docs.micronaut.io/1.3.4/guide/index.html#clientBasics)

### Run it

We can now start the application and test our endpoint by ourselves:

With Gradlew:
````bash
$ ./gradlew build run
````

If you use maven, you can use:
````
$ ./mvnw compile exec:exec
````

You can now use a browser to go to http://localhost:8080/restController

This should return "Example Response" to you.

## Extending the basic server

With the basics down, let's add some more logic.

### Adding endpoints

We'll be adding a simple POST endpoint that accepts a json body and returns it back to us. 
Open the RestController class en create a new method called 'create'. Give it a single String argument called body.
We can use the @Post annotation to set our uri, consumes en produces content types.

We will use the HttpResponse<String> to add some complexity and flexibility for later on. The 
final method should look something like this:

````java
    @Post(uri = "/", consumes = "application/json", produces = "application/json")
    public HttpResponse<String> create(String body){
        return HttpResponse.accepted().body(body);
    }
````

We can test it by using your favourite Rest client.

If you're running into issues where the server port is already in use, please check the config in your
resources/application.yml file. You can add micronaut.server.port to configure your port number:

````yaml
micronaut:
  server:
    port: 8080
  application:
    name: micronautMeetup
````

From this point on we're set to start adding more and more logic. Micronaut support RxJava 2.0. You can quite
easily get started with building reactive API's by simply retuning one of the common reactive types like Single or
Observable from your endpoint. For the full reactive experience you can also use these types as arguments in your method:

````java
    @Post(uri = "/", consumes = "application/json", produces = "application/json")
    public HttpResponse<String> create(Single<String> body){
        return HttpResponse.accepted().body(body.map(...));
    }
````

We'll not be using this for our next labs. 

You can refer to the [documentation](https://docs.micronaut.io/1.3.4/guide/index.html#httpServer) for more information about setting up and configurating
the Webserver. 
