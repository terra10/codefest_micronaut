# Lab 4 - Setting up Monitoring

At this point we have already setup a dependency on a potential external
system, a database. In this lab we will setup health checks and monitoring endpoints to
keep track of our application health.

## Monitoring Endpoints

Micronaut supports monitoring and management via so called Endpoints. These are simple annotated classes
that can be called over a web interface. 

To get us started, add the following dependency:

build.gradle
````
implementation("io.micronaut:micronaut-management")
````

pom.xml
````xml
     <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-management</artifactId>
      <scope>compile</scope>
    </dependency>
````

A number of standard endpoints wil be available by default:

- /beans
- /health
- /info
- /loggers
- /metrics
- /refresh
- /routes
- /env
- /threaddump

Let's try it real quick! Run your application and head to http://localhost:8080/health.

You should see:
````json
{
  "status": "UP"
}
````

Feel free to try any of the other endpoints as well.

You can change the port these management endpoints run on by editing your application.yml:

````yml
endpoints:
    all:
        port: 8085
````

Each of the default endpoints offers a set of parameters that can be tweaked in you application.yml. They all follow the
endpoints.{id} syntax. If we look at the beans endpoint for example, configuration can be adjused like:

````yaml
endpoints:
    beans:
        enabled: true
        sensitive: false
````

More information on these default endpoints and configuration options can de found in the [documentation](https://docs.micronaut.io/latest/guide/index.html#providedEndpoints)

## Custom Endpoints

We can create our own endpoints add more flexibility and information. The setup is relatively simple:

- Create a java class annotated with @Endpoint
- Provide an id as argument on the @Endpoint annotation
- Add a method annotated with @Read

Let's create an Endpoint that checks the connection to our database. Call it DatabaseEndpoint:

````java
@Endpoint(id = "database", defaultSensitive = false)
public class DatabaseEndpoint {

}
````

Set defaultSensitive = false to ensure that the endpoint will be enabled by default and will
not require any authorization to be accessed.

Then, wire our dataSource:

````java
    private DataSource dataSource;

    public DatabaseEndpoint(DataSource dataSource) {
        this.dataSource = dataSource;
    }
````

Lastly, we can add our @Read method. Lets just check the status of our database Conection:

````java
    @Read
    public boolean dataSource() throws SQLException {
        return !dataSource.getConnection().isClosed();
    }
````

Let's test it! Head over to your browser: http://localhost:8080/database:

````
true
````

We can also update our endpoints to allow for Write / POST operations. Syntax is similar to the @Read, with the 
exception of an optional 'consumes' parameter on the annotation:

````java
    @Write(consumes = "application/json")
    public String write(@Body String message){
       return message;
    }
````

You can configure your own endpoint in a way similar to the build-in ones using the endpoints.{id}:

````yml
endpoints:
  database:
    enabled: alse
````

More information on options on the build-in endpoints and other management features can be found [here](https://docs.micronaut.io/latest/guide/index.html#management)
