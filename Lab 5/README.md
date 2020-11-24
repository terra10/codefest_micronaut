# Lab 5 - Working with Configuration

In this lab we will take a look a different ways of injection properties into
you application.

## Application.yml

We have been adding configuration to the application.yml file for a while now. Until now,
all of that configuration was used by components that where added by the Framework. We can 
also use this same file to house your own properties

Let's add some properties with the prefix 'meetup':

````yaml
meetup:
  hello: 'Hello World'
````

Next up, let's use it in our RestController:

````java
    @Value("${meetup.hello}")
    private String hello;
````

Now we need to use it somewhere. Let's create a new hello endpoint and return the value of our new
property:

````java
    @Get(uri = "/property/hello", processes = "text/plain")
    public String hello(){
        return hello;
    }
````

Let's test it! Build and run your application en then go to 
http://localhost:8080/restController/property/hello:
````
Hello World
````

## ConfigurationClass

In some scenarios it might be usefull to have your configuration available as a 
Java POJO. It is fairly easy to set this up. First, we need a class that will hold our
configuration. Let's call it MeetupConfiguration.

We need to annotate this class with the @ConfigurationProperties annotation and provide our 'meetup'
prefix:

````java
@ConfigurationProperties("meetup")
public class MeetupConfiguration {
}
````

Lastly, we need to create fields that match the fields in our property file. We have 
a property called 'hello' in the application.yml, so we need to add a String field 'hello'
to our MeetupConfiguration. Make sure to inlude getters and setters as well:

The final class should look like this:
````java
@ConfigurationProperties("meetup")
public class MeetupConfiguration {
    
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
````

Lastly, we can inject this Configuration class into our RestController:

````java
@Controller("/restController")
public class RestController {

    private JpaService jpaService;
    private MeetupConfiguration meetupConfiguration;

    public RestController(JpaService jpaService, MeetupConfiguration meetupConfiguration) {
        this.jpaService = jpaService;
        this.meetupConfiguration = meetupConfiguration;
    }
//
}
````

Remove the 'hello' field and change the hello method to use the meetupConfiguration class:

````java
    @Get(uri = "/property/hello", processes = "text/plain")
    public String hello(){
        return meetupConfiguration.getHello();
    }
````

Rebuild, run and test our hello endpoint:
http://localhost:8080/restController/property/hello
````
Hello world
````

Properties in the application.yml will be converted to the type you 
specify in the MeetupConfiguration if possible. 

Properties with longer names will be converted from application.yml to Java fields
as such:

- hello-world in yaml will bind to helloWorld in the Java class
- my-long-property will bind to myLongProperty

let's do a quick test and change our meetup.hello property to meetup.hello-world;

````yml
meetup:
  hello-world: 'Hello world'
````

Update the field in our MeetupConfig to match:

````java
@ConfigurationProperties("meetup")
public class MeetupConfiguration {

    private String helloWorld;

    public String getHelloWorld() {
        return helloWorld;
    }

    public void setHelloWorld(String helloWorld) {
        this.helloWorld = helloWorld;
    }
}
````

And update the restController to use the correct getter:
````java

    @Get(uri = "/property/hello", processes = "text/plain")
    public String hello() {
        return meetupConfiguration.getHelloWorld();
    }
````

When you run it again, it should still return 'Hello World'

More information can be found in the [documentation](https://docs.micronaut.io/latest/guide/index.html#environments)

